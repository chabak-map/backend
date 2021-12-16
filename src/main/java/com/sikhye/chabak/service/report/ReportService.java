package com.sikhye.chabak.service.report;

import com.sikhye.chabak.service.report.dto.ReportReq;

public interface ReportService {

	Long saveReport(ReportReq reportReq);

	void sendEmailToAdminAndClear();
}
