package com.voyageone.web2.cms.rest;

import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.common.util.StringUtils;
import com.voyageone.web2.base.BaseAppComponent;
import com.voyageone.web2.sdk.api.VoApiRequest;

/**
 * Rest webservice Service 层提供基类
 * Created by chuanyu.liang on 15/6/26.
 * @author chuanyu.liang
 */
public abstract class BaseRestService extends BaseAppComponent {

    /**
     * buildProjection
     * @param request VoApiRequest
     * @param queryObject JomgoQuery
     */
    protected void buildProjection(VoApiRequest request, JomgoQuery queryObject) {
        if (StringUtils.isEmpty(request.getFields())) {
            return;
        }
        String fieldsTmp = request.getFields().replaceAll("[\\s]*;[\\s]*", " ; ");
        String[] fieldsTmpArr = fieldsTmp.split(" ; ");
        queryObject.setProjection(fieldsTmpArr);
    }

    /**
     * buildSort
     * @param request VoApiRequest
     * @param queryObject queryObject
     */
    protected void buildSort(VoApiRequest request, JomgoQuery queryObject) {
        if (StringUtils.isEmpty(request.getSorts())) {
            return;
        }
        String sortsTmp = request.getSorts().replaceAll("[\\s]*;[\\s]*", ", ");
        queryObject.setSort("{ " + sortsTmp + " }");
    }



}
