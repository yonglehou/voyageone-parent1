define(function () {

    // 缓存作用域
    var CACHE = {
        NONE: 0,
        ONCE: 1,
        SESSION: 2,
        LOCAL: 3
    };

    // 作为额外缓存关键字的关键字名称
    var KEY = {
        USERNAME: 'username',
        CHANNEL: 'channel'
    };

    /**
     * 生产一个配置好的 action 配置对象
     * @param {string} action Action 名称
     * @param {Array} [cacheWith] 缓存时, 额外追加的缓存关键字, 参见 KEY 对象提供的字段
     * @returns {{url: *, cache: number, cacheWith: *}}
     */
    function session(action, cacheWith) {
        return {
            url: action,
            cache: CACHE.SESSION,
            cacheWith: cacheWith
        };
    }

    return {
        "admin": {
            /** 用户信息 */
            "user": {
                "adminUserService": {
                    "root": "/admin/user/self",
                    "searchUser": 'searchUser',
                    "init": 'init',
                    "addUser": "addUser",
                    "updateUser": "updateUser",
                    "deleteUser": "deleteUser",
                    "getAuthByUser": "getAuthByUser",
                    "showAuth": "showAuth",
                    "resetPass": "resetPass",
                    "editPass": "editPass",
                    "forgetPass": "forgetPass",
                    "getUserByToken": "getUserByToken",
                    "modifyPass": "modifyPass",
                    "getAllApp": "getAllApp",
                    "getAppsByUser": "getAppsByUser",
                    "addRoles": "addRoles"
                },
                "adminRoleService": {
                    "root": "/admin/user/role",
                    "searchRole": 'searchRole',
                    "init": 'init',
                    "addRole": "addRole",
                    "updateRole": "updateRole",
                    "deleteRole": "deleteRole",
                    "setAuth": "setAuth",
                    "addAuth": "addAuth",
                    "removeAuth": "removeAuth",
                    "getAuthByRoles": "getAuthByRoles",
                    "getAllRole": "getAllRole",
                    "getAllRoleType": "getAllRoleType",
                    "copyRole": "copyRole"
                },
                "adminOrgService": {
                    "root": "/admin/user/org",
                    "searchOrg": 'searchOrg',
                    "init": 'init',
                    "addOrg": "addOrg",
                    "updateOrg": "updateOrg",
                    "deleteOrg": "deleteOrg",
                    "getAllOrg": "getAllOrg"
                },
                "adminResService": {
                    "root": "/admin/user/res",
                    "searchRes": 'searchRes',
                    "init": 'init',
                    "addRes": "addRes",
                    "updateRes": "updateRes",
                    "deleteRes": "deleteRes",
                    "getMenu": "getMenu",
                    "getAllMenu": "getAllMenu"
                }
            },
            /** 渠道信息 */
            "channel": {
                "channelService": {
                    "root": "/admin/channel/self",
                    "getAllChannel": 'getAllChannel',
                    "getAllCompany": 'getAllCompany',
                    "addChannel": "addChannel",
                    "updateChannel": "updateChannel",
                    "deleteChannel": "deleteChannel",
                    "searchChannelByPage": "searchChannelByPage",
                    "generateSecretKey": "generateSecretKey",
                    "generateSessionKey": "generateSessionKey",
                    "searchChannelConfigByPage": "searchChannelConfigByPage",
                    "addChannelConfig": "addChannelConfig",
                    "updateChannelConfig": "updateChannelConfig",
                    "deleteChannelConfig": "deleteChannelConfig",
                    "isChannelUsed": "isChannelUsed"
                },
                "channelAttributeService": {
                    "root": "/admin/channel/attribute",
                    "searchChannelAttributeByPage": 'searchChannelAttributeByPage',
                    "addChannelAttribute": 'addChannelAttribute',
                    "updateChannelAttribute": "updateChannelAttribute",
                    "deleteChannelAttribute": "deleteChannelAttribute"
                },
                "thirdPartyConfigService": {
                    "root": "/admin/channel/thirdParty",
                    "searchThirdPartyConfigByPage": 'searchThirdPartyConfigByPage',
                    "addThirdPartyConfig": 'addThirdPartyConfig',
                    "updateThirdPartyConfig": "updateThirdPartyConfig",
                    "deleteThirdPartyConfig": "deleteThirdPartyConfig"
                },
                "smsConfigService": {
                    "root": "/admin/channel/sms",
                    "searchSmsConfigByPage": 'searchSmsConfigByPage',
                    "addSmsConfig": 'addSmsConfig',
                    "updateSmsConfig": "updateSmsConfig",
                    "deleteSmsConfig": "deleteSmsConfig"
                },
                "carrierConfigService": {
                    "root": "/admin/channel/carrier",
                    "searchCarrierConfigByPage": 'searchCarrierConfigByPage',
                    "addCarrierConfig": 'addCarrierConfig',
                    "updateCarrierConfig": "updateCarrierConfig",
                    "deleteCarrierConfig": "deleteCarrierConfig",
                    "getAllCarrier": "getAllCarrier"
                }
            },
            /** Cart信息 */
            "cart": {
                "AdminCartService": {
                    "root": "/admin/cart/self",
                    "getAllCart": "getAllCart",
                    "getCartByIds": "getCartByIds",
                    "searchCartByPage": "searchCartByPage",
                    "getAllPlatform": "getAllPlatform",
                    "addCart": "addCart",
                    "updateCart": "updateCart",
                    "deleteCart": "deleteCart",
                    "deleteStore": "deleteStore"
                },
                "cartShopService": {
                    "root": "/admin/cart/shop",
                    "searchCartShopByPage": "searchCartShopByPage",
                    "addCartShop": "addCartShop",
                    "updateCartShop": "updateCartShop",
                    "deleteCartShop": "deleteCartShop",
                    "searchCartShopConfigByPage": "searchCartShopConfigByPage",
                    "addCartShopConfig": "addCartShopConfig",
                    "updateCartShopConfig": "updateCartShopConfig",
                    "deleteCartShopConfig": "deleteCartShopConfig"
                },
                "cartTrackingService": {
                    "root": "/admin/cart/tracking",
                    "searchCartTrackingByPage": "searchCartTrackingByPage",
                    "addCartTracking": "addCartTracking",
                    "updateCartTracking": "updateCartTracking",
                    "deleteCartTracking": "deleteCartTracking"
                }
            },
            /** 统一属性配置 */
            "config": {
                "AdminChannelService": {
                    "root": "/admin/system/config"
                }
            },
            /** 仓库信息 */
            "store": {
                "storeService": {
                    "root": "/admin/store/self",
                    "searchStoreByPage": "searchStoreByPage",
                    "searchStoreConfigByPage": "searchStoreConfigByPage",
                    "getStoreByChannelIds": "getStoreByChannelIds",
                    "getAllStore": "getAllStore",
                    "addStore": "addStore",
                    "updateStore": "updateStore",
                    "deleteStore": "deleteStore",
                    "addStoreConfig": "addStoreConfig",
                    "updateStoreConfig": "updateStoreConfig",
                    "deleteStoreConfig": "deleteStoreConfig"
                }
            },
            /** 统一属性配置 */
            "system": {
                "typeService": {
                    "root": "/admin/system/type",
                    "searchTypeByPage": "searchTypeByPage",
                    "addType": "addType",
                    "updateType": "updateType",
                    "deleteType": "deleteType",
                    "getAllType": "getAllType"
                },
                "typeAttrService": {
                    "root": "/admin/system/attribute",
                    "searchTypeAttributeByPage": "searchTypeAttributeByPage",
                    "addTypeAttribute": "addTypeAttribute",
                    "updateTypeAttribute": "updateTypeAttribute",
                    "deleteTypeAttribute": "deleteTypeAttribute"
                },
                "codeService": {
                    "root": "/admin/system/code",
                    "searchCodeByPage": "searchCodeByPage",
                    "addCode": "addCode",
                    "updateCode": "updateCode",
                    "deleteCode": "deleteCode"
                },
                /** 港口信息 */
                "portConfigService": {
                    "root": "/admin/system/port",
                    "searchPortConfigByPage": "searchPortConfigByPage",
                    "addPortConfig": "addPortConfig",
                    "updatePortConfig": "updatePortConfig",
                    "deletePortConfig": "deletePortConfig",
                    "getAllPort": "getAllPort"
                }
            },
            /** 任务信息 */
            "task": {
                "taskService": {
                    "root": "/admin/task/self",
                    "searchTaskByPage": "searchTaskByPage",
                    "addTask": "addTask",
                    "updateTask": "updateTask",
                    "deleteTask": "deleteTask",
                    "searchTaskConfigByPage": "searchTaskConfigByPage",
                    "deleteTaskConfig": "deleteTaskConfig",
                    "addTaskConfig": "addTaskConfig",
                    "getAllTaskType": "getAllTaskType",
                    "getAllTask": "getAllTask",
                    "startTask": "startTask",
                    "stopTask": "stopTask"
                }
            },
            "newShop": {
                /** 开店信息 */
                "newShopService": {
                    "root": "/admin/newshop/self",
                    "getChannelSeries": "getChannelSeries",
                    "saveChannelSeries": "saveChannelSeries",
                    "downloadNewShopSql": "downloadNewShopSql",
                    "deleteNewShop": "deleteNewShop",
                    "searchNewShopByPage": "searchNewShopByPage",
                    "getNewShopById": "getNewShopById"
                },
                /** 操作日志 */
                "adminLogService": {
                    "root": "/admin/log/action",
                    "init": "init",
                    "searchLog": "searchLog",
                    "getLogDetail": "getLogDetail"
                }
            },
            "log": {
                /** 登录日志 */
                "adminLoginLogService": {
                    "root": "/admin/log/login",
                    "init": "init",
                    "searchLog": "searchLog"
                },
                /** 操作日志 */
                "adminLogService": {
                    "root": "/admin/log/action",
                    "init": "init",
                    "searchLog": "searchLog",
                    "getLogDetail": "getLogDetail"
                }
            }
        }
    };
});