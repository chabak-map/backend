package com.sikhye.chabak.service.place.entity;

import java.util.ArrayList;
import java.util.List;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sikhye.chabak.global.constant.BaseStatus;

import lombok.Builder;
import lombok.Getter;

//20211216
// @Document(indexName = "places")
@Getter
@DynamicInsert
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
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

	private String url;

	@Column(name = "district_code")
	private String districtCode;

	@Enumerated(EnumType.STRING)
	private BaseStatus status;

	@Builder
	public Place(String name, String address, String content, String phoneNumber, String url) {
		this.name = name;
		this.address = address;
		this.content = content;
		this.phoneNumber = phoneNumber;
		this.url = url;
	}

	public Place() {
	}

	public void setStatusToDelete() {
		this.status = BaseStatus.DELETED;
	}

	public void setPoint(Double latitude, Double longitude, String code) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.districtCode = code;
	}

	public void setStatusToUsed() {
		this.status = BaseStatus.USED;
	}

	@OneToMany(mappedBy = "place")
	private List<PlaceImage> placeImages = new ArrayList<>();

	@OneToMany(mappedBy = "place")
	private List<PlaceTag> tags = new ArrayList<>();

	@OneToMany(mappedBy = "place")
	private List<PlaceComment> placeComments = new ArrayList<>();

	// >> ptpt: 연관관계 주인인 쪽은 자신의 외래키와 joinColumn 설정
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DISTRICT_CODE")
	private District district;
}
