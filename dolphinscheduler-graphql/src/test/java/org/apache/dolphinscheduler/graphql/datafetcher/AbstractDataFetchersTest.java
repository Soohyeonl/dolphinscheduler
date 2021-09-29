package org.apache.dolphinscheduler.graphql.datafetcher;

import org.apache.commons.lang.StringUtils;
import org.apache.dolphinscheduler.api.ApiApplicationServer;
import org.apache.dolphinscheduler.api.enums.Status;
import org.apache.dolphinscheduler.api.service.SessionService;
import org.apache.dolphinscheduler.api.utils.RegistryCenterUtils;
import org.apache.dolphinscheduler.common.Constants;
import org.apache.dolphinscheduler.common.enums.UserType;
import org.apache.dolphinscheduler.dao.entity.User;
import org.apache.dolphinscheduler.graphql.DolphinschedulerGraphqlApplication;
import org.apache.dolphinscheduler.service.registry.RegistryClient;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringRunner.class)
@SpringBootTest(classes = DolphinschedulerGraphqlApplication.class)
@PrepareForTest({ RegistryCenterUtils.class, RegistryClient.class })
@PowerMockIgnore({"javax.management.*"})
public class AbstractDataFetchersTest {

    public static final String SESSION_ID = "sessionId";

    protected MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private SessionService sessionService;

    protected User user;

    protected String sessionId;

    @Before
    public void setUp() {
        PowerMockito.suppress(PowerMockito.constructor(RegistryClient.class));
        PowerMockito.mockStatic(RegistryCenterUtils.class);

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        createSession();
    }

    @After
    public void after() throws Exception {
        sessionService.signOut("127.0.0.1", user);
    }

    private void createSession() {

        User loginUser = new User();
        loginUser.setId(1);
        loginUser.setUserType(UserType.GENERAL_USER);

        user = loginUser;

        String session = sessionService.createSession(loginUser, "127.0.0.1");
        sessionId = session;

        Assert.assertTrue(!StringUtils.isEmpty(session));
    }

    public Map<String, Object> success() {
        Map<String, Object> serviceResult = new HashMap<>();
        putMsg(serviceResult, Status.SUCCESS);
        return serviceResult;
    }

    public void putMsg(Map<String, Object> result, Status status, Object... statusParams) {
        result.put(Constants.STATUS, status);
        if (statusParams != null && statusParams.length > 0) {
            result.put(Constants.MSG, MessageFormat.format(status.getMsg(), statusParams));
        } else {
            result.put(Constants.MSG, status.getMsg());
        }
    }

    public String toJson(HashMap<String, String> paramsMap) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");

        stringBuilder.append("\"").append("query").append("\"").append(":");
        String s = paramsMap.get("query").replaceAll("\\n", "\\\\n").replaceAll("\\\"", "\\\\\\\"");
        stringBuilder.append("\"").append(s).append("\"");
        stringBuilder.append(",");

        stringBuilder.append("\"").append("variables").append("\"").append(":");
        stringBuilder.append(paramsMap.get("variables"));

        stringBuilder.append("}");

        return stringBuilder.toString();
    }
}