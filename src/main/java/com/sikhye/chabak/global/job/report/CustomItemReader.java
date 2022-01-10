package com.sikhye.chabak.global.job.report;

import java.util.Iterator;
import java.util.List;

import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import com.sikhye.chabak.service.report.dto.ReportRankRes;

@Component
public class CustomItemReader implements ItemReader<ReportRankRes> {

	private final Iterator<ReportRankRes> data;

	public CustomItemReader(List<ReportRankRes> data) {
		this.data = data.iterator();
	}

	@Override
	public ReportRankRes read() throws Exception {
		if (this.data.hasNext()) {
			return this.data.next();
		}
		return null;
	}
}
