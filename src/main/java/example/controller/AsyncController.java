package example.controller;

import java.util.*;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
@RequestMapping("/rest")
public class AsyncController {

    /**
     * Hello world, asynchronously
     *
     * @return a promise (deferred) of the result
     */
    @RequestMapping({"/hello"})
    public DeferredResult<Map<String, String>> hello() {
        DeferredResult<Map<String, String>> deferred = new DeferredResult<>();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Map<String, String> result = new HashMap<>();
                result.put("hello", "WORLD");
                deferred.setResult(result);
            }
        }, Math.round(Math.random() * 200 + 1900));

        return deferred;
    }

    /**
     * Traditional hello world, blocking.
     *
     * @return json object of the result
     */
    @RequestMapping({"/blocking-hello"})
    public Map<String, String> blockingHello() {
        Map<String, String> result = new HashMap<>();
        try {
            Thread.sleep(Math.round(Math.random() * 200 + 1900));
            result.put("hello", "blocking");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
}
