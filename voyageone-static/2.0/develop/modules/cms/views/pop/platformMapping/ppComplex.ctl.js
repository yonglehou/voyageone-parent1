/**
 * complexMappingPopupController
 */

define([
    'cms',
    'underscore',
    'modules/cms/enums/FieldTypes',
    'modules/cms/models/ComplexMappingBean',
    'modules/cms/views/pop/platformMapping/ppPlatformMapping.serv'
], function (cms, _, FieldTypes, ComplexMappingBean) {
    'use strict';
    return cms.controller('complexMappingPopupController', (function () {

        /**
         * Complex Mapping 弹出框的 Controller
         * @param {SimpleListMappingPopupContext} context
         * @param $uibModalInstance
         * @param {PopupPlatformMappingService} ppPlatformMappingService
         * @param notify 参见 Components/Angular/Factory/Notify
         * @constructor
         */
        function ComplexMappingPopupController(context, $uibModalInstance, ppPlatformMappingService, notify) {

            // 保存依赖
            this.$modal = $uibModalInstance;
            this.context = context;
            this.ppService = ppPlatformMappingService;
            this.notify = notify;

            // 复制属性到 Controller
            _.extend(this, context);

            // 保存当前属性
            this.platform.property = context.path[0];

            // 当前可选的所有属性
            this.options = {
                /**
                 * @type {PropGroup[]}
                 */
                propGroups: null
            };

            /**
             * @type {ComplexMappingBean}
             */
            this.complexMapping = null;

            this.selectedValue = null;
        }

        ComplexMappingPopupController.prototype = {
            init: function () {
                var $ = this;
                var $mainCate = this.maindata.category;
                var $service = this.ppService;
                var $platform = this.platform;

                // 尝试加载原有数据
                $service.getPlatformPropertyMapping(
                    $.path, $mainCate.id, $platform.category.id, $.context.cartId
                ).then(function (complexMapping) {

                    if (!complexMapping)
                    // 新建默认
                        complexMapping = new ComplexMappingBean(
                            $platform.property.id,
                            null,
                            []
                        );

                    $.complexMapping = complexMapping;
                    $.selectedValue = complexMapping.masterPropId;
                    $.loadValue();
                });
            },
            loadValue: function () {
                var $ = this;
                var $service = this.ppService;
                var $mainCate = this.maindata.category;
                var $mapping = this.complexMapping;
                var $options = this.options;

                $service.getMainCategoryProps(this.maindata.category.id)
                    .then(function (props) {

                        $options.propGroups = [];
                        props = $.filterComplex(props);

                        // 加载第一级下拉菜单
                        // 同时根据 Mapping 设定默认选中值
                        if (!$mapping.masterPropId) {
                            // 加载的 Mapping 无默认选中内容
                            $options.propGroups.push({
                                selected: null,
                                props: props
                            });
                            return;
                        }

                        // 获取默认选中值,所在的属性路径
                        $service.getPropertyPath($mainCate.id, $mapping.masterPropId).then(function (properties) {
                            _.each(properties.reverse(), function (property) {
                                $options.propGroups.push({selected: property, props: props});
                                props = $.filterComplex(property.fields);
                            }.bind(this));
                        });
                    });
            },

            loadNext: function (propGroup, $index) {
                var propGroups = this.options.propGroups;
                var prop = propGroup.selected;
                var children = this.filterComplex(prop.fields);
                // 清空指定级别以下的所有数据
                propGroups.splice($index + 1);
                if (children.length)
                    propGroups.push({selected: null, props: children});
                this.setSelectedValue();
            },

            setSelectedValue: function() {

                var $propGroups = this.options.propGroups;
                var index = $propGroups.length - 1;
                var group = $propGroups[index];

                while (!group.selected && index >= 0) {
                    group = $propGroups[index--];
                }

                this.selectedValue = group.selected ? group.selected.id : '';
            },

            filterComplex: function (fieldArr) {
                return _.filter(fieldArr, function (f) {
                    return f.type === FieldTypes.complex;
                });
            },

            ok: function () {

                var $modal = this.$modal;
                var $platform = this.platform;
                var $notify = this.notify;

                this.complexMapping.masterPropId = this.selectedValue;

                this.ppService.saveMapping(
                        this.maindata.category.id,
                        $platform.category.id,
                        this.context.cartId,
                        this.complexMapping,
                        $platform.property)
                    .then(function (updated) {
                        if (updated)
                            $notify.success('已更新');
                        else
                            $notify.warning('没有更新任何数据');
                        $modal.close(updated);
                    });
            },
            cancel: function () {
                this.$modal.dismiss('cancel');
            }
        };

        return ComplexMappingPopupController;

    })());
});