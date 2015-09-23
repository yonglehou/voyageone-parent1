package com.voyageone.bi.task.sup;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.bi.base.BiException;
import com.voyageone.bi.commonutils.ConditionUtil.Dimension;
import com.voyageone.bi.dao.sqlolap.SqlOlapConstantSales;
import com.voyageone.bi.dao.sqlolap.SqlOlapDaoSupport;
import com.voyageone.bi.dao.sqlolap.SqlOlapSalesUtils;
import com.voyageone.bi.disbean.ChartGridDisBean;
import com.voyageone.bi.disbean.ConditionBean;
import com.voyageone.bi.tranbean.UserInfoBean;

@Service
public class SalesSizeTask {

	private static String SIZE_ORG_SQL = 
			"SELECT  \n"
			+ "cur.id code  \n"
			+ ",'' parent  \n"
			+ ",cur.name name  \n"
			+ SqlOlapConstantSales.SALES_SUB_COLUMN
			+ "FROM (  \n"
			+ "#CUR_SUB_SQL#"
			+ " ) cur \n"
			+ "LEFT JOIN (\n"
			+ "#MOM_SUB_SQL#"
			+ " ) mom ON mom.id=cur.id \n"
			+ "LEFT JOIN (\n"
			+ "#YOY_SUB_SQL#"
			+ " ) yoy ON yoy.id=cur.id \n"
			+ " ORDER BY #ORDER_COLUMN# \n"
			+ " #LIMIT_COLUMN# \n";

	//vt_sales_sku
	private static String SIZE_SKU_SUB_SQL = "SELECT  \n"
			+ "size.id id \n"
			+ ",size.code code \n"
			+ ",size.name name \n"
			+ SqlOlapConstantSales.SALES_SUB_SQL_SKU
			+ "GROUP BY size.id \n";
	
	private static String SIZE_SKU_SQL = 
			SIZE_ORG_SQL.replace("#CUR_SUB_SQL#", SIZE_SKU_SUB_SQL)
			.replace("#MOM_SUB_SQL#",  SIZE_SKU_SUB_SQL.replace("#date_condition#", "#mom_date_condition#"))
			.replace("#YOY_SUB_SQL#",  SIZE_SKU_SUB_SQL.replace("#date_condition#", "#yoy_date_condition#"));	
	
	@Autowired
	private SqlOlapDaoSupport sqlOlapDaoSupport;
	
	/**
	 * getTopSalesSizeInfo
	 * @param condition
	 * @param userInfoBean
	 * @return
	 * @throws BiException
	 */
	public List<ChartGridDisBean> getTopSalesSizeInfo(ConditionBean condition, UserInfoBean userInfoBean, int topCount) throws BiException{
		ConditionBean cur_condition = condition.createCopy();
		if (topCount>0) {
			cur_condition.setLimit(String.valueOf(topCount));
		}
		List<ChartGridDisBean> result = getSalesSizeInfo(cur_condition, userInfoBean);
		return result;
	}
	
	/**
	 * getSalesSizeInfo
	 * @param condition
	 * @param userInfoBean
	 * @return
	 * @throws BiException
	 */
	public List<ChartGridDisBean> getSalesSizeInfo(ConditionBean condition, UserInfoBean userInfoBean) throws BiException{
		Dimension dimension = Dimension.Size;
		String queryStrOrg = SIZE_SKU_SQL;
		String tableName = SqlOlapSalesUtils.getTableName(condition, dimension);
		
		// limit
		String limitStr = "";
		if (condition.getLimit() != null && condition.getLimit().length()>0) {
			limitStr = "LIMIT " + condition.getLimit();
		}
		queryStrOrg = queryStrOrg.replaceAll("#LIMIT_COLUMN#", limitStr);
		
		// ORDER_COLUMN
		String sort_column = "amt_n";
		if (condition.getSort_col() !=null && condition.getSort_col().length()>0) {
			sort_column = condition.getSort_col();
		}
		// ORDER_COLUMN ASC
		String sord = "DESC";
		if (condition.getSord() !=null &&  condition.getSord().length()>0) {
			sord = condition.getSord().toUpperCase();
		}
		String sortStr = sort_column + " " +  sord + ", code ASC";
		queryStrOrg = queryStrOrg.replaceAll("#ORDER_COLUMN#", sortStr);
				
		List<ChartGridDisBean> result = SqlOlapSalesUtils.executeSqlAndMomYoy(
				queryStrOrg, 
				tableName,
				condition,
				dimension, 
				sqlOlapDaoSupport);
		return result;
	}
	
}
