package org.apache.dolphinscheduler.graphql;

import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.TypeRuntimeWiring;
import org.apache.dolphinscheduler.api.utils.PageInfo;
import org.apache.dolphinscheduler.dao.entity.AlertGroup;
import org.apache.dolphinscheduler.graphql.datafetcher.AccessTokenDataFetchers;
import org.apache.dolphinscheduler.graphql.datafetcher.AlertGroupDataFetchers;
import org.apache.dolphinscheduler.graphql.datafetcher.DataAnalysisDataFetchers;
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

    protected RuntimeWiring buildWiring() {
        return RuntimeWiring.newRuntimeWiring()
                // Wiring every GraphQL type
                .type("Query", this::addWiringForQueryType)
                .type("Mutation", this::addWiringForMutationType)
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

        return typeWiring;
    }


    protected TypeRuntimeWiring.Builder addWiringForGroupPageInfo(TypeRuntimeWiring.Builder typeWiring) {
        typeWiring.dataFetcher("total", environment -> {
            PageInfo<AlertGroup> pageInfo = environment.getSource();
            return pageInfo.getTotalCount();
        });
        return typeWiring;
    }

}
