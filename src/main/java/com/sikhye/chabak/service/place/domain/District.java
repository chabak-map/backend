package com.sikhye.chabak.service.place.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;

import com.sikhye.chabak.global.time.BaseEntity;

import lombok.Builder;
import lombok.Getter;

@Getter
@DynamicInsert
@Entity
@Table(name = "District")
public class District extends BaseEntity {

	@Id
	private String code;

	@Column(name = "region_1depth")
	private String region1Depth;

	@Column(name = "region_2depth")
	private String region2Depth;

	public District() {
	}

	@Builder
	public District(String code, String region1Depth, String region2Depth) {
		this.code = code;
		this.region1Depth = region1Depth;
		this.region2Depth = region2Depth;
	}

	@OneToMany(mappedBy = "district")
	private List<Place> places = new ArrayList<>();

}
