package eu.busz.rss.model.endpoint;

import eu.busz.rss.model.feed.Feed;
import eu.busz.rss.model.feed.Feeds;
import eu.busz.rss.model.xml.XmlModelParser;
import eu.busz.rss.persitance.FeedRepository;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static java.util.Arrays.asList;

public class FeedEndpoint {

    private final FeedRepository repository;
    private final XmlModelParser parser;

    @Inject
    public FeedEndpoint(FeedRepository repository, XmlModelParser parser) {
        this.repository = repository;
        this.parser = parser;
    }

    public void getXmlFeeds(RoutingContext request) {
        getFeeds(request, this::encodeFeedsToXml);
    }

    public void getJsonFeeds(RoutingContext request) {
        getFeeds(request, Json::encode);
    }

    private void getFeeds(RoutingContext request, Function<Feeds, String> encoder) {
        if (request.request().getParam("latest") != null) {
            respondWithLatestFeed(request, encoder);
        } else {
            Feeds feeds = getAllFeeds();
            request.response().end(encoder.apply(feeds));
        }
    }

    public void getXmlFeedById(RoutingContext request) {
        getFeedById(request, this::encodeFeedToXml);
    }

    public void getJsonFeedById(RoutingContext request) {
        getFeedById(request, Json::encode);
    }

    private void getFeedById(RoutingContext request, Function<Feed, String> encoder) {
        String id = request.request().getParam("id");
        Optional<Feed> feed = repository.getFeed(id);

        if (!feed.isPresent()) {
            request.response().setStatusCode(404).end();
        }
        feed.ifPresent(res -> request.response().end(encoder.apply(res)));
    }

    public void respondWithLatestFeed(RoutingContext request, Function<Feeds, String> encoder) {
        Optional<Feed> latestFeed = repository.getLatestFeed();

        if (!latestFeed.isPresent()) {
            request.response().setStatusCode(404).end();
        }
        latestFeed.ifPresent(feed -> {
            Feeds feeds = new Feeds(asList(feed));
            request.response().end(encoder.apply(feeds));
        });
    }

    private String encodeFeedToXml(Feed res) {
        return parser.serialize(res, Feed.class).getContent();
    }

    private String encodeFeedsToXml(Feeds res) {
        return parser.serialize(res, Feeds.class).getContent();
    }

    private Feeds getAllFeeds() {
        List<Feed> feeds = repository.getFeeds();
        return new Feeds(feeds);
    }
}
