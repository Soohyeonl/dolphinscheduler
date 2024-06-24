package org.apache.dolphinscheduler.graphql;

import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.TypeRuntimeWiring;
import org.apache.dolphinscheduler.api.utils.PageInfo;
import org.apache.dolphinscheduler.dao.entity.AlertGroup;
import org.apache.dolphinscheduler.graphql.datafetcher.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GraphQLWiring {

    @Autowired
    private AlertGroupDataFetchers alertGroupDataFetchers;

    @Autowired
    private AccessTokenDataFetchers accessTokenDataFetchers;

    @Autowired
    private DataAnalysisDataFetchers dataAnalysisDataFetchers;

    @Autowired
    private DataSourceDataFetchers dataSourceDataFetchers;


    protected RuntimeWiring buildWiring() {
        return RuntimeWiring.newRuntimeWiring()
                // Wiring every GraphQL type
                .type("Query", this::addWiringForQueryType)
                .type("Mutation", this::addWiringForMutationType)
                .type("QueryDataSourceType", this::addWiringForQueryDataSourceType)
                .type("DataSource", this::addWiringForDataSource)
                .build();
    } // buildWiring()


    protected TypeRuntimeWiring.Builder addWiringForQueryType(TypeRuntimeWiring.Builder typeWiring) {
        // AlertGroup GraphQL Query
        typeWiring.dataFetcher("queryAllGroupList", alertGroupDataFetchers.dataFetchersQueryTypeQueryAllGroupList());
        typeWiring.dataFetcher("queryAlertGroupListPaging", alertGroupDataFetchers.dataFetchersQueryTypeQueryAlertGroupListPaging());
        typeWiring.dataFetcher("verifyGroupName", alertGroupDataFetchers.dataFetchersQueryTypeVerifyGroupName());

        // AccessToken GraphQL Query
        typeWiring.dataFetcher("generateToken", accessTokenDataFetchers.dataFetchersQueryTypeGenerateToken());
        typeWiring.dataFetcher("queryAccessTokenList", accessTokenDataFetchers.dataFetchersQueryTypeQueryAccessTokenList());

        // DataAnalysis GraphQL Query
        typeWiring.dataFetcher("countTaskStateByProject", dataAnalysisDataFetchers.dataFetchersQueryTypeCountTaskStateByProject());
        typeWiring.dataFetcher("countProcessInstanceState", dataAnalysisDataFetchers.dataFetchersQueryTypeCountProcessInstanceState());
        typeWiring.dataFetcher("countDefinitionByUser", dataAnalysisDataFetchers.dataFetchersCountDefinitionByUser());
        typeWiring.dataFetcher("countCommandState", dataAnalysisDataFetchers.dataFetchersQueryTypeCountCommandState());
        typeWiring.dataFetcher("countQueueState", dataAnalysisDataFetchers.dataFetchersQueryTypeCountQueueState());

        // DataSource GraphQL Query
        typeWiring.dataFetcher("queryDataSource", dataSourceDataFetchers.dataFetchersQueryTypeQueryDataSource());
        typeWiring.dataFetcher("queryDataSourceList", dataSourceDataFetchers.dataFetchersQueryTypeQueryDataSourceList());
        typeWiring.dataFetcher("queryDataSourceListPaging", dataSourceDataFetchers.dataFetchersQueryTypeQueryDataSourceListPaging());
        typeWiring.dataFetcher("connectDataSource", dataSourceDataFetchers.dataFetchersQueryTypeConnectDataSource());
        typeWiring.dataFetcher("connectionTest", dataSourceDataFetchers.dataFetchersQueryTypeConnectionTest());
        typeWiring.dataFetcher("verifyDataSourceName", dataSourceDataFetchers.dataFetchersQueryTypeVerifyDataSourceName());
        typeWiring.dataFetcher("unauthDatasource", dataSourceDataFetchers.dataFetchersQueryTypeUnauthDatasource());
        typeWiring.dataFetcher("authedDatasource", dataSourceDataFetchers.dataFetchersQueryTypeAuthedDatasource());
        typeWiring.dataFetcher("getKerberosStartupState", dataSourceDataFetchers.dataFetchersQueryTypeGetKerberosStartupState());

        return typeWiring;
    }


    protected TypeRuntimeWiring.Builder addWiringForMutationType(TypeRuntimeWiring.Builder typeWiring) {
        // AccessToken GraphQL Mutation
        typeWiring.dataFetcher("createAlertGroup", alertGroupDataFetchers.dataFetchersMutationTypeCreateAlertGroup());
        typeWiring.dataFetcher("delAlertGroupById", alertGroupDataFetchers.dataFetchersMutationTypeDelAlertGroupById());
        typeWiring.dataFetcher("grantUser", alertGroupDataFetchers.dataFetchersMutationTypeGrantUser());
        typeWiring.dataFetcher("updateAlertGroup", alertGroupDataFetchers.dataFetchersMutationTypeUpdateAlertGroup());

        // AccessToken GraphQL Mutation
        typeWiring.dataFetcher("createToken", accessTokenDataFetchers.dataFetchersMutationTypeCreateToken());
        typeWiring.dataFetcher("delAccessTokenById", accessTokenDataFetchers.dataFetchersMutationTypeDelAccessTokenById());
        typeWiring.dataFetcher("updateToken", accessTokenDataFetchers.dataFetchersMutationTypeUpdateToken());

        // DataSource GraphQL Mutation
        typeWiring.dataFetcher("createDataSource", dataSourceDataFetchers.dataFetchersMutationTypeCreateDataSource());
        typeWiring.dataFetcher("updateDataSource", dataSourceDataFetchers.dataFetchersMutationTypeUpdateDataSource());
        typeWiring.dataFetcher("deleteDataSource", dataSourceDataFetchers.dataFetchersMutationTypeDeleteDataSource());

        return typeWiring;
    }


    protected TypeRuntimeWiring.Builder addWiringForGroupPageInfo(TypeRuntimeWiring.Builder typeWiring) {
        typeWiring.dataFetcher("total", environment -> {
            PageInfo<AlertGroup> pageInfo = environment.getSource();
            return pageInfo.getTotalCount();
        });
        return typeWiring;
    }

    protected TypeRuntimeWiring.Builder addWiringForDataSource(TypeRuntimeWiring.Builder typeWiring) {
        typeWiring.dataFetcher("dbType", dataSourceDataFetchers.dataFetchersDataSourceTypeDbType());
        return typeWiring;
    }

    protected TypeRuntimeWiring.Builder addWiringForQueryDataSourceType(TypeRuntimeWiring.Builder typeWiring) {
        typeWiring.dataFetcher("dbType", dataSourceDataFetchers.dataFetchersQueryDataSourceTypeDbType());
        return typeWiring;
    }

}
