package com.theseeker.util.application;

import com.theseeker.crawler.entities.OrderedURL;
import com.theseeker.crawler.entities.dnsDAO.DNSDao;
import com.theseeker.crawler.entities.orderedURLsDAO.OrderedURLDAO;
import com.theseeker.crawler.entities.queuedURLDAO.queuedURLDAO;
import com.theseeker.crawler.entities.seenURL;
import com.theseeker.crawler.entities.seenURLDAO.seenURLDAO;
import com.theseeker.crawler.entities.queuedURL;
import com.theseeker.util.url.URLCanonicalizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by claudinei on 11/04/17.
 */
@Component
public class Initialize {

    BufferedReader brQueuedURL;

    @Autowired
    queuedURLDAO queuedURLDAO;

    @Autowired
    seenURLDAO seenURLDAO;

    @Autowired
    OrderedURLDAO orderedURLDAO;


    @PostConstruct
    public void initialize() throws IOException {
        brQueuedURL = new BufferedReader( new FileReader(System.getProperty("user.dir") + "/database/seeds.txt") );
        String read = null;

        while ( ( read = brQueuedURL.readLine() ) != null ) {
            seenURL sl = new seenURL(URLCanonicalizer.getCanonicalURL(read));
            queuedURL qurl = new queuedURL(URLCanonicalizer.getCanonicalURL(read), "");

            if(!(seenURLDAO.exists(sl)) && !(queuedURLDAO.exists(qurl))){
                OrderedURL ourl = new OrderedURL(read, 0);
                try{
                    orderedURLDAO.insert(ourl);
                }catch (Exception e){
                    System.out.println("url: " + ourl.getUrl());
                    e.printStackTrace();
                }

            }
        }

        brQueuedURL.close();
    }
}
