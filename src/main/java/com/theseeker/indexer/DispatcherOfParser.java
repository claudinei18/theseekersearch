package com.theseeker.indexer;

import com.theseeker.crawler.entities.Pages;
import com.theseeker.crawler.entities.pagesDAO.PagesDAO;
import com.theseeker.crawler.entities.queuedURL;
import com.theseeker.crawler.entities.seenURL;
import com.theseeker.crawler.fetcher.Fetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by claudinei on 05/05/17.
 */

@Component
public class DispatcherOfParser {

    @Autowired
    ParserOfIndexer parser;

    @Autowired
    PagesDAO pagesDAO;

    ExecutorService executorService;

    public DispatcherOfParser() {

    }

    @PostConstruct
    public void initExecutor() {
//        executorService = Executors.newFixedThreadPool(100);
        startIndexer();
    }

    public void startIndexer() {
        new Thread(t1).start();
    }

    private Runnable t1 = new Runnable() {
        public void run() {
            while (true) {
                List<Pages> list = pagesDAO.getPagesToIndexer();
                for (Pages p : list) {
                    // some code to run in parallel
                    parser.start(p);
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    };

    /*public void startIndexer() {
        new Thread(t1).start();
    }

    private Runnable t1 = new Runnable() {
        public void run() {
            while (true) {
                List<Pages> list = pagesDAO.getPagesToIndexer();
                for (Pages p : list) {
                    executorService.submit(new Runnable() {
                        public void run() {
                            // some code to run in parallel
                            parser.start(p);
                        }
                    });
                }
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    };*/

    /*@PostConstruct
    public void startIndexer(){
        while(true){
            List<Pages> list = pagesDAO.getPagesToIndexer();
            for (Pages p : list) {
                parser.start(p);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }*/

}
