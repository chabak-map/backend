package com.sikhye.chabak.src.place.entity;


import com.sikhye.chabak.base.entity.BaseStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@Entity
@Table(name = "PlaceTag")
public class PlaceTag {

	@Id	@GeneratedValue(strategy = GenerationType.IDENTITY)
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

	// 연관관계 매핑
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "PLACE_ID")	// >> pt. 대문자로 해야 PLACE에 있는 ID필드로 인식한다. 그게 아니면 Tags에 있는 place_id로 인식함
	private Place place;

}
