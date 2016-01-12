package com.voyageone.batch.wms.service.inventory.sync;

import com.google.gson.Gson;
import com.jd.open.api.sdk.JdException;
import com.jd.open.api.sdk.response.ware.WareSkuStockUpdateResponse;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.wms.modelbean.InventorySynLogBean;
import com.voyageone.common.components.jd.JdInventoryService;
import com.voyageone.common.components.jumei.Bean.SetShippingReq;
import com.voyageone.common.components.jumei.Bean.StockSyncReq;
import com.voyageone.common.components.jumei.JumeiService;
import com.voyageone.common.configs.Enums.ShopConfigEnums;
import com.voyageone.common.configs.ShopConfigs;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 库存同步的子服务，用于提供同步到聚美的功能
 * <br />
 * Created by jack on 16/01/12.
 */
@Service
public class WmsSyncToJuMeiSubService extends WmsSyncInventoryBaseService {

    @Autowired
    private JumeiService jumeiService;

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) {
        // 确保其他位置错误调用该 Service 后，不会处理任何操作
        // 可以更改任务启动内容为：只处理 Cart 为 JD 的内容。但暂时搁置
    }

    public void syncJumei(List<InventorySynLogBean> inventorySynLogBeans, ShopBean shopBean) {

        logger.info(shopBean.getShop_name() + "（" + shopBean.getComment() + ")库存同步开始");
        logger.info(shopBean.getShop_name() + "（" + shopBean.getComment() + ")库存需要同步件数："+inventorySynLogBeans.size());

        for (InventorySynLogBean inventorySynLogBean : inventorySynLogBeans)

            syncJumei(inventorySynLogBean, shopBean);

        logger.info(shopBean.getShop_name() + "（" + shopBean.getComment() + ")库存同步结束");
    }

    private void syncJumei(InventorySynLogBean inventorySynLogBean, ShopBean shopBean) {

        String res = null;

        try {
            StockSyncReq req = new StockSyncReq();
            req.setBusinessman_code(inventorySynLogBean.getSku());
            req.setEnable_num(String.valueOf(inventorySynLogBean.getQty()));

            //$info(shopBean.getShop_name() + "（" + shopBean.getComment() + ")库存同步记录：" + new Gson().toJson(req));

            res = jumeiService.stockSync(shopBean, req);

            //$info(shopBean.getShop_name() + "（" + shopBean.getComment() + ")库存同步结果：" + res);

        } catch (Exception e) {
            logFailRecord(e, inventorySynLogBean);
            return;
        }

        // 有返回内容，但没有错误代码，就认为是成功调用
        if (res != null) {

            Map<String, Object> resultMap = JsonUtil.jsonToMap(res);

            //  0 处理正确(已发货的订单再次调用此接口, 更改快递信息,更改成功后也会返回 0)
            if ("0".equals(resultMap.get("error"))) {
                // 成功后，迁移数据到历史表
                movePass(inventorySynLogBean);
                return;
            }
            else {
                // 失败的话，记录失败的信息
                moveIgnore(inventorySynLogBean, (String) resultMap.get("result"));

                logFailRecord( (String) resultMap.get("result"), inventorySynLogBean);
                return;
            }
        }

        logFailRecord(shopBean.getShop_name() + "（" + shopBean.getComment() + ")库存同步失败，错误未知", inventorySynLogBean);
    }
}
