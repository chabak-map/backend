package com.sikhye.chabak.src.post.entity;

import com.sikhye.chabak.base.entity.BaseEntity;
import com.sikhye.chabak.base.entity.BaseStatus;
import com.sikhye.chabak.src.comment.entity.PostingComment;
import com.sikhye.chabak.src.member.entity.Member;
import com.sikhye.chabak.src.tag.entity.PostingTag;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@Entity
@Table(name = "Posting")
public class Posting extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "member_id")
	private Long memberId;

	private String title;

	private String content;

	@Enumerated(EnumType.STRING)
	private BaseStatus status;

	public void setStatusToDelete() {
		this.status = BaseStatus.deleted;
	}

	@Builder
	public Posting(Long memberId, String title, String content) {
		this.memberId = memberId;
		this.title = title;
		this.content = content;
	}

	// 연관관계 매핑
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "MEMBER_ID")
	private Member member;

	@OneToMany(mappedBy = "posting")
	private List<PostingTag> postingTags = new ArrayList<>();

	@OneToMany(mappedBy = "posting")
	private List<PostingComment> postingComments = new ArrayList<>();

	@OneToMany(mappedBy = "posting")
	private List<PostingImage> postingImages = new ArrayList<>();


}
