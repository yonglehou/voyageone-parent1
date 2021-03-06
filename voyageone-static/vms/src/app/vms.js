define(function (require) {

    require('components/menu.component');
    require('controllers/topbar.controller');
    require('./popups.factory');

    var angularAMD = require('angularAMD');
    var services = require('./actions.services');
    var router = require('./router');
    var _ = require('underscore');

    var en = require('./translate/en');
    var zh = require('./translate/zh');

    var app = angular.module('vms', [
        'ngSanitize',
        'ngRoute',
        'ngAnimate',
        'ngCookies',
        'ngStorage',
        'pascalprecht.translate',
        'blockUI',
        'ui.bootstrap',
        'vo.ng',
        'localytics.directives',
        'angular-md5',
        'vms.menu',
        'vms.topbar',
        'vms.popups',
        'angularFileUpload',
        'chart.js'
    ]).config(function ($routeProvider, $translateProvider) {

        // 修改时间格式的json转换为时间戳格式
        Date.prototype.toJSON = function () {
            return this.getTime();
        };

        $translateProvider.translations('zh', zh);
        $translateProvider.translations('en', en);

        _.each(router.routes, function (module) {
            return $routeProvider.when(module.hash, angularAMD.route(module));
        });

        $routeProvider.otherwise(router.otherwise);
    }).run(function(translateService) {
        translateService.setLanguage('en');
    });

    function eachDeclareService(_services) {
        _.each(_services, function (content, key) {
            if (content instanceof CommonDataService) {
                app.service(key, content.getDeclare());
            } else if (_.isObject(content)) {
                eachDeclareService(content);
            }
        });
    }

    eachDeclareService(services);

    return angularAMD.bootstrap(app);
});