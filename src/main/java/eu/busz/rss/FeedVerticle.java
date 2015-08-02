package eu.busz.rss;

import eu.busz.rss.guice.FeedModule;
import eu.busz.rss.model.endpoint.FeedEndpoint;
import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;

import javax.inject.Inject;

import static com.google.inject.Guice.createInjector;

public class FeedVerticle extends AbstractVerticle {

    @Inject
    private FeedEndpoint feedEndpoint;

    @Override
    public void start(io.vertx.core.Future<Void> startFuture) {
        createInjector(new FeedModule(vertx)).injectMembers(this);
        Router router = Router.router(vertx);

        router.get("/").produces("application/xml").handler(feedEndpoint::getXmlFeeds);
        router.get("/").produces("application/json").handler(feedEndpoint::getJsonFeeds);
        router.get("/:id").produces("application/xml").handler(feedEndpoint::getXmlFeedById);
        router.get("/:id").produces("application/json").handler(feedEndpoint::getJsonFeedById);

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(8080, r -> {
                    if (r.succeeded()) {
                        startFuture.complete();
                    } else {
                        startFuture.fail(r.cause());
                    }
                });
    }
}
