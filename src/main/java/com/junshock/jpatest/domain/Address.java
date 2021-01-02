package com.junshock.jpatest.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable // 코드 가독성 , jpa Entity 안의 column 하나의 객체로 사용
@Getter
public class Address {

    private String city;
    private String street;
    private String zipcode;
}
