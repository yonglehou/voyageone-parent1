package com.voyageone.wms;


public final class WmsConstants {

    public final static class Return {
        public final static String STATUS = "returnStatus";
        public final static String REASON = "returnReason";
        public final static String EXPRESS = "returnExpress";
        public final static String SESSIONSTATUS = "returnSessionStatus";
        public final static String CONDITION = "returnCondition";
        public final static String RETURNTYPE = "returnType";
    }

    public final static class Stocktake {
        public final static String SESSIONSTATUS = "sessionStatus";
        public final static String SECTIONSTATUS = "sectionStatus";
        public final static String STOCKTAKETYPE = "stocktakeType";
    }

    public final static class Common{
        public final static String RESMSG = "returnResMsg";
        public final static String SUCCESEFLG = "successFlg";
    }

    /**
     * 对应 wms_bt_item_location_log 表的 Comment 字段。
     */
    public final static class ItemLocationLogComments {
        public final static String INSERT = "insert";
        public final static String DELETE = "delete";
    }

    /**
     * 对应 捡货画面 的 scanType 字段。
     */
    public final static class ScanType {
        public final static String SCAN = "Scan";
        public final static String RELABEL = "ReLabel";
    }

    /**
     * Reservation变更区分
     */
    public final static class ChangeKind {
        // 仓库变更
        public final static String Store = "1";
        // 备注记入
        public final static String Note = "2";
        // 取消
        public final static String Cancelled = "3";
        // 超卖
        public final static String BackOrdered = "4";
        // 超卖再次打开
        public final static String Open = "5";
        // 超卖确认
        public final static String BackOrderConfirmed = "6";
    }

    /**
     * 仓库名称（为了对应synship：国内仓库统称为TM，虚拟仓库统称为CH）
     */
    public final static class StoreName {
        // 国内仓库
        public final static String CN = "TM";
        // 虚拟仓库
        public final static String CH = "CH";
    }

    /**
     * CloseDayFlg
     */
    public final static class CloseDayFlg {
        public final static String Process = "0";
        public final static String Sim = "1";
        public final static String Done = "2";
    }

    /**
     * SynFlg
     */
    public final static class SynFlg {
        // 推送忽略
        public static final String IGNORE = "4";
        // 1:已推送
        public static final String SENDED = "1";
        //  0:未推送
        public static final String UNSEND = "0";
    }

    /**
     * Reserve
     */
    public final static class ReserveType {
        // 捡货
        public final static String PickUp = "PickUp";
        // 收货
        public final static String Receive = "Receive";
    }

    /**
     * 报表相关设置
     */
    public final static class ReportSetting {

        // 模板路径
        public final static String WMS_REPORT_TEMPLATE_PATH = "wms.report.template.path";
    }

    public final static class ReportItems{

        // 库存详情模板配置
        public final static class InvDelRpt {
            public final static String TEMPLATE_PATH = "wms.report.template.path";
            public final static String TEMPLATE_NAME = "invDelRptTemplate.xlsx";
            public final static String RPT_NAME = "SalesDetailsInventory";
            public final static String RPT_SUFFIX = ".xlsx";
            public final static String RPT_SHEET_NAME = "SalesDetailsInventory";
            public final static class RptType1 {
                public final static int TEMPLATE_SHEET_NO = 0;
                public final static int TEMPLATE_FIRSTROW_NO = 2;
                public final static int START = 1;
                public final static int COLNUM = 7;

                public final static class Col {
                    public final static int COLNUM_SKU = 1;
                    public final static int COLNUM_INITINV = 2;
                    public final static int COLNUM_PO = 3;
                    public final static int COLNUM_SELL = 4;
                    public final static int COLNUM_RETURN = 5;
                    public final static int COLNUM_WIT = 6;
                    public final static int COLNUM_CURRINV = 7;
                }
            }
            public final static class RptType2 {
                public final static int TEMPLATE_SHEET_NO = 1;
                public final static int TEMPLATE_FIRSTROW_NO = 2;
                public final static int START = 0;
                public final static int COLNUM = 8;

