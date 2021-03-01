package com.java.boot.billy.hnb.controller;

import com.java.boot.billy.hnb.model.Average;
import com.java.boot.billy.hnb.model.Occurrance;
import com.java.boot.billy.hnb.services.Logika;
import com.java.boot.billy.hnb.services.MyService;
import com.java.boot.billy.hnb.services.ReadHNB;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@RestController
public class HNBController {

    private final ReadHNB readHNB;
    private final Logika logika;
    private final MyService myService;

    public HNBController(ReadHNB readHNB, Logika logika, MyService myService) {
        this.readHNB = readHNB;
        this.logika = logika;
        this.myService = myService;
    }

    @GetMapping("populateDB")
    public String populateDB() {
        readHNB.getCrurrency();
        return "<h1>Popunjavam bazu!</h1>";
    }

    @GetMapping(value = "currencies", produces = "application/json")
    public List<String> getValute() {
        return logika.getAllCurrencies();
    }

    @GetMapping(path = "currency/{valuta}/{odDatum}/{doDatum}", produces = "application/json")
    public List<String> getSrednjaVrijednost(@PathVariable(name = "valuta") String valuta, @PathVariable(name = "odDatum") Date odDatum, @PathVariable(name = "doDatum") Date doDatum) {
        return logika.getSrednja(valuta.toUpperCase(), odDatum, doDatum);
    }

    @GetMapping(path = "prosjecna_srednja_vrijednost", produces = "application/json")
    public Average getProsjecnaSrednjaVrijednost(@RequestBody Average average) {
        System.out.println(average.getDatumDo());
        return logika.getProsjecnaSrednja(average.getValuta(), average.getDatumOd(), average.getDatumDo());
    }

    @GetMapping(path = "pojava/{valuta}", produces = "application/json")
    public Occurrance pojava(@PathVariable(name = "valuta") String valuta) {
        return logika.getOccurrance(valuta.toUpperCase());
    }

    @GetMapping("update")
    public void update() {
        myService.updateCurrencies();
    }

    @PostMapping(path = "updateDan/{datum}")
    public String updateDate(@PathVariable(name = "datum") String datum) {
        myService.updateDate(datum);
        return "Updated " + datum;
    }

}
