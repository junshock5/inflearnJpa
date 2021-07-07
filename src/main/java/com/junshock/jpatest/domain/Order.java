package com.junshock.jpatest.domain;

import lombok.Getter;
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
    public void setMember(Member member){
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }
}
