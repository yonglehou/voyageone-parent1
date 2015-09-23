package com.voyageone.cms.service.impl;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.cms.dao.FeedPropMappingDao;
import com.voyageone.cms.feed.Condition;
import com.voyageone.cms.feed.Operation;
import com.voyageone.cms.feed.OperationBean;
import com.voyageone.cms.modelbean.*;
import com.voyageone.cms.service.FeedPropMappingService;
import com.voyageone.cms.service.FeedPropMappingType;
import com.voyageone.common.configs.Enums.ChannelConfigEnums.Channel;
import com.voyageone.core.ajax.dt.DtRequest;
import com.voyageone.core.ajax.dt.DtResponse;
import com.voyageone.core.modelbean.UserSessionBean;
import com.voyageone.ims.enums.CmsFieldEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.voyageone.cms.CmsMsgConstants.FeedPropMappingMsg.*;
import static com.voyageone.core.MessageConstants.ComMsg.NOT_FOUND_CHANNEL;
import static com.voyageone.core.MessageConstants.ComMsg.UPDATE_BY_OTHER;

/**
 * 第三方品牌数据，和主数据，进行属性和值的匹配
 * <p>
 * Created by Jonas on 9/1/15.
 */
@Service
public class FeedPropMappingServiceImpl implements FeedPropMappingService {

    @Autowired
    private FeedPropMappingDao feedPropMappingDao;

    /**
     * 获取目录的完整目录名称
     *
     * @param categoryId 目录主键
     * @return 完整的目录名称
     */
    @Override
    public String getCategoryPath(int categoryId) {
        return feedPropMappingDao.selectCategoryPath(categoryId);
    }

    /**
     * 获取目录下所有的属性
     *
     * @param params     带有目录主键的 DataTable 请求参数
     * @param channel_id 当前渠道
     * @return 返回给 DataTable 的数据
     */
    @Override
    public DtResponse<List<FeedMappingProp>> getProps(DtRequest<Integer> params, String channel_id) {

        Integer categoryId = params.getParam();

        if (categoryId == null) throw new BusinessException(NO_PARAM);

        Channel channel = Channel.valueOfId(channel_id);

        if (channel == null) throw new BusinessException(NOT_FOUND_CHANNEL);

        String byRequired = params.getColumns().get(3).getSearch().getValue();

        String byIgnored = params.getColumns().get(4).getSearch().getValue();

        // 抽取数据
        List<FeedMappingProp> masterProperties = feedPropMappingDao.selectProps(params.getParam(), channel.getId(),
                params.getStart(), params.getLength(), byIgnored, byRequired);

        // 抽取不分页的总数量
        int total = feedPropMappingDao.selectPropsCount(params.getParam(), channel.getId(), byIgnored, byRequired);

        // 创建返回结果
        DtResponse<List<FeedMappingProp>> response = new DtResponse<>();
        response.setData(masterProperties);
        response.setRecordsTotal(total);
        response.setRecordsFiltered(total);
        response.setDraw(params.getDraw());

        return response;
    }

    /**
     * 获取一个主数据属性下的所有值映射
     *
     * @param prop_id    主数据属性
     * @param channel_id 当前渠道
     * @return 所有映射
     */
    @Override
    public List<FeedPropMapping> getPropMappings(int prop_id, String channel_id) {
        return feedPropMappingDao.selectMappings(prop_id, channel_id);
    }

    /**
     * 获取可绑定的 cms 属性
     *
     * @return CmsModelEnum 的集合
     */
    @Override
    public List<CmsFieldEnum.CmsModelEnum> getCmsProps() {
        return Arrays.asList(CmsFieldEnum.CmsModelEnum.values());
    }

    /**
     * 获取第三方品牌的属性
     *
     * @param channel_id 渠道
     * @return FeedConfig 集合
     */
    @Override
    public List<FeedConfig> getFeedProps(String channel_id) {
        return feedPropMappingDao.selectFeedProps(channel_id);
    }

    /**
     * 获取第三方品牌属性的值
     *
     * @param attr_name  属性名
     * @param channel_id 渠道
     * @return FeedValue 集合
     */
    @Override
    public List<FeedValue> getFeedValues(String attr_name, String channel_id) {
        return feedPropMappingDao.selectFeedValues(attr_name, channel_id);
    }

    /**
     * 可用的品牌属性比较操作
     *
     * @return Operation 集合
     */
    @Override
    public List<OperationBean> getConditionOperations() {

        List<OperationBean> operationBeans = new ArrayList<>();

        for (Operation operation : Operation.values()) operationBeans.add(operation.toBean());

        return operationBeans;
    }

    /**
     * 获取主数据的可选项
     *
     * @param prop_id 主数据属性
     * @return PropertyOption 集合
     */
    @Override
    public List<PropertyOption> getPropOptions(long prop_id) {
        return feedPropMappingDao.selectPropOptions(prop_id);
    }

    /**
     * 更新主数据属性的可忽略属性
     *
     * @param prop 主数据属性
     * @param user 当前用户
     * @return 影响的行数
     */
    @Override
    public int updateIgnore(FeedMappingProp prop, UserSessionBean user) {

        String channel_id = user.getSelChannel();
        String userName = user.getUserName();

        // 检查是插入还是更新
        Boolean isIgnore = feedPropMappingDao.selectIgnoreValue(prop, channel_id);

        int count = 0;

        if (isIgnore == null) {
            // 查询不到则为插入
            count = feedPropMappingDao.updateIgnoreValue(prop, channel_id, userName);
        } else if (isIgnore != prop.is_ignore()) {
            // 如果查询的值和提交的不同，再进行更新
            count = feedPropMappingDao.insertIgnoreValue(prop, channel_id, userName);
        }

        if (count == 1)
            // 检查目录是否完全匹配完成
            completeCategory(prop.getCategory_id(), user.getSelChannel(), user);

        // 如果不需要更新则直接返回成功
        return count;
    }

