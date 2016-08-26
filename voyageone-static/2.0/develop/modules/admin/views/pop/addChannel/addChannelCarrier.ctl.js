/**
 * Created by sofia on 2016/8/19.
 */
define([
    'admin'
], function (admin) {
    admin.controller('AddChannelCarrierController', (function () {
        function AddChannelCarrierController(context, channelService, popups, carrierConfigService, $uibModalInstance) {
            this.sourceData = context ? context : {};
            this.append = context == 'add' ? true : false;
            this.popups = popups;
            this.channelService = channelService;
            this.carrierConfigService = carrierConfigService;
            this.popType = '编辑';
            this.$uibModalInstance = $uibModalInstance
        }

        AddChannelCarrierController.prototype = {
            init: function () {
                var self = this;
                if (self.sourceData == 'add') {
                    self.popType = '添加';
                    self.sourceData = {}
                }
                self.sourceData.active = self.sourceData.active ?  self.sourceData.active ? "0" : "1":'';
                self.channelService.getAllChannel().then(function (res) {
                    self.channelAllList = res.data;
                });
                self.carrierConfigService.getAllCarrier().then(function (res) {
                    self.carrierList = res.data;
                });
            },
            cancel: function () {
                this.$uibModalInstance.close();
            },
            save: function () {
                var self = this;
                var result = {};
                self.sourceData.active = self.sourceData.active == '0' ? true : false;
                if (self.append == true) {
                    self.carrierConfigService.addCarrierConfig(self.sourceData).then(function (res) {
                        _.extend(result, {'res': 'success', 'sourceData': self.sourceData});
                        self.$uibModalInstance.close(result);
                    })
                } else {
                    self.carrierConfigService.updateCarrierConfig(self.sourceData).then(function (res) {
                        _.extend(result, {'res': 'success', 'sourceData': self.sourceData});
                        self.$uibModalInstance.close(result);
                    })
                }
            }
        };
        return AddChannelCarrierController;
    })())
});