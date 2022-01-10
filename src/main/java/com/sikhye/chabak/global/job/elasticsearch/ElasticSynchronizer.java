// package com.sikhye.chabak.global.job;
//
// import java.time.LocalDateTime;
// import java.util.HashMap;
// import java.util.Map;
//
// import org.springframework.batch.core.JobExecution;
// import org.springframework.batch.core.JobParameter;
// import org.springframework.batch.core.JobParameters;
// import org.springframework.batch.core.launch.JobLauncher;
// import org.springframework.scheduling.annotation.Scheduled;
// import org.springframework.stereotype.Component;
//
// import com.sikhye.chabak.global.job.config.SyncDataJobConfiguration;
//
// import lombok.extern.slf4j.Slf4j;
//
// @Slf4j
// @Component
// public class ElasticSynchronizer {
//
// 	private final JobLauncher jobLauncher;
// 	private final SyncDataJobConfiguration syncDataJobConfiguration;
//
// 	public ElasticSynchronizer(JobLauncher jobLauncher,
// 		SyncDataJobConfiguration syncDataJobConfiguration) {
// 		this.jobLauncher = jobLauncher;
// 		this.syncDataJobConfiguration = syncDataJobConfiguration;
// 	}
//
// 	@Scheduled(cron = "0 */2 * * * *")
// 	public void syncData() {
// 		JobExecution execution;
// 		try {
// 			log.info("Start Syncing - {}", LocalDateTime.now());
// 			execution = jobLauncher.run(syncDataJobConfiguration.syncDataJob(), syncDataJobParam());
// 			log.info(" >> +++++++++++++++++ \n +++++++++++++++++++++ \n +++++++++++++ \n: " + execution.getStatus());
// 			log.info(" >> [+] Job finished with status : " + execution.getStatus());
// 			log.info(" >> [+] Current Thread: {}", Thread.currentThread().getName());
// 			log.info(" End Syncing - {}", LocalDateTime.now());
// 		} catch (Exception e) {
// 			log.error(e.getMessage());
// 			e.printStackTrace();
// 		}
// 	}
//
// 	//같은 이름의 batch는 생길 수 없기 때문에 param에 시간을 넣는다.
// 	private JobParameters syncDataJobParam() {
// 		Map<String, JobParameter> confMap = new HashMap<>();
// 		confMap.put("time", new JobParameter(System.currentTimeMillis()));
// 		return new JobParameters(confMap);
// 	}
// }
