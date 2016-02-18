// Generated by CoffeeScript 1.9.3

/*
  @Name: DictCustomController
  @Date: 2015-09-15 16:06:34

  @User: Jonas
  @Version: 0.0.1
 */

define([
  'cms'
], function (cms) {

  cms.controller('popDictValueCtl', function ($scope, $dictionaryService, $modalInstance, $translate, notify, dictValue) {

    $scope.vm = {
      masterData: {},
      valueTypes: {
        text: 'TEXT',
        dict: 'DICT',
        master: 'MASTER'
      },
      selected: {
        valueType: 'MASTER',
        masterValue: {},
        dictValue: {},
        textValue: ""
      }
    };

    $scope.initialize = initialize;
    $scope.save = save;
    $scope.cancel = cancel;

    /**
     * 初始化页面获得主数据
     */
    function initialize() {
      $dictionaryService.getConst().then(function (res) {
        $scope.vm.masterData = res.data;

        if (!_.isUndefined(dictValue)) {
          _returnInitInfo (dictValue);
        }
      });
    }

    /**
     * 保存现有数据到主页面
     */
    function save () {
      var data = {
        type: $scope.vm.selected.valueType,
        value: _returnValue()
      };
      $modalInstance.close(data);
      notify.success ($translate.instant('TXT_COM_UPDATE_SUCCESS'));
      $scope.$close();
    }

    /**
     * 取消新添加数据
     */
    function cancel () {
      $scope.vm.selected = {
        valueType: 'MASTER',
        masterValue: {},
        dictValue: {},
        textValue: ""
      };
      $scope.$close();
    }

    /**
     * 根据不同的选择种类返回不同的值
     * @returns {*}
     * @private
     */
    function _returnValue () {
      switch ($scope.vm.selected.valueType) {
        case $scope.vm.valueTypes.text:
          return $scope.vm.selected.textValue;
          break;
        case $scope.vm.valueTypes.master:
          return $scope.vm.selected.masterValue;
          break;
        case $scope.vm.valueTypes.dict:
          return $scope.vm.selected.dictValue;
          break;
      }
    }

    /**
     * 编辑的时候,根据不同的类型,显示初始化值
     * @param dictValue
     * @returns {*}
     * @private
     */
    function _returnInitInfo (dictValue) {
      $scope.vm.selected.valueType = dictValue.type;

      switch (dictValue.type) {
        case $scope.vm.valueTypes.text:
          return $scope.vm.selected.textValue = dictValue.value;
          break;
        case $scope.vm.valueTypes.master:
          return $scope.vm.selected.masterValue = dictValue.value;
          break;
        case $scope.vm.valueTypes.dict:
          return $scope.vm.selected.dictValue = dictValue.value;
          break;
      }
    }
  });
});