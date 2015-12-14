package se.sysmaps.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import se.sysmaps.entities.Artikel;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by eboh on 11/11/15.
 */
@CrossOrigin
@RepositoryRestResource(collectionResourceRel = "artiklar", path = "artiklar")
public interface ArtikelRepository extends JpaRepository<Artikel, BigInteger> {

    @RestResource(path = "findHighestPrice", rel = "findHighestPrice")
    @Query("select a from Artikel a where a.prisPerLiter = (Select max(prisPerLiter) from Artikel)")
    Artikel findHighestPrice();

}