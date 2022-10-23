package org.example;

import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import java.util.concurrent.*;
import java.util.stream.IntStream;

public class Main {

    public static final FutureExample FUTURE_EXAMPLE = new FutureExample();
    public static final Integer RANGE_END = Integer.MAX_VALUE / 200;

    public static void main(String[] args) {
        futures();
        sequentialStream();
        parallelStream();
        reactor();
        System.exit(0);
    }

    private static void sequentialStream() {
        long start = System.nanoTime();
        IntStream.range(1, RANGE_END)
                .boxed()
                .map((integer) -> "testing")
                .map(String::toUpperCase)
                .map(s -> new StringBuilder(s).reverse().toString())
                .allMatch(s -> true);
        double duration = (double) (System.nanoTime() - start) / 1_000_000_000;
        System.out.println("sequential stream duration:" + duration + "sec");
    }

    private static void parallelStream() {
        long start = System.nanoTime();
        IntStream.range(1, RANGE_END)
                .boxed()
                .parallel()
                .map((integer) -> "testing")
                .map(String::toUpperCase)
                .map(s -> new StringBuilder(s).reverse().toString())
                .allMatch(s -> true);
        double duration = (double) (System.nanoTime() - start) / 1_000_000_000;
        System.out.println("parallel stream duration:" + duration + "sec");
    }

    private static void futures() {
        long start = System.nanoTime();
        var collect = IntStream.range(1, RANGE_END)
                .boxed()
                .map((integer) -> "testing")
                .map(FUTURE_EXAMPLE::createTask)
                .toArray(CompletableFuture[]::new);
        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.allOf(collect);
        voidCompletableFuture.join();
        double duration = (double) (System.nanoTime() - start) / 1_000_000_000;
        System.out.println("futures duration:" + (duration) + "sec");
    }

    private static void reactor() {
        long start = System.nanoTime();
        Disposable subscribe = Flux.range(1, RANGE_END)
                .parallel()
                .map((integer) -> "testing")
                .map(String::toUpperCase)
                .map(s -> new StringBuilder(s).reverse().toString())
                .subscribe();
        subscribe.dispose();
        double duration = (double) (System.nanoTime() - start) / 1_000_000_000;
        System.out.println("reactor duration:" + (duration) + "sec");
    }


}