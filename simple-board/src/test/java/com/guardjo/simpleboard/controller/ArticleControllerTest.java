package com.guardjo.simpleboard.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.guardjo.simpleboard.config.TestSecurityConfig;
import com.guardjo.simpleboard.domain.Article;
import com.guardjo.simpleboard.domain.ArticleSearchType;
import com.guardjo.simpleboard.domain.FormType;
import com.guardjo.simpleboard.dto.ArticleCreateRequest;
import com.guardjo.simpleboard.dto.ArticleUpdateDto;
import com.guardjo.simpleboard.dto.ArticleWithCommentDto;
import com.guardjo.simpleboard.generator.TestDataGenerator;
import com.guardjo.simpleboard.service.ArticleService;
import com.guardjo.simpleboard.service.HashtagService;
import com.guardjo.simpleboard.service.PaginationService;
import com.guardjo.simpleboard.util.DtoConverter;

@Import(TestSecurityConfig.class)
@WebMvcTest(ArticleController.class)
class ArticleControllerTest {

	@MockBean
	private ArticleService articleService;
	@MockBean
	private PaginationService paginationService;
	@MockBean
	private HashtagService hashtagService;
	private final MockMvc mockMvc;
	private final TestDataGenerator testDataGenerator;

	private final static String MEMBER_MAIL = "test@mail.com";

	ArticleControllerTest(@Autowired MockMvc mockMvc) {
		this.mockMvc = mockMvc;
		this.testDataGenerator = new TestDataGenerator();
	}

	@DisplayName("Article 목록 페이지 반환 테스트")
	@Test
	void testGetArticleView() throws Exception {
		given(articleService.findArticles(eq(null), eq(null), any(Pageable.class))).willReturn(Page.empty());
		given(paginationService.getPaginationNumbers(anyInt(), anyInt())).willReturn(List.of(1, 2, 3, 4, 5));

		mockMvc.perform(get("/article"))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
			.andExpect(model().attributeExists("articles"))
			.andExpect(model().attributeExists("paginationNumbers"));

		then(articleService).should().findArticles(eq(null), eq(null), any(Pageable.class));
		then(paginationService).should().getPaginationNumbers(anyInt(), anyInt());
	}

	@DisplayName("Article 목록을 반환 (paging 및 sorting 파라미터 추가) 테스트")
	@Test
	void testGetArticlesWithPagingAndSorting() throws Exception {
		String sortField = "title";
		int pageNumber = 0;
		int pageSize = 5;
		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.DESC, sortField);
		List<Integer> paginationBarNumbers = List.of(0, 1, 2, 3, 4);

		given(articleService.findArticles(null, null, pageable)).willReturn(Page.empty(pageable));
		given(paginationService.getPaginationNumbers(anyInt(), anyInt())).willReturn(paginationBarNumbers);

		mockMvc.perform(get("/article")
				.param("page", String.valueOf(pageNumber))
				.param("size", String.valueOf(pageSize))
				.param("sort", sortField + "," + Sort.Direction.DESC.name())
			)
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
			.andExpect(model().attributeExists("articles"))
			.andExpect(model().attributeExists("paginationNumbers"));

