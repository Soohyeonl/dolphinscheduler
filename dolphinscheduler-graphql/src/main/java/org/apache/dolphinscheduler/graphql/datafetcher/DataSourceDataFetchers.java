package org.apache.dolphinscheduler.graphql.datafetcher;

import graphql.schema.DataFetcher;
import org.apache.dolphinscheduler.api.controller.BaseController;
import org.apache.dolphinscheduler.api.enums.Status;
import org.apache.dolphinscheduler.api.service.DataSourceService;
import org.apache.dolphinscheduler.api.utils.Result;
import org.apache.dolphinscheduler.common.Constants;
import org.apache.dolphinscheduler.common.enums.DbConnectType;
import org.apache.dolphinscheduler.common.enums.DbType;
import org.apache.dolphinscheduler.common.utils.CommonUtils;
import org.apache.dolphinscheduler.common.utils.ParameterUtils;
import org.apache.dolphinscheduler.dao.entity.DataSource;
import org.apache.dolphinscheduler.dao.entity.User;
import org.apache.dolphinscheduler.graphql.datafetcher.service.UserArgumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.apache.dolphinscheduler.api.enums.Status.*;

@Component
public class DataSourceDataFetchers extends BaseDataFetchers {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceDataFetchers.class);

    @Autowired
    private DataSourceService dataSourceService;

    @Autowired
    private UserArgumentService userArgumentService;

    public DataFetcher<Result> dataFetchersMutationTypeCreateDataSource() {
        return dataFetchingEnvironment -> {
            LinkedHashMap<String, String> loginUserMap = dataFetchingEnvironment.getArgument("loginUser");
            Result selectUserResult = userArgumentService.getUserFromArgument(loginUserMap);
            if (selectUserResult.getCode() != Status.SUCCESS.getCode()) {
                logger.error("user not exist,  user id {}", loginUserMap.get("id"));
                return selectUserResult;
            }
            User loginUser = (User) selectUserResult.getData();

            String name = dataFetchingEnvironment.getArgument("name");
            String note = dataFetchingEnvironment.getArgument("note");
            DbType type = DbType.valueOf(dataFetchingEnvironment.getArgument("dbType"));
            String host = dataFetchingEnvironment.getArgument("host");
            String port = dataFetchingEnvironment.getArgument("port");
            String database = dataFetchingEnvironment.getArgument("database");
            String principal = dataFetchingEnvironment.getArgument("principal");
            String userName = dataFetchingEnvironment.getArgument("userName");
            String password = dataFetchingEnvironment.getArgument("password");
            DbConnectType connectType = DbConnectType.valueOf(dataFetchingEnvironment.getArgument("connectType"));
            String other = dataFetchingEnvironment.getArgument("other");

            logger.info("login user {} create datasource name: {}, note: {}, type: {}, host: {}, port: {}, database : {}, principal: {}, userName : {}, connectType: {}, other: {}",
                    loginUser.getUserName(), name, note, type, host, port, database, principal, userName, connectType, other);
            String parameter = dataSourceService.buildParameter(name, note, type, host, port, database, principal, userName, password, connectType, other);
            Map<String, Object> result = dataSourceService.createDataSource(loginUser, name, note, type, parameter);
            return returnDataList(result);
        };
    }

    public DataFetcher<Result> dataFetchersMutationTypeUpdateDataSource() {
        return dataFetchingEnvironment -> {
            LinkedHashMap<String, String> loginUserMap = dataFetchingEnvironment.getArgument("loginUser");
            Result selectUserResult = userArgumentService.getUserFromArgument(loginUserMap);
            if (selectUserResult.getCode() != Status.SUCCESS.getCode()) {
                logger.error("user not exist,  user id {}", loginUserMap.get("id"));
                return selectUserResult;
            }
            User loginUser = (User) selectUserResult.getData();

            int id = dataFetchingEnvironment.getArgument("id");
            String name = dataFetchingEnvironment.getArgument("name");
            String note = dataFetchingEnvironment.getArgument("note");
            DbType type = DbType.valueOf(dataFetchingEnvironment.getArgument("dbType"));
            String host = dataFetchingEnvironment.getArgument("host");
            String port = dataFetchingEnvironment.getArgument("port");
            String database = dataFetchingEnvironment.getArgument("database");
            String principal = dataFetchingEnvironment.getArgument("principal");
            String userName = dataFetchingEnvironment.getArgument("userName");
            String password = dataFetchingEnvironment.getArgument("password");
            DbConnectType connectType = DbConnectType.valueOf(dataFetchingEnvironment.getArgument("connectType"));
            String other = dataFetchingEnvironment.getArgument("other");

            logger.info("login user {} updateProcessInstance datasource name: {}, note: {}, type: {}, connectType: {}, other: {}",
                    loginUser.getUserName(), name, note, type, connectType, other);
            String parameter = dataSourceService.buildParameter(name, note, type, host, port, database, principal, userName, password, connectType, other);
            Map<String, Object> dataSource = dataSourceService.updateDataSource(id, loginUser, name, note, type, parameter);
            return returnDataList(dataSource);
        };
    }

    public DataFetcher<Result> dataFetchersQueryTypeQueryDataSource() {
        return dataFetchingEnvironment -> {
            LinkedHashMap<String, String> loginUserMap = dataFetchingEnvironment.getArgument("loginUser");
            Result selectUserResult = userArgumentService.getUserFromArgument(loginUserMap);
            if (selectUserResult.getCode() != Status.SUCCESS.getCode()) {
                logger.error("user not exist,  user id {}", loginUserMap.get("id"));
                return selectUserResult;
            }
            User loginUser = (User) selectUserResult.getData();

            int id = dataFetchingEnvironment.getArgument("id");

            logger.info("login user {}, query datasource: {}",
                    loginUser.getUserName(), id);
            Map<String, Object> result = dataSourceService.queryDataSource(id);
            return returnDataList(result);
        };
    }

    public DataFetcher<DbType> dataFetchersQueryDataSourceTypeDbType() {
        return dataFetchingEnvironment -> {
            Map<String, Object> map = dataFetchingEnvironment.getSource();

            return DbType.valueOf((String) map.get("type"));
        };
    }

    public DataFetcher<Result> dataFetchersQueryTypeQueryDataSourceList() {
        return dataFetchingEnvironment -> {
            LinkedHashMap<String, String> loginUserMap = dataFetchingEnvironment.getArgument("loginUser");
            Result selectUserResult = userArgumentService.getUserFromArgument(loginUserMap);
            if (selectUserResult.getCode() != Status.SUCCESS.getCode()) {
                logger.error("user not exist,  user id {}", loginUserMap.get("id"));
                return selectUserResult;
            }
            User loginUser = (User) selectUserResult.getData();

            DbType type = DbType.valueOf(dataFetchingEnvironment.getArgument("dbType"));

            Map<String, Object> result = dataSourceService.queryDataSourceList(loginUser, type.ordinal());
            return returnDataList(result);
        };
    }

    public DataFetcher<DbType> dataFetchersDataSourceTypeDbType() {
        return dataFetchingEnvironment -> {
            DataSource dataSource = dataFetchingEnvironment.getSource();

            return dataSource.getType();
        };
    }

    public DataFetcher<Result> dataFetchersQueryTypeQueryDataSourceListPaging() {
        return dataFetchingEnvironment -> {
            LinkedHashMap<String, String> loginUserMap = dataFetchingEnvironment.getArgument("loginUser");
            Result selectUserResult = userArgumentService.getUserFromArgument(loginUserMap);
            if (selectUserResult.getCode() != Status.SUCCESS.getCode()) {
                logger.error("user not exist,  user id {}", loginUserMap.get("id"));
                return selectUserResult;
            }
            User loginUser = (User) selectUserResult.getData();

            String searchVal = dataFetchingEnvironment.getArgument("searchVal");
            int pageNo = dataFetchingEnvironment.getArgument("pageNo");
            int pageSize = dataFetchingEnvironment.getArgument("pageSize");

            Map<String, Object> result = checkPageParams(pageNo, pageSize);
            if (result.get(Constants.STATUS) != Status.SUCCESS) {
                return returnDataListPaging(result);
            }
            searchVal = ParameterUtils.handleEscapes(searchVal);
            result = dataSourceService.queryDataSourceListPaging(loginUser, searchVal, pageNo, pageSize);
            return returnDataListPaging(result);
        };
    }

    public DataFetcher<Result> dataFetchersQueryTypeConnectDataSource() {
        return dataFetchingEnvironment -> {
            LinkedHashMap<String, String> loginUserMap = dataFetchingEnvironment.getArgument("loginUser");
            Result selectUserResult = userArgumentService.getUserFromArgument(loginUserMap);
            if (selectUserResult.getCode() != Status.SUCCESS.getCode()) {
                logger.error("user not exist,  user id {}", loginUserMap.get("id"));
                return selectUserResult;
            }
            User loginUser = (User) selectUserResult.getData();

            String name = dataFetchingEnvironment.getArgument("name");
            String note = dataFetchingEnvironment.getArgument("note");
            DbType type = DbType.valueOf(dataFetchingEnvironment.getArgument("dbType"));
            String host = dataFetchingEnvironment.getArgument("host");
            String port = dataFetchingEnvironment.getArgument("port");
            String database = dataFetchingEnvironment.getArgument("database");
            String principal = dataFetchingEnvironment.getArgument("principal");
            String userName = dataFetchingEnvironment.getArgument("userName");
            String password = dataFetchingEnvironment.getArgument("password");
            DbConnectType connectType = DbConnectType.valueOf(dataFetchingEnvironment.getArgument("connectType"));
            String other = dataFetchingEnvironment.getArgument("other");

            logger.info("login user {}, connect datasource: {}, note: {}, type: {}, connectType: {}, other: {}",
                    loginUser.getUserName(), name, note, type, connectType, other);
            String parameter = dataSourceService.buildParameter(name, note, type, host, port, database, principal, userName, password, connectType, other);
            Boolean isConnection = dataSourceService.checkConnection(type, parameter);
            Result result = new Result();

            if (isConnection) {
                putMsg(result, SUCCESS);
            } else {
                putMsg(result, CONNECT_DATASOURCE_FAILURE);
            }
            return result;
        };
    }

    public DataFetcher<Result> dataFetchersQueryTypeConnectionTest() {
        return dataFetchingEnvironment -> {
            LinkedHashMap<String, String> loginUserMap = dataFetchingEnvironment.getArgument("loginUser");
            Result selectUserResult = userArgumentService.getUserFromArgument(loginUserMap);
            if (selectUserResult.getCode() != Status.SUCCESS.getCode()) {
                logger.error("user not exist,  user id {}", loginUserMap.get("id"));
                return selectUserResult;
            }
            User loginUser = (User) selectUserResult.getData();

            int id = dataFetchingEnvironment.getArgument("id");

            logger.info("connection test, login user:{}, id:{}", loginUser.getUserName(), id);

            Boolean isConnection = dataSourceService.connectionTest(loginUser, id);
            Result result = new Result();

            if (isConnection) {
                putMsg(result, SUCCESS);
            } else {
                putMsg(result, CONNECTION_TEST_FAILURE);
            }
            return result;
        };
    }

    public DataFetcher<Result> dataFetchersMutationTypeDeleteDataSource() {
        return dataFetchingEnvironment -> {
            LinkedHashMap<String, String> loginUserMap = dataFetchingEnvironment.getArgument("loginUser");
            Result selectUserResult = userArgumentService.getUserFromArgument(loginUserMap);
            if (selectUserResult.getCode() != Status.SUCCESS.getCode()) {
                logger.error("user not exist,  user id {}", loginUserMap.get("id"));
                return selectUserResult;
            }
            User loginUser = (User) selectUserResult.getData();

            int id = dataFetchingEnvironment.getArgument("id");

            logger.info("delete datasource,login user:{}, id:{}", loginUser.getUserName(), id);
            return dataSourceService.delete(loginUser, id);
        };
    }

    public DataFetcher<Result> dataFetchersQueryTypeVerifyDataSourceName() {
        return dataFetchingEnvironment -> {
            LinkedHashMap<String, String> loginUserMap = dataFetchingEnvironment.getArgument("loginUser");
            Result selectUserResult = userArgumentService.getUserFromArgument(loginUserMap);
            if (selectUserResult.getCode() != Status.SUCCESS.getCode()) {
                logger.error("user not exist,  user id {}", loginUserMap.get("id"));
                return selectUserResult;
            }
            User loginUser = (User) selectUserResult.getData();

            String name = dataFetchingEnvironment.getArgument("name");

            logger.info("login user {}, verfiy datasource name: {}",
                    loginUser.getUserName(), name);

            return dataSourceService.verifyDataSourceName(loginUser, name);
        };
    }

    public DataFetcher<Result> dataFetchersQueryTypeUnauthDatasource() {
        return dataFetchingEnvironment -> {
            LinkedHashMap<String, String> loginUserMap = dataFetchingEnvironment.getArgument("loginUser");
            Result selectUserResult = userArgumentService.getUserFromArgument(loginUserMap);
            if (selectUserResult.getCode() != Status.SUCCESS.getCode()) {
                logger.error("user not exist,  user id {}", loginUserMap.get("id"));
                return selectUserResult;
            }
            User loginUser = (User) selectUserResult.getData();

            int userId = dataFetchingEnvironment.getArgument("userId");

            logger.info("unauthorized datasource, login user:{}, unauthorized userId:{}",
                    loginUser.getUserName(), userId);
            Map<String, Object> result = dataSourceService.unauthDatasource(loginUser, userId);
            return returnDataList(result);
        };
    }

    public DataFetcher<Result> dataFetchersQueryTypeAuthedDatasource() {
        return dataFetchingEnvironment -> {
            LinkedHashMap<String, String> loginUserMap = dataFetchingEnvironment.getArgument("loginUser");
            Result selectUserResult = userArgumentService.getUserFromArgument(loginUserMap);
            if (selectUserResult.getCode() != Status.SUCCESS.getCode()) {
                logger.error("user not exist,  user id {}", loginUserMap.get("id"));
                return selectUserResult;
            }
            User loginUser = (User) selectUserResult.getData();

            int userId = dataFetchingEnvironment.getArgument("userId");

            logger.info("authorized data source, login user:{}, authorized useId:{}",
                    loginUser.getUserName(), userId);
            Map<String, Object> result = dataSourceService.authedDatasource(loginUser, userId);
            return returnDataList(result);
        };
    }

    public DataFetcher<Result> dataFetchersQueryTypeGetKerberosStartupState() {
        return dataFetchingEnvironment -> {
            LinkedHashMap<String, String> loginUserMap = dataFetchingEnvironment.getArgument("loginUser");
            Result selectUserResult = userArgumentService.getUserFromArgument(loginUserMap);
            if (selectUserResult.getCode() != Status.SUCCESS.getCode()) {
                logger.error("user not exist,  user id {}", loginUserMap.get("id"));
                return selectUserResult;
            }
            User loginUser = (User) selectUserResult.getData();

            logger.info("login user {}", loginUser.getUserName());
            // if upload resource is HDFS and kerberos startup is true , else false
            return success(Status.SUCCESS.getMsg(), CommonUtils.getKerberosStartupState());
        };
    }

}
