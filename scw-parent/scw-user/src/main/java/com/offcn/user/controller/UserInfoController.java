package com.offcn.user.controller;

import com.offcn.dycommon.response.AppResponse;
import com.offcn.user.po.TMemberAddress;
import com.offcn.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
@Api(tags = "用户信息")
@Slf4j
public class UserInfoController {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserService userService;
    @ApiOperation(value = "获取用户收货地址")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(value = "访问令牌", name = "accessToken", required = true)
    })
    @GetMapping("/findAddressList")
    public AppResponse findAddressList(String accessToken){
        String memberIdString = redisTemplate.opsForValue().get(accessToken);
        if (StringUtils.isEmpty(memberIdString)){
            return AppResponse.fail("请登录");
        }
        //2.查询该会员收货地址列表
        List<TMemberAddress> addressList = userService.findAddressList(Integer.parseInt(memberIdString));
        return AppResponse.ok(addressList);
    }
}
