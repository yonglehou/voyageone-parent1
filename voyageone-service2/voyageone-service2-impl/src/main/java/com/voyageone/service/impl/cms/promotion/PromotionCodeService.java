package com.voyageone.service.impl.cms.promotion;

import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.DateTimeUtilBeijing;
import com.voyageone.service.bean.cms.CmsBtPromotionCodesBean;
import com.voyageone.service.daoext.cms.CmsBtPromotionCodesDaoExt;
import com.voyageone.service.impl.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author aooer 2016/2/18.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class PromotionCodeService extends BaseService {

    @Autowired
    private CmsBtPromotionCodesDaoExt cmsBtPromotionCodesDaoExt;

    public List<CmsBtPromotionCodesBean> getPromotionCodeList(Map<String, Object> param) {
        return cmsBtPromotionCodesDaoExt.selectPromotionCodeList(param);
    }

    public int getPromotionCodeListCnt(Map<String, Object> params) {
        return cmsBtPromotionCodesDaoExt.selectPromotionCodeListCnt(params);
    }

    public List<CmsBtPromotionCodesBean> getPromotionCodeListByIdOrgChannelId(int promotionId, String orgChannelId) {
        Map<String, Object> params = new HashMap<>();
        params.put("promotionId", promotionId);
        params.put("orgChannelId", orgChannelId);
        return cmsBtPromotionCodesDaoExt.selectPromotionCodeSkuList(params);
    }

    public List<Map<String, Object>> getPromotionCodesByPromotionIds(List<String> promotionIdList) {
        return cmsBtPromotionCodesDaoExt.selectCmsBtPromotionAllCodeByPromotionIdS(promotionIdList);
    }


    /**
     * 判断是否存在现在时点，指定Code正处于没有结束的活动中。
     */
    public String getExistCodeInActivePromotion(String channelId, String productCode) {
        Map<String, Object> param = new HashMap<>();
        param.put("channelId", channelId);
        param.put("productCode", productCode);
        param.put("now", DateTimeUtil.format(DateTimeUtilBeijing.getCurrentBeiJingDate(), DateTimeUtil.DEFAULT_DATETIME_FORMAT));
        return cmsBtPromotionCodesDaoExt.selectCodeInActivePromotionName(param);
    }

}
