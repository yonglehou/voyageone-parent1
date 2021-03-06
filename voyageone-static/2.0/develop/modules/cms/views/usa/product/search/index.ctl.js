/**
 * @description 高级检索
 *              由于和类目逻辑类似请尽量把共同部分写在searchUtilService
 *
 * @author piao
 */
define([
    'cms',
    'modules/cms/enums/Carts',
    'modules/cms/directives/platFormStatus.directive',
    'modules/cms/directives/navBar.directive',
    'modules/cms/service/search.util.service'
], function (cms,cartEntity) {

    cms.controller('usProductSearchController', class UsProductSearchController {

        constructor(popups, advanceSearch,selectRowsFactory,$parse,$translate,alert,confirm,$searchAdvanceService2,notify,$routeParams,$rootScope,searchUtilService) {
            let self = this;

            self.accordionOpen = false;
            self.popups = popups;
            self.srInstance = new selectRowsFactory();
            self.$rootScope = $rootScope;
            self.$parse = $parse;
            self.$translate = $translate;
            self.alert = alert;
            self.confirm = confirm;
            self.advanceSearch = advanceSearch;
            self.$searchAdvanceService2 = $searchAdvanceService2;
            self.notify = notify;
            self.$routeParams = $routeParams;
            self.searchUtilService = searchUtilService;
            self.pageOption = {curr: 1, total: 0, size: 10, fetch: function(){
                self.query();
            }};

            self.defaultSearchInfo = {
                brandSelType:1, // brand include
                pCatPathType:1, // 平台类目
                shopCatType:1,  // 店铺内分类
                // 默认排序条件
                sortOneName:'created',
                sortOneType:"-1"
            };
            // 检索条件
            self.searchInfo = angular.copy(self.defaultSearchInfo);
            // 检索结果
            self.searchResult = {
                productList: []
            };
            self.tempUpEntity = {};
            self.productSelList =  {selList: []};
            self.masterData = {
                platforms: [],
                usPlatforms: [],
                brandList: [],
                platformStatus:[
                    {status:'Pending',display:'Pending'},
                    {status:'OnSale', display:'List'},
                    {status:'InStock', display:'Delist'}
                ]
            };
            self.tranferData = {};
            self.customColumns = {
                commonProps:[],
                platformAttributes:[],
                platformSales:[],
                selCommonProps:[],
                selPlatformAttributes:[],
                selPlatformSales:[]
            };
            self.platformSalesEntity = {};
            self.columnArrow = {};
            self.cartEntity = cartEntity;

            if (self.$routeParams.code) {
                let routePrams = eval('(' + self.$routeParams.code + ')');
                let cartId = routePrams.cartId;
                let platformStatus = routePrams.platformStatus;
                if (cartId && platformStatus) {
                    self.searchInfo.cartId = cartId;
                    if (!self.searchInfo.platformStatus) {
                        self.searchInfo.platformStatus = {};
                    }
                    // 如果状态在self.masterData不存在(all)则默认查询平台下所有状态数据
                    let platformStatusObj = _.find(self.masterData.platformStatus, item => {
                        return platformStatus === item.status;
                    });
                    if (!platformStatusObj) {
                        _.each(self.masterData.platformStatus, item => {
                            let tempPlatformStatus = item.status;
                            self.searchInfo.platformStatus[tempPlatformStatus] = true;
                        });
                    } else {
                        self.searchInfo.platformStatus[platformStatus] = true;
                    }
                } else {
                    let model = routePrams.model;
                    if (model) {
                        self.searchInfo.codeList = model;
                    }
                }
            }
            self.sort = {
                sName:'Newest',
                sortType:-1
            };
            self.customAttributeResult = {};
        }

        init() {
            let self = this;
            // 查询masterData,包括platforms、brandList
            this.advanceSearch.init().then(res => {
                if (res.data) {
                    // 美国平台、中国平台
                    let channelPlatforms = res.data.platforms;
                    self.masterData.platforms = _.filter(channelPlatforms, cartObj => {
                        let cartId = parseInt(cartObj.value);
                        return cartId >= 20 && cartId < 928;
                    });
                    self.masterData.usPlatforms = _.filter(channelPlatforms, cartObj => {
                        let cartId = parseInt(cartObj.value);
                        return cartId > 0 && cartId < 20;
                    });
                    // 品牌列表
                    self.masterData.brandList = res.data.brandList;
                    self.masterData.freeTags = {};

                    _.each(res.data.freeTags,freeTag => {
                        self.masterData.freeTags[freeTag.tagPath] = freeTag;
                    });

                    // 用户自定义列
                    self.customColumnNames = {};
                    self.customColumns.commonProps = res.data.commonProps;
                    self.customColumns.platformAttributes = res.data.platformAttributes;
                    self.customColumns.platformSales = res.data.platformSales;

                    res.data.platformSales.forEach(item => {
                        self.platformSalesEntity[item.cartId] = item;
                    });

                    self.search();
                }
            });
        }

        search() {
            let self = this;
            self.pageOption.curr = 1;
            self.query();
        }

        // 获取数据
        query() {
            let self = this,
                searchInfo = self.searchUtilService.handleQueryParams(self);

            self.srInstance.clearCurrPageRows();
            self.advanceSearch.search(searchInfo).then(res => {
                if (res.data) {
                    self.searchResult.productList = res.data.productList;
                    self.pageOption.total = res.data.productListTotal;

                    self.searchResult.productList.forEach(productInfo => {
                        self.setFreeTagList(productInfo);
                        self.srInstance.currPageRows({"id": productInfo.prodId, "code": productInfo.common.fields["code"]});
                    });

                    self.productSelList = self.srInstance.selectRowsInfo;

                    // 最新的自定义列信息
                    self.customColumnNames = {};
                    self.customColumns.selCommonProps = self.getSelectedProps(self.customColumns.commonProps,res.data.selCommonProps,'propId');
                    self.customColumns.selPlatformAttributes = self.getSelectedProps(self.customColumns.platformAttributes, res.data.selPlatformAttributes,'value');
                    self.customColumns.selPlatformSales = res.data.selPlatformSales;

                }
            });
        }

        /**
         * 获取自由标签tagName
         * @param productInfo
         */
        setFreeTagList(productInfo){
            let self = this,
                _usFreeTags = [];

            productInfo.usFreeTags.forEach(tag => {
                let _tag = self.masterData.freeTags[tag];

                if(_tag && _tag.tagPathName)
                    _usFreeTags.push(_tag.tagPathName);
            });

            productInfo._usFreeTags = _usFreeTags;

        }

        /**
         * @description 此方法可优化 先偷个懒
         * @param array  总数组
         * @param selectedArray 用户选择数组
         * @returns {Array}
         */
        getSelectedProps(array,selectedArray,attrName){
            let self = this,
                result = [];

            if(!selectedArray || selectedArray.length === 0)
                return result;

            selectedArray.forEach(prop => {

                let obj = array.find(item => {
                    if(attrName === 'value'){
                        if(item.value.split(",").length === 2){
                            return _.contains(item.value.split(","),prop.value);
                        }else{
                            return item.value === prop.value;
                        }
                    }else{
                        return item[attrName] === prop;
                    }

                });

                if(!obj)
                    return;

                if(attrName === 'value'){
                    if(!self.customColumnNames[obj.name]){
                        result.push(obj);
                        self.customColumnNames[obj.name] = true;
                    }
                }else{
                    result.push(obj);
                }

            });

            return result;

        }

        getProductValue(element,prop){
            let self = this;
            return self.searchUtilService.getProductValue(element, prop);
        }

        getPlatformSaleValue(productInfo,prop){
            let self = this;
            return self.searchUtilService.getPlatformSaleValue(productInfo, prop);
        }

        clear() {
            let self = this;
            self.searchInfo = angular.copy(self.defaultSearchInfo);
            self.tempUpEntity = {};
        }

        dismiss(attrName){
            this.searchInfo[attrName] = null;
        }

        /**
         * 批量操作前判断是否选中
         * @param cartId
         * @param callback
         */
        $chkProductSel(cartId, callback) {
            let self = this;

            if (cartId === null || cartId === undefined) {
                cartId = 0;
            } else {
                cartId = parseInt(cartId);
            }

            let selList;

            if (!self._selall) {
                selList = self.getSelectedProduct('code');
                if (selList.length === 0) {
                    self.alert(self.$translate.instant('TXT_MSG_NO_ROWS_SELECT'));
                    return;
                }
                callback(cartId, selList);
            } else {
                if (self.pageOption.total === 0) {
                    self.alert(self.$translate.instant('TXT_MSG_NO_ROWS_SELECT'));
                    return;
                }

                self.confirm(`You have started “Select All Items By Query Terms ”，This action object is the search result for all products<h3>Modify the number of records:&emsp;<span class='label label-danger'>${self.pageOption.total}</span></h3>`).then(function () {
                    callback(cartId);
                });
            }
        }

        // 自定义列弹出
        popCustomAttributes() {
            let self = this;
            let ctx = {
                customColumns:self.customColumns,
                usPlatforms:self.masterData.usPlatforms,
                customAttributeResult:self.customAttributeResult
            };
            self.popups.openCustomAttributes(ctx).then(() => {
                self.notify.success("save success");
                self.search();

            })
        }

        popBatchPrice(cartId,usPlatformName) {
            let self = this,
                selCodeList = self.getSelectedProduct('code');

            if (!self._selall && _.size(selCodeList) === 0) {
                self.alert("Please select at least one record.");
                return;
            }
            self.popups.openBatchPrice({
                selAll:self._selall,
                codeList:self.getSelectedProduct('code'),
                queryMap:self.searchUtilService.handleQueryParams(self),
                cartId:cartId? cartId :0,
                usPlatformName:usPlatformName?usPlatformName:null
            }).then(res => {
                //根据返回参数确定勾选状态,"1",需要清除勾选状态,"0"不需要清除勾选状态
                if(res.success == "1"){
                    //需要清除勾选状态
                    self.clearSelList();
                    self._selall = 0;
                }
                if(res.type == 1){
                    self.notify.success('Update Success');
                }else {
                    self.alert('Update Defeated');
                }
            });
        }

        popUsFreeTag() {
            let self = this;

            self.popups.openUsFreeTag({
                orgFlg: '1',
                tagType: '4',
                orgChkStsMap:self.searchInfo.usFreeTags
            }).then(res => {
                self.tranferData.usFreeTags = res.selectdTagList;
                self.searchInfo.usFreeTags = _.pluck(res.selectdTagList,'tagPath');
            });
        }

        /**
         * @description 类目pop框
         */
        popCategory() {
            let self = this;

            if (Number(self.searchInfo.cartId) === 5) {
                //只有亚马逊显示类目
                self.tempUpEntity.cidValueTmp = null;
                self.tempUpEntity.cidValue = null;

                self.popups.openAmazonCategory({cartId: 5,froms:self.tempUpEntity.pCatPathListTmp,muiti:true}).then(res => {
                    self.tempUpEntity.pCatPathListTmp = res;
                });
            } else {
                //sneakerhead 显示店铺内分类
                self.tempUpEntity.pCatPathListTmp = null;
                self.tempUpEntity.pCatPathList = null;

                self.popups.openUsCategory({cartId:self.searchInfo.cartId,froms:self.tempUpEntity.cidValueTmp,muiti:true}).then(res => {
                    self.tempUpEntity.cidValueTmp = res;
                });
            }
        }

        canCategory() {
            const arr = ['8', '12', '6', '11', '5'];
            return arr.indexOf(this.searchInfo.cartId + "") < 0;
        }

        batchCategory(){
            let self = this;

        }

        /**
         * 获取选中产品 getSelectedProduct('code')
         * @param  id  or code
         * @returns {Array}
         */
        getSelectedProduct(onlyAttr){
            let self = this;

            if(onlyAttr){
                return _.pluck(self.productSelList.selList,onlyAttr);
            }else{
                return self.productSelList.selList;
            }
        }

        clearSelList(){
            this.srInstance.clearSelectedList();
        }

        //进行上下架操作
        batchList(cartId,activeStatus,usPlatformName){
            let self = this,
                selCodeList = self.getSelectedProduct('code');

            if (!self._selall && _.size(selCodeList) === 0) {
                self.alert("Please select at least one record.");
                return;
            }

            self.popups.openUsList({
                selAll:self._selall,
                codeList:self.getSelectedProduct('code'),
                queryMap:self.searchUtilService.handleQueryParams(self),
                cartId:cartId? cartId :0,
                //操作状态1为上架,0为下架
                activeStatus:activeStatus,
                usPlatformName:usPlatformName
            }).then(res => {
                if(res.success == "1"){
                    //需要清除勾选状态
                    self.clearSelList();
                    self._selall = 0;
                }
                self.notify.success('Update Success');
            });
        }

        /**
         * @description 批量修改类目
         * @param option
         * @return move:0|1
         */
        batchUpdateCategory(option){

            let self = this,
                selCodeList = self.getSelectedProduct('code');

            if (!self._selall && _.size(selCodeList) === 0) {
                self.alert("Please select at least one record.");
                return;
            }

            let flag = false;

            self.popups.openUsCategory(option).then(res => {

                //判断是否continue
                //option.continue = false;
                if(!option.muiti){
                    //单个
                    let value = "Whether to cover the properties associated with the SN primary category？（including Google Category，Google DepartMent，PriceGrabber Category，Amazon Browse Tree，Visible On Menu，Enable Filter，Publish ，SEO attributes···）"
                    self.confirm(value).then(() => {
                            //确定
                            flag =true;
                            self.advanceSearch.updatePrimaryCategory(
                                {
                                    selAll:self._selall == "1"?"true":"false",
                                    codeList:self.getSelectedProduct('code'),
                                    searchInfo:self.searchUtilService.handleQueryParams(self),
                                    mapping:res.mapping,
                                    pCatPath:res.catPath,
                                    pCatId:res.catId,
                                    cartId:option.cartId,
                                    flag:flag
                                }
                            ).then(() =>{
                                    self.notify.success('Update Success');
                                    if(!option.continue){
                                        //save,去除勾选状态
                                        self.clearSelList();
                                        self._selall = 0;
                                    }
                                }
                            );
                        },() => {
                            //取消
                            self.advanceSearch.updatePrimaryCategory(
                                {
                                    selAll:self._selall == "1"?"true":"false",
                                    codeList:self.getSelectedProduct('code'),
                                    searchInfo:self.searchUtilService.handleQueryParams(self),
                                    mapping:res.mapping,
                                    pCatPath:res.catPath,
                                    pCatId:res.catId,
                                    cartId:option.cartId,
                                    flag:flag
                                }
                            ).then(() =>{
                                    self.notify.success('Update Success');

                                    if(!option.continue){
                                        //save,去除勾选状态
                                        self.clearSelList();
                                        self._selall = 0;
                                    }
                                }
                            );
                        }
                    )
                }else{
                    //多个
                    let pCatPaths = [];
                    angular.forEach(res, function (item) {
                        pCatPaths.push(item.catPath);
                    });
                    self.advanceSearch.updateOtherCategory({
                            selAll:self._selall == "1"?"true":"false",
                            codeList:self.getSelectedProduct('code'),
                            searchInfo:self.searchUtilService.handleQueryParams(self),
                            cartId:option.cartId,
                            pCatPaths:pCatPaths,
                            statue:option.move == "1" ? true:false
                        }).then(() =>{
                            self.notify.success('Update Success');
                            if(!option.continue){
                                //save,去除勾选状态
                                self.clearSelList();
                                self._selall = 0;
                            }
                        }
                    );

                }
            });
        }

        /**
         * 检索列排序
         * */
        columnOrder (columnName) {
            let self  = this,
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
            if (column.count)
                column.mark = 'sort-desc';
            else
                column.mark = 'sort-up';

            columnArrow[columnName] = column;

            self.searchByOrder(columnName, column.mark);
        };

        getArrowName(columnName) {
            let columnArrow = this.columnArrow;

            if (!columnArrow || !columnArrow[columnName])
                return 'unsorted';

            return columnArrow[columnName].mark;
        };

        searchByOrder(columnName, sortOneType) {
            let self = this,
                searchInfo = self.searchInfo;

            searchInfo.sortOneName = columnName;
            searchInfo.sortOneType = sortOneType == 'sort-up' ? '1' : '-1';

            self.search();

        }

        /**
         * 添加产品到指定自由标签
         */
        addFreeTag () {
            let self = this,
                selCodeList = self.getSelectedProduct('code');

            if (!self._selall && _.size(selCodeList) === 0) {
                self.alert("Please select at least one record.");
                return;
            }

            self.popups.openUsFreeTag({
                orgFlg: '2',
                selTagType: '6',
                selAllFlg: self._selall ? 1 : 0,
                selCodeList: self.getSelectedProduct('code'),
                searchInfo: self.searchUtilService.handleQueryParams(self)
            }).then(res => {
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

                self.confirm(msg).then(function () {
                        self.$searchAdvanceService2.addFreeTag({
                            "type":"usa",
                            "tagPathList": freeTags,
                            "prodIdList": selCodeList,
                            "isSelAll": self._selall ? 1 : 0,
                            "orgDispTagList": res.orgDispTagList,
                            'searchInfo': self.searchUtilService.handleQueryParams(self)
                        }).then(function () {
                            // notify.success($translate.instant('TXT_MSG_SET_SUCCESS'));
                            self.notify.success("Set free tags succeeded.");
                            if(!res.continue)
                                self.clearSelList();
                            self._selall = false;
                            self.search();
                        })
                    });
            });
        }

    });

});