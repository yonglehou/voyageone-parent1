/**
 * @Name:    translate_wms_en.js
 * @Date:    2015/06/10
 *
 * @User:    sky
 * @Version: 0.0.2
 */

define(['components/app', 'components/services/language.service', "underscore"], function (app) {
    app.config(["$translateProvider", "languageType",
        function ($translateProvider, languageType) {
            $translateProvider.translations(languageType.en,
                {
                    WMS_CREATE: 'Create',
                    WMS_DELETE: 'Delete',
                    WMS_CANCEL: 'Cancel',
                    WMS_EDIT: 'Edit',
                    WMS_ADD: 'Add',
                    WMS_CLOSE: 'Close',
                    WMS_CHANGE: 'Change',
                    WMS_SUBMIT: 'Submit',
                    WMS_SAVE: 'Save',
                    WMS_COMPARE: 'Compare',
                    WMS_BACK: 'Back',
                    WMS_SYNCINVENTORY: 'Sync Inventory',
                    WMS_REOPEN: 'ReOpen',

                    WMS_STORE: 'Store',
                    WMS_SEARCH: 'Search',
                    WMS_LASTUPDATEUSER: 'Last Update User',
                    WMS_LASTUPDATETIME: 'Last update Time',
                    WMS_OPERATION: 'Operation',
                    WMS_ADDITEMTOBACKORDER: 'Add a item to backorder',
                    WMS_ORDERNUMBER: 'Order#',
                    WMS_RESID: 'ResId',
                    WMS_SKU: 'SKU',
                    WMS_STATUS: 'Status',
                    WMS_FROM: 'From',
                    WMS_TO: 'To',
                    WMS_TYPE: 'Type',
                    WMS_SCAN: 'Scan',
                    WMS_RELABEL: 'Relabel',
                    WMS_PRINT: 'Print',
                    WMS_CHANNEL: 'Channel',
                    WMS_SHIP: 'Ship',
                    WMS_DESCRIPTION: 'Description',
                    WMS_CREATETIME: 'CreateTime',
                    WMS_MODIFYTIME: 'ModifyTime',
                    WMS_DLPICKUPITEMSBYRESVERTION: 'Download PickUp Items By Reservation',
                    WMS_DLPICKUPITEMSBYSKU: 'Download PickUp Items By SKU',
                    WMS_NEWEDITSESSION: 'New/Edit Session',
                    WMS_SESSIONLIST: 'Session List',
                    WMS_CONDITION: 'Condition',
                    WMS_REASON: 'Reason',
                    WMS_EXPRESSCOMPANY: 'Express',
                    WMS_TRACKINGNO: 'TrackingNo',
                    WMS_NOTE: 'Note',
                    WMS_SESSIONNAME: 'Session Name',
                    WMS_CLOSESESSION: 'Close session',
                    WMS_PRODUCTNAME: 'ProductName',
                    WMS_BARCODE: 'Barcode',
                    WMS_CREATEBY: 'CreateBy',
                    WMS_DETAIL: 'Detail',
                    WMS_LOCK: 'Lock',
                    WMS_IDCARD: 'ID Card',
                    WMS_SESTATUS: 'OMS Status',
                    WMS_SHOP: 'Shop',
                    WMS_WORKLOAD: 'Work Load',
                    WMS_SYNSHIPNO: 'SynShipNo',
                    WMS_WEBORDERID: 'WebOrderId',
                    WMS_ORDERTIME: 'OrderTime',
                    WMS_BACKORDER: 'Backorder',
                    WMS_OPEN: 'Open',
                    WMS_CONFIRMED: 'Confirmed',
                    WMS_RESERVATIONLOG: 'Reservation Log',
                    WMS_INVENTORY: 'Inventory',
                    WMS_INVENTORYINFO: 'Inventory Info',
                    WMS_PRODUCTCODE: 'Product Code',
                    WMS_NEWORDER: 'New Order',
                    WMS_TOTAL: 'Total',
                    WMS_SKUHIS: 'SKU Movement History Report',
                    WMS_DATE: 'Date',
                    WMS_PONUMBER: 'PO#',
                    WMS_NEWTRANSFEROUT: 'New Transfer Out',
                    WMS_NEWTRANSFERIN: 'New Transfer In',
                    WMS_NEWPURCHASEORDER: 'New Purchase Order',
                    WMS_NEWWITHDRAWAL: 'New Withdrawal',
                    WMS_NAME: 'Name',
                    WMS_LOCATIONNAME: 'Location Name',
                    WMS_DOWNLOAD: 'Download',
                    WMS_DOWNLOADO: 'Download 1',
                    WMS_DOWNLOADT: 'Download 2',
                    WMS_TRANSFER: 'Transfer',
                    WMS_TRANSFERNAME: 'Transfer Name',
                    WMS_TARGETOUT: 'Target Out',
                    WMS_LOCATION: 'Location',
                    WMS_ADDLOCATION: 'Add location',
                    WMS_PLEASESAVETRANSFERFIREST: 'Please save transfer first.',
                    WMS_NODATA: 'there is no any data.',
                    WMS_QTY: 'Qty',
                    WMS_PACKAGENAME: 'Container Name',
                    WMS_CLOSEPACKAGE: 'Close Container',
                    WMS_ACCCEPT: 'Accept',
                    WMS_LOCATIONBIND: 'Location Bind',
                    WMS_LOGS: 'Logs',
                    WMS_ITEMINFO: 'Item Info',
                    WMS_COLOR: 'Color',
                    WMS_MSRP: 'MSRP',
                    WMS_PLEASESELECTATYPEFORPRODUCT: 'Please save new product first, or select a type for product.',
                    WMS_TRANSFER_TYPE: 'Transfer Type',
                    WMS_FIXED: 'Fixed', //fixed Session
                    WMS_ISFIXED: 'Is Fixed',
                    WMS_QTYOFFSETS: 'Qty_Offsets',
                    WMS_RESCAN: 'ReScan',
                    WMS_NEWSECTION: 'New Section',
                    WMS_NEWSESSION: 'New Session',
                    WMS_VIEW: 'View',
                    WMS_STOCKTAKETYPE: 'Stocktake Type',
                    WMS_STOCKTAKEQRY: 'Stocktake Qty',
                    WMS_CURQTY: 'Current Quantity',
                    WMS_ORIGIN: 'Origin',
                    WMS_UPC_NOTFOUNDSIZES: 'Not found sizes.',
                    WMS_ORDERNUMBERANDTRANSFERNAME: 'Order#/Transfer Name',
                    WMS_RECORDNUMBER: 'Total Record Number : ',
                    WMS_RETURNTYPE: 'Return Type',

                    // 弹出提示框的内容翻译部分
                    WMS_ALERT_UPC_MSRP: "Msrp must be number!",
                    WMS_ALERT_UPC_PRODUCT: "Product type is invalid!",
                    WMS_ALERT_UPC_MSRPGREATTHEN0: "Msrp must great then 0!",

                    WMS_ALERT_TRANSFER_DELETE: "Will delete this transfer [ {{transfer_name}} ], sure ?",
                    WMS_ALERT_TRANSFER_DELETE_FAIL: "delete transfer failed. please check its status.",

                    WMS_ALERT_TRANSFER_EDIT_NO_TYPE: "No transfer type, cant add transfer.",
                    WMS_ALERT_TRANSFER_EDIT_CLOSED: "Current Transfer, already closed.",
                    WMS_TRANSFER_EDIT_NO_CODE: "Code is required !",
                    WMS_TRANSFER_EDIT_NUM_VALID:"Num must more than 0.",
                    WMS_TRANSFER_EDIT_NO_ITEM: "Cant find the item.",
                    WMS_TRANSFER_EDIT_CANCEL: "Are you sure you want to cancel this transfer ?",
                    WMS_TRANSFER_EDIT_PKG_CLOSED: "Selected package already closed.",
                    WMS_TRANSFER_EDIT_PKG_DEL: "Are you sure you want to delete this package [ {{transfer_package_name}} ] ?",
                    WMS_TRANSFER_EDIT_NO_TYPE: "cant find transfer type",
                    WMS_TRANSFER_EDIT_PKG_REOPEN: "Are you sure you want to reOpen this package [ {{transfer_package_name}} ] ?",

                    WMS_TRANSFER_EDIT_NAME_VALID: "transfer name is empty",
                    WMS_TRANSFER_EDIT_MAP_VALID: "mapping transfer out is empty",
                    WMS_TRANSFER_EDIT_PO_VALID: "transfer po_number is empty",
                    WMS_TRANSFER_EDIT_FROM_VALID: "transfer from is empty",
                    WMS_TRANSFER_EDIT_TO_VALID: "transfer to is empty",

                    WMS_STOCK_TAKE_SESSION_STATUS_UNVALID: "The session status must be [Processing]!",
                    WMS_STOCK_TAKE_SESSION_DEL: "Are you sure ? delete this session",
                    WMS_STOCK_TAKE_NEW_FAIL: "New Session failed.<br />Please select store.",

                    WMS_STOCK_TAKE_TO_STOCK: "Are you sure ? Change status to [Stock]",
                    WMS_STOCK_TAKE_SECTION_DEL: "Are you sure ? delelte this Section",

                    WMS_STOCK_TAKE_SECTION_CLOSE: "Are you sure ? Close this section",
                    WMS_STOCK_TAKE_SECTION_RESCAN: "Are you sure ? Rescan the section !",

                    WMS_STOCK_TAKE_SESSION_FIX: "Are you sure ? Fixed this Session!",

                    WMS_RETURN_SAVE: "Are you sure ? Save you change!",
                    WMS_RETURN_TO_REFUNDED: "Are you sure ? change to Refunded.",
                    WMS_RETURN_NO_SESSION: "Select a Session Please!",
                    WMS_RETURN_SESSION_CLOSE: "Are you sure ? close this session.",
                    WMS_RETURN_ROW_DEL: "Are you sure ? delete this row",

                    WMS_INVENTORY_SYNC: "Are you sure ? synchronized inventory to the platforms",

                    WMS_PICKUP_STORE_UN_VALID: "Store is required !",
                    WMS_PICKUP_RELABEL: 'Are you sure ?<br/> ReLabel the {{scanTypeName}} [ {{scanNo}} ] s Pick note.',
                    WMS_PICKUP_TYPE_UN_VALID: "{{scanTypeName}} is required !",
                    WMS_PICKUP_PERMIT: "No permit to PickUp Items !",
                    WMS_RECEIVE_PERMIT: "No permit to Receive Orders !",

                    WMS_LOCATION_NOT_FOUND:"Not found location",
                    WMS_LOCATION_DEL: "Are you sure ?",
                    WMS_LOCATION_ADD_FAIL: 'Add location failed.<br />Please select store.',
                    WMS_LOCATION_NAME_UN_VALID: 'Location name unvalid.',
                    WMS_LOCATION_CODE_UN_VALID: 'Code or barcode is required.',
                    WMS_LOCATION_SEARCH: 'Please search first.',

                    WMS_BACK_DEL: 'Are you sure ? delete the row when sku is [ {{ sku }} ] !',
                    WMS_BACK_STORE_UA: 'Add backOrder failed.<br />Please select store.',
                    WMS_BACK_SKU_UA: 'Add backOrder failed.<br />Please input Sku name.',

                    // Notify 提示内容
                    WMS_NOTIFY_OP_SUCCESS: "Operation Success",
                    WMS_NOTIFY_OP_FAIL: "Operation Fail",

                    WMS_NOTIFY_UPC_CODE: "Please input code.",
                    WMS_NONEXISTENT_ORDERNUMBER: "Nonexistent order#.",
                    WMS_CAN_NOT_FIND_OBJECT: "Can't find the object or the object already be return.",
                    WMS_MUST_ENTER_ORDERNUMBER: "You must enter orderNumber!",
                    WMS_NOT_TO_CALCULATE_INVENTORY: '( Current session return item not to calculate inventory. )',
                    WMS_INPROCESS: 'In Process',

                    //report
                    WMS_INVENTORY_DETAIL_REPORT: 'Inventory Details Report',
                    WMS_STOCKTAKE_COMPARERES_REPORT: 'Stocktake Compare Result Report'
                });
        }]);
});

