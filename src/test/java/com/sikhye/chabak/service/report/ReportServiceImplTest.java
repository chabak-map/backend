package com.sikhye.chabak.service.report;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.sikhye.chabak.service.report.dto.ReportRankRes;

@SpringBootTest
@WebAppConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class ReportServiceImplTest {

	@Autowired
	private ReportService reportService;

	@Test
	@DisplayName("001. 테스트 랭크 가져오기")
	@Order(1)
	public void getTestRankTest() throws Exception {
		//given
		List<ReportRankRes> reportRank = reportService.getRank();

		//when
		int size = reportRank.size();

		//then
		Assertions.assertEquals(size, 3);
	}

}
