package eu.busz.rss.model.feed;

import lombok.Getter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="rss")
public class RssRoot {

    @Getter
    @XmlElement(name = "channel")
    private FeedItems feedItems;
}
