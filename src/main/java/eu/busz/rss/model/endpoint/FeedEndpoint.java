package eu.busz.rss.model.endpoint;

import eu.busz.rss.model.feed.FeedItem;
import eu.busz.rss.model.feed.FeedItems;
import eu.busz.rss.model.xml.XmlModelParser;
import eu.busz.rss.persitance.FeedRepository;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public class FeedEndpoint {

    private static final String LATEST_QUERY_PARAM = "latest";
    private static final String ALTERNATE_QUERY_PARAM = "alternate";
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

    public void getXmlFeedById(RoutingContext request) {
        getFeedById(request, this::encodeFeedToXml);
    }

    public void getJsonFeedById(RoutingContext request) {
        getFeedById(request, Json::encode);
    }

    private void getFeeds(RoutingContext request, Function<FeedItems, String> encoder) {
        if (request.request().getParam(LATEST_QUERY_PARAM) != null) {
            respondWithLatestFeed(request, encoder);
        } else if (request.request().getParam(ALTERNATE_QUERY_PARAM) != null) {
            respondWithEveryOtherFeed(request, encoder);
        } else {
            FeedItems feedItems = getAllFeeds();
            request.response().end(encoder.apply(feedItems));
        }
    }

    private void getFeedById(RoutingContext request, Function<FeedItem, String> encoder) {
        String id = request.request().getParam("id");
        Optional<FeedItem> feed = repository.getFeed(id);

        if (!feed.isPresent()) {
            request.response().setStatusCode(404).end();
        }
        feed.ifPresent(res -> request.response().end(encoder.apply(res)));
    }

    private void respondWithLatestFeed(RoutingContext request, Function<FeedItems, String> encoder) {
        Optional<FeedItem> latestFeed = repository.getLatestFeed();

        if (!latestFeed.isPresent()) {
            FeedItems feedItems = new FeedItems(emptyList());
            request.response().end(encoder.apply(feedItems));
        }
        latestFeed.ifPresent(feed -> {
            FeedItems feedItems = new FeedItems(singletonList(feed));
            request.response().end(encoder.apply(feedItems));
        });
    }

    private void respondWithEveryOtherFeed(RoutingContext request, Function<FeedItems, String> encoder) {
        List<FeedItem> everyOtherFeed = repository.getEveryOtherFeed();
        FeedItems feedItems = new FeedItems(everyOtherFeed);
        request.response().end(encoder.apply(feedItems));
    }

    private String encodeFeedToXml(FeedItem res) {
        return parser.serialize(res, FeedItem.class).getContent();
    }

    private String encodeFeedsToXml(FeedItems res) {
        return parser.serialize(res, FeedItems.class).getContent();
    }

    private FeedItems getAllFeeds() {
        List<FeedItem> feedItems = repository.getFeedItems();
        return new FeedItems(feedItems);
    }
}
