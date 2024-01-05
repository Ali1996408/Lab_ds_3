package com.han.gateway.faulttolerance;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Timer;
import java.util.TimerTask;

@Getter
@Setter
@Slf4j
public class FTCircuitBreaker {
    private static int count = 0;

    private FTCircuitBreakerState state;

    private int failsCounter;
    private int maxFails;

    private Timer retryTimer;
    private TimerTask retryTimerTask;
    private long retryTime;

    public FTCircuitBreaker() {
        state = FTCircuitBreakerState.CLOSED;
        failsCounter = 0;
        maxFails = 3;
        retryTime = 10 * 1000;
    }

    public void setRetryTimerTask(TimerTask task) {
        retryTimerTask = task;
    }

    public void onSuccess() {
        log.info("On success");
        if (state.equals(FTCircuitBreakerState.HALF_OPEN)) {
            state = FTCircuitBreakerState.CLOSED;
            failsCounter = 0;
        }
    }


    private void openCircuitBreaker() {
        log.info("Open cb");
        state = FTCircuitBreakerState.OPEN;
        retryTimer = new Timer("RetryTimer_" + (count++));
        retryTimer.schedule(retryTimerTask, retryTime);
    }

    public void onFail() {
        log.info("On fail");
        if (state.equals(FTCircuitBreakerState.CLOSED)) {
            failsCounter++;
            if (failsCounter == maxFails) {
                log.info("reach max fails count");
                openCircuitBreaker();
            }
        } else if (state.equals(FTCircuitBreakerState.HALF_OPEN)) {
            log.info("Half open -> open");
            openCircuitBreaker();
        }
    }

}
