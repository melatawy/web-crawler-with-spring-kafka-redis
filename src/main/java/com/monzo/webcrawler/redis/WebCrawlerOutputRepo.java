package com.monzo.webcrawler.redis;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebCrawlerOutputRepo extends CrudRepository<WebCrawlerOutput, String> {
    Iterable<WebCrawlerOutput> findAllByBaseUrl(String baseUrl);
}
