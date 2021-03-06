package com.voyageone.service.impl.vms.inventory;

import com.voyageone.service.dao.vms.VmsBtInventoryFileDao;
import com.voyageone.service.daoext.vms.VmsBtFeedFileDaoExt;
import com.voyageone.service.daoext.vms.VmsBtInventoryFileDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.vms.VmsBtInventoryFileModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * InventoryFileService
 *
 * @author jeff.duan 16/9/8
 * @version 1.0.0
 */
@Service
public class InventoryFileService extends BaseService {

    private final VmsBtInventoryFileDao vmsBtInventoryFileDao;

    private final VmsBtInventoryFileDaoExt vmsBtInventoryFileDaoExt;

    @Autowired
    public InventoryFileService(VmsBtInventoryFileDaoExt vmsBtInventoryFileDaoExt, VmsBtInventoryFileDao vmsBtInventoryFileDao) {
        this.vmsBtInventoryFileDaoExt = vmsBtInventoryFileDaoExt;
        this.vmsBtInventoryFileDao = vmsBtInventoryFileDao;
    }

    /**
     * 新建一条文件信息到vms_bt_inventory_file表
     *
     * @param channelId   渠道
     * @param fileName    文件名
     * @param newFileName 新文件名
     * @param uploadType 上传状态
     * @param status      状态
     * @param modifier    创建者
     * @return id
     */
    public Integer insertInventoryFileInfo(String channelId, String fileName, String newFileName, String uploadType, String status, String modifier) {
        VmsBtInventoryFileModel model = new VmsBtInventoryFileModel();
        model.setChannelId(channelId);
        model.setClientFileName(fileName);
        model.setFileName(newFileName);
        model.setUploadType(uploadType);
        model.setStatus(status);
        model.setCreater(modifier);
        model.setModifier(modifier);
        vmsBtInventoryFileDao.insert(model);
        return model.getId();

    }

    /**
     * 根据状态从vms_bt_inventory_file表取得InventoryFile信息
     *
     * @param channelId   渠道
     * @param status      状态
     * @return FeedFile列表
     */
    public List<VmsBtInventoryFileModel> getInventoryFileInfoByStatus(String channelId, String status) {
        Map<String, Object> param = new HashMap<>();
        param.put("channelId", channelId);
        param.put("status", status);
        return vmsBtInventoryFileDao.selectList(param);
    }

    /**
     * 条件搜索FeedFile
     * @param param 搜索条件
     * @return FeedFile列表
     */
    public List<Map<String, Object>> getPrcInvFileList(Map<String, Object> param) {

        return vmsBtInventoryFileDaoExt.selectList(param);

    }

    /**
     * 条件搜索FeedFile
     * @param param 搜索条件
     * @return FeedFile列表
     */
    public long getPrcInvFileListCount(Map<String, Object> param) {

        return vmsBtInventoryFileDaoExt.selectListCount(param);

    }
}
