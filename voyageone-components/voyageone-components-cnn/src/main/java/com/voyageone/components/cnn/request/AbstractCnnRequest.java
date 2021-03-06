package com.voyageone.components.cnn.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.voyageone.common.util.GenericSuperclassUtils;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.cnn.response.AbstractCnnResponse;

/**
 * Created by morse on 2017/7/31.
 */
public abstract class AbstractCnnRequest<T extends AbstractCnnResponse> {

    @JsonIgnore
    public abstract String getUrl();

    public T getResponse(String json) {
        Class<T> messageBodyClass = GenericSuperclassUtils.getGenericActualTypeClass(this);
        return JacksonUtil.json2Bean(json, messageBodyClass);
    }

    public String toJsonStr() {
        int len = this.getClass().getDeclaredFields().length;
        if (len == 0) {
            return null; // 没有参数的请求
        }
        return JacksonUtil.bean2Json(this);
    }
}
