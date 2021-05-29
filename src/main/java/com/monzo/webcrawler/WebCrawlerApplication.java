package com.monzo.webcrawler;

import com.monzo.webcrawler.redis.WebCrawlerOutput;
import com.monzo.webcrawler.redis.WebCrawlerOutputRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.net.URL;

@SpringBootApplication
@RestController
@EnableConfigurationProperties
public class WebCrawlerApplication {

	private KafkaTemplate<String, String> kafkaTemplate;
	private WebCrawlerOutputRepo webCrawlerOutputRepo;

	@Value(value = "${urls.topic.name}")
	private String urlsTopicName;

	@Autowired
	public WebCrawlerApplication(KafkaTemplate<String, String> kafkaTemplate, WebCrawlerOutputRepo webCrawlerOutputRepo) {
		this.kafkaTemplate = kafkaTemplate;
		this.webCrawlerOutputRepo = webCrawlerOutputRepo;
	}

	public static void main(String[] args) {
		SpringApplication.run(WebCrawlerApplication.class, args);
	}

	@GetMapping("/crawl")
	public String crawl(@RequestParam(value = "url", defaultValue = "") String url) throws MalformedURLException {
		URL urlObject = new URL(url);
		String baseUrl = urlObject.getProtocol() + "://" + urlObject.getHost();
		if (urlObject.getPort() > -1) {
			baseUrl = baseUrl + ":" + urlObject.getPort();
		}
		if (url.equals(baseUrl)) {
			url += "/";
		}
		baseUrl += "/";


		kafkaTemplate.send(urlsTopicName, url, baseUrl);
		return "Crawling: " + url;
	}

	@GetMapping("/crawlingResult")
	public String crawlingResult(@RequestParam(value = "baseUrl", defaultValue = "") String baseUrl) {
		Iterable<WebCrawlerOutput> allByBaseUrl = webCrawlerOutputRepo.findAllByBaseUrl(baseUrl);
		StringBuilder output = new StringBuilder("<h1>Crawling result for " + baseUrl + "</h1>");
		output.append("<ul>");
		for (WebCrawlerOutput o : allByBaseUrl) {
			output.append("<li>");
			output.append(o.getUrl());
			output.append("</li>");
			if (o.getChildren() != null && o.getChildren().size() > 0) {
				output.append("<ul>");
				for (String childUrl : o.getChildren()) {
					output.append("<li>");
					output.append(childUrl);
					output.append("</li>");
				}
				output.append("</ul>");
			}
		}
		output.append("</ul>");
		return output.toString();
	}

}
