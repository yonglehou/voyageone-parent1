package com.voyageone.service.impl.com.user;

import com.voyageone.security.dao.ComOrganizationDao;
import com.voyageone.security.model.ComOrganizationModel;
import com.voyageone.service.impl.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Ethan Shi on 2016-08-17.
 */
@Service
public class AdminOrgService extends BaseService {


    @Autowired
    ComOrganizationDao comOrganizationDao;


    /**
     * 取所有组织的列表
     *
     * @return
     */
    public Map<Integer, String> getAllOrg()
    {
        List<ComOrganizationModel> orgList = comOrganizationDao.selectList(new HashMap<>());

        List<ComOrganizationModel> result = new ArrayList<>();

        result = getChildTreeObjects(orgList, 0 , " ", result);

        Map resultMap = result.stream().collect(Collectors.toMap(ComOrganizationModel:: getId ,ComOrganizationModel:: getOrgName ));

        return  resultMap;
    }


    /**
     * 生成树状列表
     *
     * @param list
     * @param typeId
     * @param prefix
     * @param returnList
     * @return
     */
    private List<ComOrganizationModel> getChildTreeObjects(List<ComOrganizationModel> list, int typeId,String prefix , List<ComOrganizationModel> returnList){
        if(list == null) return null;
        for (Iterator<ComOrganizationModel> iterator = list.iterator(); iterator.hasNext();) {
            ComOrganizationModel node = (ComOrganizationModel) iterator.next();
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (node.getParentId().equals(typeId)) {
                recursionFn(list, node,prefix , returnList);
            }
        }
        return returnList;
    }

    private void recursionFn(List<ComOrganizationModel> list, ComOrganizationModel node,String p , List<ComOrganizationModel> returnList) {
        List<ComOrganizationModel> childList = getChildList(list, node);// 得到子节点列表
        if (hasChild(list, node)) {// 判断是否有子节点
            returnList.add(node);
            Iterator<ComOrganizationModel> it = childList.iterator();
            while (it.hasNext()) {
                ComOrganizationModel n = (ComOrganizationModel) it.next();
                n.setOrgName(p+n.getOrgName());
                recursionFn(list, n,p+p, returnList);
            }
        } else {
            returnList.add(node);
        }
    }

    // 判断是否有子节点
    private boolean hasChild(List<ComOrganizationModel> list, ComOrganizationModel t) {
        return getChildList(list, t).size() > 0 ? true : false;
    }

    // 得到子节点列表
    private List<ComOrganizationModel> getChildList(List<ComOrganizationModel> list, ComOrganizationModel t) {

        List<ComOrganizationModel> tlist = new ArrayList<ComOrganizationModel>();
        Iterator<ComOrganizationModel> it = list.iterator();
        while (it.hasNext()) {
            ComOrganizationModel n = (ComOrganizationModel) it.next();
            if (n.getParentId().equals(t.getId())) {
                tlist.add(n);
            }
        }
        return tlist;
    }

}