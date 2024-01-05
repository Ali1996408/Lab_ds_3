package com.han.gateway.service;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;

@Slf4j
public class RequestProcessConsumer implements Runnable {
    private final BlockingQueue<RequestProcessor<?, ?>> requests;
    private final long timeOut;

    public RequestProcessConsumer(BlockingQueue<RequestProcessor<?, ?>> requests, long timeOut) {
        this.requests = requests;
        this.timeOut = timeOut;
    }

    @Override
    public void run() {
        log.info("Run process request");
        try {
            log.info("starts waiting");
            Thread.sleep(this.timeOut);
            log.info("stop waiting");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        consumeRequest();

    }

    private void consumeRequest() {
        try {
            log.info("take request");
            RequestProcessor<?, ?> request = requests.take();
            try {
                log.info("running request");
                request.run();
            } catch (Exception e) {
                requests.put(request);
                log.info("service still unavailable , so re-put request");
                run();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
