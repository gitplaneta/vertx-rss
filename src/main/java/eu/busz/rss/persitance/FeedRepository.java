package eu.busz.rss.persitance;

import com.google.common.collect.ImmutableList;
import eu.busz.rss.model.feed.FeedItem;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.Collectors.toList;

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

    public List<FeedItem> getEveryOtherFeed() {
        AtomicInteger index = new AtomicInteger();
        return feedItems.stream()
                .filter(feed -> index.getAndIncrement() % 2 == 0)
                .collect(toList());
    }
}
