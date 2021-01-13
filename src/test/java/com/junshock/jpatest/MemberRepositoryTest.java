package com.junshock.jpatest;

import com.junshock.jpatest.domain.Member;
import com.junshock.jpatest.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberRepositoryTest {
    
    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional // test에서는 test끝난 후 rollback 하기에 h2 db에 데이터가 없다.
    @Rollback(false)
    public void testMember() throws Exception{
        //given
        Member member = new Member();
        member.setName("junshock5");

        //when
        Long saveId = memberRepository.save(member);
        Member findMember = memberRepository.findOne(saveId);
        
        //then
        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getName()).isEqualTo(member.getName());
        Assertions.assertThat(findMember).isEqualTo(member);

        // 같은 영속성 컨텍스트 에서는 저장한 member와 찾은 member가 1차캐시에서 동일한 값이다.
        System.out.println("findMember == member: " + (findMember == member));
    }
}