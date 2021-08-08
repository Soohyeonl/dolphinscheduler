package org.apache.dolphinscheduler.graphql.service.datafetcher;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import graphql.schema.DataFetcher;
import org.apache.dolphinscheduler.api.utils.PageInfo;
import org.apache.dolphinscheduler.common.enums.AlertType;
import org.apache.dolphinscheduler.dao.entity.AlertGroup;
import org.apache.dolphinscheduler.dao.mapper.AlertGroupMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class AlertGroupDataFetcher {

    @Autowired
    private AlertGroupMapper alertGroupMapper;

    public DataFetcher<List<AlertGroup>> getQueryAllGroupListDataFetcher() {
        return dataFetchingEnvironment -> alertGroupMapper.queryAllGroupList();
    }

    public DataFetcher<AlertType> getAlertTypeDataFetcher() {
        return dataFetchingEnvironment -> {
            AlertGroup alertGroup = dataFetchingEnvironment.getSource();
            return alertGroup.getGroupType();
        };
    }

    public DataFetcher<ResultType> getCreateAlertGroupDataFetcher() {
        return dataFetchingEnvironment -> {
            String groupName = dataFetchingEnvironment.getArgument("groupName");
            String alertTypeString = dataFetchingEnvironment.getArgument("groupType");
            String description = dataFetchingEnvironment.getArgument("description");

            AlertGroup alertGroup = new AlertGroup();
            Date now = new Date();

            alertGroup.setGroupName(groupName);
            alertGroup.setGroupType(AlertType.valueOf(alertTypeString));
            alertGroup.setDescription(description);
            alertGroup.setCreateTime(now);
            alertGroup.setUpdateTime(now);

            int insert = alertGroupMapper.insert(alertGroup);

            ResultType resultType = new ResultType();
            if (insert > 0) {
                resultType.setCode(0);
                resultType.setMsg("success");
            } else {
                resultType.setCode(10027);
                resultType.setMsg("create alert group error");
            }

            return resultType;
        };
    }

    public DataFetcher<PageInfo<AlertGroup>> getQueryAlertGroupListPagingDataFetcher() {
        return dataFetchingEnvironment -> {
            Integer pageNo = dataFetchingEnvironment.getArgument("pageNo");
            Integer pageSize = dataFetchingEnvironment.getArgument("pageSize");
            String searchVal = dataFetchingEnvironment.getArgument("searchVal");

            Page<AlertGroup> page = new Page(pageNo, pageSize);
            IPage<AlertGroup> alertGroupIPage = alertGroupMapper.queryAlertGroupPage(page, searchVal);
            PageInfo<AlertGroup> pageInfo = new PageInfo<>(pageNo, pageSize);
            pageInfo.setTotalCount((int)alertGroupIPage.getTotal());
            pageInfo.setLists(alertGroupIPage.getRecords());

            return pageInfo;
        };
    }

}
