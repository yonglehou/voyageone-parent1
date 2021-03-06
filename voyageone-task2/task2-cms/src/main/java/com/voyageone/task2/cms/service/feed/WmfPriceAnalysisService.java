package com.voyageone.task2.cms.service.feed;

import com.csvreader.CsvReader;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.FeedEnums;
import com.voyageone.common.configs.Feeds;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.daoext.cms.CmsZzFeedWmfPriceDaoExt;
import com.voyageone.service.impl.cms.feed.FeedInfoService;
import com.voyageone.service.model.cms.CmsZzFeedWmfPriceModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel_Sku;
import com.voyageone.task2.base.BaseCronTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.voyageone.common.configs.Enums.ChannelConfigEnums.Channel.WMF;
@Service
public class WmfPriceAnalysisService extends BaseCronTaskService {

    @Autowired
    private CmsZzFeedWmfPriceDaoExt cmsZzFeedWmfPriceDaoExt;

    @Autowired
    private FeedInfoService feedInfoService;

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {
        List<CmsZzFeedWmfPriceModel> wmfPrices = getRetailPriceList();
        if(!ListUtils.isNull(wmfPrices)) {
            wmfPrices.forEach(this::insertPrice);
//            Map<String, Object> map = new HashMap<>();
//            wmfPrices = cmsZzFeedWmfPriceDaoExt.selectList(map);
            wmfPrices.forEach(this::updateMastPrice);

            backupFeedFile(FeedEnums.Name.file_id_import_sku);
        }
    }

    public List<CmsZzFeedWmfPriceModel> getRetailPriceList() {

        List<CmsZzFeedWmfPriceModel> wmfPrices = new ArrayList<>();
        CsvReader reader;
        String fileName = Feeds.getVal1("014", FeedEnums.Name.file_id_import_sku);
        String filePath = Feeds.getVal1("014", FeedEnums.Name.feed_ftp_localpath);
        String fileFullName = String.format("%s/%s", filePath, fileName);

        String encode = Feeds.getVal1("014", FeedEnums.Name.feed_ftp_file_coding);
        FileInputStream fileInputStream;
        try {
            fileInputStream  = new FileInputStream(fileFullName);
            reader = new CsvReader(fileInputStream, '\t', Charset.forName(encode));
            // Body读入
            while (reader.readRecord()) {
                CmsZzFeedWmfPriceModel wmfPrice = new CmsZzFeedWmfPriceModel();
                int i = 0;
                wmfPrice.setSkuCode(reader.get(i++));
                wmfPrice.setMsrpPrice(reader.get(i++));
                wmfPrice.setFinalRmbPrice(reader.get(i++));
                wmfPrice.setCostPrice(reader.get(i++));
                wmfPrices.add(wmfPrice);
            }
        } catch (FileNotFoundException e) {
            $info("Wmf价格列表不存在");
            return wmfPrices;

        } catch (Exception e) {
            e.printStackTrace();
            issueLog.log(e, ErrorType.BatchJob, SubSystem.CMS);
            return wmfPrices;
        }
        try {
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return wmfPrices;
    }

    private void updateMastPrice(CmsZzFeedWmfPriceModel cmsZzFeedWmfPriceModel) {
        CmsBtFeedInfoModel cmsBtFeedInfoModel = feedInfoService.getProductBySku(WMF.getId(), cmsZzFeedWmfPriceModel.getSkuCode()+"-OneSize");
        if(cmsBtFeedInfoModel != null){
            for (CmsBtFeedInfoModel_Sku sku : cmsBtFeedInfoModel.getSkus()) {
                if (sku.getSku().equalsIgnoreCase(cmsZzFeedWmfPriceModel.getSkuCode()+"-OneSize")) {
                    sku.setPriceCurrent(Double.parseDouble(cmsZzFeedWmfPriceModel.getFinalRmbPrice()));
                    if(!StringUtils.isEmpty(cmsZzFeedWmfPriceModel.getMsrpPrice())){
                        sku.setPriceMsrp(Double.parseDouble(cmsZzFeedWmfPriceModel.getMsrpPrice()));
                    }
                    if(CmsConstants.FeedUpdFlgStatus.Succeed == cmsBtFeedInfoModel.getUpdFlg()
                            || CmsConstants.FeedUpdFlgStatus.Fail == cmsBtFeedInfoModel.getUpdFlg()){
                        cmsBtFeedInfoModel.setUpdFlg(CmsConstants.FeedUpdFlgStatus.Pending);
                    }
                    cmsBtFeedInfoModel.setModifier(getTaskName());
                    cmsBtFeedInfoModel.setModified(DateTimeUtil.getNowTimeStamp());
                    feedInfoService.updateFeedInfo(cmsBtFeedInfoModel);
                    break;
                }
            }
        }
    }
    /**
     *根据sku先删除在插入
     */
    private void insertPrice(CmsZzFeedWmfPriceModel cmsZzFeedWmfPriceModel) {
        cmsZzFeedWmfPriceDaoExt.deleteBySku(cmsZzFeedWmfPriceModel.getSkuCode());
        cmsZzFeedWmfPriceDaoExt.insert(cmsZzFeedWmfPriceModel);
    }

    protected boolean backupFeedFile(FeedEnums.Name name) {
        $info("备份处理文件开始");
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String date_ymd = sdf.format(date);

        String filename = Feeds.getVal1(WMF, FeedEnums.Name.feed_ftp_localpath) + "/" + StringUtils.null2Space(Feeds.getVal1(WMF, name));
        String filename_backup = Feeds.getVal1(WMF, FeedEnums.Name.feed_ftp_localpath) + "/" + date_ymd + "_"
                + StringUtils.null2Space(Feeds.getVal1(WMF, name));
        File file = new File(filename);
        File file_backup = new File(filename_backup);

        if (!file.renameTo(file_backup)) {
//            $error("产品文件备份失败");
            $info(file_backup+"备份失败");
        }

        $info("备份处理文件结束");
        return true;
    }

    @Override
    protected String getTaskName() {
        return "WmfPriceAnalysisJob";
    }

    @Override
    protected SubSystem getSubSystem() {
        return SubSystem.CMS;
    }
}
