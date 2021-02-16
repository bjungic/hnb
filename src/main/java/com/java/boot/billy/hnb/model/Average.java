package com.java.boot.billy.hnb.model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class Average {

    Date datumOd;
    Date datumDo;
    String valuta;
    Double prosijek;


    public Average(Date datumOd, Date datumDo, String valuta, Double prosijek) {
        this.datumOd = datumOd;
        this.datumDo = datumDo;
        this.valuta = valuta;
        this.prosijek = prosijek;
    }
}
