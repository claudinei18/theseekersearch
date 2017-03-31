package com.theseeker.writeReader;

import com.sun.org.apache.regexp.internal.RE;
import com.theseeker.entities.FetchedPages;
import com.theseeker.entities.fetchedPagesDAO.FetchedPagesDAO;
import com.theseeker.parser.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

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
                            parser.parseFetchedPage(fp);
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
