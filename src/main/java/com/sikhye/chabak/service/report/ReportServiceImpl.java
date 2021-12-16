package com.sikhye.chabak.service.report;

import static com.sikhye.chabak.global.constant.BaseStatus.*;
import static java.util.Objects.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sikhye.chabak.service.email.EmailSenderService;
import com.sikhye.chabak.service.jwt.JwtTokenService;
import com.sikhye.chabak.service.member.MemberService;
import com.sikhye.chabak.service.member.entity.Member;
import com.sikhye.chabak.service.report.constant.ReportType;
import com.sikhye.chabak.service.report.domain.Report;
import com.sikhye.chabak.service.report.domain.ReportRepository;
import com.sikhye.chabak.service.report.dto.ReportReq;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
public class ReportServiceImpl implements ReportService {

	private final EmailSenderService emailSenderService;
	private final MemberService memberService;
	private final ReportRepository reportRepository;
	private final JwtTokenService jwtTokenService;

	public ReportServiceImpl(EmailSenderService emailSenderService,
		MemberService memberService, ReportRepository reportRepository,
		JwtTokenService jwtTokenService) {
		this.emailSenderService = emailSenderService;
		this.memberService = memberService;
		this.reportRepository = reportRepository;
		this.jwtTokenService = jwtTokenService;
	}

	@Override
	@Transactional
	public Long saveReport(ReportReq reportReq) {
		Report report = Report.builder()
			.memberId(jwtTokenService.getMemberId())
			.targetId(reportReq.getTargetId())
			.content(reportReq.getContent())
			.reportType(ReportType.valueOf(reportReq.getReportType()))
			.build();

		Report savedReport = reportRepository.save(report);
		return savedReport.getId();
	}

	@Override
	@Transactional
	public void sendEmailToAdminAndClear() {

		List<Report> reportList = reportRepository.findAllByStatus(USED).orElseGet(Collections::emptyList);

		if (reportList.isEmpty()) {
			return;
		}

		List<String> adminEmailList = memberService.findAllAdmin().orElseGet(Collections::emptyList)
			.stream().map(Member::getEmail).collect(Collectors.toList());

		reportList
			.forEach(report -> {
				adminEmailList
					.forEach((adminEmail) -> {
						String memberEmail = requireNonNull(
							memberService.findMemberBy(report.getMemberId()).orElse(null)).getEmail();
						String targetEmail = requireNonNull(
							memberService.findMemberBy(report.getTargetId()).orElse(null)).getEmail();

						String content = "[신고 작성자 : " + memberEmail + " ]\n"
							+ "[신고 대상자 : " + targetEmail + " ]\n"
							+ report.getReportType() + "\n\n\n"
							+ report.getContent();

						SimpleMailMessage mailMessage = new SimpleMailMessage();
						mailMessage.setTo(adminEmail);
						mailMessage.setSubject("[ㅊㅂㅊㅂ] 신고메일 :: " + report.getCreatedAt());
						mailMessage.setText(content);
						emailSenderService.sendEmail(mailMessage);

						log.info("to -> {}", adminEmail);
					});
				reportRepository.delete(report);
			});
	}

}
