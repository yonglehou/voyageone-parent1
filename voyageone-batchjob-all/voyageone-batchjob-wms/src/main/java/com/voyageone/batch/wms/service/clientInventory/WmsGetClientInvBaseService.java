package com.voyageone.batch.wms.service.clientInventory;

import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.Constants;
import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.batch.wms.WmsConstants;
import com.voyageone.batch.wms.WmsConstants.updateClientInventoryConstants;
import com.voyageone.batch.wms.dao.ClientInventoryDao;
import com.voyageone.batch.wms.dao.ItemDetailDao;
import com.voyageone.batch.wms.modelbean.ClientInventoryBean;
import com.voyageone.common.components.channelAdvisor.bean.inventory.GetInventoryParamBean;
import com.voyageone.common.components.channelAdvisor.service.InventoryService;
import com.voyageone.common.components.channelAdvisor.soap.InventoryResSoapenv;
import com.voyageone.common.components.channelAdvisor.webservices.ArrayOfInventoryItemResponse;
import com.voyageone.common.components.channelAdvisor.webservices.InventoryItemResponse;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.sears.SearsService;
import com.voyageone.common.components.transaction.TransactionRunner;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.Enums.StoreConfigEnums;
import com.voyageone.common.configs.StoreConfigs;
import com.voyageone.common.configs.ThirdPartyConfigs;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.beans.StoreBean;
import com.voyageone.common.configs.beans.ThirdPartyConfigBean;
import com.voyageone.common.configs.dao.ThirdPartConfigDao;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.*;

/**
 *
 * Created by sky on 2015/7/29.
 */
@Service
public abstract class WmsGetClientInvBaseService extends BaseTaskService {
    @Autowired
    ClientInventoryDao clientInventoryDao;

    @Autowired
    ThirdPartConfigDao thirdPartConfigDao;

    @Autowired
    ItemDetailDao itemDetailDao;

    @Autowired
    InventoryService inventoryService;

    @Autowired
    SearsService searsService;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.WMS;
    }

    @Override
    public String getTaskName() {
            return "wmsGetClientInvJob";
    }


    /**
     * @description 获取渠道对应的仓库
     * @param order_channel_id 渠道
     * @return sotreId
     */
    protected Long getStore(String order_channel_id) {
        OrderChannelBean channel = ChannelConfigs.getChannel(order_channel_id);
        log("获取渠道:" + channel.getFull_name() + "的store_id开始");
        try {
            long storeID = 0;
            List<StoreBean> storeBeans = StoreConfigs.getChannelStoreList(order_channel_id);
            assert storeBeans != null;
            for (StoreBean storeBean : storeBeans) {
                if (storeBean.getInventory_manager().equals(StoreConfigEnums.Manager.NO.getId()) && storeBean.getStore_kind().equals(StoreConfigEnums.Kind.REAL.getId())) {
                    storeID = storeBean.getStore_id();
                    break;
                }
            }
            logger.info(" ----------渠道：" + channel.getFull_name() + "对应的store_id为" + storeID);
            log("获取渠道:" + channel.getFull_name() + "的store_id结束");
            return storeID;
        }catch (Exception e){
            throw new RuntimeException(channel.getFull_name()+"获取storeId失败：" + e);
        }
    }

    /**
     * 一条库存临时表的插入语句values部分
     * @param clientInventoryBeans 要插入的数据集合
     * @param orderChannelId 渠道
     * @param storeId 仓库ID
     * @return 拼接好的Sql
     */
    protected String preparetClientInventoryData(List<ClientInventoryBean> clientInventoryBeans , String orderChannelId, long storeId) {
        StringBuilder sqlValueBuffer = new StringBuilder();
        int i = 1;
        for(ClientInventoryBean clientInventoryBean: clientInventoryBeans) {
            sqlValueBuffer.append(Constants.LEFT_BRACKET_CHAR);

            // order_channel_id
            sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
            sqlValueBuffer.append(orderChannelId);
            sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
            sqlValueBuffer.append(Constants.COMMA_CHAR);

            //client_sku
            sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
            sqlValueBuffer.append(transferStr(clientInventoryBean.getClient_sku()));
            sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
            sqlValueBuffer.append(Constants.COMMA_CHAR);

            // qty
            sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
            sqlValueBuffer.append(clientInventoryBean.getQty());
            sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
            sqlValueBuffer.append(Constants.COMMA_CHAR);

            // store_id
            sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
            sqlValueBuffer.append(storeId);
            sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
            sqlValueBuffer.append(Constants.COMMA_CHAR);

            // itemCode
            sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
            sqlValueBuffer.append(transferStr(clientInventoryBean.getItemCode()));
            sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
            sqlValueBuffer.append(Constants.COMMA_CHAR);

            //sku
            sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
            sqlValueBuffer.append(transferStr(clientInventoryBean.getSku()));
            sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
            sqlValueBuffer.append(Constants.COMMA_CHAR);

            // size
            sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
            sqlValueBuffer.append(transferStr(clientInventoryBean.getSize()));
            sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
            sqlValueBuffer.append(Constants.COMMA_CHAR);

            // barcode
            sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
            sqlValueBuffer.append(clientInventoryBean.getBarcode());
            sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
            sqlValueBuffer.append(Constants.COMMA_CHAR);

            // syn_flg
            sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
            sqlValueBuffer.append(WmsConstants.SYN_FLG.SUCCESS);
            sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
            sqlValueBuffer.append(Constants.COMMA_CHAR);

            //sim_flg
            sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
            sqlValueBuffer.append("0");
            sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
            sqlValueBuffer.append(Constants.COMMA_CHAR);

            //active
            sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
            sqlValueBuffer.append("1");
            sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
            sqlValueBuffer.append(Constants.COMMA_CHAR);

            // created
            sqlValueBuffer.append(Constants.NOW_MYSQL);
            sqlValueBuffer.append(Constants.COMMA_CHAR);

            // creater
            sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
            sqlValueBuffer.append(getTaskName());
            sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
            sqlValueBuffer.append(Constants.COMMA_CHAR);

            // modified
            sqlValueBuffer.append(Constants.NOW_MYSQL);
            sqlValueBuffer.append(Constants.COMMA_CHAR);

            // modifier
            sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
            sqlValueBuffer.append(getTaskName());
            sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
            sqlValueBuffer.append(Constants.RIGHT_BRACKET_CHAR);
            if(i != clientInventoryBeans.size())
            sqlValueBuffer.append(Constants.COMMA_CHAR);
            i++;
        }
        return sqlValueBuffer.toString();
    }

    /**
     * @description 临时获取第三方配置表的配置数据
     * @param orderChannelId 渠道
     * @param propName 属性名称
     * @return 配置对象
     */
    protected ThirdPartyConfigBean getFullUpdtConfig(String orderChannelId, String propName) {
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("order_channel_id", orderChannelId);
        paramMap.put("propName", propName);
        return  clientInventoryDao.getFullUpdtConfig(paramMap);
    }

    /**
     * 转换数据中的特殊字符
     *
     * @param data
     * @return
     */
    public String transferStr(String data) {
        if (StringUtils.isNullOrBlank2(data)) {
            return Constants.EMPTY_STR;
        } else {
            return data.replaceAll("'", "''");
        }
    }

    protected void log (String logMsg){
        logger.info("===============" + logMsg + "===============");
    }

}
