package com.theseeker.crawler.dispatcher;

import com.theseeker.crawler.entities.queuedURL;
import com.theseeker.crawler.fetcher.Fetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;

import com.theseeker.crawler.entities.queuedURLDAO.queuedURLDAO;

import java.io.FileReader;
import java.io.IOException;

/**
 * Created by claudinei on 28/03/17. ;.,
 */
@Component
public class Dispatcher {
    BufferedReader brQueuedURL;

    @Autowired
    ApplicationContext ctx;

    @Autowired
    queuedURLDAO queuedURLDAO;

    public Dispatcher(){

    }

    @PostConstruct
    public void readFromQueuedURLs() throws IOException {
        brQueuedURL = new BufferedReader( new FileReader(System.getProperty("user.dir") + "/database/queuedURLs.txt") );
        String read = null;

        while ( ( read = brQueuedURL.readLine() ) != null ) {
            queuedURLDAO.insertURL(new queuedURL(read));
        }

        brQueuedURL.close();

        while(true){
            while(!(queuedURLDAO.queuedURLIsEmpty())){
                queuedURL qurl = queuedURLDAO.retrieveAndDelete();

                Fetcher fetcher = ctx.getBean(Fetcher.class);
                fetcher.start(qurl.getDominio());
            }
        }

    }
}
