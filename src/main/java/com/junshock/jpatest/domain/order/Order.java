package com.junshock.jpatest.domain.order;

import com.junshock.jpatest.domain.Delivery;
import com.junshock.jpatest.domain.Member;
import com.junshock.jpatest.domain.dto.DeliveryStatus;
import com.junshock.jpatest.domain.dto.OrderStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
/*
@Table
명시 안할시(논리명 생성) default SpringPhysicalNamingStrategy, 엔터티의 필드명을 그대로 테이블 명으로 생성

설정시(물리명 적용) (엔터티 필드 -> 테이블 컬럼)
1. 카멜 케이스 -> 언더스코어로 변경(memberPoint -> member_point)
2. .(점) -> _(언더스코어)
3. 대문자 -> 소문자
*/
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 인스턴스화 방지
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = LAZY) // 사용자 : 주문, 1 : N
    @JoinColumn(name = "member_id") // foreign_key 갖는 객체를 연관관계 주인으로 정하기, 테이블은 1개, 객체 입장에선 2개 관리 포인트
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) // 상위 객체 저장 persist 시 하위 persist 전파 가능.
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate; // 주문 시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문 상태 (ORDER, CANCEL)

    //==연관관계 메서드== Order, Member 사이 원자적 동작되게//
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    // 생성 메서드
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    // 비즈니스 로직

    /**
     * 주문 취소
     */
    public void cancel() {
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalArgumentException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }

        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }

    }
    
    /**
     * 전체 주문 가격 조회
     *
     * @return
     */
    public int getTotalPrice() {
        return orderItems.stream()
                .mapToInt(OrderItem::getTotalPrice)
                .sum();
    }
//    stream 사용하지 않았을때
//    public int getTotalPrice(){
//        int totalPrice =0;
//        for (OrderItem orderItem: orderItems){
//            totalPrice += orderItem.getTotalPrice();
//        }
//        return totalPrice;
//    }
}