		then(articleService).should().findArticles(null, null, pageable);
		then(paginationService).should().getPaginationNumbers(pageNumber, Page.empty(pageable).getTotalPages());
	}

	@DisplayName("Article 목록을 반환 (articleSearchType 및  searchValue 포함) 테스트")
	@ParameterizedTest
	@EnumSource(ArticleSearchType.class)
	void testGetArticlesWithSearchValue(ArticleSearchType searchType) throws Exception {
		Pageable pageable = Pageable.ofSize(5);
		List<Integer> paginationBarNumbers = List.of(0, 1, 2, 3, 4);
		String searchValue = "test";

		given(articleService.findArticles(any(ArticleSearchType.class), any(String.class), any(Pageable.class))).willReturn(Page.empty(pageable));
		given(paginationService.getPaginationNumbers(anyInt(), anyInt())).willReturn(paginationBarNumbers);

		mockMvc.perform(get("/article")
				.queryParam("articleSearchType", searchType.name())
				.queryParam("searchValue", searchValue)
			)
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
			.andExpect(model().attributeExists("articles"))
			.andExpect(model().attributeExists("paginationNumbers"))
			.andExpect(model().attributeExists("articleSearchTypes"));

		then(articleService).should().findArticles(any(ArticleSearchType.class), any(String.class), any(Pageable.class));
		then(paginationService).should().getPaginationNumbers(0, Page.empty(pageable).getTotalPages());
	}

	@DisplayName("로그인 없이 특정 Article 페이지 반환 테스트")
	@Test
	void testGetArticleDetailViewWithoutLogin() throws Exception {
		Long articleId = 1L;
		given(articleService.findArticle(1L)).willReturn(
			DtoConverter.fromArticleWithComment(testDataGenerator.generateArticle("test")));

		mockMvc.perform(get("/article/" + articleId))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrlPattern("**/login"));

		then(articleService).shouldHaveNoInteractions();
	}

	@DisplayName("특정 Article 페이지 반환 테스트")
	@WithMockUser
	@Test
	void testGetArticleDetailView() throws Exception {
		Long articleId = 1L;
		given(articleService.findArticle(1L)).willReturn(
			DtoConverter.fromArticleWithComment(testDataGenerator.generateArticle("test")));

		mockMvc.perform(get("/article/" + articleId))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
			.andExpect(model().attributeExists("article"))
			.andExpect(model().attributeExists("comments"));

		then(articleService).should().findArticle(1L);
	}

	@DisplayName("특정 Article 삭제 요청 테스트")
	@WithUserDetails(value = MEMBER_MAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
	@Test
	void testDeleteArticle() throws Exception {
		Long articleId = 1L;

		willDoNothing().given(articleService).deleteArticle(articleId, MEMBER_MAIL);

		mockMvc.perform(delete("/article/" + articleId)
				.with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/article"))
			.andExpect(redirectedUrl("/article"));

		then(articleService).should().deleteArticle(articleId, MEMBER_MAIL);
	}

	@Disabled
	@DisplayName("검색 페이지(임시) 반환 테스트")
	@Test
	void testGetArticleSearchView() throws Exception {
		mockMvc.perform(get("/article/search"))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
	}

	@DisplayName("Hashtag 검색 페이지 반환 테스트")
	@Test
	void testGetArticleHashtagSearchView() throws Exception {
		String searchValue = "hashtag1";
		Pageable pageable = Pageable.ofSize(10);
		List<Integer> paginationBarNumbers = List.of(0, 1, 2, 3, 4);

		given(articleService.findArticlesViaHashtag(eq(searchValue), any(Pageable.class))).willReturn(Page.empty(pageable));
		given(paginationService.getPaginationNumbers(anyInt(), anyInt())).willReturn(paginationBarNumbers);

		mockMvc.perform(get("/article/search-hashtag")
				.queryParam("searchValue", searchValue)
			)
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
			.andExpect(view().name("article/search-hashtag"))
			.andExpect(model().attribute("articles", Page.empty(pageable)))
			.andExpect(model().attributeExists("paginationNumbers"))
			.andExpect(model().attributeExists("hashtags"))
			.andExpect(model().attribute("articleSearchType", ArticleSearchType.HASHTAG));

		then(articleService).should().findArticlesViaHashtag(eq(searchValue), any(Pageable.class));
	}

	@DisplayName("게시글 수정 페이지 반환 테스트")
	@WithUserDetails(value = MEMBER_MAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
	@Test
	void testUpdateFormView() throws Exception {
		Long articleId = 1L;
		ArticleWithCommentDto article = DtoConverter.fromArticleWithComment(testDataGenerator.generateArticle("update test"));

		given(articleService.findArticle(articleId)).willReturn(article);

		mockMvc.perform(get("/article/update-view/" + articleId))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
			.andExpect(view().name("article/form"))
			.andExpect(model().attribute("formType", FormType.UPDATE))
			.andExpect(model().attributeExists("article"));

		then(articleService).should().findArticle(articleId);
	}

	@DisplayName("권한 없는 게시글 수정 페이지 반환 테스트")
	@Test
	void testUpdateFormViewWithoutAuth() throws Exception {
		Long articleId = 1L;
		ArticleWithCommentDto article = DtoConverter.fromArticleWithComment(testDataGenerator.generateArticle("update test"));

		given(articleService.findArticle(articleId)).willReturn(article);

		mockMvc.perform(get("/article/update-view/" + articleId))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrlPattern("**/login"));

		then(articleService).shouldHaveNoInteractions();
	}

	@DisplayName("게시글 수정 요청 테스트")
	@WithUserDetails(value = MEMBER_MAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
	@Test
	void testUpdateForm() throws Exception {
		Long articleId = 1L;
		ArticleUpdateDto articleUpdateDto = new ArticleUpdateDto(1L, "test", "test2");
		MultiValueMap<String, String> formParams = new LinkedMultiValueMap<>();

		formParams.add("id", Long.toString(articleUpdateDto.id()));
		formParams.add("title", articleUpdateDto.title());
		formParams.add("content", articleUpdateDto.content());

		willDoNothing().given(articleService).updateArticle(any(ArticleUpdateDto.class), anyString(), anySet());

		mockMvc.perform(post("/article/update-view/" + articleId)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.queryParams(formParams)
				.with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/article/" + articleId))
			.andExpect(redirectedUrl("/article/" + articleId));

		then(articleService).should().updateArticle(any(ArticleUpdateDto.class), anyString(), anySet());
	}

	@DisplayName("권한 없는 게시글에 대한 수정 요청 테스트")
	@Test
	void testUpdateFormWithoutAuth() throws Exception {
		Long articleId = 1L;
		ArticleUpdateDto articleUpdateDto = new ArticleUpdateDto(1L, "test", "test2");
		MultiValueMap<String, String> formParams = new LinkedMultiValueMap<>();

		formParams.add("id", Long.toString(articleUpdateDto.id()));
		formParams.add("title", articleUpdateDto.title());
		formParams.add("content", articleUpdateDto.content());

		willDoNothing().given(articleService).updateArticle(any(ArticleUpdateDto.class), anyString(), anySet());

		mockMvc.perform(post("/article/update-view/" + articleId)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.queryParams(formParams)
				.with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrlPattern("**/login"));

		then(articleService).shouldHaveNoInteractions();
	}

	@DisplayName("게시글 생성 페이지 요청 테스트")
	@WithMockUser
	@Test
	void testCreateView() throws Exception {
		mockMvc.perform(get("/article/create-view"))
			.andExpect(status().isOk())
			.andExpect(model().attribute("formType", FormType.CREATE))
			.andExpect(view().name("article/form"));
	}

	@DisplayName("로그인 없이 게시글 생성 페이지 요청 테스트")
	@Test
	void testCreateViewWithoutLogin() throws Exception {
		mockMvc.perform(get("/article/create-view"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrlPattern("**/login"));
	}

	@DisplayName("게시글 생성 요청 테스트")
	@WithUserDetails(value = MEMBER_MAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
	@Test
	void testCreateForm() throws Exception {
		Article article = testDataGenerator.generateArticle("save test");

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

		params.add("title", article.getTitle());
		params.add("content", article.getContent());

		willDoNothing().given(articleService).saveArticle(any(ArticleCreateRequest.class), eq(MEMBER_MAIL), anySet());

		mockMvc.perform(post("/article/create-view")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.queryParams(params)
				.with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/article"))
			.andExpect(redirectedUrl("/article"));

		then(articleService).should().saveArticle(any(ArticleCreateRequest.class), eq(MEMBER_MAIL), anySet());
	}
}