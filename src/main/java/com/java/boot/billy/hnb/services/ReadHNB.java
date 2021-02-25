package com.java.boot.billy.hnb.services;

import com.java.boot.billy.hnb.model.Currency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

@Service
public class ReadHNB {

    HttpClient httpClient = HttpClient.create().wiretap(true).keepAlive(true);

    Logger logger = LoggerFactory.getLogger(ReadHNB.class);

    private WebClient.Builder builder;
    private final CurrencyRepository currencyRepository;

    public ReadHNB(WebClient.Builder builder, CurrencyRepository currencyRepository) {
        this.builder = builder;
        this.currencyRepository = currencyRepository;
    }

    @Async
    public void getCrurrency() {

//        getDanPoDan();

        getGodinaPoGodina();

    }

    private void getGodinaPoGodina() {

        Currency[] currency = null;
        String zadnji = currencyRepository.getLast();
        int godina;
        if (!(zadnji == null)) {
            godina = LocalDate.parse(zadnji).getYear();
        } else godina = LocalDate.now().getYear();

        Long timer = Calendar.getInstance().getTimeInMillis();

        do {
            try {
                Long tempTimer = Calendar.getInstance().getTimeInMillis();
//                System.out.println(godina);
                currency = builder.codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(16 * 1024 * 1024))
                        .build()
                        .get()
                        .uri("http://api.hnb.hr/tecajn/v2?datum-primjene-od=" + godina + "-01-01&datum-primjene-do=" + godina + "-12-31")
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .bodyToMono(Currency[].class)
                        .block();


                for (Currency c : currency) {
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
                tempTimer = tempTimer - Calendar.getInstance().getTimeInMillis();
                if (Calendar.getInstance().getTimeInMillis() >= timer) {
                    timer += 360000; //3600000 - 1h; 60000 1min;
                    logger.info("Broj zapisa: " + currencyRepository.countAll());
                }
                logger.info(godina + " --> " + tempTimer / -1000 + "s");

            } catch (Exception e) {
                logger.warn("Exception: ", e);
                getGodinaPoGodina();
            }
            godina = godina - 1;

        } while (currency.length > 0);

    }

    private void getDanPoDan() {

        Currency[] currency;
        SimpleDateFormat smf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        String zadnji = currencyRepository.getLast();
        if (!zadnji.isEmpty()) {
            System.out.println(zadnji);
            try {
                cal.setTime(smf.parse(zadnji));
            } catch (Exception e) {

            }
        }
        Long sada = cal.getTimeInMillis();
        Long timer = Calendar.getInstance().getTimeInMillis();

        int i = 0;
        do {
            String datum = smf.format(new Date(sada));
            currency = builder.clientConnector(new ReactorClientHttpConnector(httpClient))
                    .build()
                    .get()
                    .uri("http://api.hnb.hr/tecajn/v2?datum-primjene=" + datum)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(Currency[].class)
                    .block();


            for (Currency c : currency) {
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
            if (Calendar.getInstance().getTimeInMillis() >= timer) {
                timer += 360000; //3600000 - 1h; 60000 1min;
                logger.info("Broj zapisa: " + currencyRepository.countAll());
            }

            sada = sada - 86400000;
            if (!(currency.length > 0)) {
                try {
                    logger.error(currencyRepository.getLast());
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i++;
            }
        } while (i < 10);

    }
}
