package eu.busz.rss.eu.busz.rss.unit.feed;

import eu.busz.rss.model.Enclosure;
import eu.busz.rss.model.Feed;
import eu.busz.rss.model.Feeds;
import eu.busz.rss.model.xml.Xml;
import eu.busz.rss.model.xml.XmlFeedParser;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import static java.util.Arrays.asList;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class FeedXmlParsingTest {

    @Test
    public void checkJaxBMappingBySerializingAndDeserializingFeedsThenCheckingEquality() throws JAXBException {
        XmlFeedParser parser = new XmlFeedParser(JAXBContext.newInstance(Feeds.class));
        Feed feed = Feed.builder()
                .guid(220094)
                .author("xfm.editorial@xfm.co.uk")
                .title("Episode 4 - Denzel Washington, Kelly Jones, Lazy PR")
                .description("dafdsf")
                .enclosure(Enclosure.builder()
                        .url("http://fs.geronimo.thisisglobal.com/audio/080e70957410405c87cc83176c977a78.mp3")
                        .type("audio/mpeg")
                        .length("12344")
                        .build())
                .pubDate("Thu, 30 Jul 2015 23:00:00 GMT")
                .iTunesSubtitle("...")
                .iTunesAuthor("XFM")
                .iTunesExplicit("no")
                .iTunesSummary("adsfasdf")
                .iTunesDuration("0:45:58")
                .build();
        Feeds originalFeeds = new Feeds(asList(feed));

        Xml xmlFeeds = parser.serialize(originalFeeds);
        Feeds deserializedFeeds = parser.deserializeFeeds(xmlFeeds);

        assertThat("Object before marshalling should be equal to marshalled and unmarshalled object",
                deserializedFeeds, equalTo(originalFeeds));
    }
}
