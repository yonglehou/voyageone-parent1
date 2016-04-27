package com.voyageone.web2.cms.openapi.service;
import com.voyageone.common.Snowflake.FactoryIdWorker;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.FileUtils;
import com.voyageone.service.bean.openapi.OpenApiException;
import com.voyageone.service.bean.openapi.image.*;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.imagecreate.*;
import com.voyageone.service.model.cms.CmsMtImageCreateFileModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by dell on 2016/3/18.
 */
@Service
public class CmsImageFileService extends BaseService {
    @Autowired
    private ImageCreateFileService imageCreateFileService;
    @Autowired
    private AliYunOSSFileService serviceAliYunOSSFile;
    @Autowired
    private USCDNFileService serviceUSCDNFile;
    @Autowired
    private LiquidFireImageService serviceLiquidFireImage;

    public GetImageResultBean getImage(String channelId, int templateId, String file, String vparam, String requesttQueryString, String Creater) throws Exception {
        GetImageResultBean result = new GetImageResultBean();
        CmsMtImageCreateFileModel modelFile = null;
        try {
            //hashCode做缓存key
            long hashCode = imageCreateFileService.getHashCode(channelId, templateId, file, vparam);
            //getModel
            modelFile = imageCreateFileService.getModelByHashCode(hashCode);
            if (modelFile == null) {
                //1.创建记录信息
                modelFile = imageCreateFileService.createCmsMtImageCreateFile(channelId, templateId, file, vparam, Creater, hashCode);
            }
            if (modelFile.getState() == 0) {
                //2.生成图片 from LiquidFire
                serviceLiquidFireImage.createImage(modelFile);
            }
            if (modelFile.getOssState() == 0) {
                //3.上传图片到阿里云OSS
                serviceAliYunOSSFile.upload(modelFile);
            }
            if ("001".equals(modelFile.getChannelId()) && modelFile.getUscdnState() == 0) {
                //4.上传 uscdn
                serviceUSCDNFile.upload(modelFile);
            }
        } catch (OpenApiException ex) {
            //4.处理业务异常
            result.setErrorCode(ex.getErrorCode());
            result.setErrorMsg(ex.getMsg());
            if (ex.getSuppressed() != null) {
                long requestId = FactoryIdWorker.nextId();//生成错误请求唯一id
                $error("getImage requestId:" + requestId, ex);
            }
        } catch (Exception ex) {
            //5.未知异常
            //生成错误请求唯一id
            issueLog.log(ex, ErrorType.OpenAPI, SubSystem.COM);

            long requestId = FactoryIdWorker.nextId();
            $error("getImage requestId:" + requestId, ex);
            result.setRequestId(requestId);
            result.setErrorCode(ImageErrorEnum.SystemError.getCode());
            result.setErrorMsg(ImageErrorEnum.SystemError.getMsg());//成功清理
        } finally {
            if (modelFile != null && modelFile.getFilePath() != null) {
                FileUtils.delFile(modelFile.getFilePath());
            }
        }

        if (result.getErrorCode() > 0) {//6.保存报错错误信息
            if (modelFile != null) {
                modelFile.setErrorMsg(result.getRequestId() + ":" + result.getErrorMsg());
                modelFile.setErrorCode(result.getErrorCode());
                imageCreateFileService.changeModel(modelFile);
            }
        } else {
            if (modelFile != null) {
                result.getResultData().setFilePath(modelFile.getOssFilePath());
            }
        }
        return result;
    }


    public AddListResultBean addList(AddListParameter parameter) {
        AddListResultBean result = new AddListResultBean();
        try {
            checkAddListParameter(parameter);

            for (CreateImageParameter imageInfo : parameter.getData()) {
                long hashCode = imageCreateFileService.getHashCode(imageInfo.getChannelId(), imageInfo.getTemplateId(), imageInfo.getFile(), imageInfo.getVParam());
                if (!imageCreateFileService.existsHashCode(hashCode)) {//1.创建记录信息
                    imageCreateFileService.createCmsMtImageCreateFile(imageInfo.getChannelId(), imageInfo.getTemplateId(), imageInfo.getFile(), imageInfo.getVParam(), "system addList", hashCode);
                }
            }
        } catch (OpenApiException ex) {
            result.setErrorCode(ex.getErrorCode());
            result.setErrorMsg(ex.getMsg());
            if (ex.getSuppressed() != null) {
                long requestId = FactoryIdWorker.nextId();//生成错误请求唯一id
                $error("AddList requestId:" + requestId, ex);
            }
        } catch (Exception ex) {
            long requestId = FactoryIdWorker.nextId();//生成错误请求唯一id
            $error("AddList requestId:" + requestId, ex);
            result.setRequestId(requestId);
            result.setErrorCode(ImageErrorEnum.SystemError.getCode());
            result.setErrorMsg(ImageErrorEnum.SystemError.getMsg());
        }
        return result;
    }

    public  void  checkAddListParameter(AddListParameter parameter) throws OpenApiException {
        if(parameter.getData().size()>100)
        {
           //throw  new OpenApiException("");
        }
        for (CreateImageParameter imageInfo : parameter.getData()) {
            if (StringUtil.isEmpty(imageInfo.getChannelId())) {
                throw new OpenApiException(ImageErrorEnum.ChannelIdNotNull);
            }
            if (imageInfo.getTemplateId() != 0) {
                throw new OpenApiException(ImageErrorEnum.ImageTemplateNotNull);
            }
            if (StringUtil.isEmpty(imageInfo.getFile())) {
                throw new OpenApiException(ImageErrorEnum.FileNotNull);
            }
            if (StringUtil.isEmpty(imageInfo.getVParam())) {
                throw new OpenApiException(ImageErrorEnum.VParamNotNull);
            }
        }
    }
}

