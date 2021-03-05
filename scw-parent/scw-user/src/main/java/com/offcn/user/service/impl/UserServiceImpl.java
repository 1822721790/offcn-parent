package com.offcn.user.service.impl;

import com.offcn.user.enums.UserExceptionEnum;
import com.offcn.user.exception.UserException;
import com.offcn.user.mapper.TMemberAddressMapper;
import com.offcn.user.mapper.TMemberMapper;
import com.offcn.user.po.TMember;
import com.offcn.user.po.TMemberAddress;
import com.offcn.user.po.TMemberAddressExample;
import com.offcn.user.po.TMemberExample;
import com.offcn.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private TMemberMapper memberMapper;
    @Autowired
    private TMemberAddressMapper memberAddressMapper;
    
    public void registUser(TMember tMember) {
        TMemberExample example = new TMemberExample();
        example.createCriteria().andLoginacctEqualTo(tMember.getLoginacct());
        long l = memberMapper.countByExample(example);
        if (l > 0){
            throw new UserException(UserExceptionEnum.LOGINACCT_EXIST);
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encode = encoder.encode(tMember.getUserpswd());
        tMember.setUserpswd(encode);
        tMember.setAuthstatus("0");
        tMember.setUsertype("0");
        tMember.setAccttype("2");
        tMember.setUsername(tMember.getLoginacct());

        memberMapper.insertSelective(tMember);
        System.out.println("插入成功");
    }

    @Override
    public TMember login(String username, String password) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        TMemberExample example = new TMemberExample();
        example.createCriteria().andLoginacctEqualTo(username);
        List<TMember> list = memberMapper.selectByExample(example);
        if(list!=null && list.size()==1){
            TMember member = list.get(0);
            //密码加密
            boolean matches = encoder.matches(password, member.getUserpswd());
            return matches?member:null;
        }
        return null;
    }

    @Override
    public TMember findMemberById(Integer id) {

        return memberMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<TMemberAddress> findAddressList(Integer memberId) {
        TMemberAddressExample example = new TMemberAddressExample();
        TMemberAddressExample.Criteria criteria = example.createCriteria();
        criteria.andMemberidEqualTo(memberId);
        return memberAddressMapper.selectByExample(example);
    }
}
