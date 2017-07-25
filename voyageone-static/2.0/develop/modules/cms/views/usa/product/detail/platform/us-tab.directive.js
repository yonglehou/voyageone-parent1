/**
 * @description 美国各平台详情页
 */
define([
    'cms'
], function (cms) {

    class usTabController {

        constructor($scope,detailDataService) {
            this.$scope = $scope;
            this.detailDataService = detailDataService;
            this.productInfo = $scope.productInfo;
            this.cartInfo = $scope.cartInfo;

            console.log(this.cartInfo);

            // 平台信息
            this.platform = {};

            // SKU
            this.selAllSkuFlag = false;

            // 平台状态
            this.platformStatus = this.detailDataService.platformStatus;

        }

        init() {
            let self = this;

            self.detailDataService.getProductPlatform({
                cartId: Number(self.cartInfo.value),
                prodId: self.productInfo.productId
            }).then(res => {

                if (res.data) {
                    self.platform = res.data;

                    // SKU 是否全选
                    let flag = true;
                    _.each(self.platform.skus, sku => {
                        let isSale = sku.isSale;
                        if (!isSale) {
                            flag = false;
                        }
                    });
                    self.selAllSkuFlag = flag;
                    console.log(self.platform);
                }
                // self.platform.platformFields = res.data.platformFields;
                // self.platform.fields = res.data.fields;
            })
        }

        // SKU可售选择
        selAllSku() {
            let self = this;
            _.each(self.platform.skus, sku => {
                sku.isSale = self.selAllSkuFlag;
            });
        }
        checkSelAllSku(sku) {
            let self = this;
            let isSale = sku.isSale;
            if (!isSale) {
                self.selAllSkuFlag = false;
            } else {
                let notSelOne = _.find(self.platform.skus, sku => {
                    return !sku.isSale;
                });
                self.selAllSkuFlag = !notSelOne;
            }
        }

    }

    cms.directive('usTab', function () {
        return {
            restrict: 'E',
            controller: usTabController,
            controllerAs: 'ctrl',
            scope: {
                productInfo: "=productInfo",
                cartInfo: "=cartInfo"
            },
            templateUrl: 'views/usa/product/detail/platform/us-tab.directive.html'
        }
    })

});