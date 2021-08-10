package org.apache.dolphinscheduler.graphql.datafetcher.service;

import org.apache.dolphinscheduler.api.dto.TaskCountDto;
import org.apache.dolphinscheduler.api.enums.Status;
import org.apache.dolphinscheduler.api.utils.Result;
import org.apache.dolphinscheduler.common.Constants;
import org.apache.dolphinscheduler.common.enums.UserType;
import org.apache.dolphinscheduler.common.utils.DateUtils;
import org.apache.dolphinscheduler.dao.entity.ExecuteStatusCount;
import org.apache.dolphinscheduler.dao.entity.Project;
import org.apache.dolphinscheduler.dao.entity.User;
import org.apache.dolphinscheduler.dao.mapper.ProjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class DataAnalysisService {

    private static final Logger logger = LoggerFactory.getLogger(DataAnalysisService.class);

    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProcessService processService;

    public Result countTaskStateByProject(User loginUser, int projectId, String startDate, String endDate) {
        Result result = new Result();

        boolean checkProject = checkProject(loginUser, projectId, result);
        if (!checkProject) {
            return result;
        }

        Date start = null;
        Date end = null;

        try {
            start = DateUtils.getScheduleDate(startDate);
            end = DateUtils.getScheduleDate(endDate);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            putErrorRequestParamsMsg(result);
            return result;
        }

//        Integer[] projectIds = getProjectIdsArrays(loginUser, projectId);
//        List<ExecuteStatusCount> taskInstanceStateCounts =
//                taskInstanceMapper.countTaskInstanceStateByUser(start, end, projectIds);
//
//        if (taskInstanceStateCounts != null) {
//            TaskCountDto taskCountResult = new TaskCountDto(taskInstanceStateCounts);
//            result.put(Constants.DATA_LIST, taskCountResult);
//            putMsg(result, Status.SUCCESS);
//        }
//        return result;

        return result;
    }

    private boolean checkProject(User loginUser, int projectId, Result result){
        if(projectId != 0){
            Project project = projectMapper.selectById(projectId);
            return projectService.hasProjectAndPerm(loginUser, project, result);
        }
        return true;
    }

    private void putErrorRequestParamsMsg(Result result) {
        result.setCode(Status.REQUEST_PARAMS_NOT_VALID_ERROR.getCode());
        result.setMsg(MessageFormat.format(Status.REQUEST_PARAMS_NOT_VALID_ERROR.getMsg(), "startDate,endDate"));
    }

    private Integer[] getProjectIdsArrays(User loginUser, int projectId) {
        List<Integer> projectIds = new ArrayList<>();
        if(projectId !=0){
            projectIds.add(projectId);
        }else if(loginUser.getUserType() == UserType.GENERAL_USER){
            projectIds = processService.getProjectIdListHavePerm(loginUser.getId());
            if(projectIds.size() ==0 ){
                projectIds.add(0);
            }
        }
        return projectIds.toArray(new Integer[projectIds.size()]);
    }

}
