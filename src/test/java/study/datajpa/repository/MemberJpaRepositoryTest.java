package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {
    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    public void testNamedQuery(){
        Member m1 = new Member("AAA",10,null);
        Member m2 = new Member("BBB",10,null);

        memberJpaRepository.save(m1);
        memberJpaRepository.save(m2);

        List<Member> result = memberJpaRepository.findByUsername("AAA");
        Member findMember = result.get(0);
        assertThat(findMember).isEqualTo(m1);
    }

    @Test
    public void paging(){
        //given
        memberJpaRepository.save(new Member("member1",10,null));
        memberJpaRepository.save(new Member("member2",10,null));
        memberJpaRepository.save(new Member("member3",10,null));
        memberJpaRepository.save(new Member("member4",10,null));
        memberJpaRepository.save(new Member("member5",10,null));
        memberJpaRepository.save(new Member("member6",10,null));
        memberJpaRepository.save(new Member("member7",10,null));
        memberJpaRepository.save(new Member("member8",10,null));

        int age = 10;
        int offset = 0;
        int limit = 3;

        //when
        List<Member> members = memberJpaRepository.findByPage(age, offset, limit);
        long totalCount = memberJpaRepository.totalCount(age);

        // 페이지 계산 공식 적용...
        // totalPage = totalCount / size ...
        // 마지막 페이지 ...
        // 최초 페이지 ..

        //then
        assertThat(members.size()).isEqualTo(3);
        assertThat(totalCount).isEqualTo(8);

    }

    @Test
    public void bulkUpdate(){
        //given
        memberJpaRepository.save(new Member("member1",10,null));
        memberJpaRepository.save(new Member("member2",20,null));
        memberJpaRepository.save(new Member("member3",13,null));
        memberJpaRepository.save(new Member("member4",16,null));
        memberJpaRepository.save(new Member("member5",20,null));
        memberJpaRepository.save(new Member("member6",32,null));
        memberJpaRepository.save(new Member("member7",20,null));
        memberJpaRepository.save(new Member("member8",12,null));

        //when
        int count = memberJpaRepository.bulkAgePlus(20);

        //then
        assertThat(count).isEqualTo(4);


    }


}