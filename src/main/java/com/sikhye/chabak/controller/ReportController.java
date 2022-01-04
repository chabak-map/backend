package com.sikhye.chabak.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sikhye.chabak.global.response.BaseResponse;
import com.sikhye.chabak.service.report.ReportService;
import com.sikhye.chabak.service.report.dto.ReportRankRes;
import com.sikhye.chabak.service.report.dto.ReportReq;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/reports")
public class ReportController {

	private final ReportService reportService;

	public ReportController(ReportService reportService) {
		this.reportService = reportService;
	}

	@PostMapping
	public BaseResponse<Long> reportToAdmin(@Valid @RequestBody ReportReq reportReq) {
		return new BaseResponse<>(reportService.saveReport(reportReq));
	}

	@GetMapping
	public BaseResponse<List<ReportRankRes>> getReportRank() {
		return new BaseResponse<>(reportService.getRank());
	}
}
