package com.java.boot.billy.hnb.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@Getter
@Setter
@Entity
@Table(name = "currencies")
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String broj_tecajnice;
    Date datum_primjene; //1994-05-30
    String drzava;
    String drzava_iso;
    String valuta;
    String sifra_valute;
    int jedinica;
    String kupovni_tecaj;
    String srednji_tecaj;
    String prodajni_tecaj;


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Currency{");
        sb.append("broj_tecajnice='").append(broj_tecajnice).append('\'');
        sb.append(", datum_primjene=").append(datum_primjene);
        sb.append(", drzava='").append(drzava).append('\'');
        sb.append(", drzava_iso='").append(drzava_iso).append('\'');
        sb.append(", valuta='").append(valuta).append('\'');
        sb.append(", sifra_valute='").append(sifra_valute).append('\'');
        sb.append(", jedinica=").append(jedinica);
        sb.append(", kupovni_tecaj='").append(kupovni_tecaj).append('\'');
        sb.append(", srednji_tecaj='").append(srednji_tecaj).append('\'');
        sb.append(", prodajni_tecaj='").append(prodajni_tecaj).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
