package com.voyageone.web2.cms.views.tools.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.factory.SchemaJsonReader;
import com.voyageone.common.masterdate.schema.field.*;
import com.voyageone.common.masterdate.schema.value.Value;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.dao.cms.CmsMtFeedCustomPropDao;
import com.voyageone.service.impl.cms.CommonSchemaService;
import com.voyageone.service.impl.cms.PlatformSchemaService;
import com.voyageone.service.impl.cms.tools.CmsMtPlatformCommonSchemaService;
import com.voyageone.service.impl.cms.tools.PlatformMappingService;
import com.voyageone.service.model.cms.CmsMtFeedCustomPropModel;
import com.voyageone.service.model.cms.mongo.CmsBtPlatformMappingModel;
import com.voyageone.service.model.cms.mongo.CmsMtCommonSchemaModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCommonSchemaModel;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.bean.tools.product.PlatformMappingGetBean;
import com.voyageone.web2.cms.bean.tools.product.PlatformMappingSaveBean;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * Created by jonas on 8/15/16.
 *
 * @author jonas
 * @version 2.4.0
 * @since 2.4.0
 */
@Service
class PlatformMappingViewService extends BaseAppService {

    private final PlatformMappingService platformMappingService;

    private final PlatformSchemaService platformSchemaService;

    private final CmsMtPlatformCommonSchemaService platformCommonSchemaService;

    private final CommonSchemaService commonSchemaService;

    private final CmsMtFeedCustomPropDao feedCustomPropDao;

    @Autowired
    public PlatformMappingViewService(PlatformMappingService platformMappingService,
                                      PlatformSchemaService platformSchemaService,
                                      CmsMtPlatformCommonSchemaService platformCommonSchemaService,
                                      CommonSchemaService commonSchemaService,
                                      CmsMtFeedCustomPropDao feedCustomPropDao) {
        this.platformMappingService = platformMappingService;
        this.platformSchemaService = platformSchemaService;
        this.platformCommonSchemaService = platformCommonSchemaService;
        this.commonSchemaService = commonSchemaService;
        this.feedCustomPropDao = feedCustomPropDao;
    }

    public Map<String, Object> page(Integer cartId, Integer categoryType, String categoryPath, int page, int size, UserSessionBean userSessionBean) {

        ChannelConfigEnums.Channel channel = userSessionBean.getSelChannel();

        Map<String, Object> result = new HashMap<>();

        if (categoryType != null
                && categoryType != PlatformMappingService.CATEGORY_TYPE_COMMON
                && categoryType != PlatformMappingService.CATEGORY_TYPE_SPECIFIC)
            categoryType = null;

        if (categoryType == null || categoryType != PlatformMappingService.CATEGORY_TYPE_SPECIFIC)
            categoryPath = null;

        List<CmsBtPlatformMappingModel> list = platformMappingService.getPage(channel, categoryType, cartId, categoryPath, page, size);

        long total = platformMappingService.getCount(channel, categoryType, cartId, categoryPath);

        result.put("list", list);

        result.put("total", total);

        return result;
    }

    public PlatformMappingGetBean get(CmsBtPlatformMappingModel platformMappingModel, String channelId, String lang) {

        CmsBtPlatformMappingModel _platformMappingModel = platformMappingService.get(platformMappingModel, channelId);

        Map<String, CmsBtPlatformMappingModel.FieldMapping> fieldMappingMap = null;

        if (_platformMappingModel != null) {
            platformMappingModel = _platformMappingModel;
            fieldMappingMap = platformMappingModel.getMappings();
        }

        PlatformMappingGetBean platformMappingGetBean = new PlatformMappingGetBean();

        String categoryPath = platformMappingModel.getCategoryPath();
        int cartId = platformMappingModel.getCartId();
        int type = platformMappingModel.getCategoryType();

        // 同步基础数据
        platformMappingGetBean.setCartId(cartId);
        platformMappingGetBean.setCategoryPath(categoryPath);
        platformMappingGetBean.setCategoryType(type);
        platformMappingGetBean.setModified(platformMappingModel.getModified());

        // 构造包含关联关系的 schema 数据

        PlatformMappingGetBean.Schema schema = new PlatformMappingGetBean.Schema();

        List<Field> item = null, product = null;

        if (type == 1) {
            CmsMtPlatformCommonSchemaModel commonSchemaModel = platformCommonSchemaService.get(cartId);

            List<Map<String, Object>> itemFieldMapList = commonSchemaModel.getPropsItem();
            if (itemFieldMapList != null && !itemFieldMapList.isEmpty())
                item = SchemaJsonReader.readJsonForList(itemFieldMapList);

            List<Map<String, Object>> productFieldMapList = commonSchemaModel.getPropsProduct();
            if (productFieldMapList != null && !productFieldMapList.isEmpty())
                product = SchemaJsonReader.readJsonForList(productFieldMapList);
        } else {
            Map<String, List<Field>> fieldListMap = platformSchemaService.getFieldsByCategoryPath(categoryPath, channelId, cartId, lang);
            item = fieldListMap.get(PlatformSchemaService.KEY_ITEM);
            product = fieldListMap.get(PlatformSchemaService.KEY_PRODUCT);
        }

        if (item != null && !item.isEmpty())
            fillFields(item, fieldMappingMap);

        if (product != null && !product.isEmpty())
            fillFields(product, fieldMappingMap);

        schema.setItem(item);

        schema.setProduct(product);

        platformMappingGetBean.setSchema(schema);

        return platformMappingGetBean;
    }

