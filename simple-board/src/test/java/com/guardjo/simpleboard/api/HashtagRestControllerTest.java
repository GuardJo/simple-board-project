package com.guardjo.simpleboard.api;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guardjo.simpleboard.config.TestSecurityConfig;
import com.guardjo.simpleboard.dto.HashtagDto;
import com.guardjo.simpleboard.service.HashtagService;

@Import(TestSecurityConfig.class)
@WebMvcTest(controllers = HashtagRestController.class)
class HashtagRestControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private HashtagService hashtagService;

	@DisplayName("GET : " + UrlContext.HASHTAGS_URL)
	@Test
	void test_getHashtags() throws Exception {
		List<HashtagDto> expected = List.of(
			HashtagDto.of(1L, "test1"),
			HashtagDto.of(2L, "test2"),
			HashtagDto.of(3L, "test3")
		);

		given(hashtagService.findAllHashtags()).willReturn(expected);

		String response = mockMvc.perform(get(UrlContext.HASHTAGS_URL)
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andReturn()
			.getResponse()
			.getContentAsString(StandardCharsets.UTF_8);

		JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, HashtagDto.class);
		List<HashtagDto> actual = objectMapper.readValue(response, javaType);

		assertThat(actual.size()).isEqualTo(expected.size());

		for (int i = 0; i < actual.size(); i++) {
			assertThat(actual.get(i)).isEqualTo(expected.get(i));
		}

		then(hashtagService).should().findAllHashtags();
	}
}