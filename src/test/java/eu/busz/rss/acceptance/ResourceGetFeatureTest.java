package eu.busz.rss.acceptance;

import eu.busz.rss.FeedVerticle;
import eu.busz.rss.model.Feed;
import eu.busz.rss.model.Feeds;
import eu.busz.rss.model.xml.XmlFeedParser;
import eu.busz.rss.util.HttpService;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import lombok.SneakyThrows;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import static eu.busz.rss.util.Config.HOST;
import static eu.busz.rss.util.Config.PORT;

@RunWith(VertxUnitRunner.class)
public class ResourceGetFeatureTest {

    private static final int FEED_COUNT = 121;
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
        final String feedDescription = "As another week comes screeching to a halt on The Xfm Breakfast " +
                "Show with Jon Holmes, " +
                "another Podcast can now be tentatively released into the iTunes wild! But be careful, " +
                "it bites! Episode 4 sees DENZEL WASHINGTON play a silly game…and KELLY JONES answer some " +
                "rather ridiculous questions in 'Popstars Win Prizes'. Elsewhere, a bear attacks BBC 5 Live's " +
                "studio, a rival radio presenter unashamedly slags off our very own Matt Dyson, and the " +
                "listeners get creative with film prequels that never did – but should have – existed. " +
                "JUMP IN!";
        final Feed expectedFeed = Feed.builder()
                .guid(220094)
                .author("xfm.editorial@xfm.co.uk")
                .title("Episode 4 - Denzel Washington, Kelly Jones, Lazy PR")
                .description(feedDescription)
                .enclosure(null) //todo
                .pubDate("Thu, 30 Jul 2015 23:00:00 GMT")
                .iTunesSubtitle("...")
                .iTunesAuthor("XFM")
                .iTunesExplicit("no")
                .iTunesSummary(feedDescription)
                .iTunesDuration("0:45:58")
                .build();

        http.getXml(PORT, HOST, "/").thenAccept(xml -> {
            XmlFeedParser feedsDeserializer = new XmlFeedParser(getJaxbContext());
            Feeds feeds = feedsDeserializer.deserializeFeeds(xml);
            Feed actualFeed = feeds.getFeeds().stream()
                    .filter(feed -> feed.getGuid() == 218006)
                    .findFirst()
                    .get();

            context.assertEquals(expectedFeed, actualFeed);
            context.assertEquals(FEED_COUNT, feeds.getFeeds().size());
            async.complete();
        }).exceptionally(ex -> {
            context.fail(ex);
            return null;
        });
    }

    @SneakyThrows(JAXBException.class)
    private JAXBContext getJaxbContext() {
        return JAXBContext.newInstance(Feeds.class);
    }
}
