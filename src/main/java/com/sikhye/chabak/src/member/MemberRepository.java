package com.sikhye.chabak.src.member;

import com.sikhye.chabak.base.entity.BaseStatus;
import com.sikhye.chabak.src.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

	Optional<Member> findMemberByEmailAndStatus(String email, BaseStatus status);
	Optional<Member> findMemberByNicknameAndStatus(String nickname, BaseStatus status);
	Optional<Member> findMemberByIdAndStatus(Long memberId, BaseStatus status);

}