    public boolean save(PlatformMappingSaveBean platformMappingSaveBean, UserSessionBean user) {

        String username = user.getUserName();

        Integer typeObject = platformMappingSaveBean.getCategoryType();

        String categoryPath = platformMappingSaveBean.getCategoryPath();

        int type = 0;

        if (typeObject != null)
            type = typeObject;

        if (!StringUtils.isEmpty(categoryPath))
            type = PlatformMappingService.CATEGORY_TYPE_SPECIFIC;
        else if (type != PlatformMappingService.CATEGORY_TYPE_SPECIFIC && type != PlatformMappingService.CATEGORY_TYPE_COMMON)
            throw new BusinessException("类目类型错误");

        typeObject = type;

        // 创建模型
        // 先用于查询
        CmsBtPlatformMappingModel platformMappingModel = new CmsBtPlatformMappingModel();

        platformMappingModel.setCartId(platformMappingSaveBean.getCartId());
        platformMappingModel.setCategoryType(typeObject);
        platformMappingModel.setCategoryPath(categoryPath);
        platformMappingModel.setChannelId(user.getSelChannelId());

        CmsBtPlatformMappingModel _platformMappingModel = platformMappingService.get(platformMappingModel,
                user.getSelChannelId());

        // 如果已有数据, 使用数据库数据覆盖查询模型
        // 供后续使用
        // 如果没有数据, 需要为查询模型补全数据
        // 后续插入

        if (_platformMappingModel != null)
            platformMappingModel = _platformMappingModel;
        else
            platformMappingModel.setCreater(username);

        platformMappingModel.setModifier(username);
        platformMappingModel.setModified(DateTimeUtil.getNowTimeStamp());

        // 依次循环 product 和 item 两组 schema 数据
        // 填充到模型中

        PlatformMappingSaveBean.Schema schema = platformMappingSaveBean.getSchema();

        Map<String, CmsBtPlatformMappingModel.FieldMapping> mappingMap = platformMappingModel.getMappings();

        if (mappingMap == null)
            mappingMap = new HashMap<>();

        List<Map<String, Object>> weakItem = schema.getItem();

        if (weakItem != null && !weakItem.isEmpty()) {
            List<Field> item = getStrongSchema(weakItem);
            fillMapping(mappingMap, item);
        }

        List<Map<String, Object>> weakProduct = schema.getProduct();

        if (weakProduct != null && !weakProduct.isEmpty()) {
            List<Field> product = getStrongSchema(schema.getProduct());
            fillMapping(mappingMap, product);
        }

        platformMappingModel.setMappings(mappingMap);

        return platformMappingService.saveMap(platformMappingModel);
    }

    public List<Map<String, Object>> getCommonSchema() {

        CmsMtCommonSchemaModel comSchemaModel = commonSchemaService.getComSchemaModel();

        List<Field> fields = comSchemaModel.getFields();

        if (fields == null || fields.isEmpty())
            return new ArrayList<>(0);

        return fields.stream().map(f -> {
            Map<String, Object> jsObject = new HashMap<>();
            jsObject.put("value", f.getId());
            jsObject.put("label", f.getName());
            return jsObject;
        }).collect(toList());
    }

    public List<Map<String, Object>> getFeedCustomProps(String channelId) {

        CmsMtFeedCustomPropModel feedCustomPropModel = new CmsMtFeedCustomPropModel();

        feedCustomPropModel.setChannelId(channelId);

        List<CmsMtFeedCustomPropModel> feedCustomPropModelList = feedCustomPropDao.selectList(feedCustomPropModel);

        if (feedCustomPropModelList == null || feedCustomPropModelList.isEmpty())
            return new ArrayList<>(0);

        return feedCustomPropModelList.stream().map(f -> {
            Map<String, Object> jsObject = new HashMap<>();
            jsObject.put("value", f.getFeedPropOriginal());
            jsObject.put("label", f.getFeedPropOriginal());
            return jsObject;
        }).collect(toList());
    }

