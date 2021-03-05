package com.offcn.user.service;

import com.offcn.user.po.TMember;
import com.offcn.user.po.TMemberAddress;

import java.util.List;

public interface UserService {
    void registUser(TMember tMember);
    /*
    * 登录
    * @username
    * password
    * */
    TMember login(String username,String password);
    //根据ID查询
    TMember findMemberById(Integer id);
    /**
     * 获取用户收货地址
     * @param memberId
     * @return
     */
    List<TMemberAddress>  findAddressList(Integer memberId);
}
