// package com.sikhye.chabak.global.job;
//
// import org.springframework.batch.core.Job;
// import org.springframework.batch.core.Step;
// import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
// import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
// import org.springframework.batch.repeat.RepeatStatus;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
//
// import com.sikhye.chabak.service.report.ReportService;
//
// import lombok.extern.slf4j.Slf4j;
//
// @Slf4j
// @Configuration
// public class SendEmailJob {
//
// 	private final JobBuilderFactory jobBuilderFactory;
// 	private final StepBuilderFactory stepBuilderFactory;
// 	private final ReportService reportService;
//
// 	public SendEmailJob(JobBuilderFactory jobBuilderFactory,
// 		StepBuilderFactory stepBuilderFactory, ReportService reportService) {
// 		this.jobBuilderFactory = jobBuilderFactory;
// 		this.stepBuilderFactory = stepBuilderFactory;
// 		this.reportService = reportService;
// 	}
//
// 	@Bean
// 	public Job simpleJob() {
// 		return jobBuilderFactory.get("simpleJob")    // job 이름
// 			.start(simpleStep1())    // step1이라는 step 생성
// 			.build();
// 	}
//
// 	@Bean
// 	public Step simpleStep1() {
// 		return stepBuilderFactory.get("simpleStep1")    // step 이름
// 			.tasklet((contribution, chunkContext) -> {    // 단일 수행될 커스텀 기능
// 				log.info(">>>>> This is Step1");
// 				reportService.sendEmailToAdminAndClear();
// 				return RepeatStatus.FINISHED;
// 			})
// 			.build();
// 	}
// }
