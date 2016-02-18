package com.voyageone.common.components.jumei;

import com.voyageone.common.components.jumei.Bean.JmCategoryBean;
import com.voyageone.common.components.jumei.Bean.JmImageFileBean;
import com.voyageone.common.components.jumei.Bean.NotSignString;
import com.voyageone.common.components.jumei.base.JmBase;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.JacksonUtil;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chuanyu.laing on 2016/1/25.
 */
@Service
public class JumeiImageFileService extends JmBase {

    private static List<JmCategoryBean> categoryListLevel4 = null;

    private static String IMAGWE_UPLOAD = "v1/img/upload";

    /**
     * 图片上传至聚美图片空间
     */
    public String imageFileUpload(ShopBean shopBean, JmImageFileBean fileBean) throws Exception {
        if (fileBean == null) {
            throw new Exception("fileBean not found!");
        }

        fileBean.check();

        Map<String, Object> params = new HashMap<>();
        params.put("imgName", fileBean.getImgName());
        params.put("dirName", fileBean.getDirName());
        params.put("needReplace", fileBean.getNeedReplace().toString());

        String imageStr = fileBean.getBase64Content();
        imageStr = URLEncoder.encode(imageStr, "utf-8");
        NotSignString imgData = new NotSignString(imageStr);
        params.put("imgData", imgData);

        String reqResult = reqJmApi(shopBean, IMAGWE_UPLOAD, params);
        Map<String, Object> resultMap = JacksonUtil.jsonToMap(reqResult);

        String result = null;
        if (resultMap != null && resultMap.containsKey("imgUrl")) {
            result = (String)resultMap.get("imgUrl");
        }

        return result;
    }

}