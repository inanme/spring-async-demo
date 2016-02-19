package example.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/rest")
public class AsyncController {

    @RequestMapping({"/async"})
    public DeferredResult<Long> async() {
        DeferredResult<Long> deferred = new DeferredResult<>();
        long start = System.currentTimeMillis();
        CompletableFuture<Long> result = CompletableFuture.supplyAsync(() -> new Service(2l).call());
        result.whenComplete((stamp, ex) -> {
            if (ex != null) {
                deferred.setErrorResult(ex);
            } else {
                deferred.setResult(stamp - start);
            }
        });
        return deferred;
    }

    @RequestMapping({"/sync"})
    public Long sync() {
        long start = System.currentTimeMillis();
        try {
            Thread.sleep(Math.round(Math.random() * 200 + 1900));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return System.currentTimeMillis() - start;
    }
}


class Service implements Callable<Long> {

    private final Long value;

    public Service(Long value) {
        this.value = value;
    }

    @Override
    public Long call() {
        try {
            TimeUnit.SECONDS.sleep(value);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return System.currentTimeMillis();
    }
}


