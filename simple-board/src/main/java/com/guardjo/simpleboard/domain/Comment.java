package com.guardjo.simpleboard.domain;

import java.time.LocalDateTime;

public class Comment {
    private long id;
    private Article article;
    private String content;
    private String hashtag;
    private String creator;
    private LocalDateTime createTime;
    private String modifier;
    private LocalDateTime modifiedTime;
}
