package eu.busz.rss.model;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "items")
@XmlAccessorType (XmlAccessType.FIELD)
@EqualsAndHashCode
public class Feeds {

    @XmlElement(name = "item")
    private List<Feed> feeds;

    public List<Feed> getFeeds() {
        return ImmutableList.copyOf(feeds);
    }
}
