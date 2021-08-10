package org.apache.dolphinscheduler.graphql.datafetcher;

import graphql.schema.DataFetcher;
import org.apache.dolphinscheduler.api.controller.BaseController;
import org.apache.dolphinscheduler.api.enums.Status;
import org.apache.dolphinscheduler.api.service.DataAnalysisService;
import org.apache.dolphinscheduler.api.utils.Result;
import org.apache.dolphinscheduler.dao.entity.User;
import org.apache.dolphinscheduler.graphql.datafetcher.service.UserArgumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class DataAnalysisDataFetchers extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(DataAnalysisDataFetchers.class);

    @Autowired
    private DataAnalysisService dataAnalysisService;

    @Autowired
    private UserArgumentService userArgumentService;

    public DataFetcher<Result> dataFetchersQueryTypeCountTaskStateByProject() {
        return dataFetchingEnvironment -> {
            LinkedHashMap<String, String> loginUserMap = dataFetchingEnvironment.getArgument("loginUser");

            Result selectUserResult = userArgumentService.getUserFromArgument(loginUserMap);

            if (selectUserResult.getCode() != Status.SUCCESS.getCode()) {
                logger.error("user not exist,  user id {}", loginUserMap.get("id"));
                return selectUserResult;
            }

            User loginUser = (User) selectUserResult.getData();

            String startDate = dataFetchingEnvironment.getArgument("startDate");
            String endDate = dataFetchingEnvironment.getArgument("endDate");
            int projectId = dataFetchingEnvironment.getArgument("projectId");

            Map<String, Object> result = dataAnalysisService.countTaskStateByProject(loginUser, projectId, startDate, endDate);
            return returnDataList(result);
        };
    }

}
