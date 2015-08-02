package eu.busz.rss.model.xml;

import eu.busz.rss.model.feed.Feed;
import eu.busz.rss.model.feed.Feeds;
import eu.busz.rss.model.feed.RssRoot;
import lombok.SneakyThrows;

import javax.inject.Inject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

public class XmlModelParser {

    @SneakyThrows(JAXBException.class)
    public <T> Xml serialize(T object, Class<T> clazz) {
        JAXBContext context = JAXBContext.newInstance(clazz);
        StringWriter sw = new StringWriter();

        context.createMarshaller().marshal(object, sw);
        return new Xml(sw.toString());
    }

    @SneakyThrows(JAXBException.class)
    public <T> T deserialize(Xml xml, Class<T> clazz) {
        JAXBContext context = JAXBContext.newInstance(clazz);
        StringReader xmlReader = new StringReader(xml.getContent());

        return (T)context.createUnmarshaller().unmarshal(xmlReader);
    }
}
