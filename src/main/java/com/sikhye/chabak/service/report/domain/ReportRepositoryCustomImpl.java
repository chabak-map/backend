package com.sikhye.chabak.service.report.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.sikhye.chabak.service.report.dto.ReportRankRes;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class ReportRepositoryCustomImpl implements ReportRepositoryCustom {

	private final JdbcTemplate jdbcTemplate;

	public ReportRepositoryCustomImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Optional<List<ReportRankRes>> getReportRank() {
		String reportRankQuery =
			"SELECT target_id, count(target_id) AS 'count', rank() over (order by COUNT(target_id) desc) as 'ranking'"
				+ " FROM Report"
				+ " WHERE status = 'USED' AND DATE_FORMAT(created_at, '%Y-%m-%D')=DATE_FORMAT(NOW(), '%Y-%m-%D')"
				+ " group by target_id";

		return Optional.of(this.jdbcTemplate.query(reportRankQuery, getReportRankMapper()));
	}

	// INTERNAL USE
	private RowMapper<ReportRankRes> getReportRankMapper() {
		return ((rs, rowNum) -> ReportRankRes.builder()
			.targetId(rs.getLong("target_id"))
			.count(rs.getLong("count"))
			.ranking(rs.getInt("ranking"))
			.build());
	}
}
