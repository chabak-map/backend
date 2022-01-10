package com.sikhye.chabak.service.report;

import java.util.List;

import com.sikhye.chabak.service.report.dto.ReportRankRes;
import com.sikhye.chabak.service.report.dto.ReportReq;

public interface ReportService {

	Long saveReport(ReportReq reportReq);

	List<ReportRankRes> getRank();

	void sendEmailToAdmin();
}
