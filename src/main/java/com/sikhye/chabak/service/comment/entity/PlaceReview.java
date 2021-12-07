package com.sikhye.chabak.service.comment.entity;

import static javax.persistence.FetchType.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import com.sikhye.chabak.service.place.entity.Place;

import lombok.Builder;
import lombok.Getter;

@Getter
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

	public PlaceReview() {
	}

	public void setStatusToDelete() {
		this.status = BaseStatus.DELETED;
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
