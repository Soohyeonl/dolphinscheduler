package org.apache.dolphinscheduler.graphql.datafetcher;

import graphql.schema.DataFetcher;
import org.apache.dolphinscheduler.api.utils.Result;
import org.apache.dolphinscheduler.graphql.datafetcher.service.AlertGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AlertGroupDataFetchers {

    @Autowired
    private AlertGroupService alertGroupService;

    public DataFetcher<Result> dataFetchersQueryTypeQueryAllGroupList() {
        return dataFetchingEnvironment -> alertGroupService.queryAllGroupList();
    }

    public DataFetcher<Result> dataFetchersMutationTypeCreateAlertGroup() {
        return dataFetchingEnvironment -> {
            String groupName = dataFetchingEnvironment.getArgument("groupName");
            String alertTypeString = dataFetchingEnvironment.getArgument("groupType");
            String description = dataFetchingEnvironment.getArgument("description");

            return alertGroupService.createAlertGroup(groupName, alertTypeString, description);
        };
    }

    public DataFetcher<Result> dataFetchersQueryTypeQueryAlertGroupListPaging() {
        return dataFetchingEnvironment -> {
            Integer pageNo = dataFetchingEnvironment.getArgument("pageNo");
            Integer pageSize = dataFetchingEnvironment.getArgument("pageSize");
            String searchVal = dataFetchingEnvironment.getArgument("searchVal");

            return alertGroupService.queryAlertGroupPage(pageNo, pageSize, searchVal);
        };
    }

    public DataFetcher<Result> dataFetchersMutationTypeDelAlertGroupById() {
        return dataFetchingEnvironment -> {
            Integer id = Integer.valueOf(dataFetchingEnvironment.getArgument("id"));

            return alertGroupService.delAlertGroupById(id);
        };
    }

    public DataFetcher<Result> dataFetchersMutationTypeGrantUser() {
        return dataFetchingEnvironment -> {
            Integer alertGroupId = dataFetchingEnvironment.getArgument("alertGroupId");
            Integer id = dataFetchingEnvironment.getArgument("id");
            String userIds = dataFetchingEnvironment.getArgument("userIds");

            return alertGroupService.grantUser(alertGroupId, id, userIds);
        };
    }

    public DataFetcher<Result> dataFetchersMutationTypeUpdateAlertGroup() {
        return dataFetchingEnvironment -> {
            String groupName = dataFetchingEnvironment.getArgument("groupName");
            String groupType = dataFetchingEnvironment.getArgument("groupType");
            Integer id = dataFetchingEnvironment.getArgument("id");
            String description = dataFetchingEnvironment.getArgument("description");

            return alertGroupService.updateAlertGroup(groupName, groupType, id, description);
        };
    }

    public DataFetcher<Result> dataFetchersQueryTypeVerifyGroupName() {
        return dataFetchingEnvironment -> {
            String groupName = dataFetchingEnvironment.getArgument("groupName");

            return alertGroupService.verifyGroup(groupName);
        };
    }

}
