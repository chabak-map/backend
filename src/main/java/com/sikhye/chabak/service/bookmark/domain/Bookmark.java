package com.sikhye.chabak.service.bookmark.domain;

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
import com.sikhye.chabak.service.place.entity.Place;

import lombok.Builder;
import lombok.Getter;

@Getter
@DynamicInsert
@Entity
@Table(name = "Bookmark")
public class Bookmark extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "place_id")
	private Long placeId;

	@Column(name = "member_id")
	private Long memberId;

	@Enumerated(EnumType.STRING)
	private BaseStatus status;

	@Builder
	public Bookmark(Long placeId, Long memberId) {
		this.placeId = placeId;
		this.memberId = memberId;
	}

	public Bookmark() {

	}

	public void setStatusToUsed() {
		this.status = BaseStatus.USED;
	}

	public void setStatusToDelete() {
		this.status = BaseStatus.DELETED;
	}

	// 연관관계
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MEMBER_ID")
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PLACE_ID")
	private Place place;

	//	// 연관관계 지원 함수
	//	public void changePlace(Place place) {
	//		this.place = place;
	//		team.getPlaces().add(this);	// 리스트에 자신을 추가
	//	}

}
