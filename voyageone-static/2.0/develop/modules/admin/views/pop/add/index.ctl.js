/**
 * Created by sofia on 2016/8/10.
 */
define([
    'admin'
], function (admin) {
    admin.controller('AddController', (function () {
        function AddController(context, channelService, AdminCartService, $uibModalInstance) {
            this.sourceData = context ? context : {};
            this.append = context == 'add' ? true : false;
            this.channelService = channelService;
            this.AdminCartService = AdminCartService;
            this.popType = '编辑';
            this.companyId = this.sourceData.companyId;
            this.$uibModalInstance = $uibModalInstance;
        }

        AddController.prototype = {
            init: function () {
                var self = this;
                if (self.sourceData == 'add') {
                    self.popType = '添加';
                    self.sourceData = {}
                }
                self.channelService.getAllCompany().then(function (res) {
                    self.companyAllList = res.data;
                });
                self.AdminCartService.getAllCart().then(function (res) {
                    self.cartAllList = res.data
                });
                if (!self.sourceData.cartIds) return;
                self.AdminCartService.getCartByIds({'cartIds': self.sourceData.cartIds}).then(function (res) {
                    self.cartList = res.data;
                })
            },
            generate: function (type) {
                var self = this;
                if (type == 'secretKey') {
                    self.channelService.generateSecretKey().then(function (res) {
                        self.sourceData.screctKey = res.data;
                    })
                } else {
                    self.channelService.generateSessionKey().then(function (res) {
                        self.sourceData.sessionKey = res.data;
                    })
                }
            },
            selected:function (item) {
                console.log(item);
            },
            move: function (type) {
                var self = this;
                if (!self.sourceData.cartIds) {
                    self.cartList = [];
                    switch (type) {
                        case 'allInclude':
                            _.extend(self.cartList, self.cartAllList);
                            break;
                        case 'include':

                            break;
                        case 'exclude':

                            break;
                        case 'allExclude':
                            self.cartList = null;
                            break;
                    }
                } else {
                    switch (type) {
                        case 'allInclude':
                            self.AdminCartService.getCartByIds({'cartIds': self.sourceData.cartIds}).then(function (res) {
                                self.cartList = res.data;
                                _.extend(self.cartList, self.cartAllList);
                            });
                            break;
                        case 'include':

                            break;
                        case 'exclude':

                            break;
                        case 'allExclude':
                            self.cartList = null;
                            break;
                    }
                }
            },
            cancel: function () {
                this.$uibModalInstance.dismiss();
            },
            save: function () {
                var self = this;
                _.extend(self.sourceData, {'append': self.append, 'cartList': self.cartList});
                self.channelService.addOrUpdateChannel(self.sourceData).then(function (res) {
                    console.log(res);
                })
            }
        };
        return AddController;
    })())
});