package com.voyageone.service.impl.cms.usa;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.common.configs.Enums.TypeConfigEnums;
import com.voyageone.common.configs.Types;
import com.voyageone.common.configs.beans.TypeBean;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.bean.cms.CmsBtTagBean;
import com.voyageone.service.bean.cms.search.product.CmsProductCodeListBean;
import com.voyageone.service.daoext.cms.CmsBtTagDaoExt;
import com.voyageone.service.fields.cms.CmsBtTagModelTagType;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.TagService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.product.search.CmsSearchInfoBean2;
import com.voyageone.service.impl.cms.search.product.CmsProductSearchQueryService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * USA CMS Tag management
 *
 * @Author rex.wu
 * @Create 2017-07-19 12:52
 */
@Service
public class UsaTagService extends BaseService {

    @Autowired
    private CmsBtTagDaoExt cmsBtTagDaoExt;

    @Autowired
    private TagService tagService;
//    @Autowired
//    private CmsAdvanceSearchService advanceSearchService;
    @Autowired
    private CmsProductSearchQueryService cmsProductSearchQueryService;
    @Autowired
    private ProductService productService;


    /**
     * 取得标签管理初始化数据（注意：高级检索画面(查询条件)使用时，查询的是最近90天的所有活动，包括已结束的）
     * @param lang
     * @return result
     */
    public Map<String, Object> getInitTagInfo(String channelId, Map<String, Object> params, String lang) {
        String orgFlg = (String) params.get("orgFlg");
        String selAllFlg = (String) params.get("selAllFlg");
        String selTagType = (String) params.get("selTagType");
        List<String> selCodeList = (List<String>) params.get("selCodeList");
        Map<String, Object> searchInfoMap = (Map<String, Object>) params.get("searchInfo");
        CmsSearchInfoBean2 searchInfo = JacksonUtil.json2Bean(JacksonUtil.bean2Json(searchInfoMap), CmsSearchInfoBean2.class);


        Map<String, Object> result = new HashMap<>();

        // 先查询 美国自由标签(type=6), 然后将其Convert to Tree
        List<CmsBtTagBean> tagsList = cmsBtTagDaoExt.selectListByType(channelId, CmsBtTagModelTagType.usa_free_tags);
        List<CmsBtTagBean> categoryList = convertToTree(tagsList);

        // 取得所有的标签类型
        result.put("tagTree", categoryList);

        List<TypeBean>  types = Types.getTypeList(TypeConfigEnums.MastType.tagType.getId(), lang);
        if (types != null) {
            //标签类型
            result.put("tagTypeList", types.stream().filter(w->w.getValue().equals(String.valueOf(CmsBtTagModelTagType.usa_free_tags))).collect(Collectors.toList()));
        }

        orgFlg = "2";
        if (Objects.equals(orgFlg, "2")) {
            // 高级检索，设置自由标签的场合，需要检索一遍所选择商品的自由标签设值，返回到前端
//            Integer isSelAll = (Integer) param.get("isSelAll");
            Integer isSelAll = 0;
            if (isSelAll == null) {
                isSelAll = 0;
            }
            List<String> codeList = null;
            if (Objects.equals(selAllFlg, Integer.valueOf(1))) {
                // TODO: 2017/7/19 rex.wu Solr是否有未分页方法
                // 从高级检索重新取得查询结果（根据session中保存的查询条件）
                CmsProductCodeListBean productCodeListBean = cmsProductSearchQueryService.getProductCodeList(searchInfo, channelId);
                codeList = productCodeListBean.getProductCodeList();
            } else {
                codeList = selCodeList;
            }
            if (codeList == null || codeList.isEmpty()) {
//                $warn("没有code条件 params=" + param.toString());
            } else {
                // 检索商品的自由标签设值
                JongoQuery queryObj = new JongoQuery();
                queryObj.setQuery("{'common.fields.code':{$in:#}}");
                queryObj.setParameters(codeList);
                queryObj.setProjectionExt("prodId", "common.fields.code", "usFreeTags");
                List<CmsBtProductModel> prodList = productService.getList(channelId, queryObj);
                if (prodList == null || prodList.isEmpty()) {
//                    $warn("根据条件查不到商品 params=" + param.toString());
                } else {
                    // 用于标识是否已勾选
                    Map<String, Boolean> orgChkStsMap = new HashMap<>();
                    // 用于标识是否半选（即不是所有商品都设置了该标签）
                    Map<String, Boolean> orgDispMap = new HashMap<>();

                    // TODO 此段先注释掉，即勾选子节点的话，再显示弹出画面时父节点也显示被勾选
//                    for (CmsBtProductModel prodObj : prodList) {
//                        List<String> tags = prodObj.getFreeTags();
//                        if (tags == null || tags.isEmpty()) {
//                            continue;
//                        }
//                        // 先过滤一遍父节点
//                        for (int i = 0; i < tags.size(); i ++) {
//                            String tagPath = tags.get(i);
//                            for (String tagPath2 : tags) {
//                                if (tagPath != null && tagPath2 != null && tagPath2.length() > tagPath.length() && tagPath2.startsWith(tagPath)) {
//                                    tags.set(i, null);
//                                }
//                            }
//                        }
//                        tags = tags.stream().filter(tagPath -> tagPath != null).collect(Collectors.toList());
//                        prodObj.setFreeTags(tags);
//                    }

                    for (CmsBtTagBean tagBean : tagsList) {
                        // 遍历商品列表，查看是否勾选(这里的tagsList是列表,不是树型结构)
                        int selCnt = 0;
                        for (CmsBtProductModel prodObj : prodList) {
                            List<String> tags = prodObj.getFreeTags();
                            if (tags == null || tags.isEmpty()) {
                                continue;
                            }
                            if (tags.indexOf(tagBean.getTagPath()) >= 0) {
                                // 有勾选
                                selCnt ++;
                            }
                        }
                        if (selCnt == prodList.size()) {
                            orgChkStsMap.put(tagBean.getTagPath(), true);
                        } else if (0 < selCnt && selCnt < prodList.size()) {
                            orgDispMap.put(tagBean.getTagPath(), true);
                        }
                    }
                    result.put("orgChkStsMap", orgChkStsMap);
                    result.put("orgDispMap", orgDispMap);
                }
            }
        }
        //返回数据类型
        return result;
    }

    /**
     * 将数据转换为树型结构
     *
     * @param valueList
     * @return List<CmsBtTagBean>
     */
    public List<CmsBtTagBean> convertToTree(List<CmsBtTagBean> valueList) {
        //循环取得标签并对其进行分类
        List<CmsBtTagBean> ret = new ArrayList<>();
        for (CmsBtTagBean each : valueList) {
            //取得一级标签
            if (each.getParentTagId() == 0) {
                ret.add(each);
            }
            for (CmsBtTagBean inner : valueList) {
                //取得子标签
                if (each.getId().intValue() == inner.getParentTagId().intValue()) {
                    if (each.getChildren() == null) {
                        //添加一个子标签
                        each.setChildren(new ArrayList<>());
                    }
                    each.getChildren().add(inner);
                    //取得子标签的名称
                    if (inner.getTagChildrenName().contains(">")) {
                        String PathName[] = inner.getTagChildrenName().split(">");
                        inner.setTagChildrenName(PathName[PathName.length - 1]);
                    }
                }
            }
            //判断是否子标签
            if (each.getChildren() == null) {
                //子标签
                each.setIsLeaf(true);
                each.setChildren(new ArrayList<>());
            } else {
                //非子标签
                each.setIsLeaf(false);
            }
        }
        //返回数据类型
        return ret;
    }

}
