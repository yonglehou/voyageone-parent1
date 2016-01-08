/**
 * Created by linanbin on 15/12/3.
 */

define([
    'angularAMD',
    'underscore'
], function (angularAMD, _) {
    angularAMD
        .service('searchIndexService', searchIndexService);

    function searchIndexService($q, $searchIndexService) {

        this.init = init;
        this.search = search;
        this.getGroupList = getGroupList;
        this.getProductList = getProductList;

        /**
         * 初始化数据
         * @returns {*}
         */
        function init() {
            var defer = $q.defer();
            $searchIndexService.init().then(function (res) {
                defer.resolve (res);
            });
            return defer.promise;
        }

        /**
         * 检索group和product
         * @param data
         * @returns {*}
         */
        function search(data, groupPagination, productPagination) {
            var defer = $q.defer();
            data = resetSearchInfo(data);
            // 设置groupPage
            data.groupPageNum = groupPagination.curr;
            data.groupPageSize = groupPagination.size;
            // 设置productPage
            data.productPageNum = productPagination.curr;
            data.productPageSize = productPagination.size;

            $searchIndexService.search(data).then(function (res) {
                var resultGroup = _resetGroupList(res.data);
                res.data.groupCurrPageRows  = resultGroup.groupCurrPageRows;
                res.data.groupSelFlag= resultGroup.groupSelFlag;
                res.data.groupList = resultGroup.groupList;

                var resultProduct =  _resetProductList(res.data);
                res.data.groupCurrPageRows = resultProduct.groupCurrPageRows;
                res.data.groupSelFlag = resultProduct.groupSelFlag;
                res.data.productList = resultProduct.productList;

                defer.resolve (res);
            });
            return defer.promise;
        }

        /**
         * 检索group
         * @param data
         * @returns {*}
         */
        function getGroupList(data, pagination) {
            var defer = $q.defer();

            $searchIndexService.getGroupList(resetGroupPagination(data, pagination)).then(function (res) {
                var result = _resetGroupList(res.data);
                res.data.groupCurrPageRows  = result.groupCurrPageRows;
                res.data.groupSelFlag= result.groupSelFlag;
                res.data.groupList = result.groupList;
                defer.resolve (res);
            });
            return defer.promise;
        }

        /**
         * 检索product
         * @param data
         * @returns {*}
         */
        function getProductList(data, pagination) {
            var defer = $q.defer();
            $searchIndexService.getProductList(resetProductPagination(data, pagination)).then(function (res) {
                var result =  _resetProductList(res.data);
                res.data.groupCurrPageRows = result.groupCurrPageRows;
                res.data.groupSelFlag = result.groupSelFlag;
                res.data.productList = result.productList;
                defer.resolve (res);
            });
            return defer.promise;
        }

        /**
         * 将searchInfo转换成server端使用的bean接口
         * @param data
         * @returns {*}
         */
        function resetSearchInfo (data) {
            var searchInfo = angular.copy (data);
            searchInfo.productStatus = _returnKey (searchInfo.productStatus);

            searchInfo.platformStatus = _returnKey(searchInfo.platformStatus);
            searchInfo.labelType = _returnKey(searchInfo.labelType);
            if (!_.isUndefined(searchInfo.codeList) && !_.isNull(searchInfo.codeList))
                searchInfo.codeList = searchInfo.codeList.split("\n");
            return searchInfo;
        }

        /**
         * 添加group的当前页码和每页显示size到server端使用的bean接口
         * @param data
         * @param pagination
         * @returns {*}
         */
        function resetGroupPagination (data, pagination) {
            var searchInfo = resetSearchInfo(data);
            searchInfo.groupPageNum = pagination.curr;
            searchInfo.groupPageSize = pagination.size;
            return searchInfo
        }

        /**
         * 添加product的当前页码和每页显示size到server端使用的bean接口
         * @param data
         * @param pagination
         * @returns {*}
         */
        function resetProductPagination (data, pagination) {
            var searchInfo = resetSearchInfo(data);
            searchInfo.productPageNum = pagination.curr;
            searchInfo.productPageSize = pagination.size;
            return searchInfo
        }

        /**
         * 如果checkbox被选中,返回被选中的value.
         * eg.[{new: true, pending: false, approved: true}] -> [new, approved]
         * @param object
         * @returns {*}
         */
        function _returnKey(object) {
            return _.chain(object)
                .map(function(value, key) { return value ? key : null;})
                .filter(function(value) { return value;})
                .value();
        }

        /**
         * 设置group list
         * @param data
         * @returns {*}
         * @private
         */
        function _resetGroupList (data) {
            data.groupCurrPageRows  = [];
            data.groupSelFlag= [];
            _.forEach(data.groupList, function (groupInfo) {
                // 初始化数据选中需要的数组
                data.groupCurrPageRows.push({"id": groupInfo.group.prodId, "productIds": groupInfo.productIds});
                data.groupSelFlag[groupInfo.group.prodId] = false;

                // 设置Inventory Detail
                // TODO 因为group显示的时候只返回了主商品的信息,所以无法拿到下面所有product的库存.
                //groupInfo.inventoryDetail = _setInventoryDetail(groupInfo.skus);

                // 设置price detail
                groupInfo.group.groups.priceDetail = _setPriceDetail(groupInfo.group.groups);

                // 设置time detail
                groupInfo.group.groups.platforms[0].timeDetail = _setTimeDetail(groupInfo.group.groups.platforms[0]);

            });

            return data;
        }

        /**
         * 设置product list
         * @param data
         * @returns {*}
         * @private
         */
        function _resetProductList (data) {
            data.productCurrPageRows  = [];
            data.productSelFlag= [];
            _.forEach(data.productList, function (productInfo) {
                // 初始化数据选中需要的数组
                data.productCurrPageRows.push({"id": productInfo.prodId});
                data.productSelFlag[productInfo.prodId] = false;

                // 设置Inventory Detail
                productInfo.inventoryDetail = _setInventoryDetail(productInfo.skus);

                // 设置price detail
                productInfo.groups.priceDetail = _setPriceDetail(productInfo.fields);

                // 设置time detail
                productInfo.groups.platforms[0].timeDetail = _setTimeDetail(productInfo.groups.platforms[0]);

            });

            return data;
        }

        /**
         * 设置Inventory Detail
         * @param skus
         * @private
         */
        function _setInventoryDetail(skus) {
            var result = [];
            _.forEach(skus, function (sku) {
                result.push(sku.skuCode + ": " + (sku.qty ? sku.qty: 0));
            });
            return result;
        }

        /**
         * 设置Price Detail
         * @param groups
         * @returns {Array}
         * @private
         */
        function _setPriceDetail(object) {
            var result = [];
            var tempMsrpDetail = _setOnePriceDetail("MSRP", object.msrpStart, object.msrpEnd);
            if (!_.isNull(tempMsrpDetail))
                result.push(tempMsrpDetail);

            // 设置retail price
            var tempRetailPriceDetail = _setOnePriceDetail("Retail Price", object.retailPriceStart, object.retailPriceEnd);
            if (!_.isNull(tempRetailPriceDetail))
                result.push(tempRetailPriceDetail);

            return result;
        }

        /**
         * 设置Price Detail
         * @param priceStart
         * @param priceEnd
         * @returns {*}
         * @private
         */
        function _setOnePriceDetail(title, priceStart, priceEnd) {
            var result = null;
            if (_.isNumber(priceStart)
                && _.isNumber(priceEnd)) {
                result = _.isEqual(priceStart, priceEnd)
                    ? priceStart
                    : priceStart + " ~ " + priceEnd;
            } else {
                result = _.isNumber(priceStart)
                    ? priceStart
                    : ((_.isNumber(priceEnd)
                    ? priceEnd
                    : null));
            }

            return _.isNull(result) ? null : title + ": " + result;
        }

        /**
         * 设置time detail
         * @param platforms
         * @private
         */
        function _setTimeDetail(platforms) {
            var result = [];
            if(!_.isEmpty(platforms.publishTime))
                result.push("Publish Time: " + platforms.publishTime);

            if(!_.isEmpty(platforms.instockTime))
                result.push("On Sale Time: " + platforms.instockTime);

            return result;
        }
    }
});