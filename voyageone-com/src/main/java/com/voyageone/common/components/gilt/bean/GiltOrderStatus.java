package com.voyageone.common.components.gilt.bean;

/**
 * @author aooer 2016/2/1.
 * @version 2.0.0
 * @since 2.0.0
 */
public enum GiltOrderStatus {

    PLACED("placed"),

    CONFIRMED("confirmed"),

    CANCELLED("cancelled"),

    PROCESSING("processing"),

    SHIPPED("shipped");

    private String description;

    GiltOrderStatus(String description) {
        this.description = description;
    }
}
