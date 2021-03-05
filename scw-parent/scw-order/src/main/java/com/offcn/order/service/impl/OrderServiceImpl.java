package com.offcn.order.service.impl;

import com.offcn.dycommon.enums.OrderStatusEnumes;
import com.offcn.dycommon.response.AppResponse;
import com.offcn.order.mapper.TOrderMapper;
import com.offcn.order.po.TOrder;
import com.offcn.order.service.OrderService;
import com.offcn.order.service.ProjectServiceFeign;
import com.offcn.order.vo.req.OrderInfoSubmitVo;
import com.offcn.order.vo.resp.TReturn;
import com.offcn.utils.AppDateUtils;
import io.swagger.models.auth.In;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private TOrderMapper orderMapper;

    @Autowired
    private ProjectServiceFeign projectServiceFeign;

    @Override
    public TOrder saveOrder(OrderInfoSubmitVo vo) {
        //1.创建订单对象
        TOrder order = new TOrder();
        //通过令牌获取用户I
        String memberIdString = redisTemplate.opsForValue().get(vo.getAccessToken());
        int memberId = Integer.parseInt(memberIdString);
        //设置订单的用户id
        order.setMemberid(memberId);
        BeanUtils.copyProperties(vo,order);
        //设置订单号
        String orderNum = UUID.randomUUID().toString().replace("-", "");
        order.setOrdernum(orderNum);
        //支付状态  未支付
        order.setStatus(OrderStatusEnumes.UNPAY.getCode() + "");
        //发票状态
        order.setInvoice(vo.getInvoice().toString());
        //创建时间
        order.setCreatedate(AppDateUtils.getFormatTime());
        //3.服务远程调用  查询回报增量列表
        AppResponse<List<TReturn>> response = projectServiceFeign.getReturnList(vo.getProjectid());
        List<TReturn> returnList = response.getData();
        TReturn myReturn = null;
        for (TReturn tReturn : returnList) {
            if (tReturn.getId() == vo.getReturnid().intValue()){
                myReturn = tReturn;
                break;
            }
        }
        //支持金额  回报数量*回报支持金额+运费
        int money = order.getRtncount() * myReturn.getSupportmoney() + myReturn.getFreight();
        order.setMoney(money);
        orderMapper.insertSelective(order);

        return order;
    }
}
