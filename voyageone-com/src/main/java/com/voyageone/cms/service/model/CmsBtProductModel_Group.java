package com.voyageone.cms.service.model;


import com.voyageone.base.dao.mongodb.model.BaseMongoMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CmsBtProductModel_Group extends BaseMongoMap {

    public CmsBtProductModel_Group() {
        if (!this.containsKey("platforms")) {
            setAttribute("platforms", new ArrayList<CmsBtProductModel_Group_Platform>());
        }
    }
    public double getMsrpStart() {
        return (Double) getAttribute("msrpStart");
    }

    public void setMsrpStart(double msrpStart) {
        setAttribute("msrpStart", msrpStart);
    }

    public double getMsrpEnd() {
        return (Double) getAttribute("msrpEnd");
    }

    public void setMsrpEnd(double msrpEnd) {
        setAttribute("msrpEnd", msrpEnd);
    }

    public double getRetailPriceStart() {
        return (Double) getAttribute("retailPriceStart");
    }

    public void setRetailPriceStart(double retailPriceStart) {
        setAttribute("retailPriceStart", retailPriceStart);
    }

    public double getRetailPriceEnd() {
        return (Double) getAttribute("retailPriceEnd");
    }

    public void setRetailPriceEnd(double retailPriceEnd) {
        setAttribute("retailPriceEnd", retailPriceEnd);
    }

    public double getSalePriceStart() {
        return (Double) getAttribute("salePriceStart");
    }

    public void setSalePriceStart(double salePriceStart) {
        setAttribute("salePriceStart", salePriceStart);
    }

    public double getSalePriceEnd() {
        return (Double) getAttribute("salePriceEnd");
    }

    public void setSalePriceEnd(double salePriceEnd) {
        setAttribute("salePriceEnd", salePriceEnd);
    }

    public double getCurrentPriceStart() {
        return (Double) getAttribute("currentPriceStart");
    }

    public void setCurrentPriceStart(double currentPriceStart) {
        setAttribute("currentPriceStart", currentPriceStart);
    }

    public double getCurrentPriceEnd() {
        return (Double) getAttribute("currentPriceEnd");
    }

    public void setCurrentPriceEnd(double currentPriceEnd) {
        setAttribute("currentPriceEnd", currentPriceEnd);
    }

    public List<CmsBtProductModel_Group_Platform> getPlatforms() {
        if (!this.containsKey("platforms") || getAttribute("platforms") == null) {
            setAttribute("platforms", new ArrayList<CmsBtProductModel_Group_Platform>());
        }
        return (List<CmsBtProductModel_Group_Platform>) getAttribute("platforms");
    }

    public void setPlatforms(List<CmsBtProductModel_Group_Platform> platforms) {
        setAttribute("platforms", platforms);
    }

    public CmsBtProductModel_Group_Platform getPlatformByGroupId(int groupId) {
        CmsBtProductModel_Group_Platform result = null;
        List<CmsBtProductModel_Group_Platform> platforms = getPlatforms();
        for (CmsBtProductModel_Group_Platform platform : platforms) {
            if (platform.getGroupId() == groupId) {
                result = platform;
                break;
            }
        }
        return  result;
    }

    public CmsBtProductModel_Group_Platform getPlatformByNumIId(int cartId, String numIId) {
        CmsBtProductModel_Group_Platform result = null;
        List<CmsBtProductModel_Group_Platform> platforms = getPlatforms();
        for (CmsBtProductModel_Group_Platform platform : platforms) {
            if (platform.getCartId() == cartId && platform.getNumIId().equals(numIId)) {
                result = platform;
                break;
            }
        }
        return  result;
    }

    @Override
    public Object put(Object key, Object value) {
        if ("platforms".equals(key)) {
            List<Map> platformMaps = (List<Map>)value;
            if (platformMaps != null) {
                List<CmsBtProductModel_Group_Platform> platforms = new ArrayList<>();
                for (Map map : platformMaps) {
                    if (map != null) {
                        CmsBtProductModel_Group_Platform platform = new CmsBtProductModel_Group_Platform(map);
                        platforms.add(platform);
                    }
                }
                value = platforms;
            }
        }
        return super.put(key, value);
    }

}