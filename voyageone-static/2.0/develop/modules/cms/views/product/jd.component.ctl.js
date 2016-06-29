/**
 * @author tony-piao
 * 京东 & 聚美 产品概述（schema）
 */
define([
    'cms'
],function(cms) {
    cms.directive("jdSchema", function (productDetailService,feedMappingService,platformMappingService,$translate,notify,confirm,alert) {
        return {
            restrict: "E",
            templateUrl : "views/product/jd.component.tpl.html",
            /**独立的scope对象*/
            scope: {
                productId: "=productId",
                cartInfo:"=cartInfo"
            },
            link: function (scope) {
                scope.vm = {
                    productDetails:null,
                    productCode:"",
                    mastData:null,
                    platform:null,
                    status:"Pending",
                    skuTemp:{},
                    checkFlag:{translate:0, tax:0, category:0, attribute:0},
                    resultFlag:0,
                    sellerCats:[],
                    productUrl:""
                };

                initialize();
                scope.jdCategoryMapping = jdCategoryMapping;
                scope.openSellerCat = openSellerCat;
                scope.saveProduct = saveProduct;
                scope.validSchema = validSchema;
                scope.selectAll = selectAll;
                scope.pageAnchor = pageAnchor;
                /**
                 * 获取京东页面初始化数据
                 */
                function initialize(){
                    productDetailService.getProductPlatform({cartId:scope.cartInfo.value,prodId:scope.productId}).then(function(resp){
                        scope.vm.mastData = resp.data.mastData;
                        scope.vm.platform = resp.data.platform;

                        if(scope.vm.platform != null){
                            scope.vm.status = scope.vm.platform.status == null ? scope.vm.status : scope.vm.platform.status;
                            scope.vm.checkFlag.category = scope.vm.platform.pCatPath == null ? 0 : 1;
                            scope.vm.platform.pStatus = scope.vm.platform.pStatus == null ? "WaitingPublish" : scope.vm.platform.pStatus;
                            scope.vm.sellerCats = scope.vm.platform.sellerCats == null?[]:scope.vm.platform.sellerCats;
                            scope.vm.platform.pStatus = scope.vm.platform.pPublishError != null ? "Failed":scope.vm.platform.pStatus;
                        }

                        _.each(scope.vm.mastData.skus,function(mSku){
                                scope.vm.skuTemp[mSku.skuCode] = mSku;
                        });

                        scope.vm.checkFlag.translate = scope.vm.mastData.translateStatus == null ? 0 : scope.vm.mastData.translateStatus;
                        scope.vm.checkFlag.tax = scope.vm.mastData.hsCodeStatus == null ? 0 : scope.vm.mastData.hsCodeStatus;

                    });
                    productDetailService.getProductInfo({productId: scope.productId}).then(function (res) {
                            scope.vm.productDetails = res.data.productInfo;
                            scope.vm.productCode = res.data
                    });

                    switch(+scope.cartInfo.value){
                        case 26:
                            scope.vm.productUrl = "http://ware.shop.jd.com/onSaleWare/onSaleWare_viewProduct.action?wareId=";
                            break;
                        case 27:
                            scope.vm.productUrl = "http://item.jumeiglobal.com/";
                            break;
                    }
                }

                /**
                   @description 类目popup
                 * @param productInfo
                 * @param popupNewCategory popup实例
                 */
                function jdCategoryMapping(popupNewCategory) {
                    platformMappingService.getPlatformCategories({cartId: scope.cartInfo.value})
                        .then(function (res) {
                            if (!res.data || !res.data.length) {
                                alert("没数据");
                                return null;
                            }
                            return popupNewCategory({
                                from:scope.vm.platform == null?"":scope.vm.platform.pCatPath,
                                categories: res.data,
                                plateSchema:true
                            });
                        }).then(function (context) {
                            if(scope.vm.platform != null){
                                if(context.selected.catPath == scope.vm.platform.pCatPath)
                                    return;
                            }

                            productDetailService.changePlatformCategory({cartId:scope.cartInfo.value,prodId:scope.productId,catId:context.selected.catId}).then(function(resp){
                                scope.vm.platform = resp.data.platform;
                                scope.vm.platform.pCatPath = context.selected.catPath;
                                scope.vm.platform.pCatId = context.selected.catId;
                                scope.vm.checkFlag.category = 1;
                                scope.vm.platform.pStatus == 'WaitingPublish';
                                scope.vm.status =  "Pending";
                            });
                        });
                }

                /**
                 * @description 店铺内分类popup
                 * @param openAddChannelCategoryEdit
                 */
                function openSellerCat (openAddChannelCategoryEdit) {
                    var selectedIds = {};
                    scope.vm.sellerCats.forEach(function(element){
                        selectedIds[element.cId]=true;
                    });
                    var selList = [{"code": scope.vm.productDetails.productCode, "sellerCats":scope.vm.sellerCats,"cartId":scope.cartInfo.value,"selectedIds":selectedIds,plateSchema:true}];
                    openAddChannelCategoryEdit(selList).then(function (context) {
                            /**清空原来店铺类分类*/
                            scope.vm.sellerCats = [];
                            scope.vm.sellerCats = context.sellerCats;
                    });
                }

                /**
                 * 更新操作
                 */
                function saveProduct(){
                     var statusCount = 0,preStatus;
                     for(var attr in scope.vm.checkFlag){
                         statusCount += scope.vm.checkFlag[attr] == true ? 1 : 0;
                     }

                    if(scope.vm.status == "Ready" && scope.vm.platform.pBrandName == null){
                        notify.danger("请先确认是否在后台申请过相应品牌");
                        return;
                    }

                    preStatus = angular.copy(scope.vm.status);
                    switch (scope.vm.status){
                        case "Pending":
                                scope.vm.status = statusCount == 4 ? "Ready" : scope.vm.status;
                                break;
                        case "Ready":
                                scope.vm.status = "Approved";
                                break;
                    }
                     scope.vm.platform.status = scope.vm.status;
                     scope.vm.platform.pAttributeStatus = 1;
                     scope.vm.platform.sellerCats = scope.vm.sellerCats;
                     scope.vm.platform.cartId = +scope.cartInfo.value;

                     _.map(scope.vm.platform.skus, function(item){ return item.property = item.property == null?"OTHER":item.property;});
                    /**判断价格*/
                    productDetailService.updateProductPlatformChk({prodId:scope.productId,platform:scope.vm.platform}).then(function(resp){
                        scope.vm.platform.modified = resp.data.modified;
                        notify.success($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                    },function(resp){
                        if(resp.code != "4000091" && resp.code != "4000092"){
                            scope.vm.status = preStatus;
                            return;
                        }
                        confirm(resp.message + ",是否强制上新").result.then(function () {
                             productDetailService.updateProductPlatform({prodId:scope.productId,platform:scope.vm.platform}).then(function(resp){
                                 scope.vm.platform.modified = resp.data.modified;
                                 notify.success($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                             });
                        });
                    });
                }

                function validSchema(){
                    return scope.vm.platform == null || scope.vm.platform.schemaFields == null ? false : scope.schemaForm.$valid && scope.skuForm.$valid;
                }

                function selectAll(){
                    scope.vm.platform.skus.forEach(function(element){
                        element.isSale = scope.vm.skuFlag;
                    });
                }

                /**
                 * 右侧导航栏
                 * @param index div的index
                 * @param speed 导航速度 ms为单位
                 */
                function pageAnchor(index,speed){
                    var offsetTop = 0;
                    if(index != 1)
                        offsetTop = ($("#"+scope.cartInfo.name+index).offset().top);
                    $("body").animate({ scrollTop:  offsetTop-70}, speed);
                }
            }
        };
    });
});