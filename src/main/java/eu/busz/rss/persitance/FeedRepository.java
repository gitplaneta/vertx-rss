package eu.busz.rss.persitance;

import com.google.common.collect.ImmutableList;
import eu.busz.rss.model.feed.Feed;

import java.util.List;
import java.util.Optional;

public class FeedRepository {

    private final List<Feed> feeds;

    public FeedRepository(List<Feed> feeds) {
        this.feeds = feeds;
    }

    public List<Feed> getFeeds() {
        return ImmutableList.copyOf(feeds);
    }

    public Optional<Feed> getFeed(String id) {
        return feeds.stream()
                .filter(feed -> feed.getGuid().equals(id))
                .findFirst();
    }

    public Optional<Feed> getLatestFeed() {
        return feeds.stream()
                .sorted((f1, f2) -> f2.getPubDate().compareTo(f1.getPubDate()))
                .findFirst();
    }
}
