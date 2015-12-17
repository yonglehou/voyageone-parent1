package com.voyageone.cms.service;

import com.voyageone.base.dao.mongodb.model.CmsMtCateforySchemaWithValueModel;
import com.voyageone.base.dao.mongodb.model.CmsMtCategorySchemaModel;
import com.voyageone.cms.service.dao.mongodb.CmsMtCategorySchemaDao;
import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.field.*;
import com.voyageone.common.masterdate.schema.value.ComplexValue;
import com.voyageone.common.masterdate.schema.value.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by lewis on 15-12-15.
 */
@Service
public class CmsMasterBeanConvertService {

    @Autowired
    private CmsMtCategorySchemaDao cmsMtCategorySchemaDao;

    @Autowired
    private CmsProductService cmsProductService;

    public CmsMtCateforySchemaWithValueModel getViewModels(String channelId,int prodId){

        CmsBtProductModel valueModel = cmsProductService.getProductById(channelId,prodId);

        CmsMtCategorySchemaModel schemaModel = null;

        List<Field> schemaFields = null;

        if (valueModel != null){
            schemaModel = cmsMtCategorySchemaDao.getMasterSchemaModelByCatId(valueModel.getCatId());
        }

        if (schemaModel != null){
            schemaFields = schemaModel.getFields();
        }

        Map valueFields =  valueModel.getFields();

        this.setFieldsValue(schemaFields,valueFields);

        CmsMtCateforySchemaWithValueModel schemaWithValueModel = new CmsMtCateforySchemaWithValueModel();

        schemaWithValueModel.setFields(schemaFields);
        schemaWithValueModel.setChannelId(channelId);
        schemaWithValueModel.setProductId(prodId);
        schemaWithValueModel.setCatId(schemaModel.getCatId());
        schemaWithValueModel.setCatFullPath(schemaModel.getCatFullPath());

        return schemaWithValueModel;
    }


    private void setFieldsValue(List<Field> schemaFields, Map<String, Object> valueFields){

        for (Field schemaField:schemaFields){
            FieldTypeEnum fieldType = schemaField.getType();
            switch (fieldType) {
                case INPUT: {
                    InputField inputField = (InputField) schemaField;
                    Object inputObj = valueFields.get(inputField.getId());
                    if (inputObj != null) {
                        inputField.setValue(inputObj.toString());
                    }
                    break;
                }
                case SINGLECHECK: {
                    SingleCheckField singleCheckField = (SingleCheckField) schemaField;
                    Object singleCheckObj = valueFields.get(singleCheckField.getId());
                    if (singleCheckObj != null) {
                        singleCheckField.setValue(singleCheckObj.toString());
                    }
                    break;
                }
                case MULTICHECK: {
                    MultiCheckField multiCheckField = (MultiCheckField) schemaField;
                    Object multiCheckObj = valueFields.get(multiCheckField.getId());
                    if (multiCheckObj != null) {
                        List<String> values = (List<String>) multiCheckObj;
                        for (String value : values) {
                            multiCheckField.addValue(value);
                        }
                    }
                    break;
                }
                case COMPLEX: {
                    ComplexField complexField = (ComplexField) schemaField;
                    Object complexObj = valueFields.get(complexField.getId());
                    if (complexObj != null){
                        Map<String, Object> valueMap = (Map<String, Object>) complexObj;
                        ComplexValue complexValue = new ComplexValue();
                        setComplexValue(complexField, complexValue, valueMap);
                        complexField.setComplexValue(complexValue);
                    }

                    break;
                }
                case MULTICOMPLEX:
                {
                    MultiComplexField multiComplexField = (MultiComplexField) schemaField;
                    Object multiComplexObj = valueFields.get(multiComplexField.getId());
                    if(multiComplexObj != null){
                        List<Map<String, Object>> valueMaps = (List<Map<String, Object>> )multiComplexObj ;
                        List<ComplexValue> complexValues = new ArrayList<>();
                        for (Map<String, Object> valueMap : valueMaps) {
                            ComplexValue complexValue = new ComplexValue();
                            setComplexValue(multiComplexField, complexValue, valueMap);
                            complexValues.add(complexValue);
                        }
                        multiComplexField.setComplexValues(complexValues);
                    }

                    break;
                }
            }
        }
    }

    private void setComplexValue(Field field, ComplexValue complexValue, Map<String, Object> valueMap) {
        List<Field> subFields = null;
        if (field.getType() == FieldTypeEnum.COMPLEX) {
            subFields = ((ComplexField)field).getFieldList();
        } else if (field.getType() == FieldTypeEnum.MULTICOMPLEX) {
            subFields = ((MultiComplexField)field).getFieldList();
        }

        for (Field subField : subFields){
            FieldTypeEnum fieldType = subField.getType();
            switch (fieldType){
                case INPUT:
                    if (valueMap.get(subField.getId()) != null){
                        complexValue.setInputFieldValue(subField.getId(), (String)valueMap.get(subField.getId()));
                    }
                    break;
                case SINGLECHECK:
                    if (valueMap.get(subField.getId()) !=null){
                        complexValue.setSingleCheckFieldValue(subField.getId(), new Value((String)valueMap.get(subField.getId())));
                    }
                    break;
                case MULTICHECK: {
                    if (valueMap.get(subField.getId()) != null){
                        List<String> values = (List<String>) valueMap.get(subField.getId());
                        List<Value> objValues = new ArrayList<>();
                        for (String value : values) {
                            objValues.add(new Value(value));
                        }
                        complexValue.setMultiCheckFieldValues(subField.getId(), objValues);
                    }
                    break;
                }
                case COMPLEX: {

                    if (valueMap.get(subField.getId()) != null){
                        Map<String, Object> subValueMap = (Map)valueMap.get(subField.getId());
                        ComplexValue subComplexValue = new ComplexValue();
                        setComplexValue(subField, subComplexValue, subValueMap);
                        complexValue.setComplexFieldValue(subField.getId(), complexValue);
                    }
                    break;
                }
                case MULTICOMPLEX: {

                    if (valueMap.get(subField.getId()) != null){
                        List<Map<String, Object>> subValueMaps = (List<Map<String, Object>>) valueMap.get(subField.getId());
                        List<ComplexValue> subComplexValues = new ArrayList<>();

                        for (Map<String, Object> subValueMap : subValueMaps) {
                            ComplexValue subComplexValue = new ComplexValue();
                            setComplexValue(subField, subComplexValue, subValueMap);
                            subComplexValues.add(subComplexValue);
                        }
                        complexValue.setMultiComplexFieldValues(subField.getId(), subComplexValues);
                    }
                    break;
                }
            }
        }
    }

    public void fieldsToValueMap(List<Field> fields,Map<String,Object> valueMap){
        for (Field field:fields){
            FieldTypeEnum type = field.getType();
            switch (type){
                case INPUT:
                    InputField inputField = (InputField)field;
                    valueMap.put(inputField.getId(),inputField.getValue());
                    break;
                case SINGLECHECK:
                    SingleCheckField singleCheckField = (SingleCheckField)field;
                    valueMap.put(singleCheckField.getId(),singleCheckField.getValue());
                    break;
                case MULTICHECK:
                    List<String> values = new ArrayList<>();
                    MultiCheckField multiCheckField = (MultiCheckField)field;
                    List<Value> checkValues =multiCheckField.getValues();
                    for (Value cValue:checkValues){
                        values.add(cValue.getValue());
                    }
                    valueMap.put(multiCheckField.getId(),values);

                    break;
                case COMPLEX:
                    break;
                case MULTICOMPLEX:
                    break;
            }
        }

    }

}
