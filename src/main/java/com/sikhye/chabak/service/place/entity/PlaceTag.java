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

import com.sikhye.chabak.global.constant.BaseStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
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

	public PlaceTag() {

	}

	public void setStatusToDelete() {
		this.status = BaseStatus.DELETED;
	}

	public void setName(String name) {
		this.name = name;
	}

	// ptpt: 연관관계 매핑
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "PLACE_ID")
	private Place place;

}

