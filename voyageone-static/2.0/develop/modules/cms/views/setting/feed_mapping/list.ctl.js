/**
 * class FeedMappingController
 */

define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {
    "use strict";
    return cms.controller('feedMappingController', (function () {

        /**
         * @description
         * Feed Mapping 画面的 Controller 类,该类是单例,重复 new 不会返回新的实例
         * @param {FeedMappingService} feedMappingService
         * @constructor
         */
        function FeedMappingController(feedMappingService) {

            this.feedMappingService = feedMappingService;

            /**
             * feed 类目集合
             * @type {object[]}
             */
            this.feedCategories = null;
            /**
             * 当前选择的 TOP 类目
             * @type {object}
             */
            this.selectedTop = null;

            // 将被 popup 调用,需要强制绑定
            this.bindCategory = this.bindCategory.bind(this);
        }

        FeedMappingController.prototype = {
            /**
             * 画面初始化时
             */
            init: function () {

                this.feedMappingService.getFeedCategories().then(function (res) {
                    this.feedCategories = res.data.categoryTree;
                    // 如果有数据就默认选中
                    if (this.feedCategories.length) {
                        this.selectedTop = this.feedCategories[0];
                    }
                }.bind(this));
            },
            /**
             * 获取类目的默认 Mapping 类目
             * @param {{mapping:object[]}} feedCategory
             * @returns {string}
             */
            getDefaultMapping: function (feedCategory) {

                if (!feedCategory) {
                    return '?';
                }

                var defMapping = this.findDefaultMapping(feedCategory);

                if (defMapping) {
                    return defMapping.mainCategoryPath;
                }

                var parent = null;
                while (!defMapping && (parent = this.findParent(parent || feedCategory))) {
                    defMapping = this.findDefaultMapping(parent);
                }

                return defMapping ? ('可继承: ' + defMapping.mainCategoryPath) : '[未设定]';
            },

            findParent: function (category) {

                var path = category.path.split('-');

                if (path === 1) return null;

                // 从截取的类目路径中删除最后一个,即自己
                path.splice(path.length - 1);

                return this.findCategory(path);
            },
            /**
             * 在树中查找类目
             * @param {string[]} path 类目路径
             * @return {object} Feed 类目对象
             */
            findCategory: function (path) {

                var category = null;
                var categories = this.feedCategories;

                _.each(path, function (v, i) {

                    category = _.find(categories, function (cate) {
                        return cate.name === v;
                    });

                    if (i == path.length - 1)
                        return;

                    categories = category.child;
                });

                return category;
            },
            /**
             * 在类目中查找默认的 Mapping 关系
             * @param {{mapping:object[]}} category
             * @return {object} Mapping 对象
             */
            findDefaultMapping: function (category) {

                return _.find(category.mapping, function (mapping) {
                    return mapping.defaultMapping === 1;
                });
            },

            /**
             * 在类目 Popup 确定关闭后, 为相关类目进行绑定
             * @param {{from:object, selected:object}} context Popup 返回的结果信息
             */
            bindCategory: function (context) {

                this.feedMappingService.setMapping({
                    from: context.from.path,
                    to: context.selected.catPath
                }).then(function (res) {
                    // 从后台获取更新后的 mapping
                    context.from.mapping = res.data;
                });
            }
        };

        return FeedMappingController
    })());
});