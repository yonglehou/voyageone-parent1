/**
 * (c) Copyright Voyageone Corp 2016
 */

package com.voyageone.service.impl.cms.promotion;

import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.common.configs.Channels;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.service.bean.cms.CmsBtPromotionBean;
import com.voyageone.service.bean.cms.CmsTagInfoBean;
import com.voyageone.service.dao.cms.CmsBtPromotionDao;
import com.voyageone.service.dao.cms.CmsBtPromotionLogDao;
import com.voyageone.service.dao.cms.CmsBtTagDao;
import com.voyageone.service.daoext.cms.CmsBtPromotionDaoExt;
import com.voyageone.service.daoext.cms.CmsBtTagDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.TagService;
import com.voyageone.service.model.cms.CmsBtPromotionLogModel;
import com.voyageone.service.model.cms.CmsBtPromotionModel;
import com.voyageone.service.model.cms.CmsBtTagModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author aooer 16/01/14
 * @version 2.0.0
 */
@Service
public class PromotionService extends BaseService {

    @Autowired
    private CmsBtPromotionDaoExt cmsBtPromotionDaoExt;

    @Autowired
    private CmsBtPromotionDao cmsBtPromotionDao;

    @Autowired
    private CmsBtTagDaoExt cmsBtTagDaoExt;

    @Autowired
    private TagService tagService;

    @Autowired
    private CmsBtTagDao cmsBtTagDao;

    @Autowired
    private CmsBtPromotionLogDao cmsBtPromotionLogDao;

    /**
     * 根据PromotionId查询
     *
     * @param promotionId int
     * @return CmsBtPromotionModel
     */
    public CmsBtPromotionModel getByPromotionId(int promotionId) {
        Map<String, Object> params = new HashMap<>();
        params.put("promotionId", promotionId);
        return cmsBtPromotionDaoExt.selectById(params);
    }

    /**
     * 根据PromotionId查询
     *
     * @param promotionId int
     * @return CmsBtPromotionModel
     */
    public CmsBtPromotionModel getByPromotionIdOrgChannelId(int promotionId, String orgChannelId) {
        Map<String, Object> params = new HashMap<>();
        params.put("promotionId", promotionId);
        params.put("orgChannelId",orgChannelId);
        return cmsBtPromotionDaoExt.selectById(params);
    }

    /**
     * 根据条件查询
     *
     * @param params Map
     * @return List<CmsBtPromotionModel>
     */
    public List<CmsBtPromotionBean> getByCondition(Map<String, Object> params) {
        return cmsBtPromotionDaoExt.selectByCondition(params);
    }

    /**
     * 根据channelId获取promotion列表
     * @param channelId
     * @return
     */
    public List<CmsBtPromotionBean> getPromotionsByChannelId(String channelId, Map<String, Object> params) {
//        Map<String, Object> params = new HashMap<>();
        params = params == null ? new HashMap<>() : params;
        if(Channels.isUsJoi(channelId)){
            params.put("orgChannelId", channelId);
            params.put("channelId", ChannelConfigEnums.Channel.VOYAGEONE.getId());
        } else {
            params.put("channelId", channelId);
        }
        return this.getByCondition(params);
    }

    /**
     * getPromotionLogMap
     */
    public Map<String, Object> getPromotionLogMap(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        List<CmsBtPromotionLogModel> promotionList = cmsBtPromotionLogDao.selectPromotionLog(params);
        int count = cmsBtPromotionLogDao.selectPromotionLogCnt(params);
        result.put("list", promotionList);
        result.put("total", count);
        return result;
    }

