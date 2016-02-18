/**
 * Created by linanbin on 15/12/7.
 * Modified by gubuchun on 15/12/16.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {

    angularAMD.controller('popFieldEditCtl', function ($scope, $fieldEditService, $translate, $modalInstance, notify, productIds) {

        $scope.vm = {
            propertyInfo: {
                property: {},
                productIds: productIds
            },
            properties: []
        };

        $scope.initialize = initialize;
        $scope.save = save;

        function initialize() {
            $fieldEditService.getPopOptions().then(function (res) {
                $scope.vm.properties = res.data;
            });
        }

        function save () {
            $fieldEditService.setProductFields($scope.vm.propertyInfo).then(function () {
                notify.success ($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                //$scope.$close();
                $modalInstance.close('');
            });
        }
    });
});