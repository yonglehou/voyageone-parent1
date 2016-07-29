/**
 * @author tony-piao
 * 京东 & 聚美 & 天猫国际 产品概述（schema）
 */
define([
    'cms',
    'modules/cms/enums/Carts'
],function(cms,carts) {
    cms.directive("jdSchema", function (productDetailService,platformMappingService,$translate,notify,confirm,$q,$compile,alert) {
        return {
            restrict: "E",
            templateUrl : "views/product/jd.component.tpl.html",
            /**独立的scope对象*/
            scope: {
                productInfo: "=productInfo",
                cartInfo:"=cartInfo"
            },
            link: function (scope,element) {
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
                    productUrl:"",
                    preStatus:null,
                    noMaterMsg:null
                };

                initialize();
                scope.jdCategoryMapping = jdCategoryMapping;
                scope.openSellerCat = openSellerCat;
                scope.openSwitchMainPop = openSwitchMainPop;
                scope.openOffLinePop = openOffLinePop;
                scope.saveProduct = saveProduct;
                scope.validSchema = validSchema;
                scope.selectAll = selectAll;
                scope.pageAnchor = pageAnchor;
                scope.allSkuSale = allSkuSale;
                scope.focusError = focusError;
                scope.choseBrand = choseBrand;

                /**
                 * 获取京东页面初始化数据
                 */
                function initialize(){
                    //监控税号和翻译状态
                    var checkFlag = scope.$watch("productInfo.checkFlag",function(){
                        scope.vm.checkFlag.translate = scope.productInfo.translateStatus;
                        scope.vm.checkFlag.tax = scope.productInfo.hsCodeStatus;
                    });

                    //监控主类目
                    var masterCategory = scope.$watch("productInfo.masterCategory",function(){
                        getplatformData();
                    });
                }

                /**
                 * 构造平台数据
                 */
                function getplatformData(){
                    productDetailService.getProductPlatform({cartId:scope.cartInfo.value,prodId:scope.productInfo.productId}).then(function(resp){
                        scope.vm.mastData = resp.data.mastData;
                        scope.vm.platform = resp.data.platform;

                        if(scope.vm.platform){
                            scope.vm.status = scope.vm.platform.status == null ? scope.vm.status : scope.vm.platform.status;
                            scope.vm.checkFlag.category = scope.vm.platform.pCatPath == null ? 0 : 1;
                            scope.vm.platform.pStatus = scope.vm.platform.pStatus == null ? "WaitingPublish" : scope.vm.platform.pStatus;
                            scope.vm.sellerCats = scope.vm.platform.sellerCats == null?[]:scope.vm.platform.sellerCats;
                            scope.vm.platform.pStatus = scope.vm.platform.pPublishError != null && scope.vm.platform.pPublishError != "" ? "Failed":scope.vm.platform.pStatus;
                        }

                        _.each(scope.vm.mastData.skus,function(mSku){
                            scope.vm.skuTemp[mSku.skuCode] = mSku;
                        });

                    },function(resp){
                        scope.vm.noMaterMsg = resp.message.indexOf("Server Exception") >=0 ? null : resp.message;
                    });

                    scope.vm.productUrl = carts.valueOf(+scope.cartInfo.value).pUrl;

                }

                /**
                   @description 类目popup
                 * @param productInfo
                 * @param popupNewCategory popup实例
                 */
                function jdCategoryMapping(popupNewCategory) {
                    platformMappingService.getPlatformCategories({cartId: scope.cartInfo.value})
                        .then(function (res) {
                            return $q(function(resolve, reject) {
                                if (!res.data || !res.data.length) {
                                    notify.danger("数据还未准备完毕");
                                    reject("数据还未准备完毕");
                                } else {
                                    resolve(popupNewCategory({
                                        from:scope.vm.platform == null?"":scope.vm.platform.pCatPath,
                                        categories: res.data,
                                        divType:">",
                                        plateSchema:true
                                    }));
                                }
                            });
                        }).then(function (context) {
                            if(scope.vm.platform != null){
                                if(context.selected.catPath == scope.vm.platform.pCatPath)
                                    return;
                            }

                            productDetailService.changePlatformCategory({cartId:scope.cartInfo.value,prodId:scope.productInfo.productId,catId:context.selected.catId}).then(function(resp){
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
                    var selList = [{"code": scope.vm.mastData.productCode, "sellerCats":scope.vm.sellerCats,"cartId":scope.cartInfo.value,"selectedIds":selectedIds,plateSchema:true}];
                    openAddChannelCategoryEdit(selList).then(function (context) {
                            /**清空原来店铺类分类*/
                            scope.vm.sellerCats = [];
                            scope.vm.sellerCats = context.sellerCats;
                    });
                }

                /**
                 *  切换主商品  cartInfo.value,vm.mastData.productCode
                 */
                function openSwitchMainPop(openSwitchMain){

                    openSwitchMain({
                        cartId : scope.cartInfo.value,
                        productCode : scope.productInfo.masterField.code
                    }).then(function(){
                        //刷新子页面
                        getplatformData();
                        scope.vm.noMaterMsg = null;
                    });
                }

                /**
                 *  商品下线
                 */
                function openOffLinePop(openProductOffLine,type){
                    if(scope.vm.mastData == null)
                        return;

                    if(scope.vm.platform == null || scope.vm.platform.pNumIId == null || scope.vm.platform.pNumIId == ""){
                        alert("商品未完成平台上新，无法操作平台下线。");
                        return;
                    }

                    if(scope.vm.mastData.isMain && type != 'group'){
                        alert("当前商品为主商品，无法单品下线。如果想下线整个商品，请点击【全group下线】按钮");
                        return;
                    }

                    openProductOffLine({
                        cartId:scope.cartInfo.value,
                        productCode:scope.vm.mastData.productCode,
                        type:type
                    }).then(function(){
                        //刷新子页面
                        getplatformData();
                    });
                }

                /**
                 *  商品品牌选择
                 */
                function choseBrand(openPlatformMappingSetting){

                    var mainBrand = scope.productInfo.masterField.brand;

                    openPlatformMappingSetting({
                        cartId: scope.cartInfo.value,
                        cartName: scope.cartInfo.name,
                        masterName: mainBrand
                    }).then(function(context){
                        scope.vm.platform.pBrandName = context.selectedPlatform;
                    });

                }

                /**
                 * 更新操作
                 * @param mark:记录是否为ready状态,temporary:暂存
                 */
                function saveProduct(mark){

                    if(mark == "temporary"){
                        callSave("temporary");
                        return;
                    }

                    if(mark == "ready"){
                        if(!validSchema()){
                            alert("请输入必填属性，或者输入的属性格式不正确");
                            return;
                        }
                    }

                    var statusCount = 0;
                    for(var attr in scope.vm.checkFlag){
                        statusCount += scope.vm.checkFlag[attr] == true ? 1 : 0;
                    }

                    if(scope.vm.status == "Ready" && scope.vm.platform.pBrandName == null){
                        alert("请先确认是否在后台申请过相应品牌");
                        return;
                    }

                    scope.vm.preStatus = angular.copy(scope.vm.status);

                    switch (scope.vm.status){
                        case "Pending":
                                scope.vm.status = statusCount == 4 ? "Ready" : scope.vm.status;
                                break;
                        case "Ready":
                                scope.vm.status = "Approved";
                                break;
                    }

                    if((scope.vm.status == "Ready"|| scope.vm.status == "Approved") && !checkSkuSale()){
                        scope.vm.status = scope.vm.preStatus;
                        alert("请至少选择一个sku进行发布");
                        return;
                    }

                    /**构造调用接口上行参数*/
                    if(scope.vm.checkFlag.attribute == 1)
                        scope.vm.platform.pAttributeStatus = "1";
                    else
                        scope.vm.platform.pAttributeStatus = "0";

                    scope.vm.platform.status = scope.vm.status;
                    scope.vm.platform.sellerCats = scope.vm.sellerCats;
                    scope.vm.platform.cartId = +scope.cartInfo.value;

                     _.map(scope.vm.platform.skus, function(item){
                         item.property = item.property == null?"OTHER":item.property;
                     });

                    if(scope.vm.status == "Approved"){
                        confirm("您确定Approve这个商品吗？<br>选择Yes将会在相应销售平台进行发布。选择No，处理将会停止").then(function(){
                            if(scope.vm.platform.cartId != 27){
                                productDetailService.checkCategory({cartId:scope.vm.platform.cartId,pCatPath:scope.vm.platform.pCatPath}).then(function(resp){
                                    if(resp.data === false){
                                        confirm("当前类目没有申请 是否还需要保存？如果选择[确定]，那么状态会返回[待编辑]。请联系IT人员处理平台类目").then(function(){
                                            scope.vm.platform.status = scope.vm.status = "Pending";
                                            callSave();
                                        });
                                    }else{
                                        callSave();
                                    }
                                });
                            }else{
                                callSave();
                            }
                        });
                    }else{
                        callSave();
                    }

                }

                /**调用服务器接口*/
                function callSave(mark){

                    /**判断价格*/
                    productDetailService.updateProductPlatformChk({prodId:scope.productInfo.productId,platform:scope.vm.platform}).then(function(resp){
                        scope.vm.platform.modified = resp.data.modified;
                        notify.success($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                    },function(resp){
                        if(resp.code != "4000091" && resp.code != "4000092"){
                            scope.vm.status = scope.vm.preStatus;
                            return;
                        }

                        confirm(resp.message + ",是否强制保存").then(function () {
                            productDetailService.updateProductPlatform({prodId:scope.productInfo.productId,platform:scope.vm.platform}).then(function(resp){
                                scope.vm.platform.modified = resp.data.modified;
                                notify.success($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                            });
                        },function(){
                            if(mark != 'temporary')
                                scope.vm.status = scope.vm.preStatus;
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
                function pageAnchor(area,speed){
                    var offsetTop = 0;
                    if(area != 'master'){
                        offsetTop = element.find("#"+area).offset().top;
                    }

                    $("body").animate({ scrollTop:  offsetTop-100}, speed);
                }

                /**
                 * 判断是否一个都没选 true：有打钩    false：没有选择
                 */
                function checkSkuSale(){
                    return scope.vm.platform.skus.some(function(element){
                        return element.isSale === true;
                    });
                }

                /**
                 * 判断是否全部选中
                 */
                function allSkuSale(){
                    if(!scope.vm.platform)
                        return false;

                    if(!scope.vm.platform.skus)
                        return false;

                    return scope.vm.platform.skus.every(function(element){
                        return element.isSale === true;
                    });
                }

                /**错误聚焦*/
                function focusError(){
                   if(!validSchema()){
                       var firstError = element.find("schema .ng-invalid:first");
                       firstError.focus();
                       firstError.addClass("focus-error");
                   }
                }

            }
        };
    });
});