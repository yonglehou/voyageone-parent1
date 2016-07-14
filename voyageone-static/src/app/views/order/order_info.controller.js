define([
    'vms'
], function (vms) {
    vms.controller('OrderInfoController', (function () {

        function OrderInfoController(alert, notify, confirm, popups, orderInfoService) {
            this.alert = alert;
            this.notify = notify;
            this.confirm = confirm;
            this.orderInfoService = orderInfoService;
            this.popups = popups;

            this.oneDay = 24 * 60 * 60 * 1000;
            this.twoDay = 2 * this.oneDay;
            this.threeDay = 3 * this.oneDay;
            this.oneMonth = 30 * this.oneDay;

            var now = new Date();
            var today = new Date(now.getFullYear(), now.getMonth(), now.getDate());
            this.orderDateFrom = new Date(today.getTime() - 6 * this.oneMonth);
            this.orderDateTo = today;

            this.pageInfo = {
                curr: 1,
                total: 0,
                size: 10,
                fetch: this.search.bind(this)
            };

            this.searchInfo = {
                curr: this.pageInfo.curr,
                total: this.pageInfo.total,
                size: this.pageInfo.size,
                status: "1",
                orderId: "",
                sku: "",
                orderDateFrom: "",
                orderDateTo: ""
            };

            this.downloadInfo = {
                orderType: 'client_sku'
            };

            this.channelConfigs = {
                vendorOperateType: 'SKU'
            };
            this.searchOrderStatus = [];
            this.data = [];

            this.orderInfoService.init().then(function(data){
                // 获取当前shipment
                this.currentShipment = data.currentShipment;

                // 获取可选的订单状态
                this.searchOrderStatus = data.searchOrderStatus;

                // 记录用户的操作方式(sku/order)
                this.channelConfigs = data.channelConfigs;

                this.search();
            });
        }

        OrderInfoController.prototype.search = function () {
            if (this.orderDateFrom === undefined || this.orderDateTo === undefined) {
                this.alert("'TXT_PLEASE_INPUT_A_VALID_DATE'");
                return;
            } else if (this.orderDateFrom)
                this.searchInfo.orderDateFrom = this.orderDateFrom.getTime();
            else this.searchInfo.orderDateFrom = undefined;
            if (this.orderDateTo) {
                var date = angular.copy(this.orderDateTo);
                date.setDate(date.getDate() + 1);
                this.searchInfo.orderDateTo = date.getTime();
            } else {
                this.searchInfo.orderDateTo = undefined;
            }
            this.searchInfo.curr = this.pageInfo.curr;
            this.searchInfo.size = this.pageInfo.size;
            this.orderInfoService.search(this.searchInfo).then(function (data) {
                this.pageInfo.total = data.orderInfo.total;
                this.data = data.orderInfo.orderList.map(function (item) {
                        item.className = '';
                        var date = new Date();
                        if (item.status == '7')
                            item.className = 'bg-gainsboro';
                        else if (item.status == '1') {
                            if (this.channelConfigs.vendorOperateType == 'ORDER') {
                                date = new Date(item.orderDateTime);
                            } else if (this.channelConfigs.vendorOperateType == 'SKU') {
                                date = new Date(item.orderDateTimestamp);
                            } else {
                                this.alert('TXT_MISSING_REQUIRED_CHANNEL_CONFIG');
                            }
                            if ((new Date().getTime() - date) >= this.threeDay)
                                item.className = 'bg-danger';
                            else if ((new Date().getTime() - date) >= this.twoDay)
                                item.className = 'bg-warning';

                        }
                        return item;
                    }
                )
            })
        };

        OrderInfoController.prototype.cancelOrder = function (item) {
            var self = this;
            this.confirm('TXT_CONFIRM_TO_CANCEL_ORDER').then(function () {
                self.orderInfoService.cancelOrder(item).then(function () {
                    self.search()
                })
            })
        };

        OrderInfoController.prototype.cancelSku = function (item) {
            var self = this;
            this.confirm('TXT_CONFIRM_TO_CANCEL_SKU').then(function () {
                self.orderInfoService.cancelSku(item).then(function () {
                    self.search();
                })
            })
        };

        OrderInfoController.prototype.downloadPickingList = function () {

            // this.orderInfoService.downloadPickingList().then(function (res) {
            //     console.info(res);
            // });
            $.download.post('/vms/order/order_info/downloadPickingList', {"orderType": this.downloadInfo.orderType});
        };

        OrderInfoController.prototype.toggleAll = function () {
            var collapse = (this.collapse = !this.collapse);
            this.data.forEach(function (item) {
                item.collapse = collapse;
            });
        };

        OrderInfoController.prototype.getStatusName = function (statusValue) {
            var currentStatus = this.searchOrderStatus.find(function (status) {
                return status.value == statusValue;
            });
            if (!currentStatus) return statusValue;
            return currentStatus.name;
        };

        OrderInfoController.prototype.popNewShipment = function () {
            var shipmentInfo = {
                shipment: this.currentShipment,
                searchOrderStatus: this.searchOrderStatus
            };
            this.popups.openShipment(shipmentInfo);
        };
        OrderInfoController.prototype.popAddToShipment = function () {
            this.popups.openAddShipment();
        };
        return OrderInfoController;

    }()));
});