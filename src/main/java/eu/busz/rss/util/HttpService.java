package eu.busz.rss.util;

import eu.busz.rss.model.xml.Xml;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class HttpService {

    private final Vertx vertx;

    public CompletableFuture<String> get(int port, String host, String path) {
        CompletableFuture<String> future = new CompletableFuture<>();
        getClient()
                .getNow(port, host, path,
                        r -> r.handler(body -> future.complete(body.toString())))
                .close();
        return future;
    }

    public CompletableFuture<Xml> getXml(int port, String host, String path) {
        CompletableFuture<Xml> future = new CompletableFuture<>();
        getClient()
                .get(port, host, path, r -> r.handler(body -> future.complete(new Xml(body.toString()))))
                .putHeader("Content-Type", "application/xml")
                .end();
        return future;
    }

    private HttpClient getClient() {
        HttpClientOptions options = new HttpClientOptions();
        options.setKeepAlive(false);
        return vertx.createHttpClient(options);
    }
}
