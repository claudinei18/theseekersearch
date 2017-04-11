package com.theseeker.crawler.robots;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import com.theseeker.crawler.entities.dnsDAO.DNSDao;

/**
 * Created by claudinei on 11/04/17.
 */
@Component
public class Robots {

    @Autowired
    DNSDao DNSDao;

    @PostConstruct
    public void downloadRobots(){

    }
}
