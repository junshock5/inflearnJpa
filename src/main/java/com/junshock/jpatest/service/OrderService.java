package com.junshock.jpatest.service;

import com.junshock.jpatest.domain.Delivery;
import com.junshock.jpatest.domain.Member;
import com.junshock.jpatest.domain.order.Order;
import com.junshock.jpatest.domain.order.OrderItem;
import com.junshock.jpatest.domain.item.Item;
import com.junshock.jpatest.repository.ItemRepository;
import com.junshock.jpatest.repository.MemberRepository;
import com.junshock.jpatest.repository.OrderRepository;
import com.junshock.jpatest.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor // Lombok 으로 repo 의존성 주입
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
    */
    @Transactional
    public Long order(Long memberId, Long itemId, int count){

        // 엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        // 주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item,item.getPrice(), count);

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문 저장
        // Order 관계 범위 cascade = CascadeType.ALL 옵션으로 전부.
        // save overriding (Lifecycle 간의 다른곳이 사용하지 않고 상속관계만 사용)
        orderRepository.save(order);

        // Entity에 비즈니스 로직을 몰아넣는 패턴 == 도메인 모델 패턴(DDD)
        // 서비스 계층은 단순히 엔티티에 필요한 요청을 위임하는 역할을 한다. (유지보수 편리 고민 필요) VS 스크립트 트랜잭션 패턴

        return order.getId();
    }

    /**
     * 주문 취소
     */
    @Transactional
    public void cancelOrder(Long orderId){
        // 주문 엔터티 조회
        Order order = orderRepository.findOne(orderId);
        // 주문 취소
        order.cancel();
        // jpa를 사용하지 않으면 update query 로 변경된 값 수정해야한다.
        // jpa 사용시 dirty checking 으로 변경된 값을 데이터만 변경해도 db 수정 가능.
    }

    /**
     * 검색
     * @param orderSearch
     * @return
     */
    public List<Order> findOrders(OrderSearch orderSearch){
        return orderRepository.findAllByString(orderSearch);
    }
}
