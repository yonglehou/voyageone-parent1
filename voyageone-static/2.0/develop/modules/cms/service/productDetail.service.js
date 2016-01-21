/**
 * @Name:    productPropsEditService
 * @Date:    2015/12/18
 *
 * @User:    Lewis.liu
 * @Version: 1.0.0
 */

define([
	'cms',
	'underscore',
	'modules/cms/enums/Status'
],function (cms, _, Status) {

	cms.service("productDetailService", productDetailService);

	function productDetailService ($q, $productDetailService) {

		this.getProductInfo = getProductInfo;
		this.updateProductInfo = updateProductInfo;
		this.updateSkuInfo = updateSkuInfo;
		this.updateProductDetail = updateProductDetail;

		/**
		 * 获取页面产品信息
		 * @param formData
         * @returns {*}
         */
		function getProductInfo (formData) {
			var defer = $q.defer();
			$productDetailService.getProductInfo(formData)
			.then (function (res) {
				var result = angular.copy(res);

				// 设定custom列表中的feed被选中
				result.data.productInfo.customAttributes.orgAtts = _returnNew(res.data.productInfo.customAttributes.orgAtts, result.data.productInfo.customAttributes.customIds);
				result.data.productInfo.customAttributes.cnAtts = _returnNew(res.data.productInfo.customAttributes.cnAtts);

				// 设定哪些原始feed被添加到custom列表
				var feedKeys = _returnKeys(res.data.productInfo.customAttributes.orgAtts);
				result.data.feedKeys = feedKeys;
				if(res.data.productInfo.feedInfoModel)
					result.data.productInfo.feedInfoModel.attributeList = _returnNew(res.data.productInfo.feedInfoModel.attributeList, feedKeys);

				// 设置sku的渠道列表是否被选中
				angular.forEach(result.data.skus, function (sku) {
					var SelSkuCarts = [];
					angular.forEach(sku.skuCarts, function(skuCart) {
						SelSkuCarts[skuCart] = true;
					});
					sku.SelSkuCarts = SelSkuCarts;
				});

				// 设置产品状态
				var status = result.data.productInfo.productStatus.approveStatus;

				if (_.isEqual(status, Status.NEW)
						|| _.isEqual(status, Status.PENDING)
						|| _.isEqual(status, Status.READY))
					result.data.productInfo.productStatus.statusInfo = {
						isApproved: false,
						isDisable: false
					};
				else
					result.data.productInfo.productStatus.statusInfo = {
						isApproved: true,
						isDisable: true
					};

				defer.resolve(result);
			});

			return defer.promise;
		}

		/**
		 * 保存产品详情和产品自定义
		 * @param formData
		 * @returns {*|Promise}
         */
		function updateProductInfo (formData) {

			var data = {
				categoryId: formData.categoryId,
				categoryFullPath: formData.categoryFullPath,
				productId: formData.productId,
				modified: formData.modified,
				masterFields: [],
				customAttributes: formData.customAttributes
			};

			// 如果是productDetail更新时传递productStatus
			//if (detailFlag) {
			//	data.productStatus = {
			//		approveStatus: (formData.productStatus.statusInfo.isApproved && formData.productStatus.statusInfo.isDisable)
			//						|| (!formData.productStatus.statusInfo.isApproved && !formData.productStatus.statusInfo.isDisable)
			//						? formData.productStatus.approveStatus : Status.APPROVED,
			//		translateStatus: formData.productStatus.translateStatus ? "1" : "0",
			//		editStatus: formData.productStatus.editStatus ? "1" : "0"
			//	};
			//}

			angular.forEach(formData.masterFields, function (field) {
				if (field.type != "LABEL" && field.isDisplay != 0)
					data.masterFields.push(field);
			});

			return $productDetailService.updateProductMasterInfo(data);
		}

		/**
		 * 保存SKU列表信息
		 * @param formData
		 * @returns {*}
         */
		function updateSkuInfo (formData) {

			var data = {
				categoryId: formData.categoryId,
				categoryFullPath: formData.categoryFullPath,
				productId: formData.productId,
				modified: formData.modified,
				skuFields: formData.skuFields
			};
			return $productDetailService.updateProductSkuInfo(data);
		}

		/**
		 * 同时保存产品详情,产品自定义,SKU列表信息
		 * @param productFormData
		 * @param skuFormData
		 * @returns {*|Promise.<T>}
         */
		function updateProductDetail (formData) {

			var data = {
				categoryId: formData.categoryId,
				categoryFullPath: formData.categoryFullPath,
				productId: formData.productId,
				modified: formData.modified,
				masterFields: [],
				customAttributes: formData.customAttributes,
				productStatus: {
					approveStatus: (formData.productStatus.statusInfo.isApproved && formData.productStatus.statusInfo.isDisable)
					|| (!formData.productStatus.statusInfo.isApproved && !formData.productStatus.statusInfo.isDisable)
							? formData.productStatus.approveStatus : Status.APPROVED,
					translateStatus: formData.productStatus.translateStatus ? "1" : "0",
					editStatus: formData.productStatus.editStatus ? "1" : "0"
				},
				skuFields: formData.skuFields
			};

			angular.forEach(formData.masterFields, function (field) {
				if (field.type != "LABEL" && field.isDisplay != 0)
					data.masterFields.push(field);
			});

			return $productDetailService.updateProductAllInfo(data);
		}

		/**
		 * 返回新对象
		 * @param {key: value} data
		 * @private
		 */
		function _returnNew (data, list) {
			var result = [];
			for(var key in data) {
				if (list != null)
					result.push({key: key, value: data[key], selected: _.contains(list, key)});
				else
					result.push({key: key, value: data[key]});
			}
			return result;
		}

		/**
		 * 返回keys
		 * @param data
		 * @returns {Array}
         * @private
         */
		function _returnKeys (data) {
			var result = [];
			for(var key in data) {
				result.push(key);
			}
			return result;
		}

	}

});