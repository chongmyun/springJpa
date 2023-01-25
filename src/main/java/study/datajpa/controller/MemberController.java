package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id){
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member){
        return member.getUsername();
    }

    @GetMapping("/members")
    public Page<Member> list(@PageableDefault(size = 5) Pageable pageable){
        Page<Member> page = memberRepository.findAll(pageable);
        return page;
    }

    @GetMapping("/membersDto")
    public Page<MemberDto> listDto(@PageableDefault(size = 5) Pageable pageable){
        Page<Member> page = memberRepository.findByAgeGreaterThan(5, pageable);

        Page<MemberDto> result = page.map(m -> new MemberDto(m));
        return result;
    }

//    @PostConstruct
    public void init(){
        for(int i = 0 ;i <100 ; i++){
            memberRepository.save(new Member("user"+1,i,null));
        }
    }

}
