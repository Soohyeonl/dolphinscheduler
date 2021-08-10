package org.apache.dolphinscheduler.graphql.datafetcher.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.dolphinscheduler.dao.entity.UserAlertGroup;
import org.apache.dolphinscheduler.dao.mapper.UserAlertGroupMapper;
import org.springframework.stereotype.Service;

@Service
public class UserAlertGroupService extends ServiceImpl<UserAlertGroupMapper, UserAlertGroup> {
}
