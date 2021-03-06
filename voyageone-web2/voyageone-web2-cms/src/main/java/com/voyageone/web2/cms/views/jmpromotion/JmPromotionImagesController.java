package com.voyageone.web2.cms.views.jmpromotion;

import com.voyageone.common.asserts.Assert;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.service.impl.cms.jumei.CmsBtJmPromotionService;
import com.voyageone.service.model.cms.mongo.jm.promotion.CmsBtJmPromotionImagesModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


/**
 * @author piao
 * @version 2.8.0
 * @since 2.8.0
 */
@RestController
@RequestMapping(value = CmsUrlConstants.JMPROMOTION.Images.ROOT, method = RequestMethod.POST)
public class JmPromotionImagesController extends CmsController {

    @Autowired
    private JmPromotionImagesService jmPromotionImagesService;
    @Autowired
    private CmsBtJmPromotionService btJmPromotionService;

    @RequestMapping(CmsUrlConstants.JMPROMOTION.Images.INIT)
    public AjaxResponse init(@RequestBody Map<String, String> requestMap) {

        int promotionId = Integer.parseInt(requestMap.get("promotionId"));
        Assert.notNull(promotionId).elseThrowDefaultWithTitle("promotionId");

        int jmPromotionId = Integer.parseInt(requestMap.get("jmPromotionId"));
        Assert.notNull(jmPromotionId).elseThrowDefaultWithTitle("jmPromotionId");

        return success(jmPromotionImagesService.getJmPromotionImage(promotionId, jmPromotionId));
    }

    /**
     * 存储聚美图片信息
     *
     * @param params promotionImages：图片模型
     *               promotionId： 活动Id
     *               jmPromotionId: 聚美活动Id
     * @return
     */
    @RequestMapping(CmsUrlConstants.JMPROMOTION.Images.SAVE)
    public AjaxResponse save(@RequestBody Wrapper params) {
        btJmPromotionService.setJmPromotionStepStatus(params.getJmPromotionId(),
                CmsBtJmPromotionService.JmPromotionStepNameEnum.PromotionImage, CmsBtJmPromotionService.JmPromotionStepStatusEnum.Error, getUser().getUserName());
        Assert.notNull(params.getPromotionImages()).elseThrowDefaultWithTitle("promotionImages");

        CmsBtJmPromotionImagesModel imageEntity = params.getPromotionImages();

        Assert.notNull(params.getPromotionId()).elseThrowDefaultWithTitle("promotionId");
        imageEntity.setPromotionId(params.getPromotionId());

        Assert.notNull(params.getJmPromotionId()).elseThrowDefaultWithTitle("jmPromotionId");
        imageEntity.setJmPromotionId(params.getJmPromotionId());

        Assert.notNull(params.getBrand()).elseThrowDefaultWithTitle("brand");
        imageEntity.setBrand(params.getBrand());

        String current = DateTimeUtil.getNowTimeStamp();
        imageEntity.setModified(current);

        jmPromotionImagesService.saveJmPromotionImages(imageEntity);

        if (params.getSaveType() != null && (params.getSaveType() == 1 || params.getSaveType() == 2)) {
            // 1：提交  2：发布任务
            btJmPromotionService.setJmPromotionStepStatus(params.getJmPromotionId(),
                    CmsBtJmPromotionService.JmPromotionStepNameEnum.PromotionImage, CmsBtJmPromotionService.JmPromotionStepStatusEnum.Success, getUser().getUserName());
        }
        return success(current);
    }

    /**
     * 页面上获取套图初始化页面
     * @param requestMap
     * @return
     */
    @RequestMapping(CmsUrlConstants.JMPROMOTION.Images.GET_IMAGE_FOR_SUIT)
    public AjaxResponse getImageForSuit(@RequestBody Map<String, String> requestMap) {

        String brand = requestMap.get("brand");
        Assert.notNull(brand).elseThrowDefaultWithTitle("brand");

        return success(jmPromotionImagesService.getImageForSuit(brand));
    }

    /**
     * 获取套用模板地址
     * @param model
     * @return
     */
    @RequestMapping(CmsUrlConstants.JMPROMOTION.Images.GET_IMAGE_TEMPLATE)
    public AjaxResponse getImageForSuit(@RequestBody CmsBtJmPromotionImagesModel model) {
        return success(jmPromotionImagesService.getJmImageTemplate(model));
    }

    /**
     * 用于接受上行参数
     */
    public static class Wrapper {
        CmsBtJmPromotionImagesModel promotionImages;

        private Integer promotionId;
        private Integer jmPromotionId;
        private Integer saveType;
        private String brand;

        public CmsBtJmPromotionImagesModel getPromotionImages() {
            return promotionImages;
        }

        public void setPromotionImages(CmsBtJmPromotionImagesModel promotionImages) {
            this.promotionImages = promotionImages;
        }

        public Integer getPromotionId() {
            return promotionId;
        }

        public void setPromotionId(Integer promotionId) {
            this.promotionId = promotionId;
        }

        public Integer getJmPromotionId() {
            return jmPromotionId;
        }

        public void setJmPromotionId(Integer jmPromotionId) {
            this.jmPromotionId = jmPromotionId;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public Integer getSaveType() {
            return saveType;
        }

        public void setSaveType(Integer saveType) {
            this.saveType = saveType;
        }
    }
}
