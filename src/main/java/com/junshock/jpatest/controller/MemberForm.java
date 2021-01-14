package com.junshock.jpatest.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class MemberForm { // Member가 있는데 추가한 이유는 db valid과 view단 dto성 데이터와 분리 해야 코드 가독성 좋아짐.

    @NotEmpty(message = "회원 이름은 필수 입니다.")
    private String name;

    private String city;
    private String street;
    private String zipcode;
}
