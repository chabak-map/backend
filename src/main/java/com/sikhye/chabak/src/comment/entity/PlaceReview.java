package com.sikhye.chabak.src.comment.entity;

import com.sikhye.chabak.base.entity.BaseEntity;
import com.sikhye.chabak.base.entity.BaseStatus;
import com.sikhye.chabak.src.member.entity.Member;
import com.sikhye.chabak.src.place.entity.Place;
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
@Table(name = "PlaceReview")
public class PlaceReview extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "place_id")
	private Long placeId;

	@Column(name = "member_id")
	private Long memberId;

	private String content;

	@Enumerated(EnumType.STRING)
	private BaseStatus status;

	@Builder
	public PlaceReview(Long placeId, Long memberId, String content) {
		this.placeId = placeId;
		this.memberId = memberId;
		this.content = content;
	}

	public void setStatusToDelete() {
		this.status = BaseStatus.deleted;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "PLACE_ID")
	private Place place;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "MEMBER_ID")
	private Member member;

	//	// 연관관계 지원 함수
//	public void changePlace(Place place) {
//		this.place = place;
//		team.getPlaces().add(this);	// 리스트에 자신을 추가
//	}


}
