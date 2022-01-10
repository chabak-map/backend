package com.sikhye.chabak.service.report.domain;

import java.util.List;
import java.util.Optional;

import com.sikhye.chabak.service.report.dto.ReportRankRes;

public interface ReportRepositoryCustom {

	Optional<List<ReportRankRes>> getReportRank();
}
