package com.junshock.jpatest.repository.order.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager em;

    /**
     * 주문 조회
     * Query: 루트 1번, 컬렉션 N번 실행
     * ToOne(N:1:1:1)관계들을 먼저 조회하고, ToMnay(1:N) 관계는 각각 별도로 처리한다.
     * 이유는 ToOne관계는 조인해도 row수가 증가하지않는다.
     * ToMany(1:N)관계는 조인하면 row수가 증가한다.
     * row 수가 증가하지 않는 ToOne관계는 조인최적화하기 쉬워서 한번조회하고,
     * ToMany관계는 최적화하기 어려움으로 findOrderItems 메서드로 따로 조회한다.
     *
     * @return
     */
    public List<OrderQueryDto> findOrderQueryDtos() {
        List<OrderQueryDto> result = findOrders(); //query N번

        result.forEach(o -> {
            List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId()); //query N번
            o.setOrderItems(orderItems);
        });
        return result;
    }

    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery(
                "select new com.junshock.jpatest.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                        " from OrderItem oi" +
                        " join oi.item i" +
                        " where oi.order.id = : orderId", OrderItemQueryDto.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }

    public List<OrderQueryDto> findOrders() {
        return em.createQuery(
                "select new com.junshock.jpatest.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                        " from Order o" +
                        " join o.member m" +
                        " join o.delivery d", OrderQueryDto.class)
                .getResultList();
    }
}
