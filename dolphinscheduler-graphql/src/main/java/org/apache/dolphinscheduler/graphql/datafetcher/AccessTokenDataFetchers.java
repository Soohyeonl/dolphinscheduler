package org.apache.dolphinscheduler.graphql.datafetcher;

import graphql.schema.DataFetcher;
import org.apache.dolphinscheduler.api.enums.Status;
import org.apache.dolphinscheduler.api.utils.Result;
import org.apache.dolphinscheduler.common.Constants;
import org.apache.dolphinscheduler.common.utils.ParameterUtils;
import org.apache.dolphinscheduler.dao.entity.User;
import org.apache.dolphinscheduler.api.service.AccessTokenService;
import org.apache.dolphinscheduler.graphql.datafetcher.service.UserArgumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class AccessTokenDataFetchers extends BaseDataFetchers {

    private static final Logger logger = LoggerFactory.getLogger(AccessTokenDataFetchers.class);

    @Autowired
    private AccessTokenService accessTokenService;

    @Autowired
    private UserArgumentService userArgumentService;


    public Map<String, Object> checkPageParams(int pageNo, int pageSize) {
        Map<String, Object> result = new HashMap<>(4);
        Status resultEnum = Status.SUCCESS;
        String msg = Status.SUCCESS.getMsg();
        if (pageNo <= 0) {
            resultEnum = Status.REQUEST_PARAMS_NOT_VALID_ERROR;
            msg = MessageFormat.format(Status.REQUEST_PARAMS_NOT_VALID_ERROR.getMsg(), Constants.PAGE_NUMBER);
        } else if (pageSize <= 0) {
            resultEnum = Status.REQUEST_PARAMS_NOT_VALID_ERROR;
            msg = MessageFormat.format(Status.REQUEST_PARAMS_NOT_VALID_ERROR.getMsg(), Constants.PAGE_SIZE);
        }
        result.put(Constants.STATUS, resultEnum);
        result.put(Constants.MSG, msg);
        return result;
    }


    public DataFetcher<Result> dataFetchersMutationTypeCreateToken() {
        return dataFetchingEnvironment -> {
            LinkedHashMap<String, String> loginUserMap = dataFetchingEnvironment.getArgument("loginUser");
            Result selectUserResult = userArgumentService.getUserFromArgument(loginUserMap);
            if (selectUserResult.getCode() != Status.SUCCESS.getCode()) {
                logger.error("user not exist,  user id {}", loginUserMap.get("id"));
                return selectUserResult;
            }
            User loginUser = (User) selectUserResult.getData();

            Integer userId = dataFetchingEnvironment.getArgument("userId");
            String expireTime = dataFetchingEnvironment.getArgument("expireTime");
            String token = dataFetchingEnvironment.getArgument("token");

            logger.info("login user {}, create token , userId : {} , token expire time : {} , token : {}", loginUser.getUserName(),
                    userId, expireTime, token);

            Map<String, Object> result = accessTokenService.createToken(loginUser, userId, expireTime, token);
            return returnDataList(result);
        };
    }

    public DataFetcher<Result> dataFetchersQueryTypeGenerateToken() {
        return dataFetchingEnvironment -> {
            LinkedHashMap<String, String> loginUserMap = dataFetchingEnvironment.getArgument("loginUser");
            Result selectUserResult = userArgumentService.getUserFromArgument(loginUserMap);
            if (selectUserResult.getCode() != Status.SUCCESS.getCode()) {
                logger.error("user not exist,  user id {}", loginUserMap.get("id"));
                return selectUserResult;
            }
            User loginUser = (User) selectUserResult.getData();

            Integer userId = dataFetchingEnvironment.getArgument("userId");
            String expireTime = dataFetchingEnvironment.getArgument("expireTime");

            logger.info("login user {}, generate token , userId : {} , token expire time : {}", loginUser, userId, expireTime);

            Map<String, Object> result = accessTokenService.generateToken(loginUser, userId, expireTime);
            return returnDataList(result);
        };
    }

    public DataFetcher<Result> dataFetchersQueryTypeQueryAccessTokenList() {
        return dataFetchingEnvironment -> {
            LinkedHashMap<String, String> loginUserMap = dataFetchingEnvironment.getArgument("loginUser");
            Result selectUserResult = userArgumentService.getUserFromArgument(loginUserMap);
            if (selectUserResult.getCode() != Status.SUCCESS.getCode()) {
                logger.error("user not exist,  user id {}", loginUserMap.get("id"));
                return selectUserResult;
            }
            User loginUser = (User) selectUserResult.getData();

            Integer pageNo = dataFetchingEnvironment.getArgument("pageNo");
            String searchVal = dataFetchingEnvironment.getArgument("searchVal");
            Integer pageSize = dataFetchingEnvironment.getArgument("pageSize");

            logger.info("login user {}, list access token paging, pageNo: {}, searchVal: {}, pageSize: {}",
                    loginUser.getUserName(), pageNo, searchVal, pageSize);

            Map<String, Object> result = checkPageParams(pageNo, pageSize);
            if (result.get(Constants.STATUS) != Status.SUCCESS) {
                return returnDataListPaging(result);
            }
            searchVal = ParameterUtils.handleEscapes(searchVal);
            result = accessTokenService.queryAccessTokenList(loginUser, searchVal, pageNo, pageSize);
            return returnDataListPaging(result);
        };
    }


    public DataFetcher<Result> dataFetchersMutationTypeDelAccessTokenById() {
        return dataFetchingEnvironment -> {
            LinkedHashMap<String, String> loginUserMap = dataFetchingEnvironment.getArgument("loginUser");
            Result selectUserResult = userArgumentService.getUserFromArgument(loginUserMap);
            if (selectUserResult.getCode() != Status.SUCCESS.getCode()) {
                logger.error("user not exist,  user id {}", loginUserMap.get("id"));
                return selectUserResult;
            }
            User loginUser = (User) selectUserResult.getData();

            int id = dataFetchingEnvironment.getArgument("id");

            logger.info("login user {}, delete access token, id: {},", loginUser.getUserName(), id);
            Map<String, Object> result = accessTokenService.delAccessTokenById(loginUser, id);
            return returnDataList(result);
        };
    }


    public DataFetcher<Result> dataFetchersMutationTypeUpdateToken() {
        return dataFetchingEnvironment -> {
            LinkedHashMap<String, String> loginUserMap = dataFetchingEnvironment.getArgument("loginUser");

            Result selectUserResult = userArgumentService.getUserFromArgument(loginUserMap);

            if (selectUserResult.getCode() != Status.SUCCESS.getCode()) {
                logger.error("user not exist,  user id {}", loginUserMap.get("id"));
                return selectUserResult;
            }

            User loginUser = (User) selectUserResult.getData();

            int id = dataFetchingEnvironment.getArgument("id");
            int userId = dataFetchingEnvironment.getArgument("userId");
            String expireTime = dataFetchingEnvironment.getArgument("expireTime");
            String token = dataFetchingEnvironment.getArgument("token");

            logger.info("login user {}, update token , userId : {} , token expire time : {} , token : {}", loginUser.getUserName(),
                    userId, expireTime, token);

            Map<String, Object> result = accessTokenService.updateToken(loginUser, id, userId, expireTime, token);
            return returnDataList(result);
        };
    }

}
