package com.sikhye.chabak.src.member;

import com.sikhye.chabak.src.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

	Member findMemberByEmail(String email);

}
