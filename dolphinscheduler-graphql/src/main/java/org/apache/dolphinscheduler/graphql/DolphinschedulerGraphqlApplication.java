package org.apache.dolphinscheduler.graphql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"org.apache.dolphinscheduler.dao","org.apache.dolphinscheduler.graphql"})
public class DolphinschedulerGraphqlApplication {

    public static void main(String[] args) {
        SpringApplication.run(DolphinschedulerGraphqlApplication.class, args);
    }

}
