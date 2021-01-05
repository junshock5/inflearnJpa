package com.junshock.jpatest.service;

import com.junshock.jpatest.domain.Member;
import com.junshock.jpatest.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional // default test시 reapeatable하기 위해 지운다.
public class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager em;

    @Test
    @Rollback(false) //롤백시 db 데이터를 전부 비우기 떄문에 영속성 컨텍스트가 flush를 안한다.
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("junshock6");

        //when
        Long savedId = memberService.join(member);

        //then
        //em.flush(); //롤백을 하더라도 insert한다. 즉 영속성 컨텍스트가 db에 insert를 rollback하기전에 한다.
        assertEquals(member, memberRepository.find(savedId));
    }

    @Test(expected = IllegalArgumentException.class) //try-catch 문에서 정상 예외를 잡은 후 return 하기에 테스트 성공.
    public void 중복_회원_예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("jun1");

        Member member2 = new Member();
        member2.setName("jun1");

        //when
        memberService.join(member1);
        memberService.join(member2); //예외가 발생해야 한다.

        //then
        fail("예외가 발생해야 한다.");
    }

}