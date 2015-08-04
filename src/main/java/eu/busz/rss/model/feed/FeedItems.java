package eu.busz.rss.model.feed;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "items")
@XmlAccessorType(XmlAccessType.FIELD)
@EqualsAndHashCode
public class FeedItems {

    @XmlElement(name = "item")
    private List<FeedItem> feedItems = new ArrayList<>();

    public List<FeedItem> getFeedItems() {
        return ImmutableList.copyOf(feedItems);
    }
}
