package com.sikhye.chabak.service.report;

import static com.sikhye.chabak.global.response.BaseResponseStatus.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.mail.MessagingException;

import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sikhye.chabak.global.exception.BaseException;
import com.sikhye.chabak.service.email.EmailSenderService;
import com.sikhye.chabak.service.jwt.JwtTokenService;
import com.sikhye.chabak.service.member.MemberService;
import com.sikhye.chabak.service.member.domain.Member;
import com.sikhye.chabak.service.report.constant.ReportType;
import com.sikhye.chabak.service.report.domain.Report;
import com.sikhye.chabak.service.report.domain.ReportRepository;
import com.sikhye.chabak.service.report.dto.ReportRankRes;
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
	public List<ReportRankRes> getRank() {
		return reportRepository.getReportRank().orElseGet(Collections::emptyList);
	}

	@Override
	public void sendEmailToAdmin() {

		List<String> adminEmailList = memberService.findAllAdmin().orElseGet(Collections::emptyList)
			.stream().map(Member::getEmail).collect(Collectors.toList());

		String subject = "[ㅊㅂㅊㅂ] 신고 리포트";
		String content = DateTime.now().toString();

		adminEmailList.forEach(email -> {
			try {
				emailSenderService.sendEmail(email, subject, content);
			} catch (MessagingException e) {
				throw new BaseException(SEND_MAIL_ERROR);
			}
		});

	}

}
