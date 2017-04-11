package com.theseeker.crawler.merger;

import com.theseeker.crawler.entities.OrderedURL;
import com.theseeker.crawler.entities.orderedURLsDAO.OrderedURLDAO;
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
    OrderedURLDAO orderedURLDAO;

    public Merger(){

    }

    public void execute(List urls, String origemdalista){
        System.out.println("MERGE RECEBEU" + origemdalista);
        for(Object e: urls){
            OrderedURL ourl = new OrderedURL(e.toString(), 0);
            orderedURLDAO.insert(ourl);
        }
    }
}
