package com.voyageone.bi.task;

import com.voyageone.bi.ajax.bean.AjaxFinancialBillBean;
import com.voyageone.bi.ajax.bean.AjaxFinancialCostReportBean;
import com.voyageone.bi.ajax.bean.AjaxFinancialMonthlyReportBean;
import com.voyageone.bi.ajax.bean.AjaxFinancialTaxReportBean;
import com.voyageone.bi.base.BiApplication;
import com.voyageone.bi.base.BiException;
import com.voyageone.bi.bean.BillBean;
import com.voyageone.bi.commonutils.Contants;
import com.voyageone.bi.dao.BillInfoDao;
import com.voyageone.bi.dao.ReportInfoDao;
import com.voyageone.bi.dao.UserInfoDao;
import com.voyageone.bi.disbean.DetailReportDisBean;
import com.voyageone.bi.disbean.MonthlyReportDisBean;
import com.voyageone.bi.disbean.TaxDetailReportDisBean;
import com.voyageone.bi.task.sup.FinancialBillTask;
import com.voyageone.bi.task.sup.FinancialExportTask;
import com.voyageone.bi.task.sup.FinancialReportTask;
import com.voyageone.bi.tranbean.UserChannelBean;
import com.voyageone.bi.tranbean.UserInfoBean;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Kylin on 2015/7/24.
 */
@Service
public class DataExportTask {
    private static Log logger = LogFactory.getLog(DataExportTask.class);

    @Autowired
    private FinancialExportTask financialExportTask;
    @Autowired
    private FinancialBillTask financialBillTask;
    @Autowired
    private FinancialReportTask financialReportTask;
    @Autowired
    private UserInfoDao userInfoDao;
    @Autowired
    private BillInfoDao billInfoDao;
    @Autowired
    private ReportInfoDao reportInfoDao;


    public void ajaxExportFinancialCostReport(AjaxFinancialCostReportBean bean, UserInfoBean userInfoBean) {
        AjaxFinancialCostReportBean.Result result = bean.getResponseBean();
        try {

            //根目录绝对路径
            String strWebRootPath = bean.getWeb_root_path();
            //出力文件绝对路径
            String strCreateWorkBookPath = bean.getReport_file_path();
            WritableWorkbook writableWorkbook = financialExportTask.getFinanceCostWorkBook(strWebRootPath, strCreateWorkBookPath);

            if (writableWorkbook != null) {

                String strSheetName = "";
                String[] channel = bean.getChannel_ids().replace("\"", "").replace("'", "").split(",");
                Map<String, String> mapChannel = getChannelMap(userInfoBean);
                for (int i = 0; i < channel.length; i++) {
                    if (mapChannel.containsKey(channel[i])) {

                        bean.setChannel_ids("'" + channel[i] + "'");
                        List<DetailReportDisBean> beanReportList = reportInfoDao.getDetailReportList(bean);
                        if (beanReportList != null) {
                            strSheetName = BiApplication.readValue("cost_sheet") + mapChannel.get(channel[i]);
                            financialExportTask.exportExcelCostReport(writableWorkbook, strSheetName, beanReportList);
                        }
                    }
                }
                try {
                    writableWorkbook.write();
                    writableWorkbook.close();
                } catch (FileNotFoundException e) {
                    logger.error("ajaxExportFinancialCostReport", e);
                } catch (IOException e) {
                    logger.error("ajaxExportFinancialCostReport error", e);
                }

                result.setReport_file_path(bean.getWeb_file_path());
                result.setReport_file_name(bean.getReport_file_name());

                List<DetailReportDisBean> beanList = financialReportTask.getFinancialCostReportInfo(bean, userInfoBean);

                result.setDetailReportDisBean(beanList);
                result.setReport_page(bean.getReport_page());
                result.setReport_record_count(bean.getReport_record_count());
            }

            result.setReqResult(Contants.AJAX_RESULT_OK);

        } catch (WriteException e) {
            logger.error("ajaxExportFinancialCostReport", e);
        } catch (Exception e) {
            logger.error("ajaxExportFinancialCostReport", e);
        }

    }

    public void ajaxExportFinancialMonthlyReport(AjaxFinancialMonthlyReportBean bean, UserInfoBean userInfoBean) {

        AjaxFinancialMonthlyReportBean.Result result = bean.getResponseBean();
        try {

            //根目录绝对路径
            String strWebRootPath = bean.getWeb_root_path();
            //出力文件绝对路径
            String strCreateWorkBookPath = bean.getReport_file_path();

            WritableWorkbook writableWorkbook = financialExportTask.getFinanceMonthlyWorkBook(strWebRootPath, strCreateWorkBookPath);

            if (writableWorkbook != null) {

                String strSheetName = "";
                List<MonthlyReportDisBean> beanReportList = reportInfoDao.getMonthlyReportList(bean);
                if (beanReportList != null) {
                    strSheetName = BiApplication.readValue("monthly_sheet");
                    financialExportTask.exportExcelMonthlyReport(writableWorkbook, strSheetName, beanReportList);
                }

                try {
                    writableWorkbook.write();
                    writableWorkbook.close();
                } catch (FileNotFoundException e) {
                    logger.error("ajaxExportFinancialCostReport", e);
                } catch (IOException e) {
                    logger.error("ajaxExportFinancialCostReport error", e);
                }
                result.setReport_file_path(bean.getWeb_file_path());
                result.setReport_file_name(bean.getReport_file_name());

                List<MonthlyReportDisBean> beanList = financialReportTask.getFinancialMonthlyReportInfo(bean, userInfoBean);
                result.setMonthlyReportDisBean(beanList);
                result.setReport_page(bean.getReport_page());
                result.setReport_record_count(bean.getReport_record_count());
            }

            result.setReqResult(Contants.AJAX_RESULT_OK);

        } catch (WriteException e) {
            logger.error("ajaxExportFinancialMonthlyReport", e);
        } catch (Exception e) {
            logger.error("ajaxExportFinancialMonthlyReport", e);
        }

    }