    /**
     * 新增一个第三方品牌属性的值映射
     *
     * @param mapping FeedPropMapping 映射的实例
     * @param user    当前用户
     * @return 更新的实例
     */
    @Override
    public FeedPropMapping addMapping(FeedPropMapping mapping, UserSessionBean user) {

        mapping.setChannel_id(user.getSelChannel());

        // 先检查提交内容
        // 检查失败会直接抛 BusinessException
        checkMapping(mapping);

        // 补全属性
        mapping.setModifier(user.getUserName());
        mapping.setCreater(user.getUserName());

        if (feedPropMappingDao.insertMapping(mapping) < 1)
            throw new BusinessException(UPDATE_BY_OTHER);

        // 检查目录是否完全匹配完成
        completeCategory(mapping.getMain_category_id(), mapping.getChannel_id(), user);
        
        return feedPropMappingDao.selectMappingByFinger(mapping);
    }

    /**
     * 修改一个第三方品牌属性的值映射
     *
     * @param mapping FeedPropMapping 映射的实例
     * @param user    当前用户
     * @return 更新的实例
     */
    @Override
    public FeedPropMapping updateMapping(FeedPropMapping mapping, UserSessionBean user) {

        checkMapping(mapping);

        mapping.setModifier(user.getUserName());

        if (feedPropMappingDao.updateMapping(mapping) < 1)
            throw new BusinessException(UPDATE_BY_OTHER);

        // 检查目录是否完全匹配完成
        completeCategory(mapping.getMain_category_id(), mapping.getChannel_id(), user);
        
        return feedPropMappingDao.selectMappingById(mapping.getId());
    }

    /**
     * 删除一个第三方品牌属性的值映射
     *
     * @param mapping FeedPropMapping 映射的实例
     * @return 影响行数
     */
    @Override
    public int deleteMapping(FeedPropMapping mapping, UserSessionBean user) {
        if (mapping == null)
            throw new BusinessException(NO_PARAM);

        int count = feedPropMappingDao.deleteMapping(mapping.getId());

        if (count == 1)
            // 检查目录是否完全匹配完成
            completeCategory(mapping.getMain_category_id(), mapping.getChannel_id(), user);

        return count;
    }

    /**
     * 查找这个属性的默认设定值。
     *
     * @param prop       主数据属性
     * @param channel_id 渠道
     * @return 属性的默认设定值
     */
    @Override
    public String getDefaultValue(FeedMappingProp prop, String channel_id) {
        if (prop == null)
            throw new BusinessException(NO_PARAM);

        return feedPropMappingDao.selectDefaultValue(prop, channel_id);
    }

    private void completeCategory(int category_id, String channel_id, UserSessionBean user) {
        // 先获取统计数量
        CategoryPropCount propCount = feedPropMappingDao.selectCountsByCategory(channel_id, category_id);

        // 获取目录记录
        ImsCategoryExtend categoryExtend = feedPropMappingDao.selectCategoryExtend(category_id);
        
        // 如果两数量不相同，说明有属性还没有忽略或设置 mapping，并且没有默认值
        int isSetAttr = propCount.getProps() != propCount.getHasValue() ? 0 : 1;
        
        // 如果取出的 extend 没有值，则插入，否则更新
        if (categoryExtend != null) {
            categoryExtend.setIs_set_attr(isSetAttr);
            categoryExtend.setModifier(user.getUserName());
            feedPropMappingDao.isSetAttr(categoryExtend);
            return;
        }

        categoryExtend = new ImsCategoryExtend();
        categoryExtend.setIs_set_attr(isSetAttr);
        categoryExtend.setChannel_id(channel_id);
        categoryExtend.setMain_category_id(category_id);
        categoryExtend.setCreater(user.getUserName());
        categoryExtend.setModifier(user.getUserName());
        feedPropMappingDao.insertCategoryExtend(categoryExtend);
    }

    private void checkMapping(FeedPropMapping mapping) {
        if (mapping == null)
            throw new BusinessException(NO_PARAM);

        Channel channel = Channel.valueOfId(mapping.getChannel_id());

        if (channel == null)
            throw new BusinessException(NOT_FOUND_CHANNEL);

        if (mapping.getEType() == null)
            throw new BusinessException(ERR_MAPPING_TYPE);

        if (mapping.getProp_id() < 0)
            throw new BusinessException(NO_PARAM);

        String condition = mapping.getConditions();

        // 条件是可以为空的，但是如果不为空，则格式不能错
        if (!Condition.valid(condition))
            throw new BusinessException(ERR_CONDITION_FORMAT);

        // 如果映射的值来源是 feed，则要求不能使用 condition
        if (mapping.getEType().equals(FeedPropMappingType.FEED) && !StringUtils.isEmpty(condition))
            throw new BusinessException(CONDITION_FOR_FEED);

        if (StringUtils.isEmpty(mapping.getValue()))
            throw new BusinessException(NO_MAPPING_VALUE);
    }
}
