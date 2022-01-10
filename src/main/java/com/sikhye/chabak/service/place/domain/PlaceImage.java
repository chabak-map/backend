package com.sikhye.chabak.service.place.domain;

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

//20211216
// @Document(indexName = "placeiamge")
@Getter
@DynamicInsert
@Entity
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

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "PLACE_ID")
	private Place place;

}
