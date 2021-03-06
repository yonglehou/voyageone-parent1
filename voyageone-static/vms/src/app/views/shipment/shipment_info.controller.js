/**
 * Created by sofia on 7/6/2016.
 */
define([
    'vms'
], function (vms) {
    vms.controller('ShipmentInfoController', (function () {

        function ShipmentInfoController(popups, alert, notify, confirm, shipmentInfoService) {
            this.popups = popups;
            this.alert = alert;
            this.notify = notify;
            this.confirm = confirm;
            this.shipmentInfoService = shipmentInfoService;
            this.shipmentStatusList = [];
            this.pageInfo = {
                curr: 1,
                total: 0,
                size: 10,
                fetch: this.search.bind(this)
            };
            this.searchInfo = {
                curr: this.pageInfo.curr,
                total: this.pageInfo.total,
                size: this.pageInfo.size
            };

            this.oneDay = 24 * 60 * 60 * 1000;
            this.twoDay = 2 * this.oneDay;
            this.threeDay = 3 * this.oneDay;
            this.oneMonth = 30 * this.oneDay;
            this.searchInfo.shippedDateFrom = null;
            this.searchInfo.shippedDateTo = null;
        }

        ShipmentInfoController.prototype.init = function () {
            var self = this;
            self.shipmentInfoService.init().then(function (data) {
                self.shipmentStatusList = data.shipmentStatusList;
                self.channelConfig = data.channelConfig;
                var sessionSearchInfo = JSON.parse(sessionStorage.getItem('shipmentSearchInfo'));
                if (sessionSearchInfo) {
                    self.searchInfo = sessionSearchInfo;
                    self.pageInfo.curr = self.searchInfo.curr;
                    self.pageInfo.total = self.searchInfo.total;
                    self.pageInfo.size = self.searchInfo.size;
                    if (sessionSearchInfo.shippedDateFrom)
                        self.shippedDateFrom = new Date(sessionSearchInfo.shippedDateFrom);
                    if (sessionSearchInfo.shippedDateTo)
                        self.shippedDateTo = new Date(sessionSearchInfo.shippedDateTo);
                }
                self.search(self.searchInfo.curr);
            });
        };

        ShipmentInfoController.prototype.search = function (curr) {
            var self = this;
            if (self.shippedDateFrom)
                self.searchInfo.shippedDateFrom = self.shippedDateFrom;
            else self.searchInfo.shippedDateFrom = undefined;
            if (self.shippedDateTo) {
                self.searchInfo.shippedDateTo = self.shippedDateTo;
            } else self.searchInfo.shippedDateTo = undefined;
            self.pageInfo.curr = self.searchInfo.curr = curr;
            self.pageInfo.size = self.searchInfo.size = self.pageInfo.size;
            var req = angular.copy(self.searchInfo);
            if (self.shippedDateTo) {
                var date = angular.copy(self.shippedDateTo);
                date.setDate(date.getDate() + 1);
                req.shippedDateTo = date;
            } else {
                req.shippedDateTo = undefined;
            }
            self.shipmentInfoService.search(req).then(function (data) {
                self.pageInfo.total = data.shipmentInfo.total;
                self.data = data.shipmentInfo.shipmentList;
            });
            sessionStorage.setItem('shipmentSearchInfo', JSON.stringify(self.searchInfo));
        };

        ShipmentInfoController.prototype.popShipment = function (shipment, type) {
            var self = this;
            //1:Open；3：Shipped；4：Arrived；5：Received；6：Receive Error
            var pendingShipmentStatus = "1";
            if (type == "edit") {
                pendingShipmentStatus = shipment.status;
            } else if (type == "end") {
                pendingShipmentStatus = "3";
            }
            var popShipment = angular.copy(shipment);
            popShipment.orderTotal = 0;
            popShipment.skuTotal = 0;
            delete popShipment.$$hashKey;
            var shipmentInfo = {
                shipment: popShipment,
                channelConfig: self.channelConfig,
                type: type,
                pendingShipmentStatus: pendingShipmentStatus,
                statusList: this.shipmentStatusList
            };

            this.popups.openShipment(shipmentInfo).then(function () {
                self.search(self.searchInfo.curr);
            });
        };

        ShipmentInfoController.prototype.getStatusName = function (statusValue) {
            var self = this;
            for (var i = 0; i < self.shipmentStatusList.length; i++) {
                if (self.shipmentStatusList[i].value == statusValue) {
                    var currentStatus = self.shipmentStatusList[i];
                    break;
                }
            }
            if (!currentStatus) return statusValue;
            return currentStatus.name;
        };

        ShipmentInfoController.prototype.isPrinted = function (item) {
            var self = this;
            return item.printed ? 'TXT_YES' : 'TXT_NO';
        };

        ShipmentInfoController.prototype.configTitle = function (title, model) {
            if (model) return title;
            return '';
        };

        return ShipmentInfoController;

    }()));
});