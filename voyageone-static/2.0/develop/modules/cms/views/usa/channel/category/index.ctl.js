/**
 * @description 美国店铺内分类
 * @author piao
 */
define([
    'cms',
    'modules/cms/enums/Carts',
    './sortEnum',
    'modules/cms/directives/navBar.directive',
    'modules/cms/service/search.util.service'
], function (cms, carts, sortEntity) {

    cms.controller('usCategoryController', class UsCategoryController {

        constructor($routeParams, advanceSearch, productTopService, alert, notify, confirm, $filter, popups, selectRowsFactory, $parse, searchUtilService) {
            let self = this;

            self.sortEntity = sortEntity;
            self.$routeParams = $routeParams;
            self.srInstance = new selectRowsFactory();
            self.productTopService = productTopService;
            self.alert = alert;
            self.notify = notify;
            self.confirm = confirm;
            self.$filter = $filter;
            self.$parse = $parse;
            self.advanceSearch = advanceSearch;
            self.tempUpEntity = {};
            self.searchResult = {};
            self.masterData = {};
            self.productSelList = {selList: []};
            self.catInfo = angular.fromJson(this.$routeParams.category);
            self.popups = popups;
            self.searchInfo = {
                cartId: carts.Sneakerhead.id,
                codeList: ''
            };
            self.carts = carts;
            self.customColumns = {
                selCommonProps: [],
                selPlatformAttributes: [],
                selPlatformSales: []
            };
            self.columnArrow = {};
            self.moveKeys = {
                up: 'up',
                upToTop: 'upToTop',
                down: 'down',
                downToLast: 'downToLast'
            };
            self.pageOption = {
                curr: 1, total: 0, size: 10, fetch: function () {
                    self.search();
                }
            };
            self.searchUtilService = searchUtilService;
            self.sort = sortEntity[1];
        }

        init() {
            let self = this,
                _sort,
                catInfo = self.catInfo;

            //初始化右侧搜索初始化
            this.advanceSearch.init().then(res => {
                if (res.data) {
                    // 美国平台、中国平台
                    let channelPlatforms = res.data.platforms;

                    self.masterData.usPlatforms = _.filter(channelPlatforms, cartObj => {
                        let cartId = parseInt(cartObj.value);
                        return cartId > 0 && cartId < 20;
                    });
                    // 品牌列表
                    self.masterData.brandList = res.data.brandList;
                    self.masterData.freeTags = {};
                    _.each(res.data.freeTags, freeTag => {
                        self.masterData.freeTags[freeTag.tagPath] = freeTag;
                    });

                    // 用户自定义列
                    self.customColumnNames = {};
                    self.customColumns.commonProps = res.data.commonProps;
                    self.customColumns.platformAttributes = res.data.platformAttributes;
                    self.customColumns.platformSales = res.data.platformSales;
                    self.customColumns.selCommonProps = self.searchUtilService.getSelectedProps(res.data.commonProps, res.data.selCommonProps, 'propId', self);
                    self.customColumns.selPlatformAttributes = self.searchUtilService.getSelectedProps(res.data.platformAttributes, res.data.selPlatformAttributes, 'value', self);
                    self.customColumns.selPlatformSales = res.data.selPlatformSales;
                }
            });

            //获取保存的排序结果
            self.productTopService.init({cartId: carts.Sneakerhead.id, catId: catInfo.catId}).then(function (res) {

                _.each(self.sortEntity, (value) => {
                    if (value['sortValue'] === res.data.sortColumnName && Number(value['sortType']) === res.data.sortType) {
                        _sort = value;
                    }
                });

                if (_sort) {
                    self.sort = _sort;

                    self.combineSort();
                } else {
                    self.search();
                }

            });

            //左侧栏top50
            self.getTopList();

        }

        clear() {
            let self = this;
            self.searchInfo = {
                brandSelType: 1,
                pCatPathType: 1,
                shopCatType: 1
            };
        }

        dismiss(attrName) {
            this.searchInfo[attrName] = null;
        }

        search() {
            let self = this,
                upEntity = self.searchUtilService.handleQueryParams(self);

            upEntity.cidValue = [self.catInfo.catId];
            upEntity.shopCatType = 1;

            self.srInstance.clearCurrPageRows();
            self.advanceSearch.search(upEntity).then(res => {
                if (res.data) {
                    self.searchResult.productList = res.data.productList;
                    self.pageOption.total = res.data.productListTotal;

                    self.searchResult.productList.forEach(productInfo => {
                        // self.setFreeTagList(productInfo);
                        self.srInstance.currPageRows({
                            "id": productInfo.prodId,
                            "code": productInfo.common.fields["code"]
                        });
                    });

                    self.productSelList = self.srInstance.selectRowsInfo;

                }
            });

            this.selAll = false;
        }

        getTopList() {
            let self = this;
            self.productTopService.getTopList({
                "cartId": carts.Sneakerhead.id,
                "sellerCatId": self.catInfo.catId
            }).then(res => {
                if (res.data) {
                    self.topList = res.data;
                }
            });
        }

        addTopProductClick(productInfo) {
            let self = this,
                parameter = {};
            parameter.codeList = [productInfo.common.fields.code];

            parameter.cartId = carts.Sneakerhead.id;
            parameter.sellerCatId = self.catInfo.catId;
            self.callAddTopProduct(parameter);
        };

        callAddTopProduct(para) {
            let self = this;

            self.productTopService.addTopProduct(para).then(function () {
                self.search();
                self.getTopList();
                self.isSeachAdd = false;
                self.notify.success('提交成功');

            });
        };

        getTopCodeList() {
            let self = this,
                codeList = [];

            if (self.topList) {
                for (let i = 0, length = self.topList.length; i < length; i++) {
                    codeList.push(this.topList[i].code);
                }
            }

            return codeList;
        };

        /**
         * 保存置顶区商品
         */
        saveTopProduct() {
            let self = this,
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

        moveProduct(i, moveKey) {
            let self = this,
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


        popUsFreeTag() {
            let self = this;

            self.popups.openUsFreeTag({
                orgFlg: '1',
                tagType: '4',
                orgChkStsMap: self.searchInfo.usFreeTags
            }).then(res => {
                self.searchInfo.usFreeTagsOption = res.selectdTagList;
                self.searchInfo.usFreeTags = _.pluck(res.selectdTagList, 'tagPath');
            });

        }

        // 自定义列弹出
        popCustomAttributes() {
            let self = this;
            self.popups.openCustomAttributes().then(res => {
                self.customColumnNames = {};
                self.customColumns.selCommonProps = self.searchUtilService.getSelectedProps(self.customColumns.commonProps, res.selCommonProps, 'propId', self);
                self.customColumns.selPlatformAttributes = self.searchUtilService.getSelectedProps(self.customColumns.platformAttributes, res.selPlatformAttributes, 'value', self);
                self.customColumns.selPlatformSales = res.selPlatformSales;
            })
        }

        popBatchPrice(cartId) {
            let self = this;
            if (self.getSelectedProduct('code').length == 0) {
                self.alert("please choose at least one!!!");
                return;
            }
            self.popups.openBatchPrice({
                selAll: self._selall,
                codeList: self.getSelectedProduct('code'),
                queryMap: self.searchUtilService.handleQueryParams(),
                cartId: cartId ? cartId : 0
            }).then(res => {
                //根据返回参数确定勾选状态,"1",需要清除勾选状态,"0"不需要清除勾选状态
                if (res.success == "1") {
                    //需要清除勾选状态
                    self.clearSelList();
                    self._selall = 0;
                }
                if (res.type == 1) {
                    self.notify.success('Update Success');
                } else {
                    self.alert('Update Defeated');
                }
            });
        }

        // 批量修改Free tags
        /**
         * 添加产品到指定自由标签
         */
        addFreeTag() {
            let self = this;
            let selCodeList = self.getSelectedProduct('code');
            if (!self._selall && _.size(selCodeList) == 0) {
                self.alert("Please select at least one record.");
                return;
            }
            let params = {
                orgFlg: '2',
                selTagType: '6',
                selAllFlg: self._selall ? 1 : 0,
                selCodeList: self.getSelectedProduct('code'),
                searchInfo: self.searchUtilService.handleQueryParams()
            };
            self.popups.openUsFreeTag(params).then(res => {
                let msg = '';
                if (_.size(res.selectdTagList) > 0) {
                    let freeTagsTxt = _.chain(res.selectdTagList).map(function (key, value) {
                        return key.tagPathName;
                    }).value();
                    msg = "Set free tags for selected products:<br>" + freeTagsTxt.join('; ');
                } else {
                    msg = "Clear free tags for selected products";
                }
                let freeTags = _.chain(res.selectdTagList).map(function (key, value) {
                    return key.tagPath;
                }).value();
                self.confirm(msg)
                    .then(function () {
                        var data = {
                            "type": "usa",
                            "tagPathList": freeTags,
                            "prodIdList": selCodeList,
                            "isSelAll": self._selall ? 1 : 0,
                            "orgDispTagList": res.orgDispTagList,
                            'searchInfo': self.searchUtilService.handleQueryParams()
                        };
                        self.$searchAdvanceService2.addFreeTag(data).then(function () {
                            // notify.success($translate.instant('TXT_MSG_SET_SUCCESS'));
                            self.notify.success("Set free tags succeeded.");
                            self.clearSelList();
                            self._selall = false;
                            self.search();
                        })
                    });
            });
        };

        //进行上下架操作
        batchList(cartId, activeStatus, usPlatformName) {
            let self = this;
            if (self.getSelectedProduct('code').length == 0) {
                self.alert("please choose at least one!!!");
                return;
            }
            self.popups.openUsList({
                selAll: self._selall,
                codeList: self.getSelectedProduct('code'),
                queryMap: self.searchUtilService.handleQueryParams(),
                cartId: cartId ? cartId : 0,
                //操作状态1为上架,0为下架
                activeStatus: activeStatus,
                usPlatformName: usPlatformName
            }).then(res => {
                if (res.success == "1") {
                    //需要清除勾选状态
                    self.clearSelList();
                    self._selall = 0;
                }
                self.notify.success('Update Success');
            });
        }

        /**
         * 获取自由标签tagName
         * @param productInfo
         */
        setFreeTagList(productInfo) {
            let self = this,
                _usFreeTags = [];

            productInfo.usFreeTags.forEach(tag => {
                let _tag = self.masterData.freeTags[tag];

                _usFreeTags.push(_tag.tagName);
            });

            productInfo._usFreeTags = _usFreeTags;

        }

        /**
         * 获取选中产品 getSelectedProduct('code')
         * @param  id  or code
         * @returns {Array}
         */
        getSelectedProduct(onlyAttr) {
            let self = this;

            if (onlyAttr) {
                return _.pluck(self.productSelList.selList, onlyAttr);
            } else {
                return self.productSelList.selList;
            }
        }


        /**
         * 检索列排序
         * sortType 1:升序  -1:降序
         * */
        columnOrder(columnName, sortType) {
            let self = this,
                column,
                columnArrow = self.columnArrow;

            _.forEach(columnArrow, function (value, key) {
                if (key != columnName)
                    columnArrow[key] = null;
            });

            column = columnArrow[columnName];

            if (!column) {
                column = {};
                column.mark = 'unsorted';
                column.count = null;
            }

            column.count = !column.count;

            //偶数升序，奇数降序
            if (column.count) {
                column.mark = 'sort-desc';
            } else {
                column.mark = 'sort-up';
            }

            if (sortType) {
                column.mark = sortType === '1' ? 'sort-up' : 'sort-desc';
            }

            columnArrow[columnName] = column;

            self.searchByOrder(columnName, column.mark);
        };

        searchByOrder(columnName, sortOneType) {
            let self = this,
                searchInfo = self.searchInfo;

            searchInfo.sortOneName = columnName;
            searchInfo.sortOneType = sortOneType == 'sort-up' ? '1' : '-1';

            self.search();

        }

        getArrowName(columnName) {
            let columnArrow = this.columnArrow;

            if (!columnArrow || !columnArrow[columnName])
                return 'unsorted';

            return columnArrow[columnName].mark;
        };

        getProductValue(element, prop) {
            let self = this;
            return self.searchUtilService.getProductValue(element, prop);
        }

        /**
         * 保存默认排序结果
         * @param type
         */
        sortSave(type) {
            let self = this;

            self.sort = self.sortEntity[type];

            self.combineSort();

            self.productTopService.saveSortColumnName({
                cartId: 8,
                sellerCatId: self.catInfo.catId,
                sortColumnName: self.sort.sortValue,
                sortType: self.sort.sortType
            }).then(() => {

                self.notify.success('Save Success');

            });

        }

        combineSort() {
            let self = this;

            self.columnOrder(self.sort.sortValue, self.sort.sortType);

            self.getTopList();

        }

        /**
         * 保存置顶区商品
         */
        saveTopProduct() {
            let self = this;

            self.confirm('Comfirm to save this produts?').then(() => {
                self.productTopService.saveTopProduct({
                    cartId: 8,
                    sellerCatId: self.catInfo.catId,
                    codeList: self.getTopCodeList()
                }).then(function () {
                    self.notify.success('save success!');
                });
            });


        }

        showPriceSale(priceLow, priceHigh) {
            var $filter = this.$filter;

            if (!priceLow || !priceHigh)
                return '';

            if (priceLow === priceHigh)
                return $filter('currency')(priceLow, '');
            else
                return $filter('currency')(priceLow, '') + "~"
                    + $filter('currency')(priceHigh, '');
        };

    });

});