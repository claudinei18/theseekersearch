package com.theseeker.dispatcher;

import com.theseeker.fetcher.Fetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;

import java.io.FileReader;
import java.io.IOException;

/**
 * Created by claudinei on 28/03/17.
 */
@Component
public class Dispatcher {
    BufferedReader brQueuedURL;

    @Autowired
    ApplicationContext ctx;

    public Dispatcher(){

    }

    @PostConstruct
    public void readFromQueuedURLs() throws IOException {
        brQueuedURL = new BufferedReader( new FileReader(System.getProperty("user.dir") + "/database/queuedURLs.txt") );
        String read = null;

        while ( ( read = brQueuedURL.readLine() ) != null ) {
            for(int i = 0; i < 10; i++){
                String readAux = null;
                readAux = brQueuedURL.readLine();
                if(readAux != null){
                    read += "\n" + brQueuedURL.readLine();
                }else{
                    i = 10;
                }
            }


            Fetcher fetcher = ctx.getBean(Fetcher.class);
            fetcher.start(read);

        }
    }
}