    public void ajaxExportFinancialTaxReport(AjaxFinancialTaxReportBean bean, UserInfoBean userInfoBean) throws BiException {

        WritableWorkbook writableWorkbook = null;
        AjaxFinancialTaxReportBean.Result result = bean.getResponseBean();
        try {
            //根目录绝对路径
            String strWebRootPath = bean.getWeb_root_path();
            //出力文件绝对路径
            String strCreateWorkBookPath = bean.getReport_file_path();

            writableWorkbook = financialExportTask.getFinanceTaxWorkBook(strWebRootPath, strCreateWorkBookPath);

            if (writableWorkbook != null) {

                String strSheetName = "";
                String[] channel = bean.getChannel_ids().replace("\"", "").replace("'", "").split(",");
                Map<String, String> mapChannel = getChannelMap(userInfoBean);
                for (int i = 0; i < channel.length; i++) {
                    if (mapChannel.containsKey(channel[i])) {
                        bean.setChannel_ids("'" + channel[i] + "'");
                        List<TaxDetailReportDisBean> beanReportList = reportInfoDao.getTaxDetailReportList(bean);
                        if (beanReportList != null) {
                            strSheetName = BiApplication.readValue("tax_sheet") + mapChannel.get(channel[i]);
                            financialExportTask.exportExcelTaxReport(writableWorkbook, strSheetName, beanReportList);
                        }
                    }
                }

                try {
                    writableWorkbook.write();
                    writableWorkbook.close();
                } catch (FileNotFoundException e) {
                    logger.error("ajaxExportFinancialCostReport", e);
                } catch (IOException e) {
                    logger.error("ajaxExportFinancialCostReport error", e);
                }

                result.setReport_file_path(bean.getWeb_file_path());
                result.setReport_file_name(bean.getReport_file_name());

                List<TaxDetailReportDisBean> beanList = financialReportTask.getFinancialTaxReportInfo(bean, userInfoBean);
                result.setTaxDetailReportDisBean(beanList);
                result.setReport_page(bean.getReport_page());
                result.setReport_record_count(bean.getReport_record_count());
            }

            result.setReqResult(Contants.AJAX_RESULT_OK);

        } catch (WriteException e) {
            logger.error("ajaxExportFinancialTaxReport", e);
        } catch (Exception e) {
            logger.error("ajaxExportFinancialTaxReport", e);
        }

    }

    public void ajaxExportFinancialBillReport(AjaxFinancialBillBean bean, UserInfoBean userInfoBean) {
        AjaxFinancialBillBean.Result result = bean.getResponseBean();
        try {

            WritableWorkbook writableWorkbook = null;

            //根目录绝对路径
            String strWebRootPath = bean.getWeb_root_path();
            //出力文件绝对路径
            String strCreateWorkBookPath = bean.getReport_file_path();

            writableWorkbook = financialExportTask.getFinanceInvoiceWorkBook(strWebRootPath, strCreateWorkBookPath);

            if (writableWorkbook != null) {

                List<BillBean> billList = billInfoDao.getBillList(bean);

                if (billList != null) {

                    financialExportTask.exportExcelInvoiceReport(writableWorkbook, billList);

                }

                try {
                    writableWorkbook.write();
                    writableWorkbook.close();
                } catch (FileNotFoundException e) {
                    logger.error("ajaxExportFinancialCostReport", e);
                } catch (IOException e) {
                    logger.error("ajaxExportFinancialCostReport error", e);
                }

                result.setReport_file_path(bean.getWeb_file_path());
                result.setReport_file_name(bean.getReport_file_name());

                List<BillBean> listTaxBillBean = financialBillTask.getFinancialTaxBillInfo(bean, userInfoBean);

                result.setTaxBillBean(listTaxBillBean);
                result.setTax_page(bean.getTax_page());
                result.setTax_record_count(bean.getTax_record_count());

                List<BillBean> listTranspotationBillBean = financialBillTask.getFinancialTranspotationBillInfo(bean, userInfoBean);

                result.setTranspotationBillBean(listTranspotationBillBean);
                result.setTranspotation_page(bean.getTranspotation_page());
                result.setTranspotation_record_count(bean.getTranspotation_record_count());
            }
            result.setReqResult(Contants.AJAX_RESULT_OK);

        } catch (WriteException e) {
            logger.error("ajaxExportFinancialBillReport", e);
        } catch (Exception e) {
            logger.error("ajaxExportFinancialBillReport", e);
        }

    }

    private Map<String, String> getChannelMap(UserInfoBean userInfoBean) {
        // 画面店铺显示数据
        Map<String, String> mapChannel = new HashMap<>();
        List<UserChannelBean> userChannelList = userInfoDao.selectUserChannelById(userInfoBean.getUid());
        for (UserChannelBean userChannelBean : userChannelList) {
            mapChannel.put(userChannelBean.getCode(), userChannelBean.getName());
        }

        return mapChannel;
    }
}
