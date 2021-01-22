package com.junshock.jpatest.api;

import com.junshock.jpatest.domain.order.Order;
import com.junshock.jpatest.repository.OrderRepository;
import com.junshock.jpatest.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * xToOne(ManyToOne, OneToOne)
 * Order
 * Order -> Member
 * Order -> Delievery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;

    // 엔티티 직접 노출시 문제 발생
    // 1. 양방향 연관관계가 생겨 무한루프에 빠짐.. -> jsonIgnore
    // 2. Type definition Orders를 가지고 있는데 fetch = LAZY, db에 바로 가져오지 않는다.
    // hibernate가 proxyMember 멤버를 상속받아서 넣어놓는다. bytebuddy 인터셉터로 가짜로 넣어놓는데..
    // Jackson 라이브러리가 bytebuddy 멤버가아니고 어떻게 할수가 없다.. -> hibernate module 설치
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for(Order order: all){
            order.getMember().getName(); //Lazy 강제 초기화
            order.getDelivery().getAddress(); //Lazy 강제 초기화
        }

        return all;
    }
}
