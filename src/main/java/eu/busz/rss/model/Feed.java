package eu.busz.rss.model;

import lombok.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
@XmlRootElement(name = "feed")
@XmlAccessorType(XmlAccessType.FIELD)
public class Feed {
    private int guid;
    private String author;
    private String title;
    private String description;
    private String pubDate;
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
}
