/**
 * FeedMapping 属性匹配画面,对属性进行具体匹配的弹出框 Controller
 */

define(['cms'], function (cms) {

    return cms.controller('feedPropMappingPopupController', (function () {

        function FeedPropMappingPopupController(context, feedMappingService, $uibModalInstance) {

            this.$uibModalInstance = $uibModalInstance;
            this.feedMappingService = feedMappingService;

            this.context = context;
            /**
             * 具体到字段的 Mapping 设定
             * @typedef {{property:string,operation:string,value:string}} Condition
             * @typedef {{condition:Condition[],type:string,val:string}} Mapping
             * @type {{prop:string,mapping:Mapping[]}}
             */
            this.fieldMapping = null;
            /**
             * 当前编辑的主类目字段
             */
            this.field = this.context.field;

            this.addValueMapping = this.addValueMapping.bind(this);
        }

        FeedPropMappingPopupController.prototype = {
            init: function () {

                this.feedMappingService.getFieldMapping({
                    feedCategoryPath: this.context.feedCategoryPath,
                    mainCategoryPath: this.context.mainCategoryPath,
                    field: this.context.field.id
                }).then(function (res) {

                    this.fieldMapping = res.data;
                }.bind(this));
            },
            /**
             * 增加值匹配到属性匹配中
             * @param mapping 由上层 Popup 返回的值
             */
            addValueMapping: function (mapping) {
                if (!this.fieldMapping) {
                    this.fieldMapping = {};
                }
                if (!this.fieldMapping.mapping || !this.fieldMapping.mapping.length) {
                    this.fieldMapping.mapping = [];
                }
                this.fieldMapping.mapping.push(mapping);
            },
            ok: function () {
                this.context.fieldMapping = this.fieldMapping;
                this.$uibModalInstance.close(this.context);
            },
            cancel: function () {
                this.$uibModalInstance.dismiss('cancel');
            }
        };

        return FeedPropMappingPopupController;
    })());
});