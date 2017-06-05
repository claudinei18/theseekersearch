package com.theseeker.crawler.merger;

import com.theseeker.crawler.entities.OrderedURL;
import com.theseeker.crawler.entities.orderedURLsDAO.OrderedURLDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import com.theseeker.crawler.entities.seenURLDAO.seenURLDAO;

/**
 * Created by claudinei on 04/04/17.
 */
@Component
public class Merger {

    @Autowired
    OrderedURLDAO orderedURLDAO;

    @Autowired
    seenURLDAO seenURLDAO;

    public Merger() {

    }

    public void execute(List urls, String origemdalista) {
//        System.out.println("MERGE RECEBEU" + origemdalista);
        for (Object e : urls) {
            if (!(seenURLDAO.exists(e.toString()))) {
                int peso = 0;
                String url = e.toString();

                if (url.startsWith("https://en.wikipedia.org/wiki/")) {
                    peso = 10;
                } else if (url.startsWith("https://en.wikipedia.org/w")) {
                    peso = 9;
                }
                OrderedURL ourl = new OrderedURL(e.toString(), peso);
                orderedURLDAO.insert(ourl);
            }

        }
//        System.out.println("MERGE CONCLUIU" + origemdalista);
    }
}
