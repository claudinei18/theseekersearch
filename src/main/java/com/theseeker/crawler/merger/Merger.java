package com.theseeker.crawler.merger;

import com.theseeker.crawler.entities.seenURL;
import com.theseeker.crawler.entities.seenURLDAO.seenURLDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by claudinei on 04/04/17.
 */
@Component
public class Merger {

    @Autowired
    seenURLDAO seenURLDAO;

    public Merger(){

    }

    public void execute(List urls, String origemdalista){
        System.out.println("MERGE RECEBEU" + origemdalista);
        for(Object e: urls){
            seenURL sl = new seenURL(e.toString());
            seenURLDAO.insertURL(sl);
        }
    }
}
