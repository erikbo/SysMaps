package se.sysmaps.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.sysmaps.entities.Artikel;
import se.sysmaps.repositories.ArtikelRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by eboh on 20/11/15.
 */
@RestController
@RequestMapping("/meta")
public class MetaController {

    @Autowired
    ArtikelRepository artikelRepository;

    @RequestMapping("/categories")
    public Set<String> findAllCategories() {
        return artikelRepository.findAll().stream()
                .collect(Collectors.toMap(Artikel::getVarugrupp, artikel -> artikel, (first, second) -> first))
                .keySet();
    }

    @RequestMapping("/originCountry")
    public Set<String> findAllOriginCountries(){
        return artikelRepository.findAll().stream()
                .collect(Collectors.toMap(Artikel::getUrsprunglandnamn, artikel -> artikel, (first, second) -> first))
                .keySet();
    }

    @RequestMapping("/originYear")
    public List<String> findAllOriginYears(){
        return artikelRepository.findAll().stream()
                .collect(Collectors.toMap(Artikel::getArgang, artikel -> artikel, (first, second) -> first))
                .keySet().stream().filter(key -> !key.equals(""))
                .sorted((o1, o2) -> Integer.valueOf(o1) - Integer.valueOf(o2))
                .collect(Collectors.toList());
    }

}
