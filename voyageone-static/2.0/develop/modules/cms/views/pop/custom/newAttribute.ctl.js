/**
 * Created by linanbin on 15/12/7.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {

    angularAMD.controller('popAddAttributeValueCtl', function ($scope, $modalInstance, context, alert) {

        $scope.vm = {
            prop_original:"",
            prop_translation:""
        };

        /**
         * 提交属性追加
         */
        $scope.ok = function () {
            var checkResult = true;
            if($scope.vm.prop_original==""){
                alert("请输入属性名");
                checkResult = false;
            }else{
                _.each(context.from, function(value) {
                    if (_.isEqual(value.prop_original, $scope.vm.prop_original)) {
                        alert("该属性已经存在");
                        checkResult = false;
                    }
                });
            }

            if (checkResult) {
                $modalInstance.close($scope.vm);
            }
        };
    });


});