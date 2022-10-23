package org.example;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FutureExample {
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public CompletableFuture<String> createTask(String path) {
        return CompletableFuture.supplyAsync(() -> getPath(path), executorService)
                .thenApplyAsync(this::toUpper)
                .thenApplyAsync(this::reverse);
    }

    private String getPath(String path) {
        return path;
    }

    private String toUpper(String path) {
        return path.toUpperCase();
    }

    private String reverse(String path) {
        return new StringBuilder(path).reverse().toString();
    }

}
