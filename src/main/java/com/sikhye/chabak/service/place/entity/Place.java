package com.sikhye.chabak.service.place.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;

import com.sikhye.chabak.global.constant.BaseStatus;
import com.sikhye.chabak.service.comment.entity.PlaceReview;
import com.sikhye.chabak.service.tag.entity.PlaceTag;

import lombok.Builder;
import lombok.Getter;

@Getter
@DynamicInsert
@Entity
@Table(name = "Place")
public class Place {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private String address;
	private String content;

	private Double latitude;
	private Double longitude;

	@Column(name = "phone_number")
	private String phoneNumber;

	@Enumerated(EnumType.STRING)
	private BaseStatus status;

	@Builder
	public Place(String name, String address, String content, Double latitude, Double longitude) {
		this.name = name;
		this.address = address;
		this.content = content;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public Place() {
	}

	public void setStatusToDelete() {
		this.status = BaseStatus.DELETED;
	}

	public void setPoint(Double latitude, Double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	// TODO: for test
	public void setStatusToUsed() {
		this.status = BaseStatus.USED;
	}

	@OneToMany(mappedBy = "place")
	private List<PlaceImage> placeImages = new ArrayList<>();

	@OneToMany(mappedBy = "place")
	private List<PlaceTag> tags = new ArrayList<>();

	@OneToMany(mappedBy = "place")
	private List<PlaceReview> placeReviews = new ArrayList<>();
}
