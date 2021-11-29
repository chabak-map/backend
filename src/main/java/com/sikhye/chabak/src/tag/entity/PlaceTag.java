package com.sikhye.chabak.src.tag.entity;


import com.sikhye.chabak.base.entity.BaseStatus;
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
@Table(name = "PlaceTag")
public class PlaceTag {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@Column(name = "place_id")
	private Long placeId;


	@Enumerated(EnumType.STRING)
	private BaseStatus status;

	@Builder
	public PlaceTag(String name, Long placeId) {
		this.name = name;
		this.placeId = placeId;
	}

	public void setStatusToDelete() {
		this.status = BaseStatus.deleted;
	}

	public void setName(String name) {
		this.name = name;
	}

	// ptpt: 연관관계 매핑
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "PLACE_ID")
	private Place place;

}

