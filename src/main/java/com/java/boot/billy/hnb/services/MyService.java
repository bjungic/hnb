package com.java.boot.billy.hnb.services;

import com.java.boot.billy.hnb.model.Currency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Service
@EnableScheduling
public class MyService {

    Logger logger = LoggerFactory.getLogger(MyService.class);

    private WebClient.Builder builder;
    private WebClient webClient = WebClient.create();
    private final CurrencyRepository currencyRepository;

    public MyService(WebClient.Builder builder, CurrencyRepository currencyRepository) {
        this.builder = builder;
        this.currencyRepository = currencyRepository;
    }

    @Scheduled(cron = "1 * * * * ?")
    public void updateCurrencies() {
        try {
            Currency[] currency;
            SimpleDateFormat smf = new SimpleDateFormat("yyyy-MM-dd");
            Long sada = Calendar.getInstance().getTimeInMillis();

            String datum = smf.format(new Date(sada));

            currency = builder
                    .build()
                    .get()
                    .uri("http://api.hnb.hr/tecajn/v2?datum-primjene=" + datum)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(Currency[].class)
                    .block();

            for (Currency c : currency) {
//            Currency curr = currencyRepository.checkData(c.getValuta(), c.getKupovni_tecaj(), c.getProdajni_tecaj(), c.getSrednji_tecaj(), c.getDatum_primjene());
                Currency curr = currencyRepository.getCurrency(c.getValuta(), c.getDatum_primjene());
                if (curr == null) {
                    currencyRepository.save(c);
                } else {
                    curr.setKupovni_tecaj(c.getKupovni_tecaj());
                    curr.setSrednji_tecaj(c.getSrednji_tecaj());
                    curr.setProdajni_tecaj(c.getProdajni_tecaj());
                    currencyRepository.save(curr);
                }
            }

        } catch (Exception e) {
            logger.warn("UpdateCurrencies error: ", e);
        }

    }

    public void updateDate(String datum) {
        try {
            Currency[] currency;
//        SimpleDateFormat smf = new SimpleDateFormat("yyyy-MM-dd");
//        Long sada = Calendar.getInstance().getTimeInMillis();
//
//        String datum = smf.format(new Date(sada));

            currency = builder
                    .build()
                    .get()
                    .uri("http://api.hnb.hr/tecajn/v2?datum-primjene=" + datum)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(Currency[].class)
                    .block();

            for (Currency c : currency) {
//            Currency curr = currencyRepository.checkData(c.getValuta(), c.getKupovni_tecaj(), c.getProdajni_tecaj(), c.getSrednji_tecaj(), c.getDatum_primjene());
                Currency curr = currencyRepository.getCurrency(c.getValuta(), c.getDatum_primjene());
                if (curr == null) {
                    currencyRepository.save(c);
                } else {
                    curr.setKupovni_tecaj(c.getKupovni_tecaj());
                    curr.setSrednji_tecaj(c.getSrednji_tecaj());
                    curr.setProdajni_tecaj(c.getProdajni_tecaj());
                    currencyRepository.save(curr);
                }
            }

        } catch (Exception e) {
            logger.warn("UpdateDate error: ", e);
        }
    }


}
