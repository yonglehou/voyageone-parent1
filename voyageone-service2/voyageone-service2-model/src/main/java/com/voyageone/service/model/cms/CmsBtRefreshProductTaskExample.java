/*
 * CmsBtRefreshProductTaskExample.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.model.cms;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CmsBtRefreshProductTaskExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public CmsBtRefreshProductTaskExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * 默认属性功能下的商品重刷任务记录表
     */
    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("`id` is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("`id` is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("`id` =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("`id` <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("`id` >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("`id` >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("`id` <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("`id` <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("`id` in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("`id` not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("`id` between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("`id` not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andChannelidIsNull() {
            addCriterion("`channelId` is null");
            return (Criteria) this;
        }

        public Criteria andChannelidIsNotNull() {
            addCriterion("`channelId` is not null");
            return (Criteria) this;
        }

        public Criteria andChannelidEqualTo(String value) {
            addCriterion("`channelId` =", value, "channelid");
            return (Criteria) this;
        }

        public Criteria andChannelidNotEqualTo(String value) {
            addCriterion("`channelId` <>", value, "channelid");
            return (Criteria) this;
        }

        public Criteria andChannelidGreaterThan(String value) {
            addCriterion("`channelId` >", value, "channelid");
            return (Criteria) this;
        }

        public Criteria andChannelidGreaterThanOrEqualTo(String value) {
            addCriterion("`channelId` >=", value, "channelid");
            return (Criteria) this;
        }

        public Criteria andChannelidLessThan(String value) {
            addCriterion("`channelId` <", value, "channelid");
            return (Criteria) this;
        }

        public Criteria andChannelidLessThanOrEqualTo(String value) {
            addCriterion("`channelId` <=", value, "channelid");
            return (Criteria) this;
        }

        public Criteria andChannelidLike(String value) {
            addCriterion("`channelId` like", value, "channelid");
            return (Criteria) this;
        }

        public Criteria andChannelidNotLike(String value) {
            addCriterion("`channelId` not like", value, "channelid");
            return (Criteria) this;
        }

        public Criteria andChannelidIn(List<String> values) {
            addCriterion("`channelId` in", values, "channelid");
            return (Criteria) this;
        }

        public Criteria andChannelidNotIn(List<String> values) {
            addCriterion("`channelId` not in", values, "channelid");
            return (Criteria) this;
        }

        public Criteria andChannelidBetween(String value1, String value2) {
            addCriterion("`channelId` between", value1, value2, "channelid");
            return (Criteria) this;
        }

        public Criteria andChannelidNotBetween(String value1, String value2) {
            addCriterion("`channelId` not between", value1, value2, "channelid");
            return (Criteria) this;
        }

        public Criteria andCartidIsNull() {
            addCriterion("`cartId` is null");
            return (Criteria) this;
        }

        public Criteria andCartidIsNotNull() {
            addCriterion("`cartId` is not null");
            return (Criteria) this;
        }

        public Criteria andCartidEqualTo(Integer value) {
            addCriterion("`cartId` =", value, "cartid");
            return (Criteria) this;
        }

        public Criteria andCartidNotEqualTo(Integer value) {
            addCriterion("`cartId` <>", value, "cartid");
            return (Criteria) this;
        }

        public Criteria andCartidGreaterThan(Integer value) {
            addCriterion("`cartId` >", value, "cartid");
            return (Criteria) this;
        }

        public Criteria andCartidGreaterThanOrEqualTo(Integer value) {
            addCriterion("`cartId` >=", value, "cartid");
            return (Criteria) this;
        }

        public Criteria andCartidLessThan(Integer value) {
            addCriterion("`cartId` <", value, "cartid");
            return (Criteria) this;
        }

        public Criteria andCartidLessThanOrEqualTo(Integer value) {
            addCriterion("`cartId` <=", value, "cartid");
            return (Criteria) this;
        }

        public Criteria andCartidIn(List<Integer> values) {
            addCriterion("`cartId` in", values, "cartid");
            return (Criteria) this;
        }

        public Criteria andCartidNotIn(List<Integer> values) {
            addCriterion("`cartId` not in", values, "cartid");
            return (Criteria) this;
        }

        public Criteria andCartidBetween(Integer value1, Integer value2) {
            addCriterion("`cartId` between", value1, value2, "cartid");
            return (Criteria) this;
        }

        public Criteria andCartidNotBetween(Integer value1, Integer value2) {
            addCriterion("`cartId` not between", value1, value2, "cartid");
            return (Criteria) this;
        }

        public Criteria andCategorypathIsNull() {
            addCriterion("`categoryPath` is null");
            return (Criteria) this;
        }

        public Criteria andCategorypathIsNotNull() {
            addCriterion("`categoryPath` is not null");
            return (Criteria) this;
        }

        public Criteria andCategorypathEqualTo(String value) {
            addCriterion("`categoryPath` =", value, "categorypath");
            return (Criteria) this;
        }

        public Criteria andCategorypathNotEqualTo(String value) {
            addCriterion("`categoryPath` <>", value, "categorypath");
            return (Criteria) this;
        }

        public Criteria andCategorypathGreaterThan(String value) {
            addCriterion("`categoryPath` >", value, "categorypath");
            return (Criteria) this;
        }

        public Criteria andCategorypathGreaterThanOrEqualTo(String value) {
            addCriterion("`categoryPath` >=", value, "categorypath");
            return (Criteria) this;
        }

        public Criteria andCategorypathLessThan(String value) {
            addCriterion("`categoryPath` <", value, "categorypath");
            return (Criteria) this;
        }

        public Criteria andCategorypathLessThanOrEqualTo(String value) {
            addCriterion("`categoryPath` <=", value, "categorypath");
            return (Criteria) this;
        }

        public Criteria andCategorypathLike(String value) {
            addCriterion("`categoryPath` like", value, "categorypath");
            return (Criteria) this;
        }

        public Criteria andCategorypathNotLike(String value) {
            addCriterion("`categoryPath` not like", value, "categorypath");
            return (Criteria) this;
        }

        public Criteria andCategorypathIn(List<String> values) {
            addCriterion("`categoryPath` in", values, "categorypath");
            return (Criteria) this;
        }

        public Criteria andCategorypathNotIn(List<String> values) {
            addCriterion("`categoryPath` not in", values, "categorypath");
            return (Criteria) this;
        }

        public Criteria andCategorypathBetween(String value1, String value2) {
            addCriterion("`categoryPath` between", value1, value2, "categorypath");
            return (Criteria) this;
        }

        public Criteria andCategorypathNotBetween(String value1, String value2) {
            addCriterion("`categoryPath` not between", value1, value2, "categorypath");
            return (Criteria) this;
        }

        public Criteria andCategorytypeIsNull() {
            addCriterion("`categoryType` is null");
            return (Criteria) this;
        }

        public Criteria andCategorytypeIsNotNull() {
            addCriterion("`categoryType` is not null");
            return (Criteria) this;
        }

        public Criteria andCategorytypeEqualTo(Integer value) {
            addCriterion("`categoryType` =", value, "categorytype");
            return (Criteria) this;
        }

        public Criteria andCategorytypeNotEqualTo(Integer value) {
            addCriterion("`categoryType` <>", value, "categorytype");
            return (Criteria) this;
        }

        public Criteria andCategorytypeGreaterThan(Integer value) {
            addCriterion("`categoryType` >", value, "categorytype");
            return (Criteria) this;
        }

        public Criteria andCategorytypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("`categoryType` >=", value, "categorytype");
            return (Criteria) this;
        }

        public Criteria andCategorytypeLessThan(Integer value) {
            addCriterion("`categoryType` <", value, "categorytype");
            return (Criteria) this;
        }

        public Criteria andCategorytypeLessThanOrEqualTo(Integer value) {
            addCriterion("`categoryType` <=", value, "categorytype");
            return (Criteria) this;
        }

        public Criteria andCategorytypeIn(List<Integer> values) {
            addCriterion("`categoryType` in", values, "categorytype");
            return (Criteria) this;
        }

        public Criteria andCategorytypeNotIn(List<Integer> values) {
            addCriterion("`categoryType` not in", values, "categorytype");
            return (Criteria) this;
        }

        public Criteria andCategorytypeBetween(Integer value1, Integer value2) {
            addCriterion("`categoryType` between", value1, value2, "categorytype");
            return (Criteria) this;
        }

        public Criteria andCategorytypeNotBetween(Integer value1, Integer value2) {
            addCriterion("`categoryType` not between", value1, value2, "categorytype");
            return (Criteria) this;
        }

        public Criteria andFieldidIsNull() {
            addCriterion("`fieldId` is null");
            return (Criteria) this;
        }

        public Criteria andFieldidIsNotNull() {
            addCriterion("`fieldId` is not null");
            return (Criteria) this;
        }

        public Criteria andFieldidEqualTo(String value) {
            addCriterion("`fieldId` =", value, "fieldid");
            return (Criteria) this;
        }

        public Criteria andFieldidNotEqualTo(String value) {
            addCriterion("`fieldId` <>", value, "fieldid");
            return (Criteria) this;
        }

        public Criteria andFieldidGreaterThan(String value) {
            addCriterion("`fieldId` >", value, "fieldid");
            return (Criteria) this;
        }

        public Criteria andFieldidGreaterThanOrEqualTo(String value) {
            addCriterion("`fieldId` >=", value, "fieldid");
            return (Criteria) this;
        }

        public Criteria andFieldidLessThan(String value) {
            addCriterion("`fieldId` <", value, "fieldid");
            return (Criteria) this;
        }

        public Criteria andFieldidLessThanOrEqualTo(String value) {
            addCriterion("`fieldId` <=", value, "fieldid");
            return (Criteria) this;
        }

        public Criteria andFieldidLike(String value) {
            addCriterion("`fieldId` like", value, "fieldid");
            return (Criteria) this;
        }

        public Criteria andFieldidNotLike(String value) {
            addCriterion("`fieldId` not like", value, "fieldid");
            return (Criteria) this;
        }

        public Criteria andFieldidIn(List<String> values) {
            addCriterion("`fieldId` in", values, "fieldid");
            return (Criteria) this;
        }

        public Criteria andFieldidNotIn(List<String> values) {
            addCriterion("`fieldId` not in", values, "fieldid");
            return (Criteria) this;
        }

        public Criteria andFieldidBetween(String value1, String value2) {
            addCriterion("`fieldId` between", value1, value2, "fieldid");
            return (Criteria) this;
        }

        public Criteria andFieldidNotBetween(String value1, String value2) {
            addCriterion("`fieldId` not between", value1, value2, "fieldid");
            return (Criteria) this;
        }

        public Criteria andAllproductIsNull() {
            addCriterion("`allProduct` is null");
            return (Criteria) this;
        }

        public Criteria andAllproductIsNotNull() {
            addCriterion("`allProduct` is not null");
            return (Criteria) this;
        }

        public Criteria andAllproductEqualTo(Boolean value) {
            addCriterion("`allProduct` =", value, "allproduct");
            return (Criteria) this;
        }

        public Criteria andAllproductNotEqualTo(Boolean value) {
            addCriterion("`allProduct` <>", value, "allproduct");
            return (Criteria) this;
        }

        public Criteria andAllproductGreaterThan(Boolean value) {
            addCriterion("`allProduct` >", value, "allproduct");
            return (Criteria) this;
        }

        public Criteria andAllproductGreaterThanOrEqualTo(Boolean value) {
            addCriterion("`allProduct` >=", value, "allproduct");
            return (Criteria) this;
        }

        public Criteria andAllproductLessThan(Boolean value) {
            addCriterion("`allProduct` <", value, "allproduct");
            return (Criteria) this;
        }

        public Criteria andAllproductLessThanOrEqualTo(Boolean value) {
            addCriterion("`allProduct` <=", value, "allproduct");
            return (Criteria) this;
        }

        public Criteria andAllproductIn(List<Boolean> values) {
            addCriterion("`allProduct` in", values, "allproduct");
            return (Criteria) this;
        }

        public Criteria andAllproductNotIn(List<Boolean> values) {
            addCriterion("`allProduct` not in", values, "allproduct");
            return (Criteria) this;
        }

        public Criteria andAllproductBetween(Boolean value1, Boolean value2) {
            addCriterion("`allProduct` between", value1, value2, "allproduct");
            return (Criteria) this;
        }

        public Criteria andAllproductNotBetween(Boolean value1, Boolean value2) {
            addCriterion("`allProduct` not between", value1, value2, "allproduct");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("`status` is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("`status` is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(Integer value) {
            addCriterion("`status` =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Integer value) {
            addCriterion("`status` <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Integer value) {
            addCriterion("`status` >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("`status` >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Integer value) {
            addCriterion("`status` <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Integer value) {
            addCriterion("`status` <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Integer> values) {
            addCriterion("`status` in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Integer> values) {
            addCriterion("`status` not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Integer value1, Integer value2) {
            addCriterion("`status` between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("`status` not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andCreatedIsNull() {
            addCriterion("`created` is null");
            return (Criteria) this;
        }

        public Criteria andCreatedIsNotNull() {
            addCriterion("`created` is not null");
            return (Criteria) this;
        }

        public Criteria andCreatedEqualTo(Date value) {
            addCriterion("`created` =", value, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedNotEqualTo(Date value) {
            addCriterion("`created` <>", value, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedGreaterThan(Date value) {
            addCriterion("`created` >", value, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedGreaterThanOrEqualTo(Date value) {
            addCriterion("`created` >=", value, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedLessThan(Date value) {
            addCriterion("`created` <", value, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedLessThanOrEqualTo(Date value) {
            addCriterion("`created` <=", value, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedIn(List<Date> values) {
            addCriterion("`created` in", values, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedNotIn(List<Date> values) {
            addCriterion("`created` not in", values, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedBetween(Date value1, Date value2) {
            addCriterion("`created` between", value1, value2, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedNotBetween(Date value1, Date value2) {
            addCriterion("`created` not between", value1, value2, "created");
            return (Criteria) this;
        }

        public Criteria andCreaterIsNull() {
            addCriterion("`creater` is null");
            return (Criteria) this;
        }

        public Criteria andCreaterIsNotNull() {
            addCriterion("`creater` is not null");
            return (Criteria) this;
        }

        public Criteria andCreaterEqualTo(String value) {
            addCriterion("`creater` =", value, "creater");
            return (Criteria) this;
        }

        public Criteria andCreaterNotEqualTo(String value) {
            addCriterion("`creater` <>", value, "creater");
            return (Criteria) this;
        }

        public Criteria andCreaterGreaterThan(String value) {
            addCriterion("`creater` >", value, "creater");
            return (Criteria) this;
        }

        public Criteria andCreaterGreaterThanOrEqualTo(String value) {
            addCriterion("`creater` >=", value, "creater");
            return (Criteria) this;
        }

        public Criteria andCreaterLessThan(String value) {
            addCriterion("`creater` <", value, "creater");
            return (Criteria) this;
        }

        public Criteria andCreaterLessThanOrEqualTo(String value) {
            addCriterion("`creater` <=", value, "creater");
            return (Criteria) this;
        }

        public Criteria andCreaterLike(String value) {
            addCriterion("`creater` like", value, "creater");
            return (Criteria) this;
        }

        public Criteria andCreaterNotLike(String value) {
            addCriterion("`creater` not like", value, "creater");
            return (Criteria) this;
        }

        public Criteria andCreaterIn(List<String> values) {
            addCriterion("`creater` in", values, "creater");
            return (Criteria) this;
        }

        public Criteria andCreaterNotIn(List<String> values) {
            addCriterion("`creater` not in", values, "creater");
            return (Criteria) this;
        }

        public Criteria andCreaterBetween(String value1, String value2) {
            addCriterion("`creater` between", value1, value2, "creater");
            return (Criteria) this;
        }

        public Criteria andCreaterNotBetween(String value1, String value2) {
            addCriterion("`creater` not between", value1, value2, "creater");
            return (Criteria) this;
        }

        public Criteria andModifiedIsNull() {
            addCriterion("`modified` is null");
            return (Criteria) this;
        }

        public Criteria andModifiedIsNotNull() {
            addCriterion("`modified` is not null");
            return (Criteria) this;
        }

        public Criteria andModifiedEqualTo(Date value) {
            addCriterion("`modified` =", value, "modified");
            return (Criteria) this;
        }

        public Criteria andModifiedNotEqualTo(Date value) {
            addCriterion("`modified` <>", value, "modified");
            return (Criteria) this;
        }

        public Criteria andModifiedGreaterThan(Date value) {
            addCriterion("`modified` >", value, "modified");
            return (Criteria) this;
        }

        public Criteria andModifiedGreaterThanOrEqualTo(Date value) {
            addCriterion("`modified` >=", value, "modified");
            return (Criteria) this;
        }

        public Criteria andModifiedLessThan(Date value) {
            addCriterion("`modified` <", value, "modified");
            return (Criteria) this;
        }

        public Criteria andModifiedLessThanOrEqualTo(Date value) {
            addCriterion("`modified` <=", value, "modified");
            return (Criteria) this;
        }

        public Criteria andModifiedIn(List<Date> values) {
            addCriterion("`modified` in", values, "modified");
            return (Criteria) this;
        }

        public Criteria andModifiedNotIn(List<Date> values) {
            addCriterion("`modified` not in", values, "modified");
            return (Criteria) this;
        }

        public Criteria andModifiedBetween(Date value1, Date value2) {
            addCriterion("`modified` between", value1, value2, "modified");
            return (Criteria) this;
        }

        public Criteria andModifiedNotBetween(Date value1, Date value2) {
            addCriterion("`modified` not between", value1, value2, "modified");
            return (Criteria) this;
        }

        public Criteria andModifierIsNull() {
            addCriterion("`modifier` is null");
            return (Criteria) this;
        }

        public Criteria andModifierIsNotNull() {
            addCriterion("`modifier` is not null");
            return (Criteria) this;
        }

        public Criteria andModifierEqualTo(String value) {
            addCriterion("`modifier` =", value, "modifier");
            return (Criteria) this;
        }

        public Criteria andModifierNotEqualTo(String value) {
            addCriterion("`modifier` <>", value, "modifier");
            return (Criteria) this;
        }

        public Criteria andModifierGreaterThan(String value) {
            addCriterion("`modifier` >", value, "modifier");
            return (Criteria) this;
        }

        public Criteria andModifierGreaterThanOrEqualTo(String value) {
            addCriterion("`modifier` >=", value, "modifier");
            return (Criteria) this;
        }

        public Criteria andModifierLessThan(String value) {
            addCriterion("`modifier` <", value, "modifier");
            return (Criteria) this;
        }

        public Criteria andModifierLessThanOrEqualTo(String value) {
            addCriterion("`modifier` <=", value, "modifier");
            return (Criteria) this;
        }

        public Criteria andModifierLike(String value) {
            addCriterion("`modifier` like", value, "modifier");
            return (Criteria) this;
        }

        public Criteria andModifierNotLike(String value) {
            addCriterion("`modifier` not like", value, "modifier");
            return (Criteria) this;
        }

        public Criteria andModifierIn(List<String> values) {
            addCriterion("`modifier` in", values, "modifier");
            return (Criteria) this;
        }

        public Criteria andModifierNotIn(List<String> values) {
            addCriterion("`modifier` not in", values, "modifier");
            return (Criteria) this;
        }

        public Criteria andModifierBetween(String value1, String value2) {
            addCriterion("`modifier` between", value1, value2, "modifier");
            return (Criteria) this;
        }

        public Criteria andModifierNotBetween(String value1, String value2) {
            addCriterion("`modifier` not between", value1, value2, "modifier");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    /**
     * 默认属性功能下的商品重刷任务记录表
     */
    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}