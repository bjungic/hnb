package com.java.boot.billy.hnb.services;

import com.java.boot.billy.hnb.model.Average;
import com.java.boot.billy.hnb.model.Occurrance;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class Logika {
    private final CurrencyRepository currencyRepository;

    public Logika(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    public List<String> getAllCurrencies() {
        List<String> valute = currencyRepository.getCurrencies();
        for (String s : valute) {
            System.out.println(s);
        }
        return valute;
    }

    public List<String> getSrednja(String valuta, Date odDatum, Date doDatum) {
        List<String> vrijednost = currencyRepository.getCurrenciesOnDate(valuta, odDatum, doDatum);
        for (String s : vrijednost) {
        }
        return vrijednost;
    }

    public Average getProsjecnaSrednja(String valuta, Date odDatum, Date doDatum) {
        List<String> vrijednost = currencyRepository.getAverageCurrenciesOnDate(valuta, odDatum, doDatum);
        Double srednjaVrijednost = 0.0;
        for (String s : vrijednost) {
            Double temp = Double.parseDouble(s.replace(",", "."));
            srednjaVrijednost = srednjaVrijednost + temp;
        }
        srednjaVrijednost = srednjaVrijednost / vrijednost.size();
        Average average = new Average(odDatum, doDatum, valuta, srednjaVrijednost);
        return average;
    }

    public Occurrance getOccurrance(String valuta) {
        Occurrance occurrance = new Occurrance(currencyRepository.getFirstOccurrance(valuta), currencyRepository.getLastOccurrance(valuta));
        return occurrance;
    }

}
