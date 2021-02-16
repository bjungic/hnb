package com.java.boot.billy.hnb.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Occurrance {
    String prvo;
    String zadnje;

    public Occurrance(String prvo, String zadnje) {
        this.prvo = prvo;
        this.zadnje = zadnje;
    }
}