    /**
     * 添加或者修改
     */
    @VOTransactional
    public int saveOrUpdate(CmsBtPromotionBean cmsBtPromotionBean) {
        int result;
        if (cmsBtPromotionBean.getId() != 0) {
            result = cmsBtPromotionDaoExt.update(cmsBtPromotionBean);
            cmsBtPromotionBean.getTagList().forEach(cmsBtTagModel -> {
                cmsBtTagModel.setModifier(cmsBtPromotionBean.getModifier());
                if (cmsBtTagDao.update(cmsBtTagModel) == 0) {
                    cmsBtTagModel.setChannelId(cmsBtPromotionBean.getChannelId());
                    cmsBtTagModel.setParentTagId(cmsBtPromotionBean.getRefTagId());
                    cmsBtTagModel.setTagType(2);
                    cmsBtTagModel.setTagStatus(0);
                    cmsBtTagModel.setTagPathName(String.format("-%s-%s-", cmsBtPromotionBean.getPromotionName(), cmsBtTagModel.getTagName()));
                    cmsBtTagModel.setTagPath("");
                    cmsBtTagModel.setCreater(cmsBtPromotionBean.getModifier());
//                    cmsBtTagModel.setModifier(cmsBtPromotionBean.getModifier());
                    cmsBtTagDao.insert(cmsBtTagModel);
                    cmsBtTagModel.setTagPath(String.format("-%s-%s-", cmsBtTagModel.getParentTagId(), cmsBtTagModel.getId()));
                    cmsBtTagDao.update(cmsBtTagModel);
                }
            });
        } else {
            result = cmsBtPromotionDaoExt.insert(insertTagsAndGetNewModel(cmsBtPromotionBean));
        }
        return result;
    }

    /**
     * insertTagsAndGetNewModel
     */
    private CmsBtPromotionBean insertTagsAndGetNewModel(CmsBtPromotionBean cmsBtPromotionBean) {
        CmsTagInfoBean requestModel = new CmsTagInfoBean();
        requestModel.setChannelId(cmsBtPromotionBean.getChannelId());
        requestModel.setTagName(cmsBtPromotionBean.getPromotionName());
        requestModel.setTagType(2);
        requestModel.setTagStatus(0);
        requestModel.setParentTagId(0);
        requestModel.setSortOrder(0);
        requestModel.setModifier(cmsBtPromotionBean.getModifier());
        //Tag追加
        int refTagId = tagService.addTag(requestModel);
        cmsBtPromotionBean.setRefTagId(refTagId);

        // 子TAG追加
        cmsBtPromotionBean.getTagList().forEach(cmsBtTagModel -> {
            cmsBtTagModel.setChannelId(cmsBtPromotionBean.getChannelId());
            cmsBtTagModel.setParentTagId(refTagId);
            cmsBtTagModel.setTagType(2);
            cmsBtTagModel.setTagStatus(0);
            cmsBtTagModel.setTagPathName(String.format("-%s-%s-", cmsBtPromotionBean.getPromotionName(), cmsBtTagModel.getTagName()));
            cmsBtTagModel.setTagPath("");
            cmsBtTagModel.setCreater(cmsBtPromotionBean.getCreater());
            cmsBtTagModel.setModifier(cmsBtPromotionBean.getCreater());
            cmsBtTagDao.insert(cmsBtTagModel);
            cmsBtTagModel.setTagPath(String.format("-%s-%s-", refTagId, cmsBtTagModel.getId()));
            cmsBtTagDao.update(cmsBtTagModel);
        });
        return cmsBtPromotionBean;
    }

    /**
     * 删除
     */
    @VOTransactional
    public int delete(CmsBtPromotionModel cmsBtPromotionModel) {
        Map<String, Object> param = new HashMap<>();
        param.put("promotionId", cmsBtPromotionModel.getId());
        param.put("modifier", cmsBtPromotionModel.getModifier());

        // 删除对应的tag
        CmsBtTagModel cmsBtTagModel = new CmsBtTagModel();
        cmsBtTagModel.setParentTagId(cmsBtPromotionModel.getRefTagId());
        cmsBtTagModel.setId(cmsBtPromotionModel.getRefTagId());
        cmsBtTagDaoExt.deleteCmsBtTagByParentTagId(cmsBtTagModel);
        cmsBtTagDaoExt.deleteCmsBtTagByTagId(cmsBtTagModel);

        return cmsBtPromotionDaoExt.deleteById(param);
    }

    public Map<String,String>  getPromotionIDByCartId(String promotionId) {
        return cmsBtPromotionDaoExt.selectPromotionIDByCartId(promotionId);
    }

}
