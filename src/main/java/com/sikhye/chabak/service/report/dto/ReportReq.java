package com.sikhye.chabak.service.report.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ReportReq {

	@NotNull
	private Long targetId;

	@NotBlank
	private String content;

	private String reportType;

	public ReportReq() {
	}

	@Builder
	public ReportReq(Long targetId, String content, String reportType) {
		this.targetId = targetId;
		this.content = content;
		this.reportType = reportType;
	}
}
