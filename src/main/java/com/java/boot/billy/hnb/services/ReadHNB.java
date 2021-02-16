package com.java.boot.billy.hnb.services;

import com.java.boot.billy.hnb.model.Currency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.netty.http.client.HttpClient;
import reactor.util.retry.Retry;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
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
//        Long sada = Long.parseLong("772927200000");

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
//                System.out.println("Broj zapisa: " + currencyRepository.countAll());
                logger.info("Broj zapisa: " + currencyRepository.countAll());
            }

            sada = sada - 86400000;
            if (!(currency.length > 0)) {
                try {
//                    System.out.println(currencyRepository.getLast());
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
