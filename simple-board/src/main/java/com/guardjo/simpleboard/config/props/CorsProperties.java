package com.guardjo.simpleboard.config.props;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "cors-props")
public record CorsProperties(
        List<String> origins
) {
}
