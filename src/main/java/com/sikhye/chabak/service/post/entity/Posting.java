package com.sikhye.chabak.service.post.entity;

import static javax.persistence.FetchType.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;

import com.sikhye.chabak.global.constant.BaseStatus;
import com.sikhye.chabak.global.time.BaseEntity;
import com.sikhye.chabak.service.member.entity.Member;

import lombok.Builder;
import lombok.Getter;

// 20211216
// @Document(indexName = "posting")
@Getter
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
		this.status = BaseStatus.DELETED;
	}

	@Builder
	public Posting(Long memberId, String title, String content) {
		this.memberId = memberId;
		this.title = title;
		this.content = content;
	}

	public Posting() {
	}

	public void setPosting(String content) {
		this.content = content;
	}

	// 20211216
	// 연관관계 매핑
	@ManyToOne(fetch = LAZY)
	// @Field(type = FieldType.Nested, ignoreFields = {"member"})
	@JoinColumn(name = "MEMBER_ID")
	private Member member;

	@OneToMany(mappedBy = "posting")
	// @Field(type = FieldType.Nested, ignoreFields = {"postingTags"})
	private List<PostingTag> postingTags = new ArrayList<>();

	@OneToMany(mappedBy = "posting")
	// @Field(type = FieldType.Nested, ignoreFields = {"postingComments"})
	private List<PostingComment> postingComments = new ArrayList<>();

	@OneToMany(mappedBy = "posting")
	// @Field(type = FieldType.Nested, ignoreFields = {"postingImages"})    // >> ptpt: ES에서 ignore 하기(무한루프 방지)
	// https://stackoverflow.com/questions/47142738/spring-data-elastic-and-recursive-document-mapping
	private List<PostingImage> postingImages = new ArrayList<>();

}
