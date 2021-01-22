package com.junshock.jpatest.api;

import com.junshock.jpatest.domain.dto.Address;
import com.junshock.jpatest.domain.dto.OrderStatus;
import com.junshock.jpatest.domain.order.Order;
import com.junshock.jpatest.repository.OrderRepository;
import com.junshock.jpatest.repository.OrderSearch;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
        for (Order order : all) {
            order.getMember().getName(); //Lazy 강제 초기화
            order.getDelivery().getAddress(); //Lazy 강제 초기화
        }

        return all;
    }

    // Lazy 로딩으로 인한 db n+1 쿼리 문제 코드
    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2() {
        //ORDER 2개
        //N + 1: order->member , order->delivery 회원 n개를 가져 올때 배송 N번이 추가 쿼리가 발생한다.
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());

        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());

        return result;
    }

    // Lazy 로딩으로 인한 db n+1 쿼리 문제 해결 코드 (fetch join 객체 그래프 사용)
    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3() {
        //fetch 를 LAZY로 변경 후 fetch join을 이용해서 해결하자.
        List<Order> orders = orderRepository.findAllWithMemberDelivery();

        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());

        return result;
    }

    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName(); //LAZY 초기화
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress(); //LAZY 초기화
        }

    }
}
