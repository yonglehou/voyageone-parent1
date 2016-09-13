/**
 * Created by sofia on 2016/8/19.
 */
define([
    'admin',
    'modules/admin/controller/popup.ctl'
], function (admin) {
    admin.controller('HistoryConfigController', (function () {
        function HistoryConfigController(popups, alert, confirm, newShopService, selectRowsFactory) {
            this.popups = popups;
            this.alert = alert;
            this.confirm = confirm;
            this.selectRowsFactory = selectRowsFactory;
            this.newShopService = newShopService;
            this.pageOption = {curr: 1, size: 10, total: 0, fetch: this.search.bind(this)};
            this.historyList = [];
            this.historySelList = {selList: []};
            this.tempSelect = null;
            this.searchInfo = {
                channelId: '',
                channelName: '',
                modifiedFrom: '',
                modifiedTo: '',
                pageInfo: this.pageOption
            }
        }

        HistoryConfigController.prototype = {
            init: function () {
                var self = this;
                self.search();
            },
            search: function (page) {
                var self = this;
                page == 1 ? self.searchInfo.pageInfo.curr = 1 : page;
                self.newShopService.searchNewShopByPage({
                        'pageNum': self.searchInfo.pageInfo.curr,
                        'pageSize': self.searchInfo.pageInfo.size,
                        'channelId': self.searchInfo.channelId,
                        'channelName': self.searchInfo.channelName,
                        'modifiedFrom': self.searchInfo.modifiedFrom,
                        'modifiedTo': self.searchInfo.modifiedTo
                    })
                    .then(function (res) {
                        self.historyList = res.data.result;
                        self.pageOption.total = res.data.count;

                        // 设置勾选框
                        if (self.tempSelect == null) {
                            self.tempSelect = new self.selectRowsFactory();
                        } else {
                            self.tempSelect.clearCurrPageRows();
                            self.tempSelect.clearSelectedList();
                        }
                        _.forEach(self.historyList, function (Info) {
                            if (Info.updFlg != 8) {
                                self.tempSelect.currPageRows({
                                    "id": Info.id
                                });
                            }
                        });
                        self.historySelList = self.tempSelect.selectRowsInfo;
                    })
            },
            clear: function () {
                var self = this;
                self.searchInfo = {
                    pageInfo: this.pageOption,
                    channelId: '',
                    channelName: '',
                    modifiedFrom: '',
                    modifiedTo: ''
                }
            },
            edit: function (item) {
                var self = this;
                self.newShopService.getNewShopById(item.id).then(function (res) {
                    var data = res.data.data;
                    data.id = item.id;
                    window.sessionStorage.setItem('valueBean', data);
                    window.location.href = "#/newShop/guide?reload";
                })
            },
            delete: function (item) {
                var self = this;
                self.confirm('TXT_CONFIRM_DELETE_MSG').then(function () {
                        self.newShopService.deleteNewShop(item.id).then(function (res) {
                            if (res.data.success == false)self.confirm(res.data.message);
                            self.search(1);
                        })
                    }
                );
            }
        };
        return HistoryConfigController;
    })())
});