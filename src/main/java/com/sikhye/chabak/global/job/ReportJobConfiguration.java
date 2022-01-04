package com.sikhye.chabak.global.job;

import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import com.sikhye.chabak.service.report.ReportService;
import com.sikhye.chabak.service.report.dto.ReportRankRes;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class ReportJobConfiguration {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final ReportService reportService;

	public ReportJobConfiguration(
		JobBuilderFactory jobBuilderFactory,
		StepBuilderFactory stepBuilderFactory, ReportService reportService) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
		this.reportService = reportService;
	}

	@Bean
	public Job reportJob() throws Exception {
		return jobBuilderFactory.get("reportJob")
			.incrementer(new RunIdIncrementer())
			.start(csvWriterStep())
			.next(sendEmailStep())
			.build();
	}

	@Bean
	public Step csvWriterStep() throws Exception {
		return this.stepBuilderFactory.get("csvWriterStep")
			.<ReportRankRes, ReportRankRes>chunk(10)
			.reader(itemReader())
			.writer(csvFileWriter())
			.build();
	}

	// >> ptpt: csv 파일 작성
	private ItemWriter<ReportRankRes> csvFileWriter() throws Exception {
		BeanWrapperFieldExtractor<ReportRankRes> fieldExtractor = new BeanWrapperFieldExtractor<>();
		fieldExtractor.setNames(new String[] {"ranking", "targetId", "count"});

		DelimitedLineAggregator<ReportRankRes> lineAggregator = new DelimitedLineAggregator<>();
		lineAggregator.setDelimiter(",");
		lineAggregator.setFieldExtractor(fieldExtractor);

		FlatFileItemWriter<ReportRankRes> itemWriter = new FlatFileItemWriterBuilder<ReportRankRes>()
			.name("csvFileItemWriter")
			.encoding("UTF-8")
			.resource(new FileSystemResource("output/report.csv"))    // 파일 생성 목적
			.lineAggregator(lineAggregator)
			.build();

		itemWriter.afterPropertiesSet();

		return itemWriter;
	}

	private ItemReader<ReportRankRes> itemReader() {
		return new CustomItemReader(getItems());
	}

	private List<ReportRankRes> getItems() {
		return reportService.getRank();
	}

	@Bean
	public Step sendEmailStep() {
		return stepBuilderFactory.get("sendEmailStep")
			.tasklet((contribution, chunkContext) -> {
				log.info(">>> [+] Start sendEmail to Admin");
				reportService.sendEmailToAdmin();
				return RepeatStatus.FINISHED;
			})
			.build();
	}
}