                public final static class Col {
                    public final static int COLNUM_MODEL = 0;
                    public final static int COLNUM_COLOR = 1;
                    public final static int COLNUM_SIZE = 2;
                    public final static int COLNUM_INITINV = 3;
                    public final static int COLNUM_PO = 4;
                    public final static int COLNUM_SELL = 5;
                    public final static int COLNUM_RETURN = 6;
                    public final static int COLNUM_WIT = 7;
                    public final static int COLNUM_CURRINV = 8;
                }
            }
        }

        // 盘点比对结果报告模板配置
        public final static class StocktakeCompResRpt {
            public final static String TEMPLATE_PATH = "wms.report.template.path";
            public final static String TEMPLATE_NAME = "stocktakeOffsetRptTemplate.xlsx";
            public final static String RPT_NAME = "stocktakeOffset";
            public final static String RPT_SUFFIX = ".xlsx";
            public final static String RPT_SHEET_NAME = "stocktakeOffset";
            public final static int TEMPLATE_SHEET_NO = 0;
            public final static int TEMPLATE_FIRSTROW_NO = 2;
            public final static int COLNUM = 5;
            public final static class Col{
                public final static int COLNUM_SKU = 1;
                public final static int COLNUM_UPC = 2;
                public final static int COLNUM_INVENTORY = 3;
                public final static int COLNUM_STOCKQTY = 4;
                public final static int COLNUM_STOCKQTY_OFFSET = 5;
            }
        }

        // ClientShipment模板配置
        public final static class ClientShipment {
            public final static String TEMPLATE_PATH = "wms.report.template.path";
            public final static String TEMPLATE_NAME = "ASN.xlsx";
            public final static String RPT_NAME = "ASN";
            public final static String RPT_SUFFIX = ".xlsx";
            public final static String RPT_SHEET_NAME = "ASN";
            public final static int TEMPLATE_SHEET_NO = 0;
            public final static int TEMPLATE_FIRSTROW_NO = 2;
            public final static int COLNUM_MAX = 6;
            public final static class ASN{
                public final static int ROW = 0;
                public final static int COLNUM_FILE_NAME = 1;
                public final static int COLNUM_TOTAL_QTY = 5;
            }
            public final static class Col{
                public final static int COLNUM_CARTON_NO = 0;
                public final static int COLNUM_CARTON_QTY = 1;
                public final static int COLNUM_CLIENT_SKU = 2;
                public final static int COLNUM_UPC = 3;
                public final static int COLNUM_SKU = 4;
                public final static int COLNUM_QTY = 5;
            }
        }

        // TransferCompare模板配置
        public final static class TransferCompare {
            public final static String TEMPLATE_PATH = "wms.report.template.path";
            public final static String TEMPLATE_NAME = "transferCompare.xlsx";
            public final static String RPT_NAME = "transferCompare";
            public final static String RPT_SUFFIX = ".xlsx";
            public final static String RPT_SHEET_NAME = "transferCompare";
            public final static int TEMPLATE_SHEET_NO = 0;
            public final static int TEMPLATE_FIRSTROW_NO = 2;
            public final static int COLNUM_MAX = 12;
            public final static class Col{
                public final static int COLNUM_NO = 0;
                public final static int COLNUM_ASN_FILE = 1;
                public final static int COLNUM_ASN_CLIENT_SKU = 2;
                public final static int COLNUM_ASN_UPC = 3;
                public final static int COLNUM_ASN_SKU = 4;
                public final static int COLNUM_ASN_QTY = 5;
                public final static int COLNUM_TRANSFER_NAME = 6;
                public final static int COLNUM_TRANSFER_PO = 7;
                public final static int COLNUM_TRANSFER_CLIENT_SKU = 8;
                public final static int COLNUM_TRANSFER_UPC = 9;
                public final static int COLNUM_TRANSFER_SKU = 10;
                public final static int COLNUM_TRANSFER_QTY = 11;
            }
        }

