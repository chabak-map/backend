package com.sikhye.chabak.src.place.entity;

import com.sikhye.chabak.base.entity.BaseStatus;
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

	public void setStatusToDelete() {
		this.status = BaseStatus.deleted;
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
