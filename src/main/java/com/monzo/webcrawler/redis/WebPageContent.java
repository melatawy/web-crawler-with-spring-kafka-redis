package com.monzo.webcrawler.redis;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Getter
@Builder
@RedisHash("WebPageContent")
public class WebPageContent implements Serializable {
    @Id
    private String url;
    @Setter
    private String pageContent;
}
