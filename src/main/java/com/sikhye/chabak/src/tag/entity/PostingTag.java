package com.sikhye.chabak.src.tag.entity;

import com.sikhye.chabak.base.entity.BaseStatus;
import com.sikhye.chabak.src.post.entity.Posting;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@Entity
@Table(name = "PostingTag")
public class PostingTag {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@Column(name = "posting_id")
	private Long postingId;

	@Enumerated(EnumType.STRING)
	private BaseStatus status;

	public void setStatusToDelete() {
		this.status = BaseStatus.deleted;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Builder
	public PostingTag(String name, Long postingId) {
		this.name = name;
		this.postingId = postingId;
	}

	// 연관관계 매핑
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "POSTING_ID")
	private Posting posting;

}
