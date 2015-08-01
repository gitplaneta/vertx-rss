package eu.busz.rss.model.xml;

import eu.busz.rss.model.Feeds;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

@RequiredArgsConstructor
public class XmlFeedParser {

    private final JAXBContext jaxbContext;
    private Marshaller marshaller;
    private Unmarshaller unmarshaller;

    @SneakyThrows(JAXBException.class)
    public Xml serialize(Feeds originalFeeds) {
        if (marshaller == null) {
            marshaller = jaxbContext.createMarshaller();
        }
        StringWriter sw = new StringWriter();
        marshaller.marshal(originalFeeds, sw);

        return new Xml(sw.toString());
    }

    @SneakyThrows(JAXBException.class)
    public Feeds deserializeFeeds(Xml xmlFeeds) {
        if (unmarshaller == null) {
            unmarshaller = jaxbContext.createUnmarshaller();
        }
        return (Feeds) unmarshaller.unmarshal(new StringReader(xmlFeeds.getContent()));
    }
}