    private void fillMapping(Map<String, CmsBtPlatformMappingModel.FieldMapping> mappingMap, List<Field> fieldList) {

        // 遍历传回来的每一个 Field
        // 根据类型, 获取内容, 并保存到 Map
        // 对于 Check 类的, 只有固定值
        // Input 的, 只有匹配关系

        for (Field field : fieldList) {

            String fieldId = field.getId();

            CmsBtPlatformMappingModel.FieldMapping mapping = mappingMap.get(fieldId);

            if (mapping == null) {
                mapping = new CmsBtPlatformMappingModel.FieldMapping();
                mapping.setFieldId(fieldId);
            }

            switch (field.getType()) {

                case SINGLECHECK:
                    SingleCheckField singleCheckField = (SingleCheckField) field;
                    Value valueObject = singleCheckField.getValue();
                    String value = valueObject.getValue();
                    if (StringUtils.isEmpty(value))
                        continue;
                    mapping.setValue(value);
                    break;
                case MULTICHECK:
                    MultiCheckField multiCheckField = (MultiCheckField) field;
                    List<Value> valueObjectList = multiCheckField.getValues();
                    List<String> valueList = valueObjectList.stream().map(Value::getValue).collect(toList());
                    if (valueList.isEmpty())
                        continue;
                    mapping.setValue(valueList);
                    break;
                case COMPLEX:
                    ComplexField complexField = (ComplexField) field;
                    List<Field> children = complexField.getFields();
                    Map<String, CmsBtPlatformMappingModel.FieldMapping> childrenMapping = new HashMap<>();
                    fillMapping(childrenMapping, children);
                    mapping.setChildren(childrenMapping);
                    break;
                case INPUT:
                    InputField inputField = (InputField) field;
                    String expressionListJson = inputField.getValue();
                    if (StringUtils.isEmpty(expressionListJson))
                        continue;
                    setExpressionList(mapping, expressionListJson);
                    break;
            }

            mappingMap.put(fieldId, mapping);
        }
    }

    private void setExpressionList(CmsBtPlatformMappingModel.FieldMapping mapping, String json) {
        if (StringUtils.isEmpty(json))
            return;
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<CmsBtPlatformMappingModel.FieldMappingExpression> expressionList = mapper.readValue(json,
                    mapper.getTypeFactory().constructCollectionType(ArrayList.class, CmsBtPlatformMappingModel.FieldMappingExpression.class));
            mapping.setExpressions(expressionList);
            mapping.setValue(null);
        } catch (IOException e) {
            throw new BusinessException("读取 ExpressionList 出错", e);
        }
    }

    private List<Field> getStrongSchema(List<Map<String, Object>> weakSchema) {
        return SchemaJsonReader.readJsonForList(weakSchema);
    }

    private void fillFields(List<Field> fieldList, Map<String, CmsBtPlatformMappingModel.FieldMapping> fieldMappingMap) {

        if (fieldMappingMap == null || fieldMappingMap.isEmpty())
            return;

        fieldList.stream()
                .filter(f -> {
                    // 对这三种类型进行过滤
                    // 这三种不需要去前端显示了, 留下看家
                    switch (f.getType()) {
                        case MULTIINPUT:
                        case MULTICOMPLEX:
                        case LABEL:
                            return false;
                    }
                    return true;
                })
                .map(field -> {

                    FieldTypeEnum fieldType = field.getType();

                    CmsBtPlatformMappingModel.FieldMapping mapping = fieldMappingMap.get(field.getId());

                    if (mapping == null)
                        return field;

                    switch (fieldType) {
                        case INPUT:
                            InputField inputField = (InputField) field;
                            List<CmsBtPlatformMappingModel.FieldMappingExpression> expressionList = mapping.getExpressions();
                            if (expressionList == null || expressionList.isEmpty())
                                break;
                            inputField.setValue(JacksonUtil.bean2Json(expressionList));
                            break;
                        case SINGLECHECK:
                            SingleCheckField singleCheckField = (SingleCheckField) field;
                            singleCheckField.setValue(new Value() {{
                                setValue(String.valueOf(mapping.getValue()));
                            }});
                            break;
                        case MULTICHECK:
                            MultiCheckField multiCheckField = (MultiCheckField) field;
                            @SuppressWarnings("unchecked")
                            List<String> valueList = (List<String>) mapping.getValue();
                            List<Value> valueObjectList = valueList.stream().map(v -> new Value() {{
                                setValue(v);
                            }}).collect(toList());
                            multiCheckField.setValues(valueObjectList);
                            break;
                        case COMPLEX:
                            ComplexField complexField = (ComplexField) field;
                            List<Field> children = complexField.getFields();
                            Map<String, CmsBtPlatformMappingModel.FieldMapping> childrenMapping = mapping.getChildren();
                            if (childrenMapping == null || childrenMapping.isEmpty())
                                break;
                            fillFields(children, childrenMapping);
                            break;
                    }

                    return field;
                })
                .collect(toList());
    }
}
