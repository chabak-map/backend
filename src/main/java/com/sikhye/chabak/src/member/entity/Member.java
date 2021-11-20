package com.sikhye.chabak.src.member.entity;

import com.sikhye.chabak.configure.entity.BaseStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@Entity
@Table(name = "Member")
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nickname;

	private String email;

	private String password;

	private String phoneNumber;

	private String imageUrl;

	@Enumerated(EnumType.STRING)
	private BaseStatus status;

	@Builder
	public Member(String nickname, String email, String password, String phoneNumber, String imageUrl) {
		this.nickname = nickname;
		this.email = email;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.imageUrl = imageUrl;
	}

//	public MemberDto getMemberDto() {
//
//		return MemberDto.builder()
//			.id(this.id)
//			.nickname(this.nickname)
//			.imageUrl(this.imageUrl)
//			.build();
//	}


//	public void modifyMember() {
//
//	}

	public void setStatusToDelete(BaseStatus status) {
		this.status = status;
	}

}
