package com.sikhye.chabak.src.post.entity;

import com.sikhye.chabak.base.entity.BaseStatus;
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
@Table(name = "PostingImage")
public class PostingImage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;

	@Column(name = "posting_id")
	private Long postingId;

	@Column(name = "image_url")
	private String imageUrl;

	@Enumerated(EnumType.STRING)
	private BaseStatus status;

	@Builder
	public PostingImage(Long postingId, String imageUrl) {
		this.postingId = postingId;
		this.imageUrl = imageUrl;
	}

	public void setStatusToDelete() {
		this.status = BaseStatus.deleted;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "POSTING_ID")
	private Posting posting;

}
