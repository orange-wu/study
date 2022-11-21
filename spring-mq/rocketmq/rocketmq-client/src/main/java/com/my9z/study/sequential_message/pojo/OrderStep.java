package com.my9z.study.sequential_message.pojo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 模拟订单创建步骤
 * @author: wczy9
 * @createTime: 2022-11-21  23:25
 */
@Data
public class OrderStep {

    private Long orderId;
    private String desc;

    /**
     * 生成模拟订单数据
     */
    public List<OrderStep> buildOrders() {
        List<OrderStep> orderList = new ArrayList<>();
        Long orderIdA = 15103111039L;
        Long orderIdB = 15103111065L;
        Long orderIdC = 15103117235L;

        OrderStep orderDemo = new OrderStep();
        orderDemo.setOrderId(orderIdA);
        orderDemo.setDesc("创建");
        orderList.add(orderDemo);

        orderDemo = new OrderStep();
        orderDemo.setOrderId(orderIdB);
        orderDemo.setDesc("创建");
        orderList.add(orderDemo);

        orderDemo = new OrderStep();
        orderDemo.setOrderId(orderIdA);
        orderDemo.setDesc("付款");
        orderList.add(orderDemo);

        orderDemo = new OrderStep();
        orderDemo.setOrderId(orderIdC);
        orderDemo.setDesc("创建");
        orderList.add(orderDemo);

        orderDemo = new OrderStep();
        orderDemo.setOrderId(orderIdB);
        orderDemo.setDesc("付款");
        orderList.add(orderDemo);

        orderDemo = new OrderStep();
        orderDemo.setOrderId(orderIdC);
        orderDemo.setDesc("付款");
        orderList.add(orderDemo);

        orderDemo = new OrderStep();
        orderDemo.setOrderId(orderIdB);
        orderDemo.setDesc("完成");
        orderList.add(orderDemo);

        orderDemo = new OrderStep();
        orderDemo.setOrderId(orderIdA);
        orderDemo.setDesc("推送");
        orderList.add(orderDemo);

        orderDemo = new OrderStep();
        orderDemo.setOrderId(orderIdC);
        orderDemo.setDesc("完成");
        orderList.add(orderDemo);

        orderDemo = new OrderStep();
        orderDemo.setOrderId(orderIdA);
        orderDemo.setDesc("完成");
        orderList.add(orderDemo);

        return orderList;
    }
}