package com.junshock.jpatest.domain.dto;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable // 코드 가독성 , jpa Entity 안의 column 하나의 객체로 사용
@Getter
public class Address {

    private String city;
    private String street;
    private String zipcode;

    // JPA 스펙상 엔티티나 임데디드타입은 자바 기본생성자를 public or protected로 설정해야한다.
    // 이유는 JPA 구현 라이브러리가 객체를 생성할 때 리플렉션 같은 기술을 사용하게 지원해야 한다.
    protected Address() {
    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
