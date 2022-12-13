package com.guardjo.simpleboard.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class PaginationService {
    private static final int DEFAULT_PAGE_BAR_SIZE = 5;

    /**
     * 현재 페이지 수와 총 페이지 수를 기반으로 페이징 바 내 부여할 숫자 목록 반환
     * @param currentPageNumber 현재 페이지
     * @param totalPageNumber 총 페이지
     * @return 현재 페이지와 인접한 페이지 목록
     */
    public List<Integer> getPaginationNumbers(int currentPageNumber, int totalPageNumber) {
        int startNumber = Math.max(currentPageNumber - (DEFAULT_PAGE_BAR_SIZE / 2), 0);
        int finishNumber = Math.min(startNumber + DEFAULT_PAGE_BAR_SIZE, totalPageNumber);

        return IntStream.range(startNumber, finishNumber).boxed().collect(Collectors.toList());
    }

    public int getPageBarSize() {
        return DEFAULT_PAGE_BAR_SIZE;
    }
}
