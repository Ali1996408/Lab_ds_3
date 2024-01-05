package com.han.gateway.service;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;

@Slf4j
public class RequestProcessProducer {
    private final BlockingQueue<RequestProcessor<?, ?>> requests;

    public RequestProcessProducer(BlockingQueue<RequestProcessor<?, ?>> requests) {
        this.requests = requests;
    }

    public void addRequest(RequestProcessor<?, ?> request) {
        try {
            requests.put(request);
            log.info("request added");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
