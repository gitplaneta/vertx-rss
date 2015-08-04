package eu.busz.rss.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Names;
import eu.busz.rss.persitance.FeedRepository;
import eu.busz.rss.util.FixtureLoader;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.file.FileSystem;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class FeedModule extends AbstractModule {

    private final Vertx vertx;

    @Override
    protected void configure() {
        bind(EventBus.class).toInstance(vertx.eventBus());
        bind(FileSystem.class).toInstance(vertx.fileSystem());
        Names.bindProperties(binder(), configProperties());
    }

    private Map<String, String> configProperties() {
        return vertx.getOrCreateContext().config()
                .stream()
                .map(pair -> new SimpleEntry<>(pair.getKey(), pair.getValue().toString()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Provides
    private FeedRepository provideFeedRepository(FixtureLoader fixtureLoader) throws IOException {
        return new FeedRepository(fixtureLoader.readFeedsFixture().getFeedItems());
    }
}