        // GoodsReturn模板配置
        public final static class GoodsReturn {
            public final static String TEMPLATE_PATH = "wms.report.template.path";
            public final static String TEMPLATE_NAME = "goodsReturn.xls";
            public final static String RPT_NAME = "goodsReturn";
            public final static String RPT_SUFFIX = ".xls";
            public final static String RPT_SHEET_NAME = "GoodsReturn";
            public final static int TEMPLATE_SHEET_NO = 0;
            public final static int TEMPLATE_FIRSTROW_NO = 1;
            public final static int COLNUM_MAX = 15;
            public final static class Col{
                public final static int COLNUM_NO = 0;
                public final static int COLNUM_ORDER_CHANNEL = 1;
                public final static int COLNUM_STORE = 2;
                public final static int COLNUM_RETURN_TIME = 3;
                public final static int COLNUM_ORDER_TIME = 4;
                public final static int COLNUM_SOURCE_ORDER_ID = 5;
                public final static int COLNUM_CLIENT_ORDER_ID = 6;
                public final static int COLNUM_ORDER_NUM = 7;
                public final static int COLNUM_RES_ID = 8;
                public final static int COLNUM_SKU = 9;
                public final static int COLNUM_CONDITION = 10;
                public final static int COLNUM_RECEIVED_FROM = 11;
                public final static int COLNUM_TRACKING_NO = 12;
                public final static int COLNUM_NOTES = 13;
                public final static int COLNUM_SESSION_STATUS = 14;
            }
        }

        // 库存一览模板配置
        public final static class InvListRpt {
            public final static String TEMPLATE_PATH = "wms.report.template.path";
            public final static String TEMPLATE_NAME = "invListRptTemplate.xlsx";
            public final static String RPT_NAME = "SalesInventoryList";
            public final static String RPT_SUFFIX = ".xlsx";

            public final static class RptType1 {
                public final static String RPT_SHEET_NAME = "SalesInventoryListBySku";
                public final static int TEMPLATE_SHEET_NO = 0;
                public final static int TEMPLATE_FIRSTROW_NO = 2;
                public final static int START = 0;
                public final static int COLNUM = 5;

                public final static class Col {
                    public final static int COLNUM_CHANNEL_NAME = 0;
                    public final static int COLNUM_CODE = 1;
                    public final static int COLNUM_SKU = 2;
                    public final static int COLNUM_BARCODE = 3;
                    public final static int COLNUM_QTY = 4;
                }
            }

            public final static class RptType2 {
                public final static String RPT_SHEET_NAME = "SalesInventoryListByCode";
                public final static int TEMPLATE_SHEET_NO = 1;
                public final static int TEMPLATE_FIRSTROW_NO = 2;
                public final static int START = 0;
                public final static int COLNUM = 3;

                public final static class Col {
                    public final static int COLNUM_CHANNEL_NAME = 0;
                    public final static int COLNUM_CODE = 1;
                    public final static int COLNUM_QTY = 2;
                }
            }
        }

    }

    /**
     * 可捡货列表
     */
    public final static class ReportPickupItems {

        // 文件名
        public final static String FILE_NAME = "pickupItems";

        // 模板类型
        public final static class Type {
            // 按照订单物品抽出
            public final static String Reservation = "1";

            // 按照SKU抽出
            public final static String SKU = "2";
        }

        // 出力内容
        public final static class Reservation {

            // 模板Sheet
            public final static int TEMPLATE_SHEET = 0;

            // 最大列数
            public final static int MAX_COLUMNS = 8;

            // 开始行数
            public final static int START_ROWS = 1;

            // 列：Location
            public final static int Column_Location = 0;

            // 列：SKU
            public final static int Column_SKU = 1;

            // 列：Product
            public final static int Column_Product = 2;

            // 列：OrderNumber
            public final static int Column_OrderNumber = 3;

            // 列：UploadTim
            public final static int Column_UploadTime = 4;

            // 列：ShippingMethod
            public final static int Column_ShippingMethod = 5;

