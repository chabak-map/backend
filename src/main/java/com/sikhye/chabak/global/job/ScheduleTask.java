// package com.sikhye.chabak.global.job;
//
// import org.springframework.scheduling.annotation.Scheduled;
// import org.springframework.stereotype.Component;
//
// import com.sikhye.chabak.service.report.ReportService;
//
// @Component
// public class ScheduleTask {
//
// 	private final ReportService reportService;
//
// 	public ScheduleTask(ReportService reportService) {
// 		this.reportService = reportService;
// 	}
//
// 	@Scheduled(cron = "0 */5 * * * *")
// 	public void reportTask() {
// 		reportService.sendEmailToAdminAndClear();
// 	}
// }
