package com.guardjo.simpleboard.api;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import com.guardjo.simpleboard.config.TestSecurityConfig;

@Import(TestSecurityConfig.class)
@WebMvcTest(controllers = MemberRestController.class)
class MemberRestControllerTest {
	private final static String TEST_USER_MAIL = "test@mail.com";
	private final static String TESTER_NAME = "tester";

	@Autowired
	private MockMvc mockMvc;

	@DisplayName("GET : " + UrlContext.ME_URL)
	@WithUserDetails(value = TEST_USER_MAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
	@Test
	void test_findMe() throws Exception {
		String response = mockMvc.perform(get(UrlContext.ME_URL)
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andReturn()
			.getResponse()
			.getContentAsString(StandardCharsets.UTF_8);

		assertThat(response).isEqualTo(TESTER_NAME);
	}

	@DisplayName("GET : " + UrlContext.ME_URL + " : Failed")
	@Test
	void test_findMe_NoAuth() throws Exception {
		mockMvc.perform(get(UrlContext.ME_URL)
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isUnauthorized());
	}
}