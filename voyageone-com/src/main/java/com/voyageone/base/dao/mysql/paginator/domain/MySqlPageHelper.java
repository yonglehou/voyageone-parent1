package com.voyageone.base.dao.mysql.paginator.domain;

import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分页查询对象
 *
 * @author badqiu
 * @author hunhun
 * @author miemiedev
 */
public class MySqlPageHelper {

    private static final String key = "#MySqlPageHelper#";

    /**
     * 加入检索条件
     *
     * @param queryParam SQL检索条件MAP
     * @return PageBoundsMap
     */
    public static PageBoundsMap queryParam(Map<String, Object> queryParam) {
        return new PageBoundsMap(queryParam);
    }

    public static class PageBoundsMap {
        // SQL检索条件MAP
        private Map<String, Object> param = new HashMap<>();

        /**
         * 构造函数
         *
         * @param param SQL检索条件MAP
         */
        private PageBoundsMap(Map<String, Object> param) {
            this.param.putAll(param);
            this.param.put(key, new PageBounds());
        }

        /**
         * 设置排序条件
         *
         * @param sort 排序条件
         */
        public PageBoundsMap sort(String sort) {
            ((PageBounds) this.param.get(key)).setOrders(MySqlOrder.formString(sort));
            return this;
        }

        /**
         * 添加排序条件
         * @param property 排序子段
         * @param direction 排序方向
         */
        public PageBoundsMap addSort(String property, Order.Direction direction) {
            PageBounds pageBounds = ((PageBounds) this.param.get(key));
            List<Order> orders = pageBounds.getOrders();
            orders.add(new MySqlOrder(property, direction, null));
            return this;
        }

        /**
         * 设置page数
         *
         * @param page page数
         */
        public PageBoundsMap page(int page) {
            ((PageBounds) this.param.get(key)).setPage(page);
            return this;
        }

        /**
         * 设置limit数
         *
         * @param limit limit数
         */
        public PageBoundsMap limit(int limit) {
            ((PageBounds) this.param.get(key)).setLimit(limit);
            return this;
        }

        /**
         * value
         */
        public Map<String, Object> toMap() {
            return param;
        }
    }
}