package com.sikhye.chabak.src.bookmark;

import com.sikhye.chabak.src.bookmark.dto.BookmarkRes;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@WebAppConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookmarkServiceImplTest {

	@Autowired
	private BookmarkService bookmarkService;

	@Test
	@DisplayName("001. 북마크 조회 테스트")
	@Order(1)
	public void findBookmarkTest() {
		//given
		Long memberId = 6L;

		//when
		List<BookmarkRes> bookmarkRes = bookmarkService.testFindBookmark(memberId);

		long count = bookmarkRes.stream()
			.map(BookmarkRes::getName)
			.filter(s -> s.equals("옥정호차박지"))
			.count();

		BookmarkRes bookmarkRes1 = bookmarkRes.get(0);
		System.out.println("bookmarkRes1.getName() = " + bookmarkRes1.getName());
		System.out.println("bookmarkRes1.getPlaceTagNames() = " + bookmarkRes1.getPlaceTagNames());

		//then
		assertEquals(bookmarkRes.size(), 1);
		assertEquals(count, 1);
	}


}
