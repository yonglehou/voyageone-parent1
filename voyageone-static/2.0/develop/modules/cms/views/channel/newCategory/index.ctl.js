define([
    'cms',
    './sortEnum',
    'modules/cms/directives/platFormStatus.directive'
], function (cms, sortEnum) {

    cms.controller("newCategoryController", (function () {

        function NewCategoryCtl($routeParams, productTopService, alert, notify, confirm, $filter) {
            var self = this;
            self.routeParams = angular.fromJson($routeParams.cartInfo);
            self.sortList = sortEnum.getSortByCd(this.routeParams.cartId);
            self.productTopService = productTopService;
            self.searchInfo = {};
            self.sort = {};
            self.codeStr = '';
            self.isSeachAdd = false;
            self.paging = {
                curr: 1, total: 0, size: 10, fetch: function () {
                    self.search();
                }
            };
            self.notify = notify;
            self.confirm = confirm;
            self.$filter = $filter;
            self.alert = alert;
        }

        NewCategoryCtl.prototype.init = function () {
            //初始化方法
            var self = this,
                productTopService = self.productTopService,
                routeParams = self.routeParams;

            productTopService.init({catId: routeParams.catId}).then(function (res) {
                self.brandList = res.data.brandList;

                self.sort = _.find(self.sortList, function (ele) {
                    return ele.sValue == res.data.sortColumnName;
                });
                if (self.sort)
                    self.sort.sortType = res.data.sortType;
            });

            self.search();
            self.getTopList();
        };

        NewCategoryCtl.prototype.clear = function () {
            //清空查询条件
            this.searchInfo = {};
            this.codeStr = "";
        };

        /**
         * 查询操作  调用查询结果集和获取总数两个接口
         * @param sortInfo
         */
        NewCategoryCtl.prototype.search = function () {
            var self = this,
                data = self.getSearchInfo(),
                paging = self.paging,
                sort = self.sort,
                productTopService = self.productTopService;

            if (sort) {
                data.sortColumnName = sort.sValue;
                data.sortType = sort.sortType;
            }

            self.productTopService.getPage(_.extend(paging, data)).then(function (res) {
                if(res.data.length == 0 && self.paging.curr > 1){
                    self.paging.curr = self.paging.curr - 1;
                    self.search();
                    return;
                }

                self.modelList = res.data;
            });

            productTopService.getCount(self.getSearchInfo()).then(function (res) {
                self.paging.total = res.data;
            });

            this.selAll = false;
        };

        /**
         * @returns 返回查询上行参数
         */
        NewCategoryCtl.prototype.getSearchInfo = function () {
            var self = this,
                upEntity = angular.copy(self.searchInfo);

            upEntity.cartId = this.routeParams.cartId;
            upEntity.sellerCatId = this.routeParams.catId;
            upEntity.sellerCatPath = this.routeParams.catPath;
            upEntity.codeList = self.codeStr.split("\n");

            return upEntity;
        };

        /**
         * 排序字段保存 搜索
         * @param sortColumnName
         * @param sortType
         */
        NewCategoryCtl.prototype.sortSearch = function (sortColumnName, sortType) {
            var self = this,
                _sort,
                routeParams = self.routeParams;

            if (!sortColumnName || !sortType)
                return;

            _sort = _.find(self.sortList, function (ele) {
                return ele.sValue == sortColumnName.replace("✓", routeParams.cartId);
            });

            if (self.sort)
                _sort.sortType = sortType;

            self.sort = _sort;

            self.search();
        };

        /**
         * 普通商品区全选操作
         * @param $event
         */
        NewCategoryCtl.prototype.selectAll = function () {
            var checked = this.selAll;

            for (var i = 0; i < this.modelList.length; i++) {
                this.modelList[i].isChecked = checked;
            }
        };

        /**
         * 获取选中商品的code
         * @returns Array
         */
        NewCategoryCtl.prototype.getSelectedCodeList = function () {

            var codeList = [];

            if (this.modelList && this.modelList.length > 0) {
                for (var i = 0, length = this.modelList.length; i < length; i++) {
                    if (this.modelList[i].isChecked) {
                        codeList.push(this.modelList[i].code);
                    }
                }
            }

            return codeList;
        };

        /**
         * 加入置顶区
         */
        NewCategoryCtl.prototype.addTopProductClick = function () {
            var self = this,
                routeParams = self.routeParams,
                confirm = self.confirm,
                parameter = {};

            if (self.isSeachAdd) {
                //全量加入
                parameter.isSeachAdd = self.isSeachAdd;
                parameter.searchParameter = self.getSearchInfo();
            } else {
                var codeList = self.getSelectedCodeList();
                if (codeList.length == 0) {
                    self.alert("请选择商品");
                    return;
                }
                parameter.codeList = codeList;
            }

            parameter.cartId = routeParams.cartId;
            parameter.sellerCatId = routeParams.catId;

            if (self.isSeachAdd) {
                confirm("您是否要全量移入置顶区").then(function () {
                    self.callAddTopProduct(parameter);
                });
            } else
                self.callAddTopProduct(parameter);
        };

        NewCategoryCtl.prototype.callAddTopProduct = function (para) {
            var self = this;

            self.productTopService.addTopProduct(para).then(function () {
                self.search();
                self.getTopList();
                self.isSeachAdd = false;
                self.notify.success('提交成功');

            });
        };

        /**
         * 获取置顶区商品信息
         */
        NewCategoryCtl.prototype.getTopList = function () {
            var self = this,
                routeParams = self.routeParams;

            self.productTopService.getTopList({
                cartId: routeParams.cartId,
                sellerCatId: routeParams.catId
            }).then(function (res) {
                self.topList = res.data;
            });
        };

        /**
         * 获取置顶区商品的code
         * @returns {Array}
         */
        NewCategoryCtl.prototype.getTopCodeList = function () {
            var self = this,
                codeList = [];

            if (self.topList) {
                for (var i = 0, length = self.topList.length; i < length; i++) {
                    codeList.push(this.topList[i].code);
                }
            }

            return codeList;
        };

        /**
         * 保存置顶区商品
         */
        NewCategoryCtl.prototype.saveTopProduct = function () {
            var self = this,
                routeParams = self.routeParams;

            self.productTopService.saveTopProduct({
                cartId: routeParams.cartId,
                sellerCatId: routeParams.catId,
                codeList: self.getTopCodeList()
            }).then(function () {
                self.notify.success('保存成功');
                //刷新普通商品区
                self.search();
            });
        };

        /**
         * 置顶区清空
         */
        NewCategoryCtl.prototype.clearTopProduct = function () {
            var self = this,
                confirm = self.confirm;

            confirm("您是否确定要清空置顶区？").then(function () {
                self.topList = [];
                self.saveTopProduct();

            });

        };


        NewCategoryCtl.prototype.remove = function (index) {
            this.topList.splice(index, 1);
        };

        NewCategoryCtl.prototype.moveKeys = {
            up: 'up',
            upToTop: 'upToTop',
            down: 'down',
            downToLast: 'downToLast'
        };

        NewCategoryCtl.prototype.moveProduct = function moveProduct(i, moveKey) {
            var self = this,
                source = self.topList,
                moveKeys = self.moveKeys,
                temp;

            switch (moveKey) {
                case moveKeys.up:
                    if (i === 0)
                        return;
                    temp = source[i];
                    source[i] = source[i - 1];
                    source[i - 1] = temp;
                    return;
                case moveKeys.upToTop:
                    if (i === 0)
                        return;
                    temp = source.splice(i, 1);
                    source.splice(0, 0, temp[0]);
                    return;
                case moveKeys.down:
                    if (i === source.length - 1)
                        return;
                    temp = source[i];
                    source[i] = source[i + 1];
                    source[i + 1] = temp;
                    return;
                case moveKeys.downToLast:
                    if (i === source.length - 1)
                        return;
                    temp = source.splice(i, 1);
                    source.push(temp[0]);
                    return;
            }
        };

        NewCategoryCtl.prototype.showPriceSale = function (priceLow, priceHigh) {
            var $filter = this.$filter;

            if (!priceLow || !priceHigh)
                return '';

            if (priceLow === priceHigh)
                return $filter('currency')(priceLow, '');
            else
                return $filter('currency')(priceLow, '') + "~"
                    + $filter('currency')(priceHigh, '');
        };

        return NewCategoryCtl;

    })());

});