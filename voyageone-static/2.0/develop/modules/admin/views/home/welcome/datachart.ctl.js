
define([
    'angularAMD',
    'underscore'
], function (angularAMD) {
    function dController($scope, $menuService, $location, cRoutes, $sessionStorage) {
        $scope.vm={sumData:{}};
        $scope.init = function () {
            $menuService.getHomeSumData().then(function (res) {

                $scope.vm.sumData = res.data;
            });
        };
        $scope.jump=jump;
        function jump(url,param){
            $sessionStorage.feedSearch = param;
            $location.path(url);
        }

        /**
         * search by input value.
         */
        $scope.goSearchPage = function(bizType, value) {
            $sessionStorage.feedSearch = value;
            $location.path(cRoutes.search_advance_param.url + "4/" + bizType + '/0/0');
        }

    }
    dController.$inject = ['$scope', '$menuService', '$location', 'cRoutes', '$sessionStorage'];
    return dController;
});
