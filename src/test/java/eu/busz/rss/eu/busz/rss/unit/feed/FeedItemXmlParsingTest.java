package eu.busz.rss.eu.busz.rss.unit.feed;

import eu.busz.rss.model.feed.Enclosure;
import eu.busz.rss.model.feed.FeedItem;
import eu.busz.rss.model.feed.FeedItems;
import eu.busz.rss.model.xml.FeedDateAdapter;
import eu.busz.rss.model.xml.Xml;
import eu.busz.rss.model.xml.XmlModelParser;
import org.junit.Test;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import static java.util.Collections.singletonList;
import static java.util.TimeZone.getTimeZone;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class FeedItemXmlParsingTest {

    @Test
    public void checkJaxBMappingBySerializingAndDeserializingFeedsThenCheckingEquality() {
        XmlModelParser parser = new XmlModelParser();
        FeedItem feedItem = FeedItem.builder()
                .guid("220094")
                .author("xfm.editorial@xfm.co.uk")
                .title("Episode 4 - Denzel Washington, Kelly Jones, Lazy PR")
                .description("dafdsf")
                .enclosure(Enclosure.builder()
                        .url("http://fs.geronimo.thisisglobal.com/audio/080e70957410405c87cc83176c977a78.mp3")
                        .type("audio/mpeg")
                        .length("12344")
                        .build())
                .pubDate(new Calendar.Builder()
                                .setDate(2015, 6, 30)
                                .setTimeOfDay(23, 0, 0)
                                .setTimeZone(getTimeZone("GMT"))
                                .build().getTime()
                )
                .iTunesSubtitle("...")
                .iTunesAuthor("XFM")
                .iTunesExplicit("no")
                .iTunesSummary("adsfasdf")
                .iTunesDuration("0:45:58")
                .build();
        FeedItems originalFeedItems = new FeedItems(singletonList(feedItem));

        Xml xmlFeeds = parser.serialize(originalFeedItems, FeedItems.class);
        FeedItems deserializedFeedItems = parser.deserialize(xmlFeeds, FeedItems.class);

        assertThat("Object before marshalling should be equal to marshalled and unmarshalled object",
                deserializedFeedItems, equalTo(originalFeedItems));
    }

    @Test
    public void jaxbDateAdapterCorrectlyMarshallsDate() throws ParseException {
        FeedDateAdapter adapter = new FeedDateAdapter();
        Date unmarshalledDate = adapter.unmarshal("Fri, 11 Jan 2013 00:00:00 GMT");
        Date expectedDate = new Calendar.Builder()
                .setDate(2013, 0, 11)
                .setTimeOfDay(0, 0, 0)
                .setTimeZone(getTimeZone("GMT"))
                .build().getTime();

        assertThat("Unmarshalled date should have the same date as input string",
                unmarshalledDate, equalTo(expectedDate));
    }
}
