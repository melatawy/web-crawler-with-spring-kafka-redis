package com.monzo.webcrawler.redis;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebPageContentRepo extends CrudRepository<WebPageContent, String> {
}
