package com.sikhye.chabak.service.member.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sikhye.chabak.global.constant.BaseStatus;
import com.sikhye.chabak.service.member.constant.BaseRole;
import com.sikhye.chabak.service.oauth.constant.OAuthType;

public interface MemberRepository extends JpaRepository<Member, Long> {

	Optional<Member> findMemberByEmailAndStatus(String email, BaseStatus status);

	Optional<Member> findMemberByNicknameAndStatus(String nickname, BaseStatus status);

	Optional<Member> findMemberByIdAndStatus(Long memberId, BaseStatus status);

	Optional<Member> findMemberByPhoneNumberAndStatus(String phoneNumber, BaseStatus status);

	Optional<Member> findMemberByPhoneNumberAndEmailAndStatus(String phoneNumber, String email, BaseStatus status);

	Optional<Member> findMemberBySocialTypeAndSocialIdAndStatus(OAuthType socialType, String socialId,
		BaseStatus status);

	Boolean existsByNicknameAndStatus(String nickname, BaseStatus status);

	Boolean existsByEmailAndStatus(String email, BaseStatus status);

	Optional<List<Member>> findMembersByRoleAndStatus(BaseRole role, BaseStatus status);

}
