package com.voyageone.task2.cms.service.feed.status;

import com.csvreader.CsvReader;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Enums.FeedEnums;
import com.voyageone.common.configs.Feeds;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.model.cms.CmsFeedLiveSkuModel;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class LuckyVitaminSkuStatusCheckService extends FeedStatusCheckBaseService {
    @Override
    protected List<CmsFeedLiveSkuModel> getSkuList() throws Exception {
        CsvReader reader;
        List<CmsFeedLiveSkuModel> skuList = new ArrayList<>();
        String fileName = Feeds.getVal1(getChannel().getId(), FeedEnums.Name.file_id);
        String filePath = Feeds.getVal1(getChannel().getId(), FeedEnums.Name.feed_ftp_localpath);
        String fileFullName = String.format("%s/%s", filePath, "discontinue_"+fileName);

        String encode = Feeds.getVal1(getChannel().getId(), FeedEnums.Name.feed_ftp_file_coding);

        reader = new CsvReader(new FileInputStream(fileFullName), '\t', Charset.forName(encode));

        // Head读入
        reader.readHeaders();
        reader.getHeaders();

        // Body读入
        while (reader.readRecord()) {
            String discontinued = reader.get(95);
            //upc,MerchantPrimaryCategory,cnMsrp,cNPrice,ImageList
            if (StringUtils.isEmpty(reader.get(1)) || StringUtils.isEmpty(reader.get(49))
                    || StringUtils.isEmpty(reader.get(19))
                    || StringUtils.isEmpty(reader.get(20))
                    || StringUtils.isEmpty(reader.get(37))
                    ) continue;
            if (discontinued.equalsIgnoreCase("yes")) continue;
            CmsFeedLiveSkuModel cmsFeedLiveSkuModel = new CmsFeedLiveSkuModel();
            cmsFeedLiveSkuModel.setChannelId(getChannel().getId());
            String sku = reader.get(0);
            cmsFeedLiveSkuModel.setSku(sku);
            cmsFeedLiveSkuModel.setQty(0);
            cmsFeedLiveSkuModel.setCreater(getTaskName());
            cmsFeedLiveSkuModel.setModifier(getTaskName());
            cmsFeedLiveSkuModel.setCreated(DateTimeUtil.getDate());
            cmsFeedLiveSkuModel.setModified(DateTimeUtil.getDate());
            skuList.add(cmsFeedLiveSkuModel);
        }
        return skuList;
    }

    @Override
    protected ChannelConfigEnums.Channel getChannel() {
        return ChannelConfigEnums.Channel.LUCKY_VITAMIN;
    }

    @Override
    public void backupFeedFile(){
        $info("备份处理文件开始");
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String date_ymd = sdf.format(date);

        String filename = Feeds.getVal1("017", FeedEnums.Name.feed_ftp_localpath) + "/" +"discontinue_"+ StringUtils.null2Space(Feeds.getVal1("017", FeedEnums.Name.file_id));
        String filename_backup = Feeds.getVal1("017", FeedEnums.Name.feed_ftp_localpath) + "/" + date_ymd + "_"
                + StringUtils.null2Space(Feeds.getVal1("017", FeedEnums.Name.file_id));
        File file = new File(filename);
        File file_backup = new File(filename_backup);
        if (!file.renameTo(file_backup)) {
            $info("备份处理失败");
        }

        $info("备份处理文件结束");
    }
}