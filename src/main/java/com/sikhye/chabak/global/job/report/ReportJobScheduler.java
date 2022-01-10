package com.sikhye.chabak.global.job.report;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ReportJobScheduler {

	private final JobLauncher jobLauncher;
	private final ReportJobConfiguration reportJobConfiguration;

	public ReportJobScheduler(JobLauncher jobLauncher,
		ReportJobConfiguration reportJobConfiguration) {
		this.jobLauncher = jobLauncher;
		this.reportJobConfiguration = reportJobConfiguration;
	}

	@Scheduled(cron = "0 0 23 * * *")    // 매일 23시마다 메일을 보낸다. (초/분/시/일/월/요일)
	public void updateByBestSellerJob() {
		JobExecution execution;
		try {
			log.info(" >> [+] Start report Job");
			execution = jobLauncher.run(reportJobConfiguration.reportJob(), simpleJobParam());
			log.info(" >> [+] Job finished with status : " + execution.getStatus());
			log.info(" >> [+] Current Thread: {}", Thread.currentThread().getName());
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}

	//같은 이름의 batch는 생길 수 없기 때문에 param에 시간을 넣는다.
	private JobParameters simpleJobParam() {
		Map<String, JobParameter> confMap = new HashMap<>();
		confMap.put("time", new JobParameter(System.currentTimeMillis()));
		return new JobParameters(confMap);
	}
}
