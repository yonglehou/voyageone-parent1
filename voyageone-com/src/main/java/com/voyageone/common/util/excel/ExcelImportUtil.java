package com.voyageone.common.util.excel;

import com.voyageone.common.util.ExcelUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelImportUtil {
    public static <TModel> void importSheet(Sheet productSheet, List<ExcelColumn> listProductColumn, List<TModel> listTModel, List<Map<String, Object>> listErrorMap, Class<TModel> entityClass) throws Exception {
        importSheet(productSheet, listProductColumn, listTModel, listErrorMap, entityClass, 1);
    }

    /**
     * @param productSheet listTModel
     * @param listTModel   导入集合
     * @param listErrorMap 导入错误集合
     * @throws Exception
     */
    public static <TModel> void importSheet(Sheet productSheet, List<ExcelColumn> listProductColumn, List<TModel> listTModel, List<Map<String, Object>> listErrorMap, Class<TModel> entityClass, int columnRowIndex) throws Exception {
        int LastCellNum = productSheet.getRow(0).getLastCellNum();//列数量
        Row rowColumn = productSheet.getRow(columnRowIndex);//excel列所在行
        Map<String, Integer> mapExcelColumn = new HashMap<>();//excel 列名字和列索引对应  key:excel列名字 value:excel列索引
        for (int i = 0; i < LastCellNum; i++) {
            mapExcelColumn.put(ExcelUtils.getString(rowColumn, i), i);
        }
        Map<String, Field> mapFiled = getMapFiled(entityClass);
        //TModel 所有属性字段Field   key: 字段名
        int LastRowNum = productSheet.getLastRowNum();//excel最后一行
        TModel model;
        String errorMsg;
        for (int i = columnRowIndex + 1; i <= LastRowNum; i++) {
            Row row = productSheet.getRow(i);//获取行
            if(row == null) return;
            model = entityClass.newInstance();
            errorMsg = rowToModel(mapExcelColumn, listProductColumn, row, model, mapFiled);//行转model
            if (!StringUtils.isEmpty(errorMsg)) {//转换失败   保存错误行
                Map<String, Object> errorMap = getErrorMap(row, mapExcelColumn);//row转map
                errorMap.put("errorMsg", errorMsg);
                listErrorMap.add(errorMap);//加入转换错误行集合
            } else {//转换成功
                listTModel.add(model);
            }
        }
    }

    private static <TModel> Map<String, Field> getMapFiled(Class<TModel> entityClass) {
        Map<String, Field> mapFiled = new HashMap<>();
        List<Field> listField = ReflectUtil.getListField(entityClass);
        for (Field field : listField) {
            mapFiled.put(field.getName(), field);
        }
        return mapFiled;
    }

    /**
     * 行转model
     */
    static <TModel> String rowToModel(Map<String, Integer> mapExcelColumn, List<ExcelColumn> listEnumColumn, Row row, TModel model, Map<String, Field> mapFiled) throws Exception {
        String errorMsg = "";
        for (ExcelColumn column : listEnumColumn) {
            if (mapExcelColumn.containsKey(column.getCamelColumnName())) {
                if (!mapFiled.containsKey(column.getCamelColumnName())) {
                    throw new Exception("文件导入格式不对"+ column.getCamelColumnName()+"列不存在,请检查");
                }
                Field field = mapFiled.get(column.getCamelColumnName());
                String content = getString(row.getCell(mapExcelColumn.get(column.getCamelColumnName())));
                if (!column.isNull()) {
                    if (StringUtils.isEmpty(content)) {
                        errorMsg += column.getCamelColumnName() + "不能为空";
                    }
                }
                try {
                    if (!StringUtils.isEmpty(content)) {
                        ReflectUtil.setFieldValueByName(field, content.trim(), model);
                    }
                } catch (Exception e) {
                    errorMsg += column.getCamelColumnName() + ":" + e.getMessage();
                }
            }
        }
        return errorMsg;
    }

    static Map<String, Object> getErrorMap(Row row, Map<String, Integer> mapExcelColumn) {
        Map<String, Object> map = new HashMap<>();
        for (String key : mapExcelColumn.keySet()) {
            Cell cell = row.getCell(mapExcelColumn.get(key));
            map.put(key, getString(cell));
        }
        return map;
    }

    static String getString(Cell cell) {
        String result = "";
        if (cell == null) return result;
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_NUMERIC:// 数字类型
                //1、判断是否是数值格式
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    double value = cell.getNumericCellValue();
                    Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
                    result = sdf.format(date);
                } else {
                    Double value = cell.getNumericCellValue();
                    DecimalFormat format = new DecimalFormat();
                    format.applyPattern("###################.###################");
                    result = format.format(value);
                }
                break;
            case HSSFCell.CELL_TYPE_STRING:// String类型
                result = cell.getRichStringCellValue().toString();
                break;
            case HSSFCell.CELL_TYPE_FORMULA://公式型
                //读公式计算值
                try {

                    double value = cell.getNumericCellValue();
                    if (Double.isNaN(value)) {//如果获取的数据值为非法值,则转换为获取字符串 //result.equals("NaN")
                        result = cell.getRichStringCellValue().toString();
                    } else {
                        DecimalFormat format = new DecimalFormat();
                        format.applyPattern("###################.###################");
                        result = format.format(value);
                    }
                } catch (Exception ex) {
                    result = cell.getRichStringCellValue().toString();
                }


                break;
            case HSSFCell.CELL_TYPE_BLANK:
                result = "";
                break;
            default:
                result = "";
                break;
        }
        return result;
    }
}
