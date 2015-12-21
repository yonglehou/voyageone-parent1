/**
 * class FeedMappingController
 */

define([
  'cms',
  'modules/cms/controller/popup.ctl'
], function (cms) {

  return cms.controller('feedMappingController', (function() {

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
    }

    FeedMappingController.prototype = {
      /**
       * 画面初始化时
       */
      init: function () {

        this.feedMappingService.getFeedCategories().then(function(res) {
          this.feedCategories = res.data.categoryTree;
        }.bind(this));
      },
      /**
       * 获取类目的默认 Mapping 类目
       * @param {{mapping:object[]}} feedCategory
       * @returns {string}
       */
      getDefaultMapping: function (feedCategory) {

        var defMapping = _.find(feedCategory.mapping, function(mapping) {
          return mapping.defaultMapping === 1;
        });

        // TODO 向上查找

        return defMapping ? defMapping.mainCategoryPath : '[未设定]';
      }
    };

    return FeedMappingController
  })());
});