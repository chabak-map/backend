package com.sikhye.chabak.src.place.entity;

import com.sikhye.chabak.base.entity.BaseStatus;
import com.sikhye.chabak.src.comment.entity.PlaceReview;
import com.sikhye.chabak.src.tag.entity.PlaceTag;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

	public void setStatusToDelete() {
		this.status = BaseStatus.deleted;
	}

	public void setPoint(Double latitude, Double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}


	// TODO: for test
	public void setStatusToUsed() {
		this.status = BaseStatus.used;
	}

	@OneToMany(mappedBy = "place")
	private List<PlaceImage> placeImages = new ArrayList<>();

	@OneToMany(mappedBy = "place")
	private List<PlaceTag> tags = new ArrayList<>();

	@OneToMany(mappedBy = "place")
	private List<PlaceReview> placeReviews = new ArrayList<>();
}
