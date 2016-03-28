package com.voyageone.service.impl.cms;

import com.voyageone.service.dao.cms.mongo.CommSequenceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MongoSequenceService {

    /**
     *  Sequence Name
     */
    public enum CommSequenceName {
        CMS_BT_PRODUCT_PROD_ID("cms_bt_product_prodId"), CMS_BT_PRODUCT_GROUP_ID("cms_bt_product_groupId") ;

        // 成员变量
        private String name;

        // 构造方法
        private CommSequenceName(String name) {
            this.name = name;
        }

        // get set 方法
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @Autowired
    private CommSequenceDao commSequenceDao;

    /**
     * 取得 下一个 Sequence
     */
    public long getNextSequence(CommSequenceName nameEnum) {
        return commSequenceDao.getNextSequence(nameEnum.getName());
    }

}