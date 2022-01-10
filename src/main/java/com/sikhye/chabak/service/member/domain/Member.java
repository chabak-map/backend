package com.sikhye.chabak.service.member.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;

import com.sikhye.chabak.global.constant.BaseStatus;
import com.sikhye.chabak.global.time.BaseEntity;
import com.sikhye.chabak.service.bookmark.domain.Bookmark;
import com.sikhye.chabak.service.member.constant.BaseRole;
import com.sikhye.chabak.service.oauth.constant.OAuthType;

import lombok.Builder;
import lombok.Getter;

@Getter
@DynamicInsert
@Entity
@Table(name = "Member")
public class Member extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nickname;

	private String email;

	private String password;

	@Column(name = "phone_number")
	private String phoneNumber;

	@Column(name = "image_url")
	private String imageUrl;

	@Enumerated(EnumType.STRING)
	private BaseStatus status;

	@Enumerated(EnumType.STRING)
	private BaseRole role;

	@Enumerated(EnumType.STRING)
	@Column(name = "social_type")
	private OAuthType socialType;

	@Column(name = "social_id")
	private String socialId;

	@Builder
	public Member(String nickname, String email, String password, String phoneNumber, String imageUrl) {
		this.nickname = nickname;
		this.email = email;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.imageUrl = imageUrl;
	}

	public Member() {
	}

	public void setStatusToDelete() {
		this.status = BaseStatus.DELETED;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public void editMemberInfo(String nickname, String imageUrl) {
		this.nickname = nickname;
		this.imageUrl = imageUrl;
	}

	public void editMemberNickname(String nickname) {
		this.nickname = nickname;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setSocialInfo(OAuthType socialType, String socialId) {
		this.socialType = socialType;
		this.socialId = socialId;
	}

	// @OneToMany(mappedBy = "member")
	// private List<PlaceComment> placeReviews = new ArrayList<>();

	// @OneToMany(mappedBy = "member")
	// private List<Posting> postings = new ArrayList<>();

	// @OneToMany(mappedBy = "member")
	// private List<PostingComment> postingComments = new ArrayList<>();

	@OneToMany(mappedBy = "member")
	private List<Bookmark> bookmarks = new ArrayList<>();
}
