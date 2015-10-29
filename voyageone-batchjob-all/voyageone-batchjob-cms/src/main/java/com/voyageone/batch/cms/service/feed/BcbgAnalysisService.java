package com.voyageone.batch.cms.service.feed;

import com.google.gson.Gson;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.cms.bean.BcbgStyleBean;
import com.voyageone.batch.cms.bean.SuperFeedBcbgBean;
import com.voyageone.batch.cms.dao.SuperFeedDao;
import com.voyageone.batch.cms.dao.feed.BcbgSuperFeedDao;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.ChannelConfigEnums.Channel;
import com.voyageone.common.configs.Enums.FeedEnums;
import com.voyageone.common.configs.Enums.FeedEnums.Name;
import com.voyageone.common.configs.Feed;
import com.voyageone.common.util.DateTimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.voyageone.common.configs.Enums.ChannelConfigEnums.Channel.BCBG;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
 * Bcbg 的 Feed 数据分析服务
 *
 * Created by Jonas on 10/10/15.
 */
@Service
public class BcbgAnalysisService extends BaseTaskService {

    @Autowired
    private BcbgSuperFeedDao bcbgSuperFeedDao;
    
    @Autowired
    private SuperFeedDao superFeedDao;

    @Autowired
    private Transformer transformer;

    @Autowired
    private BcbgWsdlInsert insertService;

    @Autowired
    private BcbgWsdlUpdate updateService;

    @Autowired
    private BcbgWsdlAttribute attributeService;

    /**
     * 获取子系统
     */
    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    /**
     * 获取任务名称
     */
    @Override
    public String getTaskName() {
        return "BcbgAnalysis";
    }

    /**
     * 必须实现的，具体的任务内容
     *
     * @param taskControlList job 配置
     */
    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        $info("开始处理 BCBG 数据");

        Backup backup = new Backup();

        File[] files = getDataFiles(backup);

        // 说明没获取到文件, 放弃执行. 内部会进行日志输出
        if (files == null) return;

        File feedFile = files[0];
        File styleFile = files[1];

        // 读取数据文件
        BcbgFeedFile bcbgFeedFile = BcbgFeedFile.read(feedFile);
        List<SuperFeedBcbgBean> bcbgBeans = bcbgFeedFile.getMATERIALS();

        $info("已读取文件.获得 Feed %s 个", bcbgBeans.size());

        FileReader styleFileReader = new FileReader(styleFile);
        BcbgStyleBean[] styleBeans = new Gson().fromJson(styleFileReader, BcbgStyleBean[].class);

        $info("已读取文件.获得 Style %s 个", styleBeans.length);

        // 插入数据库
        clearLastData();
        insertNewData(bcbgBeans, styleBeans);

        // 处理下拉类属性
        attributeListInsert(BCBG);

        // 开始数据分析处理阶段
        transformer.new Context(BCBG, this).transform();
        $info("数据处理阶段结束");

        // 使用接口提交
        insertService.new Context(BCBG).postNewProduct();
        updateService.new Context(BCBG).postUpdatedProduct();

        // BCBG 的特殊情况:暂不使用
        attributeService.new Context(BCBG).postAttributes();

