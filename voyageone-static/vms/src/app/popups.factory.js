/**
 * 统一管理弹出框的定义信息
 * Created by LinAn.Bin on 15/12/7.
 */

define(function (require) {
    var popups = {
        orderInfo:{
            newShipment: {
                templateUrl: "views/pop/orderInfo/shipment/index.tpl.html",
                controllerUrl: "views/pop/orderInfo/shipment/index.ctl",
                controller: 'NewShipmentController as ctrl',
                backdrop: 'static',
                size: 'md'
            },
            addShipment: {
                templateUrl: "views/pop/orderInfo/addToShipment/index.tpl.html",
                controllerUrl: "views/pop/orderInfo/addToShipment/index.ctl",
                controller: 'AddToShipmentController as ctrl',
                backdrop: 'static',
                size: 'lg'
            }
        },
        feed:{
            imagePreview:{
                templateUrl: "views/pop/feed/image/index.tpl.html",
                controllerUrl: "views/pop/feed/image/index.ctl",
                controller: 'ImagePreviewController as ctrl',
                backdrop: 'static',
                size: 'md'
            }
        }
        // 在这里增加你的 popup 配置
    };

    return angular.module('vms.popups', []).factory('popups',['$uibModal', '$q', function PopupsService($uibModal, $q) {

        function open(config, context) {
            // if (context)
            config.resolve = {
                context: function () {
                    return context;
                }
            };

            var defer = $q.defer();
            require([config.controllerUrl], function () {

                defer.resolve($uibModal.open(config).result);
            });
            return defer.promise;
        }

        return {
            openShipment: function (searchOrderStatus) {
                return open(popups.orderInfo.newShipment, searchOrderStatus);
            },
            openAddShipment: function (item) {
                return open(popups.orderInfo.addShipment,item);
            },
            openImagePreview: function (context) {
                return open(popups.feed.imagePreview,context);
            }
            // 在这里增加你的打开函数
        };
    }]);
});
