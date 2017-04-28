package com.theseeker.crawler.writeReader;

import com.theseeker.crawler.entities.FetchedPages;
import com.theseeker.crawler.entities.fetchedPagesDAO.FetchedPagesDAO;
import com.theseeker.crawler.parser.Parser;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by claudinei on 28/03/17.
 */

@Component
public class Reader {
    @Autowired
    FetchedPagesDAO fpDao;

    @Autowired
    Parser parser;

    ExecutorService executorService;

    public Reader() {

    }


    @PostConstruct
    public void startReader(){
        new Thread(t1).start();
    }

    private Runnable t1 = new Runnable() {
        public void run() {
            try{
                while (true) {
                    while (!(fpDao.fetchedPageIsEmpty())) {
                        FetchedPages fp = fpDao.retrieveAndDelete();
                        if (fp != null) {
                            System.out.println("READER LEU: " + fp.getDominio());
                            parser.parseFetchedPage(fp);
                        }
                    }
                    try {
                        synchronized (this) {
                            this.wait(2000);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e){}

        }
    };

    /*@PostConstruct
    public void init() {

        BasicThreadFactory factory = new BasicThreadFactory.Builder()
                .namingPattern("reader-thread-%d").build();

        executorService = Executors.newSingleThreadExecutor(factory);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    while (!(fpDao.fetchedPageIsEmpty())) {
                        FetchedPages fp = fpDao.retrieveAndDelete();
                        if (fp != null) {
                            System.out.println("READER LEU: " + fp.getDominio());
                            parser.parseFetchedPage(fp);
                        }
                    }
                    try {
                        synchronized (this) {
                            this.wait(2000);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @PreDestroy
    public void destroy() {
        executorService.shutdown();
    }*/
}
