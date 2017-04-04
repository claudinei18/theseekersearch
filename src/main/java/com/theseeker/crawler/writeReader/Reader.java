package com.theseeker.crawler.writeReader;

import com.theseeker.crawler.entities.FetchedPages;
import com.theseeker.crawler.entities.fetchedPagesDAO.FetchedPagesDAO;
import com.theseeker.crawler.parser.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * Created by claudinei on 28/03/17.
 */
@Component
public class Reader {
    @Autowired
    FetchedPagesDAO fpDao;

    @Autowired
    Parser parser;

    public Reader(){

    }

    @PostConstruct
    public void readerFromFatchedPages(){
        Thread thread = new Thread(){
            public void run(){
                while(true){
                    while( !(fpDao.fetchedPageIsEmpty()) ){
                        FetchedPages fp = fpDao.retrieveAndDelete();
                        if(fp != null){
                            try {
                                parser.parseFetchedPage(fp);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    try {
                        sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        thread.start();
    }
}
