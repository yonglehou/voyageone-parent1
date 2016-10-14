/**
 * @author tony-piao
 * 独立官网
 */
define([
    'cms',
    'modules/cms/enums/Carts'
], function (cms, carts) {
    cms.directive("dlSchema", function (productDetailService, $translate, notify, confirm, $q, $compile, alert, popups) {
        return {
            restrict: "E",
            templateUrl: "views/product/dl.component.tpl.html",
            scope: {
                productInfo: "=productInfo",
                cartInfo: "=cartInfo"
            },
            link: function (scope, element) {
                scope.vm = {
                    productDetails: null,
                    productCode: "",
                    mastData: null,
                    platform: null,
                    status: "Pending",
                    skuTemp: {},
                    checkFlag: {tax: 0, category: 0, attribute: 0},
                    resultFlag: 0,
                    sellerCats: [],
                    productUrl: "",
                    preStatus: null,
                    noMaterMsg: null
                };

                initialize();
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
                scope.copyMainProduct = copyMainProduct;

                /**
                 * 获取京东页面初始化数据
                 */
                function initialize() {
                    //监控税号和翻译状态
                    var checkFlag = scope.$watch("productInfo.checkFlag", function () {
                        scope.vm.checkFlag.translate = scope.productInfo.translateStatus;
                        scope.vm.checkFlag.tax = scope.productInfo.hsCodeStatus;
                    });

                    //监控主类目
                    var masterCategory = scope.$watch("productInfo.masterCategory", function () {
                        getplatformData();
                    });
                }

                /**
                 * 构造平台数据
                 */
                function getplatformData() {
                    productDetailService.getProductPlatform({
                        cartId: scope.cartInfo.value,
                        prodId: scope.productInfo.productId
                    }).then(function (resp) {
                        var mastData,
                            platform;

                        scope.vm.mastData = mastData = resp.data.mastData;
                        scope.vm.platform = platform = resp.data.platform;

                        if (platform) {
                            scope.vm.status = platform.status == null ? scope.vm.status : platform.status;
                            scope.vm.platform.pStatus = platform.pStatus == null ? "WaitingPublish" : platform.pStatus;
                            scope.vm.sellerCats = platform.sellerCats == null ? [] : platform.sellerCats;
                            /**店铺内分类状态*/
                            scope.vm.checkFlag.category = platform.sellerCats.length == 0 ? 0 : 1;
                            scope.vm.platform.pStatus = platform.pPublishMessage != null && platform.pPublishMessage != "" ? "Failed" : platform.pStatus;
                        }

                        _.each(mastData.skus, function (mSku) {
                            scope.vm.skuTemp[mSku.skuCode] = mSku;
                        });

                        if (platform.schemaFields && platform.schemaFields.product)
                            initBrand(platform.schemaFields.product, platform.pBrandId);

                    }, function (resp) {
                        scope.vm.noMaterMsg = resp.message.indexOf("Server Exception") >= 0 ? null : resp.message;
                    });

                    scope.vm.productUrl = carts.valueOf(+scope.cartInfo.value).pUrl;

                }

                /**
                 * @description 店铺内分类popup
                 * @param openAddChannelCategoryEdit
                 */
                function openSellerCat(openAddChannelCategoryEdit) {
                    var selectedIds = {};
                    scope.vm.sellerCats.forEach(function (element) {
                        selectedIds[element.cId] = true;
                    });
                    var selList = [{
                        "code": scope.vm.mastData.productCode,
                        "sellerCats": scope.vm.sellerCats,
                        "cartId": scope.cartInfo.value,
                        "selectedIds": selectedIds,
                        plateSchema: true
                    }];
                    openAddChannelCategoryEdit(selList).then(function (context) {
                        /**清空原来店铺类分类*/
                        scope.vm.sellerCats = [];
                        scope.vm.sellerCats = context.sellerCats;
                        scope.vm.checkFlag.category = context.sellerCats.length == 0 ? 0 : 1;
                    });
                }

                /**
                 *  切换主商品  cartInfo.value,vm.mastData.productCode
                 */
                function openSwitchMainPop(openSwitchMain) {

                    openSwitchMain({
                        cartId: scope.cartInfo.value,
                        productCode: scope.productInfo.masterField.code
                    }).then(function () {
                        //刷新子页面
                        getplatformData();
                        scope.vm.noMaterMsg = null;
                    });
                }

                /**
                 *  商品下线
                 */
                function openOffLinePop(openProductOffLine, type) {
                    if (scope.vm.mastData == null)
                        return;

                    if (scope.vm.status != 'Approved') {
                        alert("商品未完成平台上新，无法操作平台下线。");
                        return;
                    }

                    if (scope.vm.mastData.isMain && type != 'group') {
                        alert("当前商品为主商品，无法单品下线。如果想下线整个商品，请点击【全group下线】按钮");
                        return;
                    }

                    openProductOffLine({
                        cartId: scope.cartInfo.value,
                        productCode: scope.vm.mastData.productCode,
                        type: type
                    }).then(function () {
                        //刷新子页面
                        getplatformData();
                    });
                }

                /**
                 *  商品品牌选择
                 */
                function choseBrand(openPlatformMappingSetting) {

                    var mainBrand = scope.productInfo.masterField.brand,
                        platform = scope.vm.platform;

                    if (!platform || !platform.pBrandId)
                        return;

                    openPlatformMappingSetting({
                        cartId: scope.cartInfo.value,
                        cartName: scope.cartInfo.name,
                        masterName: mainBrand,
                        pBrandId: platform.pBrandId
                    }).then(function (context) {
                        scope.vm.platform.pBrandName = context.pBrand;
                        if (platform.schemaFields && platform.schemaFields.product)
                            initBrand(platform.schemaFields.product, context.brandId);
                    });

                }

                /**
                 * 复制主数据filed到平台编辑页
                 * */
                function copyMainProduct() {
                    var template = _.template("您确定要复制Master数据到<%=cartName%>吗？");

                    confirm(template({cartName: scope.cartInfo.name})).then(function () {
                        productDetailService.copyProperty({
                            prodId: scope.productInfo.productId,
                            cartId: +scope.cartInfo.value
                        }).then(function (res) {
                            scope.vm.platform = res.data.platform;
                        });
                    });
                }

                /**
                 * @description 更新操作
                 * @param mark:记录是否为ready状态,temporary:暂存
                 */
                function saveProduct(mark) {

                    if (mark == "ready") {
                        if (!validSchema()) {
                            alert("请输入必填属性，或者输入的属性格式不正确");
                            return;
                        }
                    }

                    var statusCount = 0;
                    for (var attr in scope.vm.checkFlag) {
                        statusCount += scope.vm.checkFlag[attr] == true ? 1 : 0;
                    }

                    scope.vm.preStatus = angular.copy(scope.vm.status);

                    switch (scope.vm.status) {
                        case "Pending":
                            scope.vm.status = statusCount == 3 ? "Ready" : scope.vm.status;
                            break;
                        case "Ready":
                            scope.vm.status = "Approved";
                            break;
                    }

                    if ((scope.vm.status == "Ready" || scope.vm.status == "Approved") && !checkSkuSale()) {
                        scope.vm.status = scope.vm.preStatus;
                        alert("请至少选择一个sku进行发布");
                        return;
                    }

                    /**构造调用接口上行参数*/
                    if (scope.vm.checkFlag.attribute == 1)
                        scope.vm.platform.pAttributeStatus = "1";
                    else
                        scope.vm.platform.pAttributeStatus = "0";

                    scope.vm.platform.status = mark == "temporary" ? "Pending" : scope.vm.status;
                    scope.vm.platform.sellerCats = scope.vm.sellerCats;
                    scope.vm.platform.cartId = +scope.cartInfo.value;

                    _.map(scope.vm.platform.skus, function (item) {
                        item.property = item.property == null ? "OTHER" : item.property;
                    });

                    if (mark == "temporary") {
                        //暂存状态都为 Pending
                        scope.vm.status = "Pending";
                        callSave("temporary");
                        return;
                    }

                    if (scope.vm.status == "Approved") {

                        popups.openApproveConfirm(scope.vm.platform.skus).then(function (context) {
                            if (context) {
                                _.map(scope.vm.platform.skus, function (element) {
                                    element.confPriceRetail = element.priceRetail;
                                });

                                productDetailService.priceConfirm({
                                    productCode: scope.productInfo.masterField.code,
                                    platform: scope.vm.platform
                                });
                                callSave();
                            } else {
                                callSave();
                            }
                        });
                    } else {
                        callSave();
                    }

                }

                /**调用服务器接口*/
                function callSave(mark) {

                    /**判断价格*/
                    productDetailService.updateProductPlatformChk({
                        prodId: scope.productInfo.productId,
                        platform: scope.vm.platform
                    }).then(function (resp) {
                        scope.vm.platform.modified = resp.data.modified;
                        notify.success($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                    }, function (resp) {
                        if (resp.code != "4000091" && resp.code != "4000092") {
                            scope.vm.status = scope.vm.preStatus;
                            return;
                        }

                        confirm(resp.message + ",是否强制保存").then(function () {
                            productDetailService.updateProductPlatform({
                                prodId: scope.productInfo.productId,
                                platform: scope.vm.platform
                            }).then(function (resp) {
                                scope.vm.platform.modified = resp.data.modified;
                                notify.success($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                            });
                        }, function () {
                            if (mark != 'temporary')
                                scope.vm.status = scope.vm.preStatus;
                        });
                    });
                }

                function validSchema() {
                    return scope.vm.platform == null || scope.vm.platform.schemaFields == null ? false : scope.schemaForm.$valid && scope.skuForm.$valid;
                }

                function selectAll() {
                    scope.vm.platform.skus.forEach(function (element) {
                        element.isSale = scope.vm.skuFlag;
                    });
                }

                /**
                 * 右侧导航栏
                 * @param area div的index
                 * @param speed 导航速度 ms为单位
                 */
                function pageAnchor(area, speed) {
                    var offsetTop = 0;

                    if (area != 'master') {
                        offsetTop = element.find("#" + area).offset().top;
                    }

                    $("body").animate({scrollTop: offsetTop - 100}, speed);
                }

                /**
                 * 判断是否一个都没选 true：有打钩    false：没有选择
                 */
                function checkSkuSale() {
                    return scope.vm.platform.skus.some(function (element) {
                        return element.isSale === true;
                    });
                }

                /**
                 * 判断是否全部选中
                 */
                function allSkuSale() {
                    if (!scope.vm.platform)
                        return false;

                    if (!scope.vm.platform.skus)
                        return false;

                    return scope.vm.platform.skus.every(function (element) {
                        return element.isSale === true;
                    });
                }

                /**错误聚焦*/
                function focusError() {
                    if (!validSchema()) {
                        var firstError = element.find("schema .ng-invalid:first");
                        firstError.focus();
                        firstError.addClass("focus-error");
                    }
                }

                /**当shema的品牌为空时，设置平台共通的品牌*/
                function initBrand(product, brandId) {

                    if (!product)
                        return;

                    if (scope.cartInfo.value != 23)
                        return;

                    var brandField = searchField("品牌", product);

                    if (!brandField)
                        return;

                    if (!brandField.value.value)
                        brandField.value.value = brandId;
                }

                /**全schema中通过name递归查找field*/
                function searchField(fieldName, schema) {

                    var result = null;

                    _.find(schema, function (field) {

                        if (field.name === fieldName) {
                            result = field;
                            return true;
                        }

                        if (field.fields && field.fields.length) {
                            result = searchField(fieldName, field.fields);
                            if (result)
                                return true;
                        }

                        return false;
                    });

                    return result;
                }

            }
        };
    });
});