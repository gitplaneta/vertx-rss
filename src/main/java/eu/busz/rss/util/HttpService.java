package eu.busz.rss.util;

import io.vertx.core.Vertx;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class HttpService {

    private final Vertx vertx;

    public CompletableFuture<String> get(int port, String host, String path) {
        CompletableFuture<String> future = new CompletableFuture<>();
        vertx.createHttpClient()
                .getNow(port, host, path,
                        r -> r.handler(body -> future.complete(body.toString())))
                .close();
        return future;
    }
}
