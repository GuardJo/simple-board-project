package com.guardjo.simpleboard.api;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guardjo.simpleboard.config.TestSecurityConfig;
import com.guardjo.simpleboard.domain.Article;
import com.guardjo.simpleboard.domain.ArticleSearchType;
import com.guardjo.simpleboard.dto.ArticleDetailInfo;
import com.guardjo.simpleboard.dto.ArticleDto;
import com.guardjo.simpleboard.dto.ArticlePageDto;
import com.guardjo.simpleboard.generator.TestDataGenerator;
import com.guardjo.simpleboard.service.ArticleService;
import com.guardjo.simpleboard.util.DtoConverter;

@Import(TestSecurityConfig.class)
@WebMvcTest(controllers = ArticleRestController.class)
class ArticleRestControllerTest {
	private final static List<Article> ARTICLE_TEST_DATA = new ArrayList<>();
	private final static int TEST_DATA_SZIE = 10;

	private final MockMvc mockMvc;
	private final ObjectMapper objectMapper;
	private final TestDataGenerator testDataGenerator = new TestDataGenerator();

	@MockBean
	private ArticleService articleService;

	@Autowired
	public ArticleRestControllerTest(MockMvc mockMvc, ObjectMapper mapper) {
		this.mockMvc = mockMvc;
		this.objectMapper = mapper;
	}

	@BeforeEach
	void setUp() {
		for (int i = 0; i < TEST_DATA_SZIE; i++) {
			ARTICLE_TEST_DATA.add(testDataGenerator.generateArticle("Test_" + i));
		}
	}

	@AfterEach
	void tearDown() {
		ARTICLE_TEST_DATA.clear();
	}

	@DisplayName("GET : " + UrlContext.ARTICLES_URL)
	@Test
	void getArticlePage() throws Exception {
		String searchValue = "";
		List<ArticleDto> expected = ARTICLE_TEST_DATA.stream()
			.map(DtoConverter::from)
			.toList();

		given(articleService.findArticles(eq(ArticleSearchType.TITLE), eq(searchValue), any(Pageable.class)))
			.willReturn(new PageImpl<>(expected));

		String response = mockMvc.perform(get(UrlContext.ARTICLES_URL)
				.contentType(MediaType.APPLICATION_JSON))
			.andReturn()
			.getResponse()
			.getContentAsString(StandardCharsets.UTF_8);

		ArticlePageDto actual = objectMapper.readValue(response, ArticlePageDto.class);

		assertThat(actual).isNotNull();
		assertThat(actual.articles()).isEqualTo(expected);

		then(articleService).should().findArticles(eq(ArticleSearchType.TITLE), eq(searchValue), any(Pageable.class));
	}

	@DisplayName("GET : " + UrlContext.ARTICLES_URL + "/{articleId}")
	@Test
	void test_getArticleDetailInfo() throws Exception {
		long articleId = 99L;
		Article article = ARTICLE_TEST_DATA.get(0);
		ArticleDetailInfo expected = DtoConverter.toArticleDetailInfo(article);

		given(articleService.findArticleDetailInfo(eq(articleId))).willReturn(expected);

		String response = mockMvc.perform(get(UrlContext.ARTICLES_URL + "/" + articleId)
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andReturn()
			.getResponse()
			.getContentAsString(StandardCharsets.UTF_8);

		ArticleDetailInfo actual = objectMapper.readValue(response, ArticleDetailInfo.class);

		assertThat(actual).isNotNull();
		assertThat(actual).isEqualTo(expected);

		then(articleService).should().findArticleDetailInfo(eq(articleId));
	}
}