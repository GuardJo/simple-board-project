package com.guardjo.simpleboard.domain.projection;

import com.guardjo.simpleboard.domain.Member;
import org.springframework.data.rest.core.config.Projection;

import java.time.LocalDateTime;

@Projection(name = "externalMember", types = Member.class)
public interface MemberProjection {
    String getCreator();
    LocalDateTime getCreateTime();
    String getModifier();
    LocalDateTime getModifiedTime();
    Long getMemberId();
    String getEmail();
    String getName();
}
