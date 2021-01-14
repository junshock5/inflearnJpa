package com.junshock.jpatest.domain.order;

import com.junshock.jpatest.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 인스턴스화 방지
public class OrderItem {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; //주문 가격

    private int count; //주문 수량

    // 생성 메서드
    public static OrderItem createOrderItem(Item item, int orederPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orederPrice);
        orderItem.setCount(count);

        item.removeStock(count);
        return orderItem;
    }

    // 비즈니스 로직
    public void cancel() {
        getItem().addStock(count); // 재고를 다시 주문수량만큼 늘려준다. 재고 수량을 원복한다.
    }

    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }

}
