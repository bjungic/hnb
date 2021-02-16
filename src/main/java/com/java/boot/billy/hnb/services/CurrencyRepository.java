package com.java.boot.billy.hnb.services;

import com.java.boot.billy.hnb.model.Currency;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.sql.Date;
import java.util.List;

public interface CurrencyRepository extends CrudRepository<Currency, Long> {

    @Query(value = "select valuta from currencies group by valuta", nativeQuery = true)
    List<String> getCurrencies ();

    @Query(value = "select datum_primjene, drzava_iso, valuta, srednji_tecaj from currencies where valuta = ?1 and datum_primjene between ?2 and ?3", nativeQuery = true)
    List<String> getCurrenciesOnDate(String valuta, Date datumOd, Date datumDo);

    @Query(value = "select srednji_tecaj from currencies where valuta = ?1 and datum_primjene between ?2 and ?3", nativeQuery = true)
    List<String> getAverageCurrenciesOnDate(String valuta, Date datumOd, Date datumDo);

    @Query(value = "select datum_primjene from currencies where valuta = ?1 order by datum_primjene desc limit 1", nativeQuery = true)
    String getLastOccurrance (String valuta);

    @Query(value = "select datum_primjene from currencies where valuta = ?1 order by datum_primjene asc limit 1", nativeQuery = true)
    String getFirstOccurrance(String valuta);

    @Query(value = "select * from currencies where valuta = ?1 and kupovni_tecaj = ?2 and prodajni_tecaj = ?3 and srednji_tecaj = ?4 and datum_primjene = ?5", nativeQuery = true)
    Currency checkData(String valute, String kupovni_tecaj, String prodajni_tecaj, String srednji_tecaj, Date datum_primjene);

    @Query(value = "select * from currencies where valuta = ?1 and datum_primjene = ?2", nativeQuery = true)
    Currency getCurrency (String valuta, Date datum_primjene);

    @Query(value = "select count(*) from currencies", nativeQuery = true)
    int countAll();

    @Query(value = "select datum_primjene from currencies order by datum_primjene asc limit 1", nativeQuery = true)
    String getLast();
}
