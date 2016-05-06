package com.voyageone.web2.cms.views.channel.listing;

import com.voyageone.common.Constants;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.service.dao.cms.mongo.CmsBtSizeChartDao;
import com.voyageone.service.model.cms.mongo.channel.CmsBtSizeChartModel;
import com.voyageone.web2.base.BaseAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gjl on 2016/5/5.
 */
@Service
public class CmsSizeChartService extends BaseAppService {
    @Autowired
    private CmsBtSizeChartDao cmsBtSizeChartDao;

    /**
     * 尺码关系一览初始化画面
     * @param language
     * @param channelId
     * @return data
     */
    public Map<String, Object>sizeChartInit(String language,String channelId) {
        Map<String, Object> data =new HashMap<>();
        //取得产品品牌
        List<TypeChannelBean> brandNameList= TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.BRAND_41, channelId, language);
        //取得产品类型
        List<TypeChannelBean> productTypeList= TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.PROUDCT_TYPE_57, channelId, language);
        //取得产品性别
        List<TypeChannelBean> sizeTypeList= TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.PROUDCT_TYPE_58, channelId, language);
        //取得尺码关系一览数据
        List<CmsBtSizeChartModel> sizeChartList=cmsBtSizeChartDao.selectInitSizeChartInfo(channelId);
        //取得产品品牌
        data.put("brandNameList",brandNameList);
        //取得产品类型
        data.put("productTypeList",productTypeList);
        //取得产品性别
        data.put("sizeTypeList",sizeTypeList);
        //取得尺码关系一览数据
        data.put("sizeChartList",sizeChartList);
        //返回数据的类型
        return data;
    }

    /**
     * 尺码关系一览检索画面
     * @param channelId
     * @param param
     * @return
     */
    public Map<String, Object>sizeChartSearch(String channelId,Map param) {
        Map<String, Object> data =new HashMap<>();
        CmsBtSizeChartModel cmsBtSizeChartModel= new CmsBtSizeChartModel();
        Map searchInfo = (Map) param.get("searchInfo");
        //公司平台销售渠道
        cmsBtSizeChartModel.setChannelId(channelId);
        //尺码名称
        cmsBtSizeChartModel.setSizeChartName((String) searchInfo.get("sizeChartName"));
        //尺码标志
        cmsBtSizeChartModel.setFinish((String) searchInfo.get("finishFlag"));
        //更新开始时间
        cmsBtSizeChartModel.setCreated((String) searchInfo.get("startTime"));
        //更新结束时间
        cmsBtSizeChartModel.setModified((String) searchInfo.get("endTime"));
        //产品品牌
        cmsBtSizeChartModel.setBrandName((List<String>) searchInfo.get("brandNameList"));
        //产品类型
        cmsBtSizeChartModel.setProductType((List<String>) searchInfo.get("productTypeList"));
        //产品性别
        cmsBtSizeChartModel.setSizeType((List<String>) searchInfo.get("sizeTypeList"));
        //尺码关系一览检索
        List<CmsBtSizeChartModel> sizeChartList=cmsBtSizeChartDao.selectSearchSizeChartInfo(channelId,cmsBtSizeChartModel);
        //尺码关系一览检索
        param.put("sizeChartList",sizeChartList);
        //返回数据的类型
        return data;
    }

    /**
     * 尺码关系一览初删除
     * @param channelId
     * @param param
     * @return data
     */
    public Map<String, Object>sizeChartDelete(String channelId,Map param) {
        Map<String, Object> data =new HashMap<>();
        CmsBtSizeChartModel cmsBtSizeChartModel= new CmsBtSizeChartModel();
        Map searchInfo = (Map) param.get("sizeChartId");
        //公司平台销售渠道
        cmsBtSizeChartModel.setChannelId(channelId);
        //自增主键
        cmsBtSizeChartModel.setSizeChartId((String)searchInfo.get("sizeChartId"));
        //尺码关系一览检索
        List<CmsBtSizeChartModel> sizeChartList=cmsBtSizeChartDao.sizeChartDelete(channelId,cmsBtSizeChartModel);
        //尺码关系一览检索
        param.put("sizeChartList",sizeChartList);
        //返回数据的类型
        return data;
    }

    /**
     * 尺码关系一览编辑画面
     * @param channelId
     * @param param
     * @return data
     */
    public Map<String, Object>sizeChartEdit(String channelId,Map param) {
        Map<String, Object> data =new HashMap<>();
        return data;
    }

    /**
     * 尺码关系一览编辑详情编辑画面
     * @param channelId
     * @param param
     * @return data
     */
    public Map<String, Object>sizeChartDetail(String channelId,Map param) {
        Map<String, Object> data =new HashMap<>();
        return data;
    }
}