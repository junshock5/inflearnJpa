package com.junshock.jpatest.repository;

import com.junshock.jpatest.domain.dto.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderSearch {

    private String memberName;
    private OrderStatus orderStatus; //주문 상태(ORDER, CANCEL)
}
