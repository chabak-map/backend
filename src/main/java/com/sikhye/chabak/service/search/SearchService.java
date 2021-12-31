package com.sikhye.chabak.service.search;

import com.sikhye.chabak.service.search.dto.SearchDto;

public interface SearchService {

	SearchDto searchBy(String keyword);

	SearchDto searchByTag(String tagName);

}
