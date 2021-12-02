package com.sikhye.chabak.src.member.entity;

import com.sikhye.chabak.base.entity.BaseEntity;
import com.sikhye.chabak.base.entity.BaseRole;
import com.sikhye.chabak.base.entity.BaseStatus;
import com.sikhye.chabak.src.comment.entity.PlaceReview;
import com.sikhye.chabak.src.comment.entity.PostingComment;
import com.sikhye.chabak.src.post.entity.Posting;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
		this.status = BaseStatus.deleted;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public void editMemberInfo(String nickname, String imageUrl) {
		this.nickname = nickname;
		this.imageUrl = imageUrl;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@OneToMany(mappedBy = "member")
	private List<PlaceReview> placeReviews = new ArrayList<>();

	@OneToMany(mappedBy = "member")
	private List<Posting> postings = new ArrayList<>();

	@OneToMany(mappedBy = "member")
	private List<PostingComment> postingComments = new ArrayList<>();
}
