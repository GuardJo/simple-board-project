package com.guardjo.simpleboard.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = PaginationService.class)
class PaginationServiceTest {
    private final PaginationService paginationService;

    PaginationServiceTest(@Autowired PaginationService paginationService) {
        this.paginationService = paginationService;
    }

    @DisplayName("현재 페이지와 인접한 페이지 항목 반환 테스트")
    @ParameterizedTest(name = "[{index}], {0}, {1} -> {2}")
    @MethodSource("getTestParams")
    void testGetPageNumbers(int currentPageNumber, int totalPageNumber, List<Integer> expected) {
        List<Integer> actual = paginationService.getPaginationNumbers(currentPageNumber, totalPageNumber);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("페이지 바에서 표기할 페이지의 총 수")
    @Test
    void testGetPageBarSize() {
        int pageBarSize = paginationService.getPageBarSize();

        assertThat(pageBarSize).isEqualTo(5);
    }

    private static Stream<Arguments> getTestParams() {
        return Stream.of(
                arguments(0, 10, List.of(0, 1, 2, 3, 4)),
                arguments(1, 10, List.of(0, 1, 2, 3, 4)),
                arguments(2, 10, List.of(0, 1, 2, 3, 4)),
                arguments(3, 10, List.of(1, 2, 3, 4, 5)),
                arguments(4, 10, List.of(2, 3, 4, 5, 6)),
                arguments(5, 10, List.of(3, 4, 5, 6, 7)),
                arguments(6, 10, List.of(4, 5, 6, 7, 8)),
                arguments(7, 10, List.of(5, 6, 7, 8, 9)),
                arguments(8, 10, List.of(6, 7, 8, 9)),
                arguments(9, 10, List.of(7, 8, 9))
        );
    }
}