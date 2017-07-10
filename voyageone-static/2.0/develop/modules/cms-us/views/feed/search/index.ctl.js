define([
    'cms'
], function (cms) {

    cms.controller('feedSearchController', class FeedSearchController {

        constructor($rootScope, popups, itemDetailService) {
            let self = this;

            self.$rootScope = $rootScope;
            console.log('auth', self.$rootScope.auth);
            self.popups = popups;
            self.feedListTotal = 0;
            self.paraMap = {
                status:"",
                approvePricing:null
            };
            //权限控制,默认为最低权限1:new,2:Pending,3:Ready
            self.flag = 3;
            self.status = [false, false, false];
            self.approvePricing = [false, false];
            self.itemDetailService = itemDetailService;
            self.paging = {
                curr: 1, total: 0, fetch: function () {
                    self.getList();
                }
            };

            //设置状态的默认选中
            if(self.flag == 1){
                self.status[0] = true;
            }
            if(self.flag == 2){
                self.status[1] = true;
            }
            if(self.flag == 3){
                self.status[2] = true;
            }

        }

        getList() {
            let self = this;

            if (self.status[0] === true) {
                self.paraMap.status += "New_";
            }
            if (self.status[1] === true) {
                self.paraMap.status += "Pending_";
            }
            if (self.status[2] === true) {
                self.paraMap.status += "Ready_";
            }
            if (self.approvePricing[0] === true && self.approvePricing[1] == false) {
                self.paraMap.approvePricing = "1";
            }
            if (self.approvePricing[1] === true && self.approvePricing[0] === false) {
                self.paraMap.approvePricing = "0";
            }

            self.itemDetailService.list(_.extend(self.paraMap, self.paging)).then(resp => {
                self.feeds = resp.data.feedList;
                self.feedListTotal = resp.data.feedListTotal;
                self.paging.total = resp.data.feedListTotal;
                //对页面显示的价格区间进行转化
                if(self.feeds != null){
                    angular.forEach(self.feeds,function(feed){
                        var skus = feed.skus;
                        var arr1 = [];
                        var arr2 = [];
                        angular.forEach(skus,function(value){
                            arr1.push(value.priceMsrp);
                            arr2.push(value.priceNet);
                        })
                        arr1.sort();
                        arr2.sort();
                        feed.priceMsrps = [arr1[0],arr1[arr1.length-1]];
                        feed.priceNets = [arr2[0],arr2[arr2.length-1]];

                    })
                }

            });

        }

        clear() {
            let self = this;
            self.paraMap = {
                status:"",
                approvePricing:null
            };
            self.status = [false, false, false];
            self.isApprove = [false, false];
            self.paging.curr =  1;
            self.paging.total = 0;

        }

        popBatchApprove() {
            let self = this;

            self.popups.openBatchApprove();
        }

        selectAll() {
            let self = this;

            _.each(self.feeds,feed => {
                feed.check = self.isAll;
            });
        }

    });

});