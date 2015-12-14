package se.sysmaps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.xml.sax.SAXException;
import se.sysmaps.entities.Artiklar;
import se.sysmaps.repositories.ArtikelRepository;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.helpers.DefaultValidationEventHandler;
import java.io.File;

/**
 * Created by eboh on 08/11/15.
 */
@SpringBootApplication
public class Application {

    @Autowired
    ArtikelRepository artikelRepository;

    public static void main(String[] args) throws JAXBException, SAXException {
        final ApplicationContext ctx = SpringApplication.run(Application.class, args);
        final Application app = ctx.getBean(Application.class);
        app.dumpToDatabase();

    }

    public void dumpToDatabase() throws JAXBException, SAXException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Artiklar.class);

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        unmarshaller.setEventHandler(new DefaultValidationEventHandler());
        File file = new File(getClass().getClassLoader().getResource("products.xml").getFile());
        Artiklar artiklar = (Artiklar) unmarshaller.unmarshal(file);

     /*   Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(artiklar, System.out);*/

        artikelRepository.save(artiklar.getArtikel());

        System.out.println("Number of articles: "+artikelRepository.count());
    }

}