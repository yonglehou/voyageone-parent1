/**
 * Created by linanbin on 15/12/7.
 */
define([
    'angularAMD',
    'underscore',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {
    angularAMD.controller('popJMPromotionDetailCtl', function ($scope,jmPromotionService,alert,context,confirm,$translate,$filter) {
        $scope.vm = {"jmMasterBrandList":[]};
        $scope.editModel = {model:{}};
        $scope.datePicker = [];

        $scope.initialize  = function () {
            if (context.id) {
                jmPromotionService.getEditModel(context.id).then(function (res) {
                    $scope.vm.isBeginPre=res.data.isBeginPre;       //预热是否开始
                    $scope.vm.isEnd= res.data.isEnd;                //活动是否结束
                    $scope.editModel.model = res.data.model;
                    $scope.editModel.tagList = res.data.tagList;
                    $scope.editModel.model.activityStart = formatToDate($scope.editModel.model.activityStart);
                    $scope.editModel.model.activityEnd = formatToDate($scope.editModel.model.activityEnd);
                    $scope.editModel.model.prePeriodStart = formatToDate($scope.editModel.model.prePeriodStart);
                    $scope.editModel.model.prePeriodEnd = formatToDate($scope.editModel.model.prePeriodEnd);
                });
            } else {
                $scope.editModel.model.status=0;
                $scope.editModel.tagList = [];
                $scope.editModel.tagList.push({"id": "", "channelId": "", "tagName": "移动端专场首推单品", active:1});
                $scope.editModel.tagList.push({"id": "", "channelId": "", "tagName": "", active:1});
            }

            jmPromotionService.init().then(function (res) {
                $scope.vm.jmMasterBrandList = res.data.jmMasterBrandList;
            });

            /** 默认值设置 */
            $scope.vm.currentTime = new Date();
        };

        $scope.addTag = function () {
            if ($scope.editModel.tagList) {
                $scope.editModel.tagList.push({"id": "", "channelId": "", "tagName": "",active:1});
            } else {
                $scope.editModel.tagList = [{"id": "", "channelId": "", "tagName": "",active:1}];
            }
        };

        $scope.getTagList = getTagList;
        function getTagList(){
            var tagList = _.filter( $scope.editModel.tagList, function(tag){ return tag.active==1; });
            return tagList || [];
        }

        $scope.delTag = function (tag) {
            confirm($translate.instant('TXT_MSG_DELETE_ITEM'))
                .then(function () {
                  tag.active=0;
                });
        };

        $scope.ok = function() {
            var start = new Date($scope.editModel.model.activityStart);
            var end = new Date($scope.editModel.model.activityEnd);

            if($scope.editModel.model.activityStart > $scope.editModel.model.activityEnd){
                alert("活动时间检查：请输入结束时间>开始时间，最小间隔为30分钟。");
                return;
            }

            if(end.getTime()-start.getTime() < 30*60*1000 ){
                alert("活动时间检查：最小间隔为30分钟。");
                return;
            }

            if($scope.editModel.model.prePeriodStart > $scope.editModel.model.prePeriodEnd){
                alert("预热时间检查：请输入结束时间>开始时间。");
                return;
            }

            if($scope.editModel.model.prePeriodStart > $scope.editModel.model.activityStart){
                alert("预热开始时间不能晚于活动开始时间");
                return;
            }

            if($scope.editModel.model.signupDeadline > $scope.editModel.model.prePeriodStart){
                alert("报名截止日期不能晚于预热开始时间");
                return;
            }

            if(getTagList().length === 0){
                alert("请至少添加一个标签");
                return;
            }

            var hasTag = _.every(getTagList(),function(element){
                return element.tagName;
            });

            if(!hasTag)
                return;

            var _upEntity = angular.copy($scope.editModel);

            _upEntity.tagList= _.filter( _upEntity.tagList, function(tag){ return tag.tagName != "";});
            _upEntity.model.activityStart = formatToStr(_upEntity.model.activityStart);
            _upEntity.model.activityEnd = formatToStr(_upEntity.model.activityEnd);
            _upEntity.model.prePeriodStart = formatToStr(_upEntity.model.prePeriodStart);
            _upEntity.model.prePeriodEnd =_upEntity.model.activityEnd; //formatToStr(_upEntity.model.prePeriodEnd);
            _upEntity.model.comment = _upEntity.model.comment || ""; //formatToStr(_upEntity.model.prePeriodEnd);

            jmPromotionService.saveModel(_upEntity).then(function () {
                context =$scope.editModel.model;
                $scope.$close();

            })
        };

        /**禁用日期*/
        $scope.disabled = function(date, mode) {
            return ( mode === 'day' && $scope.vm.isBeginPre );
        };

        /**
         *
         * @param date 字符串格式为yyyy-MM-dd ss:ss:ss
         * @returns {Date}
         */
        function formatToDate(date){
             return new Date(date) ;//$filter("date")(new Date(date),"yyyy-MM-dd HH:mm:ss");
        }

        function formatToStr(date){
            return $filter("date")(new Date(date),"yyyy-MM-dd HH:mm:ss");
        }
    });
});