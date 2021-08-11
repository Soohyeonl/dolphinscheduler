package org.apache.dolphinscheduler.graphql.datafetcher;

import graphql.schema.DataFetcher;
import org.apache.dolphinscheduler.api.utils.PageInfo;
import org.apache.dolphinscheduler.common.Constants;
import org.apache.dolphinscheduler.dao.entity.AlertGroup;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class AlertGroupPageInfoDataFetchers {

    public DataFetcher<List<AlertGroup>> dataFetchersTotalLists() {
        return dataFetchingEnvironment -> {
            Map<String, Object> map = dataFetchingEnvironment.getSource();

            return (List<AlertGroup>) map.get(Constants.TOTAL_LIST);
        };
    }

}
