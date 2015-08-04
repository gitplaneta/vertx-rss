package eu.busz.rss.acceptance;

import eu.busz.rss.FeedVerticle;
import eu.busz.rss.model.feed.Enclosure;
import eu.busz.rss.model.feed.FeedItem;
import eu.busz.rss.model.feed.FeedItems;
import eu.busz.rss.model.xml.XmlModelParser;
import eu.busz.rss.util.HttpService;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.TimeZone;

import static eu.busz.rss.util.Config.HOST;
import static eu.busz.rss.util.Config.PORT;

@RunWith(VertxUnitRunner.class)
public class ResourceGetFeatureTest {

    private static final int FEED_COUNT = 127;
    private static final String EXAMPLE_GUID = "262985";
    private Vertx vertx;
    private HttpService http;

    @Before
    public void setUp(TestContext context) {
        vertx = Vertx.vertx();
        http = new HttpService(vertx);
        vertx.deployVerticle(FeedVerticle.class.getName(), context.asyncAssertSuccess());
    }

    @After
    public void tearDown(TestContext context) {
        vertx.close(context.asyncAssertSuccess());
    }

    @Test
    public void executeHttpGetOnAllResourcesAcceptXmlFormatAndCheckIfResponseHasElements(TestContext context) {
        final Async async = context.async();
        final String feedDescription = "Welcome, welcome to this here, the seventy-seventh edition of the XFM Breakfast Podcast. This week, Jon faces serious possible charges over a tea splillage, Noel Fielding drops by for a game of 'Band or Transformer' and we broadcast a public service announcement in search for a non-leather jacket-related title for Jon's book. T";
        final FeedItem expectedFeedItem = FeedItem.builder()
                .guid(EXAMPLE_GUID)
                .author("xfm.editorial@xfm.co.uk")
                .title("Episode 77 - Noel Fielding, Book Titles and Frank Doherty")
                .description(feedDescription)
                .enclosure(Enclosure.builder()
                        .url("http://fs.geronimo.thisisglobal.com/audio/c9f40041691a45a898a4e925fa261784.mp3?referredby=rss")
                        .length("38770688")
                        .type("audio/mpeg")
                        .build())
                .pubDate(new Calendar.Builder()
                        .setDate(2014, 6, 28)
                        .setTimeOfDay(23, 0, 0)
                        .setTimeZone(TimeZone.getTimeZone("GMT"))
                        .build().getTime())
                .iTunesSubtitle(feedDescription)
                .iTunesAuthor("XFM")
                .iTunesExplicit("no")
                .iTunesSummary(feedDescription)
                .iTunesDuration("0:40:22")
                .build();

        http.getXml(PORT, HOST, "/").thenAccept(xml -> {
            FeedItems feedItems = new XmlModelParser().deserialize(xml, FeedItems.class);
            FeedItem actualFeedItem = feedItems.getFeedItems().stream()
                    .filter(feed -> EXAMPLE_GUID.equals(feed.getGuid()))
                    .findFirst()
                    .get();

            context.assertEquals(expectedFeedItem, actualFeedItem);
            context.assertEquals(FEED_COUNT, feedItems.getFeedItems().size());
            async.complete();
        }).exceptionally(ex -> {
            context.fail(ex);
            return null;
        });
    }

    @Test
    public void executeHTTPGetToSelectFeedByIdThenCheckIfResourceIdMatches(TestContext context) {
        final Async async = context.async();
        final String expectedGuid = "218006";

        http.getXml(PORT, HOST, "/" + expectedGuid).thenAccept(xml -> {
            FeedItem feedItem = new XmlModelParser().deserialize(xml, FeedItem.class);
            context.assertEquals(expectedGuid, feedItem.getGuid());
            async.complete();
        }).exceptionally(ex -> {
            context.fail(ex);
            return null;
        });
    }

    @Test
    public void executeHTTPGetToSelectMostRecentElement(TestContext context) {
        final Async async = context.async();
        final String expectedGuid = "0ccc4d3c-c478-4574-afea-93ef056a8ec1";

        http.getXml(PORT, HOST, "/?latest").thenAccept(xml -> {
            FeedItems feedItems = new XmlModelParser().deserialize(xml, FeedItems.class);
            context.assertEquals(expectedGuid, feedItems.getFeedItems().get(0).getGuid());
            async.complete();
        }).exceptionally(ex -> {
            context.fail(ex);
            return null;
        });
    }

    @Test
    public void executeHTTPGetToSelectAlteringElements(TestContext context) {
        final Async async = context.async();
        final String FIRST_ELEMENT_GUID = "0ccc4d3c-c478-4574-afea-93ef056a8ec1";
        final String SECOND_ELEMENT_GUID = "e7c0d869-b365-48d6-8b8d-9e760817b184";

        http.getXml(PORT, HOST, "/?alternate").thenAccept(xml -> {
            FeedItems feedItems = new XmlModelParser().deserialize(xml, FeedItems.class);
            context.assertEquals(FIRST_ELEMENT_GUID, feedItems.getFeedItems().get(0).getGuid());
            context.assertEquals(SECOND_ELEMENT_GUID, feedItems.getFeedItems().get(1).getGuid());
            async.complete();
        }).exceptionally(ex -> {
            context.fail(ex);
            return null;
        });
    }
}
