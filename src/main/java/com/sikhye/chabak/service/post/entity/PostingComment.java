package com.sikhye.chabak.service.post.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;

import com.sikhye.chabak.global.constant.BaseStatus;
import com.sikhye.chabak.global.time.BaseEntity;
import com.sikhye.chabak.service.member.entity.Member;

import lombok.Builder;
import lombok.Getter;

@Getter
@DynamicInsert
@Entity
@Table(name = "PostingComment")
public class PostingComment extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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

	public PostingComment() {
	}

	public void setStatusToDelete() {
		this.status = BaseStatus.DELETED;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MEMBER_ID")
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "POSTING_ID")
	private Posting posting;

}
