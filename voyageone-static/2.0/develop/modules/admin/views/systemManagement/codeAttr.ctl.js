/**
 * Created by sofia on 2016/8/23.
 */
define([
    'admin',
    'modules/admin/controller/popup.ctl'
], function (admin) {
    admin.controller('CodeAttributeController', (function () {
        function CodeAttributeController(popups, alert, confirm, codeService, selectRowsFactory) {
            this.popups = popups;
            this.alert = alert;
            this.confirm = confirm;
            this.selectRowsFactory = selectRowsFactory;
            this.codeService = codeService;
            this.channelPageOption = {curr: 1, size: 10, total: 0, fetch: this.search.bind(this)};
            this.systemList = [];
            this.codeSelList = {selList: []};
            this.tempSelect = null;
            this.searchInfo = {
                code: '',
                name: '',
                des: '',
                pageInfo: this.channelPageOption
            }
        }

        CodeAttributeController.prototype = {
            init: function () {
                var self = this;
                self.search(1);
            },
            search: function (page) {
                var self = this;
                page == 1 ? self.searchInfo.pageInfo.curr = 1 : page;
                self.codeService.searchCodeByPage({
                        'pageNum': self.searchInfo.pageInfo.curr,
                        'pageSize': self.searchInfo.pageInfo.size,
                        'code': self.searchInfo.code,
                        'name': self.searchInfo.name,
                        'des': self.searchInfo.des
                    })
                    .then(function (res) {
                        self.systemList = res.data.result;
                        self.channelPageOption.total = res.data.count;

                        // 设置勾选框
                        if (self.tempSelect == null) {
                            self.tempSelect = new self.selectRowsFactory();
                        } else {
                            self.tempSelect.clearCurrPageRows();
                            self.tempSelect.clearSelectedList();
                        }
                        _.forEach(self.systemList, function (Info) {
                            if (Info.updFlg != 8) {
                                self.tempSelect.currPageRows({
                                    "id": Info.id,
                                    "code": Info.comment
                                });
                            }
                        });
                        self.codeSelList = self.tempSelect.selectRowsInfo;
                    })
            },
            clear: function () {
                var self = this;
                self.searchInfo = {
                    code: '',
                    name: '',
                    des: '',
                    pageInfo: self.channelPageOption
                }
            },
            edit: function () {
                var self = this;
                if (self.codeSelList.selList.length <= 0) {
                    self.alert('TXT_MSG_NO_ROWS_SELECT');
                    return;
                } else {
                    _.forEach(self.systemList, function (Info) {
                        if (Info.id == self.codeSelList.selList[0].id) {
                            self.popups.openTypeAdd(Info).then(function(){
                                self.search(1);
                            });
                        }
                    })
                }

            },
            delete: function () {
                var self = this;
                self.confirm('TXT_CONFIRM_DELETE_MSG').then(function () {
                        var delList = [];
                        _.forEach(self.codeSelList.selList, function (delInfo) {
                            delList.push(delInfo.id);
                        });
                        self.typeService.deleteType(delList).then(function (res) {
                            if (res.data.success == false)self.confirm(res.data.message);
                            self.search(1);
                        })
                    }
                );
            }
        };
        return CodeAttributeController;
    })())
});