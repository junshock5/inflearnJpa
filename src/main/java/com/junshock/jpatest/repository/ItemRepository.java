package com.junshock.jpatest.repository;

import com.junshock.jpatest.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item) {
        if (item.getId() == null) { // 새로 생성된 객체
            em.persist(item);
        } else { // 이미 db에 있고 update 의미, 변경 감지를 자동화.
            // 준영속 엔티티 병합 동작 방식
            // 1. merge() 실행.
            // 2. 파라미터로 넘어온 준영속 엔티티 식별자 값으로 1차 캐시에 엔티티 조회.
            // 3. 조회한 영속 엔티티(item) 엔티티의 값을 채워 넣는다.(item 엔티티 모든값을 mergeItem에 넣는다.
            //    이때 mergeItem의 "아이템1"이라는 이름이 "아이템명변경' 으로 바뀐다.
            // 4. 영속상태 item 객체 반환.

            // 변경감지와 다른점: 변경감지는 원하는 속성만 변경 가능, 병합에는 모든속성 변경됨.(db에 null값으로 변경될 가능성이 있음)
            
            Item merge = em.merge(item); // 왼쪽이 jpa가 관리, 오른쪽은 준영속객체.
        }
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
}
