package com.sikhye.chabak.service.place.entity;

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

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sikhye.chabak.global.constant.BaseStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@DynamicInsert
@Entity
// >> ptpt: 양방향 참조 및 셀프 참조인 경우에 서로 참조하는 문제가 발생하는데 이를 해결
// 서로가 서로를 참조함 ( place -> placeimage -> place -> 반복 )- infinite recursion
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
@Table(name = "PlaceImage")
public class PlaceImage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "place_id")
	private Long placeId;

	@Column(name = "image_url")
	private String imageUrl;

	@Enumerated(EnumType.STRING)
	private BaseStatus status;

	@Builder
	public PlaceImage(Long placeId, String imageUrl) {
		this.placeId = placeId;
		this.imageUrl = imageUrl;
	}

	public PlaceImage() {
	}

	public void setStatusToDelete() {
		this.status = BaseStatus.DELETED;
	}

	// 연관관계 매핑
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "PLACE_ID")
	private Place place;

	//	// 연관관계 지원 함수
	//	public void changePlace(Place place) {
	//		this.place = place;
	//		team.getPlaces().add(this);	// 리스트에 자신을 추가
	//	}

}