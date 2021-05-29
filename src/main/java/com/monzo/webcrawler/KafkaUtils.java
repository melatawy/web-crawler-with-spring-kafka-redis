package com.monzo.webcrawler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

@Component
public class KafkaUtils {
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public KafkaUtils(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessageAsKey(String topic, String msg) {
        ListenableFuture<SendResult<String, String>> future =
                kafkaTemplate.send(topic, msg, msg);

//		future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
//
//			@Override
//			public void onSuccess(SendResult<String, String> result) {
//				System.out.println("Sent producer record=[" + result.getProducerRecord() +
//						"] and with metadata=[" + result.getRecordMetadata() + "]");
//			}
//			@Override
//			public void onFailure(Throwable ex) {
//				System.out.println("Unable to send message=["
//						+ msg + "] due to : " + ex.getMessage());
//			}
//		});
    }
}
