package com.voyageone.service.daoext.vms;

import com.voyageone.service.model.vms.VmsBtOrderDetailModel;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 订单相关CRUD扩展
 * Created by vantis on 16-7-7.
 */

@Repository
public interface VmsBtOrderDetailDaoExt {

    List<VmsBtOrderDetailModel> orderDetailselectList(Map<String, Object> orderSearchParams);

    List<String> orderIdselectList(Map<String, Object> orderSearchParams);

    int updateOrderStatus(Map<String, Object> changeStatusParam);

    List<Map<String, Object>> selectListByTime(Map<String, Object> orderSearchParams);

    int updateShipmentStatusInOrder(Map<String, Object> sortedParams);

    int cancelOrderShipmentStatus(Map<String, Object> params);

    int countOrder(Map<String, Object> params);

    int countSku(Map<String, Object> params);

    Date getLatestPrintedTIme(Map<String, Object> params);

    int clearOrderCancelInfo(Map<String, Object> params);

    int clearSkuCancelInfo(Map<String, Object> params);
}
