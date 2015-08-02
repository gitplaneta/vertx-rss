package eu.busz.rss.model.feed;

import eu.busz.rss.model.xml.FeedDateAdapter;
import lombok.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
@XmlRootElement(name = "feedItem")
@XmlAccessorType(XmlAccessType.FIELD)
public class FeedItem implements Comparable<FeedItem> {
    private String guid;
    private String author;
    private String title;
    private String description;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(FeedDateAdapter.class)
    private Date pubDate;
    private Enclosure enclosure;

    @XmlElement(namespace = "http://www.itunes.com/dtds/podcast-1.0.dtd", name = "subtitle")
    private String iTunesSubtitle;
    @XmlElement(namespace = "http://www.itunes.com/dtds/podcast-1.0.dtd", name = "author")
    private String iTunesAuthor;
    @XmlElement(namespace = "http://www.itunes.com/dtds/podcast-1.0.dtd", name = "explicit")
    private String iTunesExplicit;
    @XmlElement(namespace = "http://www.itunes.com/dtds/podcast-1.0.dtd", name = "summary")
    private String iTunesSummary;
    @XmlElement(namespace = "http://www.itunes.com/dtds/podcast-1.0.dtd", name = "duration")
    private String iTunesDuration;


    @Override
    public int compareTo(FeedItem o) {
        return this.getPubDate().compareTo(o.getPubDate());
    }
}
