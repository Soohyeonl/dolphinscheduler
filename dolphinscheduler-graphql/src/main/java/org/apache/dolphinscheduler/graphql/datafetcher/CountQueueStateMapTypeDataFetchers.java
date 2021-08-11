package org.apache.dolphinscheduler.graphql.datafetcher;

import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CountQueueStateMapTypeDataFetchers {

    public DataFetcher<Integer> dataFetchersTaskKill() {
        return dataFetchingEnvironment -> {
            Map<String, Object> map = dataFetchingEnvironment.getSource();

            return (Integer) map.get("taskKill");
        };
    }

    public DataFetcher<Integer> dataFetchersTaskQueue() {
        return dataFetchingEnvironment -> {
            Map<String, Object> map = dataFetchingEnvironment.getSource();

            return (Integer) map.get("taskQueue");
        };
    }

}
