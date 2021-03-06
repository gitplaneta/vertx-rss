package eu.busz.rss.util;

import eu.busz.rss.model.feed.FeedItems;
import eu.busz.rss.model.feed.RssRoot;
import eu.busz.rss.model.xml.Xml;
import eu.busz.rss.model.xml.XmlModelParser;
import org.apache.commons.io.IOUtils;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;

public class FixtureLoader {

    private static final String FIXTURE_FILE = "fixture.xml";
    private final XmlModelParser xmlParser;

    @Inject
    public FixtureLoader(XmlModelParser xmlParser) {
        this.xmlParser = xmlParser;
    }

    public FeedItems readFeedsFixture() throws IOException {
        String feedFixture = IOUtils.toString(getFixtureFileStream());
        Xml xml = new Xml(feedFixture);
        RssRoot rssRoot = xmlParser.deserialize(xml, RssRoot.class);

        return rssRoot.getFeedItems();
    }

    private InputStream getFixtureFileStream() {
        ClassLoader classLoader = getClass().getClassLoader();
        return classLoader.getResourceAsStream(FIXTURE_FILE);
    }

}
