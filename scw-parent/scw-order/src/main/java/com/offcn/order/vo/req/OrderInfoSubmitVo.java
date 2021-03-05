package com.offcn.order.vo.req;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class OrderInfoSubmitVo {
    private String accessToken;
    /*
    *项目ID
    * */
    private Integer projectid;
    /*
    * 回报ID
    * */
    private Integer returnid;
    /*
    * 回报数量
    * */
    private Integer rtncount;
    /*
    * 收货地址
    * */
    private String address;
    /*
    * 是否开发票 0 - 不开发票， 1 - 开发票
    * */
    private Byte invoice;
    /*
    * 发票名头
    * */
    private String invoictitle;
    /*
    *备注
    * */
    private String remark;
}
