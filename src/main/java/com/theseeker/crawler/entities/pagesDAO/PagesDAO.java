package com.theseeker.crawler.entities.pagesDAO;

import com.theseeker.crawler.entities.FetchedPages;
import com.theseeker.crawler.entities.Pages;
import org.springframework.dao.DataAccessException;

/**
 * Created by claudinei on 28/03/17.
 */
public interface PagesDAO {
    public void insertPage(Pages fp);
}
