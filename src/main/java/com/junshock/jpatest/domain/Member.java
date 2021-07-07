package com.junshock.jpatest.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/*
@Entity 어노테이션을 클래스에 선언하면 그 클래스는 JPA가 관리합니다.
        그러므로 DB의 테이블과 Class(VO, DTO)와 맵핑한다면 반드시 @Entity를 붙여주어야 합니다.
@Entity가 붙은 클래스에는 다음 제약사항이 필요합니다.
        1. 필드에 final, enum, interface, class를 사용할 수 없습니다.
        2. 생성자중 기본 생성자가 반드시 필요합니다.
@Entity의 속성
        1. name : 엔티티 이름을 지정합니다. 기본값으로 클래스 이름을 그대로 사용합니다.
*/
@Entity
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded // 내장  타입을 포함했다는 의미
    private Address address;

    @OneToMany(mappedBy = "member") // 하나의 회원이 여러개 주문을 갖는다. 1:N, 연관관계 참조 설정
    private List<Order> orders = new ArrayList<>(); // 바로 초기화하여 null 문제 안전, 하이버네이트 엔터티 영속시 내장 컬렉션 변경되기 떄문에 데이터 불일치 방지
}
