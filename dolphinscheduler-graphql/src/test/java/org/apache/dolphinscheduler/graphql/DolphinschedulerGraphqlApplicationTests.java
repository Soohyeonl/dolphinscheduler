package org.apache.dolphinscheduler.graphql;

import org.apache.dolphinscheduler.dao.mapper.AlertGroupMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback(true)
@ComponentScan(basePackages = {"org.apache.dolphinscheduler.dao","org.apache.dolphinscheduler.graphql"})
public class DolphinschedulerGraphqlApplicationTests {

    @Autowired
    AlertGroupMapper alertGroupMapper;

    @Test
    public void contextLoads() {
        alertGroupMapper.queryAllGroupList();
    }

}
