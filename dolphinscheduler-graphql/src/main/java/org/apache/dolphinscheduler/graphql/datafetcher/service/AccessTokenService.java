package org.apache.dolphinscheduler.graphql.datafetcher.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.dolphinscheduler.api.enums.Status;
import org.apache.dolphinscheduler.api.utils.PageInfo;
import org.apache.dolphinscheduler.api.utils.Result;
import org.apache.dolphinscheduler.common.enums.UserType;
import org.apache.dolphinscheduler.common.utils.DateUtils;
import org.apache.dolphinscheduler.common.utils.EncryptionUtils;
import org.apache.dolphinscheduler.dao.entity.AccessToken;
import org.apache.dolphinscheduler.dao.entity.User;
import org.apache.dolphinscheduler.dao.mapper.AccessTokenMapper;
import org.apache.dolphinscheduler.dao.mapper.UserMapper;
import org.apache.dolphinscheduler.graphql.utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedHashMap;

@Service
public class AccessTokenService {

    private static final Logger logger = LoggerFactory.getLogger(AccessTokenService.class);

    @Autowired
    private AccessTokenMapper accessTokenMapper;

    @Autowired
    private UserMapper userMapper;

    private boolean hasPerm(User operateUser, int createUserId){
        return operateUser.getId() == createUserId || isAdmin(operateUser);
    }

    private boolean isAdmin(User user) {
        return user.getUserType() == UserType.ADMIN_USER;
    }

    public Result createToken(User loginUser, int userId, String expireTime, String token) {
        Result result = new Result();

        if (!hasPerm(loginUser,userId)){
            ResultUtil.putStatus(result, Status.USER_NO_OPERATION_PERM);
            return result;
        }

        if (userId <= 0) {
            throw new IllegalArgumentException("User id should not less than or equals to 0.");
        }
        AccessToken accessToken = new AccessToken();
        accessToken.setUserId(userId);
        accessToken.setExpireTime(DateUtils.stringToDate(expireTime));
        accessToken.setToken(token);
        accessToken.setCreateTime(new Date());
        accessToken.setUpdateTime(new Date());

        // insert
        int insert = accessTokenMapper.insert(accessToken);

        if (insert > 0) {
            ResultUtil.putStatus(result, Status.SUCCESS);
        } else {
            ResultUtil.putStatus(result, Status.CREATE_ALERT_GROUP_ERROR);
        }

        return result;
    }

    public Result generateToken(User loginUser, int userId, String expireTime) {
        Result result = new Result();

        if (!hasPerm(loginUser,userId)){
            ResultUtil.putStatus(result, Status.USER_NO_OPERATION_PERM);
            return result;
        }

        String token = EncryptionUtils.getMd5(userId + expireTime + System.currentTimeMillis());

        result.setData(token);
        ResultUtil.putStatus(result, Status.SUCCESS);
        return result;
    }


    public Result queryAccessTokenList(User loginUser, String searchVal, Integer pageNo, Integer pageSize) {
        Result result = new Result();

        PageInfo<AccessToken> pageInfo = new PageInfo<>(pageNo, pageSize);
        Page<AccessToken> page = new Page<>(pageNo, pageSize);
        int userId = loginUser.getId();
        if (loginUser.getUserType() == UserType.ADMIN_USER) {
            userId = 0;
        }

        IPage<AccessToken> accessTokenIPage = accessTokenMapper.selectAccessTokenPage(page, searchVal, userId);
        pageInfo.setTotalCount((int) accessTokenIPage.getTotal());
        pageInfo.setLists(accessTokenIPage.getRecords());

        result.setData(pageInfo);
        ResultUtil.putStatus(result, Status.SUCCESS);
        return result;
    }

    public Result delAccessTokenById(User loginUser, int id) {
        Result result = new Result();

        AccessToken accessToken = accessTokenMapper.selectById(id);

        if (accessToken == null) {
            logger.error("access token not exist,  access token id {}", id);
            ResultUtil.putStatus(result, Status.ACCESS_TOKEN_NOT_EXIST);

            return result;
        }

        if (!hasPerm(loginUser, accessToken.getUserId())) {
            ResultUtil.putStatus(result, Status.USER_NO_OPERATION_PERM);
            return result;
        }

        accessTokenMapper.deleteById(id);
        ResultUtil.putStatus(result, Status.SUCCESS);

        return result;
    }

    public Result updateToken(User loginUser, int id, int userId, String expireTime, String token) {
        Result result = new Result();

        if (!hasPerm(loginUser,userId)){
            ResultUtil.putStatus(result, Status.USER_NO_OPERATION_PERM);
            return result;
        }

        AccessToken accessToken = accessTokenMapper.selectById(id);
        if (accessToken == null) {
            logger.error("access token not exist,  access token id {}", id);
            ResultUtil.putStatus(result, Status.ACCESS_TOKEN_NOT_EXIST);
            return result;
        }
        accessToken.setUserId(userId);
        accessToken.setExpireTime(DateUtils.stringToDate(expireTime));
        accessToken.setToken(token);
        accessToken.setUpdateTime(new Date());

        accessTokenMapper.updateById(accessToken);

        ResultUtil.putStatus(result, Status.SUCCESS);
        return result;
    }
}
