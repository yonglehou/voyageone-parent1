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
        
    };
});