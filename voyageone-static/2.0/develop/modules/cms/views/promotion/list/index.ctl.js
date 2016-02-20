/**
 * Created by linanbin on 15/12/7.
 */

define([
    'modules/cms/controller/popup.ctl'
], function () {

    function indexController($scope,promotionService,confirm) {

        $scope.vm = {"promotionList": [],"platformTypeList":[],"promotionStatus":[]};
        $scope.searchInfo = {};
        $scope.groupPageOption = {curr: 1, total: 198, size: 30, fetch: $scope.search};

        $scope.initialize  = function () {
            promotionService.init().then(function (res) {
                $scope.vm.platformTypeList = res.data.platformTypeList;
                $scope.vm.promotionStatus = res.data.promotionStatus;
                $scope.search();
            });
        };

        $scope.clear = function () {
            $scope.searchInfo = {};
        };

        $scope.search = function () {
            promotionService.getPromotionList($scope.searchInfo).then(function(res){
                $scope.vm.promotionList = res.data;
                $scope.groupPageOption.total = $scope.vm.promotionList.size;
            },function(res){
            })
        };

        $scope.del = function (data) {
            confirm("是否要删除  "+data.promotionName,'删除').result.then(function(){
                var index = _.indexOf($scope.vm.promotionList,data);
                data.isActive = false
                promotionService.updatePromotion(data).then(function(res){
                    $scope.vm.promotionList.splice(index,1);
                    $scope.groupPageOption.total = $scope.vm.promotionList.size;
                },function(res){
                })
            },function(){

            })

        };
    };

    indexController.$inject = ['$scope','promotionService', 'confirm', 'menuService'];
    return indexController;
});