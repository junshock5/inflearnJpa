package com.junshock.jpatest.repository;

import com.junshock.jpatest.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    public List<Order> findAll(OrderSearch orderSearch) {
        // 주문 상태 검색 동적 쿼리(가독성 헤침, 버그 발생 가능성 o)
//        String jpql = "select o from Order o join o.member m";
//        boolean isFirstCondition = true;
//        if (orderSearch.getOrderStatus() != null) {
//            if (isFirstCondition) {
//                jpql += " where";
//                isFirstCondition = false;
//            } else {
//                jpql += " and";
//            }
//            jpql += " o.status = :status";
//        }

        // Crireterai : 동적쿼리를 작성할떄 자동으로 만들어준다. 단, 유지보수x 쿼리가 안떠오름 jpql(실무 사용 x)
        // Querydsl : jpql 에서 오타날꺼를 없에줌.

        return em.createQuery("select o from Order o join o.member m" +
                "where o.status = :status" +
                "and m.name like :name", Order.class)
                .setParameter("status", orderSearch.getOrderStatus())
                .setParameter("name", orderSearch.getMemberName())
                .setMaxResults(1000)
                .getResultList();
    }

}
