/**
 * @Date:    2015-11-16 20:51:05
 * @User:    Jonas
 * @Version: 0.2.0
 */
angular.module("voyageone.angular.factories").factory("interceptorFactory", function () {
    // 未知的系统错误
    var UNKNOWN_CODE = "5";
    // 和 JAVA 同步,系统通知前端自动跳转的特殊代码
    var CODE_SYS_REDIRECT = "SYS_REDIRECT";
    // 和 JAVA 同步,回话过期的信息
    var MSG_TIMEOUT = "300001";
    var MSG_LOCKED = "A003";
    var MSG_MANYFAILS = "A004";
    var MSG_MISSAUTHENTICATION = "A005";
    var MSG_CHANGEPASS = "A006";
    var MSG_LOGINAGAIN = "A001";
    var CODE_SEL_CHANNEL = "SYS_0";

    /**
     * 对系统自动跳转的响应,执行跳转
     * @param {{code:string,redirectTo:string}} res
     * @returns {boolean}
     */
    function autoRedirect(res) {
        if (res.code != CODE_SYS_REDIRECT && res.code != CODE_SEL_CHANNEL) {
            return false;
        }
        // 如果跳转数据异常,则默认跳转登陆页
        location.href = res.redirectTo || "/login.html";
        return true;
    }

    /**
     * 对会话超时和未登录进行特殊处理
     * @param {{code:string}} res
     * @returns {boolean}
     */
    function sessionTimeout(res) {
        if (res.code != MSG_TIMEOUT) {
            return false;
        }
        // 会话超时,默认跳转到登陆页
        location.href = "/login.html";
        return true;
    }

    /**
     * 对会话超时和未登录进行特殊处理(Admin)
     */
    //function adminSessionTimeout(res) {
    //    if (res.code != MSG_MISSAUTHENTICATION) {
    //        return false;
    //    }
    //    // 会话超时,默认跳转到登陆页
    //    location.href = "/adminLogin.html";
    //    return true;
    //}

    //function adminResetPassword(res) {
    //    if (res.code != MSG_CHANGEPASS) {
    //        return false;
    //    }
    //    // 密码输入错误,默认跳转到重置密码界面
    //    location.href = "/adminResetPass.html";
    //    return true;
    //}

    function adminReLogin(res) {
        if (res.code != MSG_LOGINAGAIN) {
            return false;
        }
        // 密码输入错误,默认跳转到重置密码界面
        location.href = "/";
        return true;
    }

    /**
     * 处理位置的异常
     * @param response
     */
    function unknownException(response) {
        if (response.data.code !== UNKNOWN_CODE) {
            return;
        }
        window.$$lastUnknow = response;
        console.error("Server throw unknown exceptio. Message:", response.data.message);
    }

    return {
        request: function (config) {
            return config;
        },
        /**
         * 响应拦截
         * @param {{config:object, data:*, headers:function, status:number, statusText:string}} res
         * @returns {*}
         */
        response: function (res) {
            var result = res.data;
            // 特殊处理部分内容
           // if (autoRedirect(result) || sessionTimeout(result) || adminSessionTimeout(result) || adminResetPassword(result)||adminReLogin(result)) {
            if (autoRedirect(result) || sessionTimeout(result)||adminReLogin(result)) {
                return res;
            }
            unknownException(res);
            return res;
        },
        requestError: function (config) {
            return config;
        },
        responseError: function (res) {
        }
    };
}).config(function ($httpProvider) {
    $httpProvider.interceptors.push("interceptorFactory");
});
