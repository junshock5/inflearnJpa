package com.junshock.jpatest;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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
    private Long id;
    private String username;
}
