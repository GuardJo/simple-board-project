package com.guardjo.simpleboard.response;

import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

@Component
public class CommentUtil {
    public static Set<CommentResponse> sortComments(Set<CommentResponse> commentResponses) {
        Comparator comparator = Comparator.comparing(CommentResponse::createTime)
                .thenComparingLong(CommentResponse::id);

        Set<CommentResponse> sortComments = new TreeSet<>(comparator);
        sortComments.addAll(commentResponses);

        return sortComments;
    }
}
