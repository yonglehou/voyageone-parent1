package com.voyageone.batch.wms.mongoDao;

import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.batch.wms.modelbean.InventoryForCmsBean;
import com.voyageone.cms.service.model.CmsBtFeedInfoModel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by dell on 2015/11/27.
 */
@Repository
public class InventoryTmpDao extends BaseMongoDao {
    public InventoryTmpDao() {
        super.entityClass = getEntityClass();
        super.collectionName = "wms_bt_inventory_aggregate";
    }

    @Override
    public Class getEntityClass() {
        return CmsBtFeedInfoModel.class;
    }
}
