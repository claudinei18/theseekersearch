package com.theseeker.crawler.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.theseeker.crawler.entities.seenURLDAO.seenURLDAO;

/**
 * Created by claudinei on 10/04/17.
 */
@Component
public class Scheduler {

    @Autowired
    seenURLDAO seenURLDAO;

//    public void schedulerExecute(List ){
//
//    }
}
