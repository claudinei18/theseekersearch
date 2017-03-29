package com.theseeker.writeReader;

import com.theseeker.entities.FetchedPages;
import com.theseeker.entities.fetchedPagesDAO.FetchedPagesDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by claudinei on 28/03/17.
 */
@Component
public class Writer {

    @Autowired
    FetchedPagesDAO fpDao;

    public void writerInFetchedPages(FetchedPages fp){
        fpDao.insertFetPag(fp);
    }
}
