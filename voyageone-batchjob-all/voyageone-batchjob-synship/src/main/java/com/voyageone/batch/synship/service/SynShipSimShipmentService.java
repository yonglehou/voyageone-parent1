package com.voyageone.batch.synship.service;

import com.voyageone.batch.SynshipConstants;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.CodeConstants;
import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.batch.synship.dao.*;
import com.voyageone.batch.synship.modelbean.*;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.transaction.TransactionRunner;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Enums.PortConfigEnums;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SynShipSimShipmentService  extends BaseTaskService {

    @Autowired
    ClientTrackingDao clientTrackingDao;

    @Autowired
    ShipmentDao shipmentDao;

    @Autowired
    SequenceDao sequenceDao;

    @Autowired
    TrackingDao trackingDao;

    @Autowired
    ReservationDao reservationDao;

    @Autowired
    OrderDao orderDao;

    @Autowired
    private TransactionRunner transactionRunner;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.SYNSHIP;
    }

    @Override
    public String getTaskName() {
        return "SynShipSimShipmentJob";
    }

    /**
     * 获取打印的日志是否需要包含线程
     */
    @Override
    public boolean getLogWithThread() {
        return true;
    }

    public void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        // 允许运行的订单渠道取得
        List<String> orderChannelIdList = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);

        // 抽出件数
        String row_count = TaskControlUtils.getVal1(taskControlList, TaskControlEnums.Name.row_count);

        int intRowCount = 1;
        if (!StringUtils.isNullOrBlank2(row_count)) {
            intRowCount = Integer.valueOf(row_count);
        }

        // 线程
        List<Runnable> threads = new ArrayList<>();

        // 根据订单渠道运行
        for (final String orderChannelID : orderChannelIdList) {

            final int finalIntRowCount = intRowCount;

            // 模拟港口
            final String port = TaskControlUtils.getVal2(taskControlList, TaskControlEnums.Name.order_channel_id,orderChannelID);

            threads.add(new Runnable() {
                @Override
                public void run() {
                    new simShipment(orderChannelID, port, finalIntRowCount).doRun();
                }
            });

        }

        runWithThreadPool(threads, taskControlList);

    }

    /**
     * 按渠道进行模拟Shipment
     */
    public class simShipment  {
        private OrderChannelBean channel;
        private int rowCount;
        private String port;

        public simShipment(String orderChannelId, String port, int rowCount) {
            this.channel = ChannelConfigs.getChannel(orderChannelId);
            this.rowCount = rowCount;
            this.port = port;
        }

        public void doRun() {
            $info(channel.getFull_name() + " Shipment模拟开始，港口："+port);

            // 模拟 Shipment 失败的记录取得
            List<String> errorClientTrackingLst = new ArrayList<>();
            if (port.equals(PortConfigEnums.Port.LA.getId())) {
               errorClientTrackingLst = clientTrackingDao.getErrorClientTrackingLst(channel.getOrder_channel_id());
            }
            else if (port.equals(PortConfigEnums.Port.CN.getId())) {
                errorClientTrackingLst = clientTrackingDao.getErrorClientTrackingLstByOrderNumber(channel.getOrder_channel_id());
            }

            $info(channel.getFull_name() + "----------OrderNumber的快递信息不全：" + errorClientTrackingLst.size());
            if (errorClientTrackingLst.size() > 0 ) {
                logIssue(channel.getFull_name() + "：OrderNumber的快递信息不全，无法进行LA港口模拟发货", errorClientTrackingLst);
            }

            // 需要模拟 Shipment的记录取得
            List<ClientTrackingSimBean> clientTrackingSimLst  = new ArrayList<>();
            if (port.equals(PortConfigEnums.Port.LA.getId())) {
                clientTrackingSimLst = clientTrackingDao.getClientTrackingSimLst(channel.getOrder_channel_id(), rowCount);
            }
            else if (port.equals(PortConfigEnums.Port.CN.getId())) {
                clientTrackingSimLst = clientTrackingDao.getClientTrackingSimLstByOrderNumber(channel.getOrder_channel_id(), rowCount);
            }

            $info(channel.getFull_name() + "----------模拟发货件数：" + clientTrackingSimLst.size());

            // 获取当前日期
            final String dateFormat = DateTimeUtil.format(DateTimeUtil.getDate(), DateTimeUtil.DEFAULT_DATE_FORMAT);

            final List<ClientTrackingSimBean> finalClientTrackingSimLst = clientTrackingSimLst;

            transactionRunner.runWithTran(new Runnable() {
                @Override
                public void run() {

                    try {

                        ShipmentBean shipmentBean = new ShipmentBean();
                        // 判断当日的Shipment是否已建立，如果没有的话，模拟一个
                        for (ClientTrackingSimBean clientTracking : finalClientTrackingSimLst) {
                            String shipmentName = dateFormat;
                            // LA时以运单号为Shipment名
                            if (port.equals(PortConfigEnums.Port.LA.getId())) {
                                shipmentName = shipmentName + "-" + clientTracking.getTracking_no() + "-SIM";
                            }
                            else if (port.equals(PortConfigEnums.Port.CN.getId())) {
                                shipmentName = shipmentName + "-" + channel.getName() + "-SIM";
                            }
                            shipmentBean = shipmentDao.getShipmentDelay(channel.getOrder_channel_id(), shipmentName);

                            if (shipmentBean == null || StringUtils.isNullOrBlank2(shipmentBean.getShipment_id())) {
                                // 取得ShipmentID
                                String shipmentID = sequenceDao.getNextVal("Shipment");

                                // 设置Shipment内容
                                shipmentBean = SimShipment(channel.getOrder_channel_id(), shipmentID, shipmentName, dateFormat, clientTracking, port);

                                $info(channel.getFull_name() + "----------模拟Shipment做成：" + shipmentID);

                                // 插入模拟的Shipment
                                shipmentDao.insertShipment(shipmentBean);

                                // LA时需要插入ShipmentInfo
                                if (port.equals(PortConfigEnums.Port.LA.getId())) {
                                    // 设置ShipmentInfo内容
                                    ShipmentInfoBean shipmentinfoBean = new ShipmentInfoBean();
                                    shipmentinfoBean = SimShipmentInfo(channel.getOrder_channel_id(), shipmentID, clientTracking);

                                    $info(channel.getFull_name() + "----------模拟ShipmentInfo做成：" + shipmentinfoBean.getTracking_status());

                                    // 插入模拟的ShipmentInfo
                                    shipmentDao.insertShipmentInfo(shipmentinfoBean);
                                }

                            }

                            $info(channel.getFull_name() + "----------OrderNumber：" + clientTracking.getOrder_number() + "，ClientOrderId：" + clientTracking.getClient_order_id() + "，TrackingType：" + clientTracking.getTracking_type() + "，TrackingNo：" + clientTracking.getTracking_no());

                            // 判断Package是否做成，没有的话，新规做成
                            PackageBean packageBean = new PackageBean();
                            if (port.equals(PortConfigEnums.Port.LA.getId())) {
                                packageBean = shipmentDao.getPackage(shipmentBean.getShipment_id(), clientTracking.getTracking_no());
                            }
                            else   if (port.equals(PortConfigEnums.Port.CN.getId())) {
                                packageBean = shipmentDao.getPackage(shipmentBean.getShipment_id(),"");
                            }


                            if (packageBean == null || StringUtils.isNullOrBlank2(packageBean.getPackage_id())) {
                                // 取得PackageID
                                String packageID = sequenceDao.getNextVal("Package");

                                // 设置Package内容
                                packageBean = SimPackage(shipmentBean.getShipment_id(), packageID, clientTracking, port);

                                $info(channel.getFull_name() + "----------模拟Package做成：" + packageID);

                                // 插入模拟的Shipment
                                shipmentDao.insertPackage(packageBean);
                            }

                            String[] reservationList = clientTracking.getReservation_id().split(",");

                            for (String reservationId : reservationList) {
                                // 判断PackageItem是否做成，没有的话，新规做成
                                PackageItemBean packageItemBean = shipmentDao.getPackageItem(shipmentBean.getShipment_id(), packageBean.getPackage_id(), clientTracking.getSyn_ship_no(), Long.valueOf(reservationId));

                                if (packageItemBean == null || StringUtils.isNullOrBlank2(packageItemBean.getPackage_item_id())) {
                                    // 取得PackageItemID
                                    String packageItemID = sequenceDao.getNextVal("PackageItem");

                                    // 设置Package内容
                                    packageItemBean = SimPackageItem(shipmentBean.getShipment_id(), packageBean.getPackage_id(), packageItemID, Long.valueOf(reservationId), clientTracking, port);

                                    $info(channel.getFull_name() + "----------模拟PackageItem做成：" + packageItemID);

                                    // 插入模拟的Shipment
                                    shipmentDao.insertPackageItem(packageItemBean);
                                }
                            }

                            // 判断Tracking是否做成，没有的话，新规做成
                            TrackingBean trackingBean = new TrackingBean();
                            if (port.equals(PortConfigEnums.Port.LA.getId())) {
                                trackingBean = trackingDao.getTracking(clientTracking.getOrder_channel_id(), clientTracking.getTracking_type(), clientTracking.getTracking_no(), "");
                            }
                            else if (port.equals(PortConfigEnums.Port.CN.getId())) {
                                trackingBean = trackingDao.getTracking(clientTracking.getOrder_channel_id(), clientTracking.getTracking_type(), clientTracking.getTracking_no(), clientTracking.getSyn_ship_no());
                            }

                            if (trackingBean == null || StringUtils.isNullOrBlank2(trackingBean.getTracking_no())) {

                                // 插入Tracking
                                trackingBean = SimTracking(clientTracking, port);

                                $info(channel.getFull_name() + "----------模拟Tracking做成：" + trackingBean.getTracking_no());

                                trackingDao.insertTracking(trackingBean);
                            }

                            // 插入ResTracking
                            trackingDao.insertResTracking(clientTracking.getSyn_ship_no(), clientTracking.getTracking_no(), clientTracking.getTracking_type(), getTaskName());

                            // 插入TrackingInfo
                            if (port.equals(PortConfigEnums.Port.CN.getId())) {
                                trackingDao.insertTrackingInfoBySim(clientTracking.getSyn_ship_no(), clientTracking.getTracking_no(), CodeConstants.TRACKING.INFO_080, clientTracking.getTracking_time(), getTaskName());
                            }

                            if (port.equals(PortConfigEnums.Port.LA.getId())) {
                                // 更新物品状态
                                reservationDao.UpdateReservationStatus(clientTracking.getSyn_ship_no(), CodeConstants.Reservation_Status.ShippedUS, "2",getTaskName());

                                // 插入物品日志
                                reservationDao.insertReservationLog(clientTracking.getSyn_ship_no(), "Sim LA Port Shipment", getTaskName());
                            }
                            else  if (port.equals(PortConfigEnums.Port.CN.getId())) {
                                // 更新物品状态
                                reservationDao.UpdateReservationStatus(clientTracking.getSyn_ship_no(), CodeConstants.Reservation_Status.ShippedCN, "2",getTaskName());

                                // 更新订单状态
                                orderDao.updateOrderStatus(clientTracking.getSyn_ship_no(), CodeConstants.Reservation_Status.ShippedCN, getTaskName());

                                // 插入物品日志
                                reservationDao.insertReservationLog(clientTracking.getSyn_ship_no(), "Sim CN Port Shipment", getTaskName());
                            }

                            // 更新模拟标志位
                            clientTrackingDao.UpdatClientTrackingSimFlg(clientTracking.getSeq(), "1", getTaskName());

                    }
                } catch(Exception e) {
                    logIssue(e, channel.getFull_name() + " Shipment模拟错误，港口："+port);

                    throw new RuntimeException(e);
                }
            }
        });

            $info(channel.getFull_name() + " Shipment模拟结束，港口："+port);

        }
    }

    /**
     * 设置Shipment内容
     * @param order_channel_id
     * @param shipmentID
     * @param shipmentName
     * @param dateFormat
     * @param clientTrackingSimBean
     * @param port
     * @return ShipmentBean
     */
    private  ShipmentBean SimShipment(String order_channel_id, String shipmentID, String shipmentName, String dateFormat, ClientTrackingSimBean clientTrackingSimBean, String port) {

        ShipmentBean shipmentBean = new ShipmentBean();
        shipmentBean.setShipment_id(shipmentID);
        shipmentBean.setOrder_channel_id(order_channel_id);
        shipmentBean.setShipment_name(shipmentName);
        shipmentBean.setShipdate(dateFormat);
        shipmentBean.setPort(port);
        shipmentBean.setStatus(CodeConstants.SHIPMENT_STATUS.SHIPPED);
        shipmentBean.setDeclaration_number("");
        shipmentBean.setWay_bill_number("");
        shipmentBean.setShip_price("");
        shipmentBean.setTracking_type("");
        shipmentBean.setTracking_no("");
        if (port.equals(PortConfigEnums.Port.LA.getId())) {
            shipmentBean.setTracking_type(clientTrackingSimBean.getTracking_type());
            shipmentBean.setTracking_no(clientTrackingSimBean.getTracking_no());
        }
        shipmentBean.setComments("");
        shipmentBean.setWarehouse_id(ChannelConfigs.getVal1(order_channel_id, ChannelConfigEnums.Name.warehouse));
        shipmentBean.setCustoms_declaration("");
        shipmentBean.setPo_number("");
        shipmentBean.setDelay_flg("0");
        shipmentBean.setContract_ref("");
        shipmentBean.setCreate_time(DateTimeUtil.getNow());
        shipmentBean.setUpdate_time(DateTimeUtil.getNow());
        shipmentBean.setCreate_person(getTaskName());
        shipmentBean.setUpdate_person(getTaskName());

        return shipmentBean;

    }

    /**
     * 设置ShipmentInfo内容
     * @param order_channel_id
     * @param shipmentID
     * @param clientTrackingSimBean
     * @return ShipmentBean
     */
    private  ShipmentInfoBean SimShipmentInfo(String order_channel_id, String shipmentID, ClientTrackingSimBean clientTrackingSimBean) {

        ShipmentInfoBean shipmentInfoBean = new ShipmentInfoBean();
        shipmentInfoBean.setShipment_id(shipmentID);
        shipmentInfoBean.setOrder_channel_id(order_channel_id);
        shipmentInfoBean.setTracking_status(CodeConstants.TRACKING.INFO_052);
        shipmentInfoBean.setProccess_type(CodeConstants.PROCCESS_TYPE.BOT);
        shipmentInfoBean.setProccess_time(clientTrackingSimBean.getTracking_time());
        shipmentInfoBean.setMsg("");
        shipmentInfoBean.setCreate_time(DateTimeUtil.getNow());
        shipmentInfoBean.setUpdate_time(DateTimeUtil.getNow());
        shipmentInfoBean.setCreate_person(getTaskName());
        shipmentInfoBean.setUpdate_person(getTaskName());

        return shipmentInfoBean;

    }

    /**
     * 设置Package内容
     * @param shipmentID
     * @param packageID
     * @param clientTrackingSimBean
     * @param port
     * @return PackageBean
     */
    private  PackageBean SimPackage(String shipmentID, String packageID, ClientTrackingSimBean clientTrackingSimBean, String port) {

        PackageBean packageBean = new PackageBean();

        packageBean.setShipment_id(shipmentID);
        packageBean.setPackage_id(packageID);
        packageBean.setPackage_name(packageID);
        packageBean.setTracking_no("");
        if (port.equals(PortConfigEnums.Port.LA.getId())) {
            packageBean.setTracking_no(clientTrackingSimBean.getTracking_no());
        }
        packageBean.setLimit_weight("0");
        packageBean.setWeight_kg("0");
        packageBean.setWeight_lb("0");
        packageBean.setStatus(CodeConstants.PACKAGE_STATUS.CLOSE);
        packageBean.setComments("");
        packageBean.setCreate_time(DateTimeUtil.getNow());
        packageBean.setUpdate_time(DateTimeUtil.getNow());
        packageBean.setCreate_person(getTaskName());
        packageBean.setUpdate_person(getTaskName());

        return packageBean;
    }

    /**
     * 设置PackageItem内容
     * @param shipmentID
     * @param packageID
     * @param packageItemID
     * @param clientTrackingSimBean
     * @param port
     * @return PackageItemBean
     */
    private  PackageItemBean SimPackageItem(String shipmentID, String packageID, String packageItemID, long reservationId, ClientTrackingSimBean clientTrackingSimBean, String port) {

        PackageItemBean packageItemBean = new PackageItemBean();

        packageItemBean.setShipment_id(shipmentID);
        packageItemBean.setPackage_id(packageID);
        packageItemBean.setPackage_item_id(packageItemID);
        packageItemBean.setSyn_ship_no(clientTrackingSimBean.getSyn_ship_no());
        packageItemBean.setOrdernum(clientTrackingSimBean.getOrder_number());
        packageItemBean.setReservation_id(reservationId);
        packageItemBean.setPackage_type("3");
        if (port.equals(PortConfigEnums.Port.LA.getId())) {
            packageItemBean.setPackage_type("1");
        }
        packageItemBean.setComments("");
        packageItemBean.setDel_flg("0");
        packageItemBean.setCreate_time(DateTimeUtil.getNow());
        packageItemBean.setUpdate_time(DateTimeUtil.getNow());
        packageItemBean.setCreate_person(getTaskName());
         packageItemBean.setUpdate_person(getTaskName());

        return packageItemBean;
    }

    /**
     * 设置Track内容

     * @param clientTrackingSimBean
     * @return TrackingBean
     */
    private  TrackingBean SimTracking( ClientTrackingSimBean clientTrackingSimBean, String port) {

        TrackingBean trackingBean = new TrackingBean();

        trackingBean.setSyn_ship_no(clientTrackingSimBean.getSyn_ship_no());
        if (port.equals(PortConfigEnums.Port.LA.getId())) {
            trackingBean.setSyn_ship_no("");
        }
        trackingBean.setOrder_channel_id(clientTrackingSimBean.getOrder_channel_id());
        trackingBean.setTracking_no(clientTrackingSimBean.getTracking_no());
        trackingBean.setTracking_type(clientTrackingSimBean.getTracking_type());
        trackingBean.setSim_order_num("");
        trackingBean.setTracking_kind("0");
        trackingBean.setTracking_area("0");
        if (port.equals(PortConfigEnums.Port.LA.getId())) {
            trackingBean.setTracking_area("1");
        }
        trackingBean.setStatus("00");
        trackingBean.setClear_port_flg("0");
        trackingBean.setUse_flg("1");
        trackingBean.setWeight_kg("0");
        trackingBean.setWeight_lb("0");
        trackingBean.setTracking_fee("0");
        trackingBean.setTracking_cost("0");
        trackingBean.setMain_flg("0");
        trackingBean.setSender_code("");
        trackingBean.setReceiver_code("");
        trackingBean.setSent_kd100_poll_flg("1");
        trackingBean.setSent_kd100_poll_time("");
        trackingBean.setSent_kd100_poll_count("0");
        trackingBean.setSent_kd100_flg("0");
        trackingBean.setPrint_type("0");
        // 顺丰时，为了向快递100订阅信息，将打印类型设为1
        if (trackingBean.getTracking_type().equals("SF")) {
            trackingBean.setPrint_type("1");
        }
        trackingBean.setCreate_time(DateTimeUtil.getNow());
        trackingBean.setUpdate_time(DateTimeUtil.getNow());
        trackingBean.setCreate_person(getTaskName());
        trackingBean.setUpdate_person(getTaskName());

        return trackingBean;
    }

}
