package com.monzo.webcrawler.redis;

import lombok.Builder;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.util.Set;

@Getter
@Builder
@RedisHash("WebCrawlerOutput")
public class WebCrawlerOutput implements Serializable {
    @Id
    private String url;
    @NotNull
    @Indexed
    private String baseUrl;
    private Set<String> children;
}
