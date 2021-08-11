package org.apache.dolphinscheduler.graphql.datafetcher.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.dolphinscheduler.api.enums.Status;
import org.apache.dolphinscheduler.api.utils.PageInfo;
import org.apache.dolphinscheduler.api.utils.Result;
import org.apache.dolphinscheduler.common.enums.AlertType;
import org.apache.dolphinscheduler.common.utils.CollectionUtils;
import org.apache.dolphinscheduler.common.utils.StringUtils;
import org.apache.dolphinscheduler.dao.entity.AlertGroup;
import org.apache.dolphinscheduler.dao.entity.UserAlertGroup;
import org.apache.dolphinscheduler.dao.mapper.AlertGroupMapper;
import org.apache.dolphinscheduler.dao.mapper.UserAlertGroupMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
//@Service
public class AlertGroupService {

    @Autowired
    private AlertGroupMapper alertGroupMapper;

    @Autowired
    private UserAlertGroupMapper userAlertGroupMapper;

    @Autowired
    private UserAlertGroupService userAlertGroupService;

    public Result queryAllGroupList() {
        Result result = new Result();
        List<AlertGroup> alertGroups = alertGroupMapper.queryAllGroupList();
        result.setCode(Status.SUCCESS.getCode());
        result.setMsg(Status.SUCCESS.getMsg());
        result.setData(alertGroups);
        return result;
    }

    public Result createAlertGroup(String groupName, String groupType, String description) {
        AlertGroup alertGroup = new AlertGroup();
        Date now = new Date();

        alertGroup.setGroupName(groupName);
        alertGroup.setGroupType(AlertType.valueOf(groupType));
        alertGroup.setDescription(description);
        alertGroup.setCreateTime(now);
        alertGroup.setUpdateTime(now);

        int insert = alertGroupMapper.insert(alertGroup);

        Result result = new Result();
        if (insert > 0) {
            result.setCode(Status.SUCCESS.getCode());
            result.setMsg(Status.SUCCESS.getMsg());
        } else {
            result.setCode(Status.CREATE_ALERT_GROUP_ERROR.getCode());
            result.setMsg(Status.CREATE_ALERT_GROUP_ERROR.getMsg());
        }

        return result;
    }

    public Result queryAlertGroupPage(Integer pageNo, Integer pageSize, String searchVal) {
        Page<AlertGroup> page = new Page(pageNo, pageSize);
        IPage<AlertGroup> alertGroupIPage = alertGroupMapper.queryAlertGroupPage(page, searchVal);

        Result result = new Result();

        PageInfo<AlertGroup> pageInfo = new PageInfo<>(pageNo, pageSize);
        pageInfo.setTotalCount((int)alertGroupIPage.getTotal());
        pageInfo.setLists(alertGroupIPage.getRecords());

        result.setCode(Status.SUCCESS.getCode());
        result.setMsg(Status.SUCCESS.getMsg());
        result.setData(pageInfo);

        return result;
    }

    public Result delAlertGroupById(Integer id) {
        AlertGroup alertGroup = alertGroupMapper.selectById(id);
        Result result = new Result();
        if (alertGroup == null) {
            result.setCode(Status.ALERT_GROUP_NOT_EXIST.getCode());
            result.setMsg(Status.ALERT_GROUP_NOT_EXIST.getMsg());
            return result;
        }

        userAlertGroupMapper.deleteByAlertgroupId(id);
        alertGroupMapper.deleteById(id);
        result.setCode(Status.SUCCESS.getCode());
        result.setMsg(Status.SUCCESS.getMsg());
        return result;
    }

    public Result grantUser(Integer alertGroupId, Integer id, String userIds) {
        Result result = new Result();

        userAlertGroupMapper.deleteByAlertgroupId(alertGroupId);
        if (StringUtils.isEmpty(userIds)) {
            result.setCode(Status.SUCCESS.getCode());
            result.setMsg(Status.SUCCESS.getMsg());
            return result;
        }

        String[] userIdsArr = userIds.split(",");
        Date now = new Date();
        List<UserAlertGroup> alertGroups = new ArrayList<>(userIds.length());
        for (String userId : userIdsArr) {
            UserAlertGroup userAlertGroup = new UserAlertGroup();
            userAlertGroup.setAlertgroupId(alertGroupId);
            userAlertGroup.setUserId(Integer.parseInt(userId));
            userAlertGroup.setCreateTime(now);
            userAlertGroup.setUpdateTime(now);
            alertGroups.add(userAlertGroup);
        }

        if (CollectionUtils.isNotEmpty(alertGroups)) {
            userAlertGroupService.saveBatch(alertGroups);
        }

        result.setCode(Status.SUCCESS.getCode());
        result.setMsg(Status.SUCCESS.getMsg());

        return result;
    }

    public Result updateAlertGroup(String groupName, String groupType, Integer id, String description) {
        Result result = new Result();

        AlertGroup alertGroup = alertGroupMapper.selectById(id);
        if (alertGroup == null) {
            result.setCode(Status.ALERT_GROUP_NOT_EXIST.getCode());
            result.setMsg(Status.ALERT_GROUP_NOT_EXIST.getMsg());
            return result;
        }

        Date now = new Date();

        if (StringUtils.isNotEmpty(groupName)) {
            alertGroup.setGroupName(groupName);
        }

        if (groupType != null) {
            alertGroup.setGroupType(AlertType.valueOf(groupType));
        }
        alertGroup.setDescription(description);
        alertGroup.setUpdateTime(now);
        // updateProcessInstance
        alertGroupMapper.updateById(alertGroup);

        result.setCode(Status.SUCCESS.getCode());
        result.setMsg(Status.SUCCESS.getMsg());

        return result;
    }

    public Result verifyGroup(String groupName) {
        Result result = new Result();

        List<AlertGroup> alertGroup = alertGroupMapper.queryByGroupName(groupName);

        if (CollectionUtils.isNotEmpty(alertGroup)) {
            result.setCode(Status.ALERT_GROUP_EXIST.getCode());
            result.setMsg(Status.ALERT_GROUP_EXIST.getMsg());
        } else {
            result.setCode(Status.SUCCESS.getCode());
            result.setMsg(Status.SUCCESS.getMsg());
        }

        return result;
    }
}