            // 列：RsvId
            public final static int Column_RsvId= 6;

            // 列：Client_Sku
            public final static int Column_Client_SKU= 7;
        }

        // 出力内容
        public final static class SKU {

            // 模板Sheet
            public final static int TEMPLATE_SHEET = 1;

            // 最大列数
            public final static int MAX_COLUMNS = 5;

            // 开始行数
            public final static int START_ROWS = 1;

            // 列：Date
            public final static int Column_Date = 0;

            // 列：SKU
            public final static int Column_SKU = 1;

            // 列：Qty
            public final static int Column_Qty = 2;

            // 列：Date
            public final static int Column_Location = 3;

            // 列：Client_Sku
            public final static int Column_Client_SKU= 4;

        }

    }

    /**
     * 入库、出库、进货清单
     */
    public final static class ReportTransferItems {

        // 文件名
        public final static String FILE_NAME = "transferItems";

        // 模板Sheet
        public final static int TEMPLATE_SHEET = 0;

        // 增加行数
        public final static int ADD_ROWS = 4;

        // 开始列
        public final static int Column_Start = 0;
        // 结束列
        public final static int Column_End = 24;

        // 仓库行数、列数
        public final static class Store {
            public final static int Row = 1;
            public final static int Column = 1;
        }

        // 入出库类型行数、列数
        public final static class Type {
            public final static int Row = 1;
            public final static int Column = 23;
        }

        // PO_NUM行数、列数
        public final static class PO {
            public final static int Row = 4;
            public final static int Column = 4;
        }

        // Date行数、列数
        public final static class Date {
            public final static int Row = 4;
            public final static int Column = 14;
        }

        // From行数、列数
        public final static class From {
            public final static int Row = 5;
            public final static int Column = 4;
        }

        // To行数、列数
        public final static class To {
            public final static int Row = 5;
            public final static int Column = 14;
        }

        // TransferNumber行数、列数
        public final static class TransferNumber {
            public final static int Row = 6;
            public final static int Column = 4;
        }

        // Number of Cartons行数、列数
        public final static class Cartons {
            public final static int Row = 6;
            public final static int Column = 14;
        }

        // Notes行数、列数
        public final static class Notes {
            public final static int Row = 9;
            public final static int Column = 3;
        }

        // Code行数、列数
        public final static class Code {
            public final static int Row = 19;
            public final static int Column_Start = 2;
            public final static int Column_End = 5;
            public final static String Name = "Code：";
        }

        // CodeSize行数、列数
        public final static class Size {
            public final static int Row = 20;
            public final static int Column_Title = 2;
            public final static int Column_Detail = 3;
            public final static String SortName = "ZZZZZZTotal";
            public final static String Name = "Total";
        }

        // CodeQty行数、列数
        public final static class Qty {
            public final static int Row = 21;
            public final static int Column_Title = 2;
            public final static int Column_Detail = 3;
        }

        // TotalQty行数、列数
        public final static class Total {
            public final static int Row = 29;
            public final static int Column = 23;
            public final static String Name = "Total quantity：";
        }


    }

    /**
     * 已捡货列表
     */
    public final static class ReportPickedItems {

        // 文件名
        public final static String FILE_NAME = "pickedReport";

        // 出力内容
        public final static class Reservation {

            // 模板Sheet
            public final static int TEMPLATE_SHEET = 0;

            // 最大列数
            public final static int MAX_COLUMNS = 7;

            // 开始行数
            public final static int START_ROWS = 1;

            // 列：RowNo
            public final static int Column_RowNo = 0;

            // 列：PickUP Time
            public final static int Column_PickedTime = 1;

            // 列：OrderNumber
            public final static int Column_OrderNumber = 2;

            // 列：ResID
            public final static int Column_ResID = 3;

            // 列：SKU
            public final static int Column_SKU = 4;

            // 列：Des
            public final static int Column_Des= 5;

            // 列：Qty
            public final static int Column_Qty= 6;
        }

    }
}
