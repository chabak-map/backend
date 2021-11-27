package com.sikhye.chabak.src.post.entity;

import com.sikhye.chabak.base.entity.BaseStatus;
import com.sikhye.chabak.src.member.entity.Member;
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
@Table(name = "PostingComment")
public class PostingComment {

	@Id	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "posting_id")
	private Long postingId;

	@Column(name = "member_id")
	private Long memberId;

	private String content;

	@Enumerated(EnumType.STRING)
	private BaseStatus status;

	@Builder
	public PostingComment(Long postingId, Long memberId, String content) {
		this.postingId = postingId;
		this.memberId = memberId;
		this.content = content;
	}

	public void setStatusToDelete() {
		this.status = BaseStatus.deleted;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MEMBER_ID")
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "POSTING_ID")
	private Posting posting;


}
