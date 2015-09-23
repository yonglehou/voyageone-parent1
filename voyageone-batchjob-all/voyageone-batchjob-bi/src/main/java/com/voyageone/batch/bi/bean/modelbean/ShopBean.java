package com.voyageone.batch.bi.bean.modelbean;

import java.util.Date;

/**
 * Created by Kylin on 2015/6/5.
 */
public class ShopBean {

    private int id;
    private String code;
    private String name;
    private String name_en;
    private String name_cn;
    private int channel_id;
    private int ecomm_id;
    private int enable;
    private String shop_user;
    private String shop_ps;
    private Date create_time;
    private String create_user;
    private Date update_time;
    private String update_user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_en() {
        return name_en;
    }

    public void setName_en(String name_en) {
        this.name_en = name_en;
    }

    public String getName_cn() {
        return name_cn;
    }

    public void setName_cn(String name_cn) {
        this.name_cn = name_cn;
    }

    public int getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(int channel_id) {
        this.channel_id = channel_id;
    }

    public int getEcomm_id() {
        return ecomm_id;
    }

    public void setEcomm_id(int ecomm_id) {
        this.ecomm_id = ecomm_id;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public String getShop_user() {
        return shop_user;
    }

    public void setShop_user(String shop_user) {
        this.shop_user = shop_user;
    }

    public String getShop_ps() {
        return shop_ps;
    }

    public void setShop_ps(String shop_ps) {
        this.shop_ps = shop_ps;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public String getCreate_user() {
        return create_user;
    }

    public void setCreate_user(String create_user) {
        this.create_user = create_user;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }

    public String getUpdate_user() {
        return update_user;
    }

    public void setUpdate_user(String update_user) {
        this.update_user = update_user;
    }
}
