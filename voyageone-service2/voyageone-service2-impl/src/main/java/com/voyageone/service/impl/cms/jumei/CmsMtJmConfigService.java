package com.voyageone.service.impl.cms.jumei;
//import com.voyageone.service.dao.cms.CmsMtJmConfigDao
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.service.dao.cms.mongo.CmsMtJmConfigDao;
import com.voyageone.service.model.cms.mongo.jm.promotion.CmsMtJmConfigModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

/**
 * Created by dell on 2016/3/18.
 */
@Service
public class CmsMtJmConfigService {

    @Autowired
    private CmsMtJmConfigDao cmsMtJmConfigDao;

    public CmsMtJmConfigModel getCmsMtJmConfigById(JmCofigTypeEnum jmCofigTypeEnum){
        JongoQuery queryObject = new JongoQuery();
        Criteria criteria = Criteria.where("type").is(jmCofigTypeEnum.getValue());
        queryObject.setQuery(criteria);
        return cmsMtJmConfigDao.selectOneWithQuery(queryObject);
    }

    public static enum JmCofigTypeEnum {

        displayPlatform(1),    // 展示平台
        preDisplayChannel(2),  // 预展示频道
        sessionCategory(3),    //关联品类
        sessionType(4),        //专场类型
        promotionScene(5),     // 活动场景
        promotionType(6),     // 活动类型
        mainChannel(7),       // 主频道
        directmailType(8)     // 直邮信息
        ;
        private Integer type;

        JmCofigTypeEnum(Integer type){
            this.type = type;
        }

        public int getValue()
        {
            return  type;
        }

    }
}