        // 备份文件
        backup.fromData(feedFile, styleFile);
    }

    private File[] getDataFiles(Backup backup) {

        // 读取各种配置
        // 精简配置,减少独立配置,所以两个文件都配置在一个项目里
        String fileNames = Feed.getVal1(BCBG, Name.feed_ftp_filename); // 文件路径

        // 拆分成 feed 和 style
        String[] fileNameArr = fileNames.split(";");

        if (fileNameArr.length != 2) {
            $info("读取的文件路径错误,至少需要两个文件.退出任务");
            throw new BusinessException("BCBG 的配置 feed_ftp_filename 错误,至少两个文件.");
        }

        String sFeedXmlDir = fileNameArr[0];
        String sStyleJsonDir = fileNameArr[1];

        // 检查配置
        if (StringUtils.isAnyEmpty(sFeedXmlDir, sStyleJsonDir)) {
            $info("没读取到文件路径,退出任务");
            throw new BusinessException("BCBG 的配置 feed_ftp_filename 错误,路径为空.");
        }

        File feedFile = getDataFile(sFeedXmlDir, ".xml", backup);

        if (feedFile == null) return null;

        File styleFile = getDataFile(sStyleJsonDir, ".json", backup);

        if (styleFile == null) return null;

        return new File[] { feedFile, styleFile };
    }

    /**
     * 去 dir 下,按照 filter 过滤文件,返回第一个文件,并备份其他文件
     */
    private File getDataFile(String dir, String filter, Backup backup) {

        // 打开目录
        File feedFileDir = new File(dir);
        // 过滤文件
        File[] feedFiles = feedFileDir.listFiles(i -> i.getName().contains(filter));

        if (feedFiles == null || feedFiles.length < 1) {
            $info("'%s' 数据文件不存在,退出任务", filter);
            return null;
        }

        // 排序文件
        List<File> feedFileList =  Arrays.asList(feedFiles).stream().sorted((f1, f2) -> f1.getName().compareTo(f2.getName())).collect(toList());

        // 取第一个作为目标文件,并从其中移除
        // 其他的等待后续

        return feedFileList.remove(0);
    }

    private void clearLastData() {
        // 删除所有
        bcbgSuperFeedDao.delete();
        bcbgSuperFeedDao.deleteStyles();
    }

    private void insertNewData(List<SuperFeedBcbgBean> bcbgBeans, BcbgStyleBean[] styleBeanArr) {

        int start = 0, end, total = bcbgBeans.size(), limit = 500;

        while (start < total) {

            end = start + limit;

            if (end > total) end = total;

            List<SuperFeedBcbgBean> subList = bcbgBeans.subList(start, end);

            int count = bcbgSuperFeedDao.insertWorkTables(subList);

            $info("分段插入 Feed %s 个", count);

            start = end;
        }

        List<BcbgStyleBean> styleBeans = Arrays.asList(styleBeanArr);

        // 对数据进行有效性过滤
        Map<Boolean, List<BcbgStyleBean>> styleBeansMap = styleBeans.stream()
                .collect(groupingBy(BcbgStyleBean::isValid, toList()));

        $info("完成 Style 的有效性过滤");

        styleBeans = styleBeansMap.get(true);

        $info("预计处理<有效>数据 %s 个", styleBeans.size());

        start = 0;
        total = styleBeans.size();

        while (start < total) {

            end = start + limit;

            if (end > total) end = total;

            List<BcbgStyleBean> subList = styleBeans.subList(start, end);

            try {
                int count = bcbgSuperFeedDao.insertStyles(subList);
                $info("分段插入 Style %s 个", count);
            } catch (Exception e) {
                $info(e.getMessage());
            }

            start = end;
        }

        // 对无效数据进行警告处理

        styleBeans = styleBeansMap.get(false);

        if (styleBeans == null || styleBeans.size() < 1) return;

        logIssue("发现部分 BCBG Style 文件的无效数据", styleBeans.size() + "个");
        $info("已警告<无效>数据 %s 个", styleBeans.size());
    }

    private void attributeListInsert(Channel channel){

        String channel_id = channel.getId();

        // 取出所有预定义的可选项属性
        List<String> attributeList = superFeedDao.selectSuperfeedAttributeList(channel_id, "1", "1");
        
        for (String attribute : attributeList) {
            // 从数据中取该属性的数据,并消除重复
            List<String> distinctValues = superFeedDao.selectAllAttribute(attribute, Feed.getVal1(channel_id, FeedEnums.Name.table_id));

            for (String value : distinctValues) {
                // 针对每个值进行检查.有则跳过,没有则插入
                String countByValue = superFeedDao.selectFeedAttribute(channel_id, attribute, value);

                if (!countByValue.equals("0")) continue;

                superFeedDao.insertFeedAttributeNew(channel_id, attribute, countByValue);
            }
        }
    }

    class Backup {

        private File backupDir;

        public Backup() {

            String sBackupDir = Feed.getVal1(BCBG, Name.feed_backup_dir); // 备份的文件路径

            // 如果是模板,就尝试格式化
            if (sBackupDir.contains("%s"))
                sBackupDir = String.format(sBackupDir, DateTimeUtil.getNow("yyyyMMdd"), DateTimeUtil.getNow("HHmmss"));

            backupDir = new File(sBackupDir);

            if (!backupDir.exists() && !backupDir.mkdirs()) {
                $info("产品文件备份失败,目录创建失败");
                throw new BusinessException("产品文件备份失败,目录创建失败");
            }
        }

        protected void from(File file) {
            if (!file.renameTo(new File(backupDir, file.getName())))
                $info("文件备份失败 %s %s", file.getPath(), file.getName());
        }

        protected void fromData(File file, File styleFile) {
            from(file);
            //from(styleFile);
        }
    }
}
