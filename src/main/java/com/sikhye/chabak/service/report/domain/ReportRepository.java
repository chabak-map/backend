package com.sikhye.chabak.service.report.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sikhye.chabak.global.constant.BaseStatus;

public interface ReportRepository extends JpaRepository<Report, Long>, ReportRepositoryCustom {

	Optional<List<Report>> findAllByStatus(BaseStatus status);
}
