package com.theseeker.crawler.writeReader;

import com.theseeker.crawler.entities.FetchedPages;
import com.theseeker.crawler.entities.fetchedPagesDAO.FetchedPagesDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by claudinei on 28/03/17.
 */
@Component
public class Writer {

    @Autowired
    FetchedPagesDAO fpDao;

    public Writer(){

    }

    public void writerInFetchedPages(FetchedPages fp){
        System.out.println("WRITER ESCREVENDO EM FETCHEDPAGES: " + fp.getDominio());
        fpDao.insertFetPag(fp);
    }
}
