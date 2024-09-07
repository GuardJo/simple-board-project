package com.guardjo.simpleboard.api;

import java.util.Objects;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guardjo.simpleboard.dto.security.SimpleBoardPrincipal;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class MemberRestController {
	@GetMapping(UrlContext.ME_URL)
	public String findMe(@AuthenticationPrincipal SimpleBoardPrincipal principal) {
		if (Objects.isNull(principal)) {
			throw new SecurityException("Not Found Principal");
		}

		log.debug("Request FindMe, username = {}", principal.getUsername());
		return principal.getNickName();
	}
}
