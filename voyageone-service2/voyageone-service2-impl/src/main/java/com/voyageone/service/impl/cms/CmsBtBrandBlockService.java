package com.voyageone.service.impl.cms;

import com.voyageone.common.util.StringUtils;
import com.voyageone.service.dao.cms.CmsBtBrandBlockDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.com.mq.MqSender;
import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.service.model.cms.CmsBtBrandBlockModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 品牌屏蔽（品牌黑名单）相关的操作
 * Created by jonas on 9/6/16.
 *
 * @author jonas
 * @version 2.6.0
 * @since 2.6.0
 */
@Service
public class CmsBtBrandBlockService extends BaseService {
    public final static int BRAND_TYPE_FEED = 0;
    public final static int BRAND_TYPE_MASTER = 1;
    public final static int BRAND_TYPE_PLATFORM = 2;

    private final CmsBtBrandBlockDao brandBlockDao;

    private final MqSender sender;

    @Autowired
    public CmsBtBrandBlockService(CmsBtBrandBlockDao brandBlockDao, MqSender sender) {
        this.brandBlockDao = brandBlockDao;
        this.sender = sender;
    }

    public void block(String channelId, int cartId, int brandType, String brand, String username) {
        switch (brandType) {
            case BRAND_TYPE_FEED:
            case BRAND_TYPE_MASTER:
            case BRAND_TYPE_PLATFORM:
                if (isBlocked(channelId, cartId, brandType, brand))
                    return;
                break;
            default:
                return;
        }
        CmsBtBrandBlockModel brandBlockModel = new CmsBtBrandBlockModel();
        brandBlockModel.setChannelId(channelId);
        switch (brandType) {
            case BRAND_TYPE_FEED:
                brandBlockModel.setCartId(1);
                break;
            case BRAND_TYPE_MASTER:
                brandBlockModel.setCartId(0);
                break;
            case BRAND_TYPE_PLATFORM:
                brandBlockModel.setCartId(cartId);
                break;
        }
        brandBlockModel.setType(brandType);
        brandBlockModel.setBrand(brand);
        brandBlockModel.setCreater(username);
        brandBlockModel.setModifier(username);

        brandBlockDao.insert(brandBlockModel);

        // 通知任务进行其他部分的处理
        // 如 feed 部分的屏蔽
        // MQ 不负责的部分，应该只包含上新部分
        sender.sendMessage(MqRoutingKey.CMS_TASK_BRANDBLOCKJOB, new HashMap<String, Object>() {{
            put("blocking", true);
            put("data", brandBlockModel);
        }});
    }

    public void unblock(String channelId, int cartId, int brandType, String brand) {
        switch (brandType) {
            case BRAND_TYPE_FEED:
                cartId = 1;
                break;
            case BRAND_TYPE_MASTER:
                cartId = 0;
                break;
            case BRAND_TYPE_PLATFORM:
                break;
            default:
                return;
        }

        CmsBtBrandBlockModel brandBlockModel = new CmsBtBrandBlockModel();
        brandBlockModel.setChannelId(channelId);
        brandBlockModel.setCartId(cartId);
        brandBlockModel.setType(brandType);
        brandBlockModel.setBrand(brand);

        brandBlockModel = brandBlockDao.selectOne(brandBlockModel);

        brandBlockDao.delete(brandBlockModel.getId());

        // 同上，只是相反
        Map<String, Object> mqParams = new HashMap<>();
        mqParams.put("blocking", false);
        mqParams.put("data", brandBlockModel);
        sender.sendMessage(MqRoutingKey.CMS_TASK_BRANDBLOCKJOB, mqParams);
    }

    public boolean isBlocked(String channelId, int cartId, String feedBrand, String masterBrand, String platformBrandId) {
        return !StringUtils.isEmpty(feedBrand) && isBlocked(channelId, cartId, BRAND_TYPE_FEED, feedBrand)
                || !StringUtils.isEmpty(masterBrand) && isBlocked(channelId, cartId, BRAND_TYPE_MASTER, masterBrand)
                || !StringUtils.isEmpty(platformBrandId) && isBlocked(channelId, cartId, BRAND_TYPE_PLATFORM, platformBrandId);

    }

    public boolean isBlocked(CmsBtBrandBlockModel brandBlockModel) {
        return isBlocked(brandBlockModel.getChannelId(), brandBlockModel.getCartId(), brandBlockModel.getType(), brandBlockModel.getBrand());
    }

    private boolean isBlocked(String channelId, int cartId, int brandType, String brand) {
        return brandBlockDao.selectCount(new HashMap<String, Object>(4, 1f) {{
            put("channelId", channelId);
            put("cartId", (brandType == BRAND_TYPE_FEED) ? 1 : (brandType == BRAND_TYPE_MASTER ? 0 : cartId));
            put("type", brandType);
            put("brand", brand);
        }}) > 0;
    }

    /**
     * 匹配类型，0、1、2 分别代表：feed、master、platform
     */
    int getBrandCount(String channelId, String cartId, int type) {
        Map<String, Object> map = new HashMap<>();
        map.put("channelId", channelId);
        map.put("cartId", cartId);
        map.put("type", type);
        return brandBlockDao.selectCount(map);
    }
}
