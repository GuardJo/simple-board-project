package com.guardjo.simpleboard.api;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guardjo.simpleboard.dto.HashtagDto;
import com.guardjo.simpleboard.service.HashtagService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
public class HashtagRestController {
	private final HashtagService hashtagService;

	@GetMapping(UrlContext.HASHTAGS_URL)
	public List<HashtagDto> getHashtags() {
		log.info("GET : " + UrlContext.HASHTAGS_URL);

		return hashtagService.findAllHashtags();
	}
}
