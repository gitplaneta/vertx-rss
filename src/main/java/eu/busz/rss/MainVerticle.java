package eu.busz.rss;

import io.vertx.core.AbstractVerticle;

public class MainVerticle extends AbstractVerticle {

    @Override
    public void start(io.vertx.core.Future<Void> startFuture) {
        vertx.createHttpServer()
                .requestHandler(r -> {
                    r.response().end("Hello world");
                })
                .listen(8080, r -> {
                    if (r.succeeded()) {
                        startFuture.complete();
                    } else {
                        startFuture.fail(r.cause());
                    }
                });
    }
}
