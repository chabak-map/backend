package com.sikhye.chabak.service.report.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReportRankRes {

	private Long targetId;
	private Long count;
	private Integer ranking;

	public ReportRankRes() {
	}

	@Builder
	public ReportRankRes(Long targetId, Long count, Integer ranking) {
		this.targetId = targetId;
		this.count = count;
		this.ranking = ranking;
	}
}
