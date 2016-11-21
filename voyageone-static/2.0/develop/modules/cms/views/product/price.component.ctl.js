/**
 * @author piao
 * @description 价格一览
 * @version V2.9.0
 */
define([
    'cms',
    'modules/cms/directives/platFormStatus.directive'
],function(cms) {
    cms.directive("priceSchema", function ($productDetailService, $rootScope, alert, notify, confirm) {
        return {
            restrict: "E",
            templateUrl : "views/product/price.component.tpl.html",
            scope: {
                productInfo: "=productInfo",
                //sales:{}
            },
            link: function (scope) {
                scope.sales = {};
                scope.vm = {
                    selectSales: "codeSumAll",
                    productPriceList: []
                    ,model:{}
                };
                initialize();
                function initialize() {
                    console.log(scope.productInfo);
                    $productDetailService.getProductPriceSales(scope.productInfo.productId).then(function (resp) {
                        console.log(resp.data);
                        scope.vm.productPriceList = resp.data.productPriceList;
                        scope.vm.model = resp.data;
                        scope.sales = resp.data.sales;
                        scope.selectSalesOnChange();
                        scope.vm.productPriceList.forEach(function (f) {
                            if(f.checked ==2) {
                                f.isSale = true;
                            }
                        });
                    });
                }
                scope.selectSalesOnChange = function () {
                   // console.log(scope.vm.selectSales);
                    var cartSales = scope.sales[scope.vm.selectSales];
                    if (cartSales) {
                      //  console.log(cartSales);
                        scope.vm.productPriceList.forEach(function (f) {
                            f.saleQty = cartSales["cartId" + f.cartId];
                        });
                    }
                    else {
                        scope.vm.productPriceList.forEach(function (f) {
                            f.saleQty = 0;
                        });
                    }
                }
                scope.isSaleOnChange=function (item) {
                    console.log(item);
                    
                }
            }
        };
    });
});