package org.apache.dolphinscheduler.graphql.datafetcher;

import graphql.schema.DataFetcher;
import org.apache.dolphinscheduler.api.enums.Status;
import org.apache.dolphinscheduler.api.utils.Result;
import org.apache.dolphinscheduler.api.service.AlertGroupService;
import org.apache.dolphinscheduler.common.Constants;
import org.apache.dolphinscheduler.common.enums.AlertType;
import org.apache.dolphinscheduler.common.utils.ParameterUtils;
import org.apache.dolphinscheduler.dao.entity.User;
import org.apache.dolphinscheduler.graphql.datafetcher.service.UserArgumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class AlertGroupDataFetchers extends BaseDataFetchers {

    private static final Logger logger = LoggerFactory.getLogger(DataAnalysisDataFetchers.class);

    @Autowired
    private AlertGroupService alertGroupService;

    @Autowired
    private UserArgumentService userArgumentService;

    public DataFetcher<Result> dataFetchersQueryTypeQueryAllGroupList() {
        return dataFetchingEnvironment -> {
            LinkedHashMap<String, String> loginUserMap = dataFetchingEnvironment.getArgument("loginUser");

            Result selectUserResult = userArgumentService.getUserFromArgument(loginUserMap);

            if (selectUserResult.getCode() != Status.SUCCESS.getCode()) {
                logger.error("user not exist,  user id {}", loginUserMap.get("id"));
                return selectUserResult;
            }

            User loginUser = (User) selectUserResult.getData();

            logger.info("login user {}, query all alertGroup", loginUser.getUserName());

            HashMap<String, Object> result = alertGroupService.queryAlertgroup();

            return returnDataList(result);
        };
    }

    public DataFetcher<Result> dataFetchersMutationTypeCreateAlertGroup() {
        return dataFetchingEnvironment -> {
            LinkedHashMap<String, String> loginUserMap = dataFetchingEnvironment.getArgument("loginUser");
            Result selectUserResult = userArgumentService.getUserFromArgument(loginUserMap);
            if (selectUserResult.getCode() != Status.SUCCESS.getCode()) {
                logger.error("user not exist,  user id {}", loginUserMap.get("id"));
                return selectUserResult;
            }
            User loginUser = (User) selectUserResult.getData();

            String groupName = dataFetchingEnvironment.getArgument("groupName");
            AlertType groupType = AlertType.valueOf(dataFetchingEnvironment.getArgument("groupType"));
            String description = dataFetchingEnvironment.getArgument("description");

            logger.info("loginUser user {}, create alertgroup, groupName: {}, groupType: {}, desc: {}",
                    loginUser.getUserName(), groupName, groupType, description);

            Map<String, Object> result = alertGroupService.createAlertgroup(loginUser, groupName, groupType, description);

            return returnDataList(result);
        };
    }

    public DataFetcher<Result> dataFetchersQueryTypeQueryAlertGroupListPaging() {
        return dataFetchingEnvironment -> {
            LinkedHashMap<String, String> loginUserMap = dataFetchingEnvironment.getArgument("loginUser");
            Result selectUserResult = userArgumentService.getUserFromArgument(loginUserMap);
            if (selectUserResult.getCode() != Status.SUCCESS.getCode()) {
                logger.error("user not exist,  user id {}", loginUserMap.get("id"));
                return selectUserResult;
            }
            User loginUser = (User) selectUserResult.getData();

            Integer pageNo = dataFetchingEnvironment.getArgument("pageNo");
            Integer pageSize = dataFetchingEnvironment.getArgument("pageSize");
            String searchVal = dataFetchingEnvironment.getArgument("searchVal");

            logger.info("login  user {}, list paging, pageNo: {}, searchVal: {}, pageSize: {}",
                    loginUser.getUserName(), pageNo, searchVal, pageSize);
            Map<String, Object> result = checkPageParams(pageNo, pageSize);
            if (result.get(Constants.STATUS) != Status.SUCCESS) {
                return returnDataListPaging(result);
            }

            searchVal = ParameterUtils.handleEscapes(searchVal);
            result = alertGroupService.listPaging(loginUser, searchVal, pageNo, pageSize);

            return returnDataListPaging(result);
        };
    }

    public DataFetcher<Result> dataFetchersMutationTypeDelAlertGroupById() {
        return dataFetchingEnvironment -> {
            LinkedHashMap<String, String> loginUserMap = dataFetchingEnvironment.getArgument("loginUser");
            Result selectUserResult = userArgumentService.getUserFromArgument(loginUserMap);
            if (selectUserResult.getCode() != Status.SUCCESS.getCode()) {
                logger.error("user not exist,  user id {}", loginUserMap.get("id"));
                return selectUserResult;
            }
            User loginUser = (User) selectUserResult.getData();

            Integer id = Integer.valueOf(dataFetchingEnvironment.getArgument("id"));

            logger.info("login user {}, delete AlertGroup, id: {},", loginUser.getUserName(), id);
            Map<String, Object> result = alertGroupService.delAlertgroupById(loginUser, id);

            return returnDataList(result);
        };
    }

    public DataFetcher<Result> dataFetchersMutationTypeGrantUser() {
        return dataFetchingEnvironment -> {
            LinkedHashMap<String, String> loginUserMap = dataFetchingEnvironment.getArgument("loginUser");
            Result selectUserResult = userArgumentService.getUserFromArgument(loginUserMap);
            if (selectUserResult.getCode() != Status.SUCCESS.getCode()) {
                logger.error("user not exist,  user id {}", loginUserMap.get("id"));
                return selectUserResult;
            }
            User loginUser = (User) selectUserResult.getData();

            Integer alertGroupId = dataFetchingEnvironment.getArgument("alertGroupId");
            String userIds = dataFetchingEnvironment.getArgument("userIds");

            logger.info("login user {}, grant user, alertGroupId: {},userIds : {}", loginUser.getUserName(), alertGroupId, userIds);
            Map<String, Object> result = alertGroupService.grantUser(loginUser, alertGroupId, userIds);
            return returnDataList(result);
        };
    }

    public DataFetcher<Result> dataFetchersMutationTypeUpdateAlertGroup() {
        return dataFetchingEnvironment -> {
            LinkedHashMap<String, String> loginUserMap = dataFetchingEnvironment.getArgument("loginUser");
            Result selectUserResult = userArgumentService.getUserFromArgument(loginUserMap);
            if (selectUserResult.getCode() != Status.SUCCESS.getCode()) {
                logger.error("user not exist,  user id {}", loginUserMap.get("id"));
                return selectUserResult;
            }
            User loginUser = (User) selectUserResult.getData();

            String groupName = dataFetchingEnvironment.getArgument("groupName");
            AlertType groupType = AlertType.valueOf(dataFetchingEnvironment.getArgument("groupType"));
            Integer id = dataFetchingEnvironment.getArgument("id");
            String description = dataFetchingEnvironment.getArgument("description");

            logger.info("login  user {}, updateProcessInstance alertgroup, groupName: {}, groupType: {}, desc: {}",
                    loginUser.getUserName(), groupName, groupType, description);
            Map<String, Object> result = alertGroupService.updateAlertgroup(loginUser, id, groupName, groupType, description);
            return returnDataList(result);
        };
    }

    public DataFetcher<Result> dataFetchersQueryTypeVerifyGroupName() {
        return dataFetchingEnvironment -> {
            LinkedHashMap<String, String> loginUserMap = dataFetchingEnvironment.getArgument("loginUser");
            Result selectUserResult = userArgumentService.getUserFromArgument(loginUserMap);
            if (selectUserResult.getCode() != Status.SUCCESS.getCode()) {
                logger.error("user not exist,  user id {}", loginUserMap.get("id"));
                return selectUserResult;
            }
            User loginUser = (User) selectUserResult.getData();

            String groupName = dataFetchingEnvironment.getArgument("groupName");

            logger.info("login user {}, verify group name: {}", loginUser.getUserName(), groupName);

            boolean exist = alertGroupService.existGroupName(groupName);
            Result result = new Result();
            if (exist) {
                logger.error("group {} has exist, can't create again.", groupName);
                result.setCode(Status.ALERT_GROUP_EXIST.getCode());
                result.setMsg(Status.ALERT_GROUP_EXIST.getMsg());
            } else {
                result.setCode(Status.SUCCESS.getCode());
                result.setMsg(Status.SUCCESS.getMsg());
            }
            return result;
        };
    }

}
