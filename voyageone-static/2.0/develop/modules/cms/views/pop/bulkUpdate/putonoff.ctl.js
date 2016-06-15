/**
 * Created by sofia on 6/7/2016.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {
    angularAMD.controller('popPutOnOffCtl', function($scope, $fieldEditService, $translate, $modalInstance, confirm, notify, context) {

        $scope.vm = {
            property: context
        };

        $scope.save = function () {
            $fieldEditService.setProductFields($scope.vm.property).then(function (res) {
                if (res.data.ecd == null || res.data.ecd == undefined) {
                    alert("提交请求时出现错误");
                    return;
                }
                if (res.data.ecd == 1) {
                    // 存在未ready状态
                    alert("未选择商品，请选择后再操作。");
                    return;
                }
                if (res.data.ecd == 2) {
                    // 没有设置上下架操作
                    alert("没有设置上下架操作，请选择后重试。");
                    return;
                }
                $modalInstance.close();
                notify.success($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
            });
        }
    });
});