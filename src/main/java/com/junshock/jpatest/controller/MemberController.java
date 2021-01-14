package com.junshock.jpatest.controller;

import com.junshock.jpatest.domain.dto.Address;
import com.junshock.jpatest.domain.Member;
import com.junshock.jpatest.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Valid MemberForm form, BindingResult result) {

        // thymeleaf spring integration 되어 있어서 BindingResult가 있으면 에러 발생시 여기에 담고 코드 실행한다.
        if (result.hasErrors()) {
            return "members/createMemberForm";
        }

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(new Address(form.getCity(), form.getStreet(), form.getZipcode()));

        memberService.join(member);
        return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model){
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }
    // API를 만들떄는 절대 Entity를 외부로 반환하면 안된다.
    // 이유는 API는 spec, ex) 필드 추가시 패스워드 보이고, 스팩이 변경한다.
    // 단, template Engine thymeleaf 같은 서버 사이드에선 사용해도 무방.
}
