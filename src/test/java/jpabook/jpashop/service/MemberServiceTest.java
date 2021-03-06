package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest
{
    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired
    EntityManager em;

    @Test
    public void 회원기능() throws Exception
    {
        //given
        Member member = new Member();
        member.setName("kim");

        //when
        Long saveId = memberService.join(member);

        //then
        em.flush();
        assertEquals(member,memberRepository.findOne(saveId));
    }
    
    @Test
    public void 중복_회원_예외() throws Exception
    {
        //given
        Member member1 = new Member();
        member1.setName("kim");
        Member member2 = new Member();
        member2.setName("kim");
        //when

        memberService.join(member1);
        //then

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> memberService.join(member2));
        assertEquals("이미 존재하는 회원입니다.",exception.getMessage());
    }
    

    @Test
    public void 업데이트시_중복회원체크() throws Exception
    {
        //given
        Member member1 = new Member();
        member1.setName("kim");
        Member member2 = new Member();
        member2.setName("kim2");
        //when

        memberService.join(member1);
        memberService.join(member2);
        em.flush();
        em.clear();
        //then

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> memberService.update(member2.getId(),"kim3"));
        assertEquals("이미 존재하는 회원입니다.",exception.getMessage());
    }

}