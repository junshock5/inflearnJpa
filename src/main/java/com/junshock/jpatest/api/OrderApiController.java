package com.junshock.jpatest.api;

import com.junshock.jpatest.domain.dto.Address;
import com.junshock.jpatest.domain.dto.OrderStatus;
import com.junshock.jpatest.domain.order.Order;
import com.junshock.jpatest.domain.order.OrderItem;
import com.junshock.jpatest.repository.OrderRepository;
import com.junshock.jpatest.repository.OrderSearch;
import com.junshock.jpatest.repository.order.query.OrderFlatDto;
import com.junshock.jpatest.repository.order.query.OrderQueryDto;
import com.junshock.jpatest.repository.order.query.OrderQueryRepository;
import com.junshock.jpatest.service.query.OrderQueryService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;
    private final OrderQueryService orderQueryService;

    // Entity를 직접 노출후 호출 하는 방식(확장성 면에서 유연 하지 못하다.)
    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName();
            order.getDelivery().getAddress();

            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.stream().forEach(o -> o.getItem().getName());
        }
        return all;
    }

    // DTO로 변환 해서 호출하는 방식, DTO안에 랩핑을 했기 떄문에 부분적 노출임.
    // DTO 안에 OrderItem 까지도 DTO를 만들어야 노출이 안되고 확장에 유연한 구조임.
    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<OrderDto> collect = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());

        return collect;
    }

    @GetMapping("/api/v3.0/orders")
    public List<com.junshock.jpatest.service.query.OrderDto> ordersV3_0() {
        return orderQueryService.ordersV3();
    }

    // 패치조인, distinct 적용하여 중복row를 가져오는 쿼리를 단일 row로 최적화
    // 패치조인 단점, db의 제약(limit, offset이 없다, appliying in memory 경고)으로 *페이징이 불가능하다.
    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithItem();

        for (Order order : orders) {
            // 객체 인스턴스의 참조값 까지 똑같은 중복 데이터 발생
            System.out.println("order ref=" + order + " id=" + order.getId());
        }

        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());

        return result;
    }

    // 네트워크 호출횟수, 전송하는 데이터양은 trade-off 관계
    // 단쿼리 경우 한방 패치 조인이 낫지만
    // 한번에 1000개씩 가져올땐 default_batch_fetch_size
    // 정규화된 db 조회가 나을수 있다. was, db 서버 부하, 메모리도 생각 해야한다.
    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> ordersV3_page(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "100") int limit) {
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);

        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());

        return result;
    }

    // findOrderQueryDtos 만든 이유는 controller의 OrderDto를 사용하고
    // repository 참조 하면 의존 관계 순환 오류 가 생기기 떄문에 방지하기 위해서
    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> ordersV4() {
        return orderQueryRepository.findOrderQueryDtos();
    }

    // 메모리 Map에 collection 들을 저장함으로써 쿼리 횟수를 줄인다.
    @GetMapping("/api/v5/orders")
    public List<OrderQueryDto> ordersV5() {
        return orderQueryRepository.findAllByDto_optimization();
    }

    // 단일쿼리로 가능하지만 페이징x
    @GetMapping("/api/v6/orders")
    public List<OrderFlatDto> ordersV6() {
        List<OrderFlatDto> flats = orderQueryRepository.findAllByDto_float();
        return flats;
    }

    @Getter
    static class OrderDto {

        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;

        public OrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
            orderItems = order.getOrderItems().stream()
                    .map(orderItem -> new OrderItemDto(orderItem))
                    .collect(Collectors.toList());
        }
    }

    @Getter
    static class OrderItemDto {

        private String itemName; //상품 명
        private int orderPrice; //주문 가격
        private int count; //주문 수량

        public OrderItemDto(OrderItem orderItem) {
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }

}
