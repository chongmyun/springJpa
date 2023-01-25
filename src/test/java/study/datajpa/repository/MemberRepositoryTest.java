package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class MemberRepositoryTest {
    @PersistenceContext
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

    @Test
    public void methodNameQuery(){
        List<Member> findMember = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 10);

        List<Member> allMember = memberRepository.findAllBy();

        List<Member> top3Member = memberRepository.findTop3AllBy();

        long count = memberRepository.countAllBy();
    }

    @Test
    public void testNamedQuery(){
        Member m1 = new Member("AAA",10,null);
        Member m2 = new Member("BBB",10,null);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsername("AAA");
        Member findMember = result.get(0);
        assertThat(findMember).isEqualTo(m1);
    }

    @Test
    public void testQuery(){
        Member m1 = new Member("AAA",10,null);
        Member m2 = new Member("BBB",10,null);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findUser("AAA",10);
        assertThat(result.get(0)).isEqualTo(m1);
    }

    @Test
    public void findByNames(){
        Member m1 = new Member("AAA",10,null);
        Member m2 = new Member("BBB",10,null);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByNames(Arrays.asList("AAA","BBB"));

        for (Member member : result) {
            System.out.println(member);
        }

    }

    @Test
    public void findMemberDto(){

        Team team = new Team("teamA");
        teamRepository.save(team);

        Member m1 = new Member("AAA",10,team);
        memberRepository.save(m1);

        List<MemberDto> result = memberRepository.findMemberDto();

        for (MemberDto dto : result) {
            System.out.println("dto = "+ dto);
        }
    }

    @Test
    public void returnType(){
        Member m1 = new Member("AAA",10,null);
        Member m2 = new Member("BBB",10,null);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findListByUsername("asdasd");
        System.out.println("result = "+ result.size());

//        Optional<Member> findMember = memberRepository.findOptionalByUsername("AAA");
//        System.out.println("findMember = "+ findMember.orElseGet());
    }

    @Test
    public void paging(){
        Team team = new Team("team");
        teamRepository.save(team);

        //given
        memberRepository.save(new Member("member1",10,team));
        memberRepository.save(new Member("member2",10,null));
        memberRepository.save(new Member("member3",10,null));
        memberRepository.save(new Member("member4",10,team));
        memberRepository.save(new Member("member5",10,null));
        memberRepository.save(new Member("member6",10,null));
        memberRepository.save(new Member("member7",10,null));
        memberRepository.save(new Member("member8",10,null));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(1, 3, Sort.by(Sort.Direction.DESC, "username"));

        //when
        Page<Member> page = memberRepository.findByAgeGreaterThan(age, pageRequest);

        Page<MemberDto> toMap = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));

        //then
        List<Member> findMembers = page.getContent();

        long totalElements = page.getTotalElements();

        for (Member findMember : findMembers) {
            if(findMember.getTeam() != null) {
                System.out.println(findMember.getTeam().getName());
            }
            System.out.println("findMember = " + findMember);
        }
        System.out.println("totalElements = " + totalElements);

        assertThat(findMembers.size()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(8);
        assertThat(page.getNumber()).isEqualTo(1);
        assertThat(page.getTotalPages()).isEqualTo(3);
        assertThat(page.isFirst()).isFalse();
        assertThat(page.hasNext()).isTrue();

    }

    @Test
    public void slicePaging(){
        //given
        memberRepository.save(new Member("member1",10,null));
        memberRepository.save(new Member("member2",10,null));
        memberRepository.save(new Member("member3",10,null));
        memberRepository.save(new Member("member4",10,null));
        memberRepository.save(new Member("member5",10,null));
        memberRepository.save(new Member("member6",10,null));
        memberRepository.save(new Member("member7",10,null));
        memberRepository.save(new Member("member8",10,null));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        //when
        Slice<Member> page = memberRepository.findSliceByAge(age, pageRequest);

        //then
        List<Member> findMembers = page.getContent();

        assertThat(findMembers.size()).isEqualTo(3);
//        assertThat(page.getTotalElements()).isEqualTo(8);
        assertThat(page.getNumber()).isEqualTo(0);
//        assertThat(page.getTotalPages()).isEqualTo(3);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();

    }

    @Test
    public void listPaging(){
        //given
        memberRepository.save(new Member("member1",10,null));
        memberRepository.save(new Member("member2",10,null));
        memberRepository.save(new Member("member3",10,null));
        memberRepository.save(new Member("member4",10,null));
        memberRepository.save(new Member("member5",10,null));
        memberRepository.save(new Member("member6",10,null));
        memberRepository.save(new Member("member7",10,null));
        memberRepository.save(new Member("member8",10,null));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        //when
        List<Member> page = memberRepository.findListByAge(age, pageRequest);

        //then
//        List<Member> findMembers = page.getContent();

//        assertThat(findMembers.size()).isEqualTo(3);
//        assertThat(page.getTotalElements()).isEqualTo(8);
//        assertThat(page.getNumber()).isEqualTo(0);
//        assertThat(page.getTotalPages()).isEqualTo(3);
//        assertThat(page.isFirst()).isTrue();
//        assertThat(page.hasNext()).isTrue();

    }
    
    @Test
    public void bulkUpdate(){
        //given
        memberRepository.save(new Member("member1",10,null));
        memberRepository.save(new Member("member2",20,null));
        memberRepository.save(new Member("member3",13,null));
        memberRepository.save(new Member("member4",16,null));
        memberRepository.save(new Member("member5",20,null));
        memberRepository.save(new Member("member6",32,null));
        memberRepository.save(new Member("member7",20,null));
        memberRepository.save(new Member("member8",12,null));

        //when
        int count = memberRepository.bulkAgePlus(20);


        List<Member> result = memberRepository.findByUsername("member5");
        Member member5 = result.get(0);
        System.out.println(member5);

        //then
        assertThat(count).isEqualTo(4);
    }

    @Test
    public void findMemberLazy(){
        //given
        //member1 -> teamA
        //member2 -> teamB
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1",10,teamA);
        Member member2 = new Member("member2",10,teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        List<Member> members = memberRepository.findEntityGraphByUsername("member1");

        for (Member member : members) {
            System.out.println("member = " + member.getUsername());
            System.out.println("member.getTeam() = " + member.getTeam().getClass());
            System.out.println("team.getName() = " + member.getTeam().getName());
        }
    }

    @Test
    public void queryHint(){

        //given
        Member member1 = memberRepository.save(new Member("member1", 10, null));
        em.flush();
        em.clear();

        //when
        Member findMember = memberRepository.findReadOnlyByUsername("member1");
        findMember.setUsername("member2");

        em.flush();

    }

    @Test
    public void lock(){
        //given
        Member member1 = memberRepository.save(new Member("member1", 10, null));
        em.flush();
        em.clear();

        //when
        Member findMember = memberRepository.findLockByUsername("member1").get();
        findMember.setUsername("member2");

        em.flush();
    }

    @Test
    public void callCustom(){
        List<Member> result = memberRepository.findMemberCustom();
    }

    @Test
    public void projections(){
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member member1 = new Member("member1",10,teamA);
        Member member2 = new Member("member2",10,teamA);

        em.persist(member1);
        em.persist(member2);

        em.flush();
        em.clear();

        //when
        List<NestedClosedProjections> result = memberRepository.findProjectionsByUsername("member1",NestedClosedProjections.class);

        for (NestedClosedProjections nestedClosedProjections : result) {
            System.out.println("usernameOnly = " + nestedClosedProjections);
            System.out.println("nestedClosedProjections.getUsername() = " + nestedClosedProjections.getUsername());
            System.out.println("nestedClosedProjections.getTeam().getName() = " + nestedClosedProjections.getTeam().getName());
        }

    }
    
    @Test
    public void nativeQuery(){
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member member1 = new Member("member1",10,teamA);
        Member member2 = new Member("member2",10,teamA);

        em.persist(member1);
        em.persist(member2);

        em.flush();
        em.clear();
        
        //when
        Page<MemberProjections> result = memberRepository.findByNativeProjection(PageRequest.of(0, 10));
        List<MemberProjections> content = result.getContent();
        for (MemberProjections memberProjections : content) {
            System.out.println("memberProjections = " + memberProjections.getUsername());
            System.out.println("memberProjections = " + memberProjections.getTeamName());
        }

    }
}
