package eu.busz.rss.persitance;

import com.google.common.collect.ImmutableList;
import eu.busz.rss.model.feed.FeedItem;

import java.util.List;
import java.util.Optional;

public class FeedRepository {

    private final List<FeedItem> feedItems;

    public FeedRepository(List<FeedItem> feedItems) {
        this.feedItems = feedItems;
    }

    public List<FeedItem> getFeedItems() {
        return ImmutableList.copyOf(feedItems);
    }

    public Optional<FeedItem> getFeed(String id) {
        return feedItems.stream()
                .filter(feed -> feed.getGuid().equals(id))
                .findFirst();
    }

    public Optional<FeedItem> getLatestFeed() {
        return feedItems.stream()
                .sorted((f1, f2) -> f2.getPubDate().compareTo(f1.getPubDate()))
                .findFirst();
    }
}
