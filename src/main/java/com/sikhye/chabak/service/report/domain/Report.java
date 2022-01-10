package com.sikhye.chabak.service.report.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;

import com.sikhye.chabak.global.constant.BaseStatus;
import com.sikhye.chabak.global.time.BaseEntity;
import com.sikhye.chabak.service.report.constant.ReportType;

import lombok.Builder;
import lombok.Getter;

@Getter
@DynamicInsert
@Entity
@Table(name = "Report")
public class Report extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "member_id")
	private Long memberId;

	@Column(name = "target_id")
	private Long targetId;

	private String content;

	@Column(name = "report_type")
	@Enumerated(EnumType.STRING)
	private ReportType reportType;

	@Enumerated(EnumType.STRING)
	private BaseStatus status;

	public Report() {
	}

	@Builder
	public Report(Long id, Long memberId, Long targetId, String content, ReportType reportType) {
		this.id = id;
		this.memberId = memberId;
		this.targetId = targetId;
		this.content = content;
		this.reportType = reportType;
	}
}
