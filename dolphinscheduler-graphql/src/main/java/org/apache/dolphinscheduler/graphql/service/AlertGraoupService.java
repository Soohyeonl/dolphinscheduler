package org.apache.dolphinscheduler.graphql.service;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.apache.dolphinscheduler.api.utils.PageInfo;
import org.apache.dolphinscheduler.dao.entity.AlertGroup;
import org.apache.dolphinscheduler.graphql.service.datafetcher.AlertGroupDataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

@Service
public class AlertGraoupService {

    @Value("classpath:schema/AlertGroup.graphqls")
    Resource resource;

    private GraphQL graphQL;

    @Autowired
    AlertGroupDataFetcher alertGroupDataFetcher;

    @Bean
    public GraphQL graphQL() {
        return graphQL;
    }

    @PostConstruct
    public void init() throws IOException {
        // get the schema
        File schemaFile = resource.getFile();
        // parse schema
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(schemaFile);
        RuntimeWiring wiring = buildWiring();
        GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(typeRegistry, wiring);
        graphQL = GraphQL.newGraphQL(schema).build();
    }

    private RuntimeWiring buildWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type(newTypeWiring("Query")
                        .dataFetcher("queryAllGroupList", alertGroupDataFetcher.getQueryAllGroupListDataFetcher())
                        .dataFetcher("queryAlertGroupListPaging", alertGroupDataFetcher.getQueryAlertGroupListPagingDataFetcher()))
                .type(newTypeWiring("Mutation")
                        .dataFetcher("createAlertGroup", alertGroupDataFetcher.getCreateAlertGroupDataFetcher()))
                .type(newTypeWiring("PageInfo")
                        .dataFetcher("total", environment -> {
                            PageInfo<AlertGroup> pageInfo = environment.getSource();
                            return pageInfo.getTotalCount();
                        })
//                        .dataFetcher("totalPage", environment -> {
//                            PageInfo<AlertGroup> pageInfo = environment.getSource();
//                            return pageInfo.getTotalPage();
//                        })
                )
//                .type(newTypeWiring("AlertGroup")
//                        .dataFetcher("groupType", alertGroupDataFecher.getAlertTypeDataFetcher()))
                .build();
    }
}
