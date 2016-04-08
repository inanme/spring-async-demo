package example.controller;


import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

class CommandModule {

    private static final Logger logger = LoggerFactory.getLogger(CommandModule.class);

    static class BuggyCommand extends HystrixCommand<String> {

        private final String name;

        private Random rng = new Random();

        BuggyCommand(String name) {
            super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
            this.name = name;
        }

        @Override
        protected String getFallback() {
            return "N/A";
        }

        @Override
        protected String run() {
            logger.debug("begin");
            int x = rng.nextInt(10);
            if (x == 1) {
                return "Hello " + name + "!";
            }
            throw new IllegalStateException("failed" + x);
        }
    }

    static class BetterCommand extends HystrixCommand<String> {

        private final String name;

        private Random rng = new Random();

        BetterCommand(String name) {
            super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
            this.name = name;
        }

        @Override
        protected String getFallback() {
            return "N/A";
        }

        @Override
        protected String run() {
            logger.debug("begin");
            int x = rng.nextInt(10);
            if (x > 1) {
                return "Hello " + name + "!";
            }
            throw new IllegalStateException("failed" + x);
        }
    }
}


