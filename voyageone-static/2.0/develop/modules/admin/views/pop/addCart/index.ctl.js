/**
 * Created by sofia on 2016/8/10.
 */
define([
    'admin'
], function (admin) {
    admin.controller('CartAddController', (function () {
        function CartAddController(context, AdminCartService, $uibModalInstance) {
            this.sourceData = context ? angular.copy(context) : {};
            this.append = context == 'add' ? true : false;
            this.context = context;
            this.AdminCartService = AdminCartService;
            this.popType = '编辑';
            this.companyId = this.sourceData.companyId;
            this.$uibModalInstance = $uibModalInstance;
        }

        CartAddController.prototype = {
            init: function () {
                var self = this;
                if (self.sourceData == 'add') {
                    self.popType = '添加';
                    self.sourceData = {}
                }
                self.sourceData.active = self.sourceData.active!=null ? self.sourceData.active ? "0" : "1" : '';
                self.AdminCartService.getAllPlatform().then(function (res) {
                    self.platformAllList = res.data;
                });
            },
            cancel: function () {
                var result = {res: 'failure'};
                this.$uibModalInstance.close(result);
            },
            save: function () {
                var self = this;
                var result = {};
                self.sourceData.active = self.sourceData.active == '0' ? true : false;
                _.extend(self.context, self.sourceData);
                if (self.append == true) {
                    self.AdminCartService.addCart(self.sourceData).then(function (res) {
                        if (res.data == false) {
                            self.confirm(res.data.message);
                            return;
                        }
                        _.extend(result, {'res': 'success', 'sourceData': self.context});
                        self.$uibModalInstance.close(result);
                    })
                } else {
                    self.AdminCartService.updateCart(self.sourceData).then(function (res) {
                        if (res.data == false) {
                            self.confirm(res.data.message);
                            return;
                        }
                        _.extend(result, {'res': 'success', 'sourceData': self.context});
                        self.$uibModalInstance.close(result);
                    })
                }
            }
        };
        return CartAddController;
    })())
});