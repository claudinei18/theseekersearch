package com.theseeker.entities.fetchedPagesDAO;

import com.theseeker.entities.DNS;
import com.theseeker.entities.FetchedPages;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * Created by claudinei on 28/03/17.
 */
public interface FetchedPagesDAO {
    public String getContent(String dominio);
    public String getIdDNS(String dominio) throws DataAccessException;
    public void insertFetPag(FetchedPages fp);
    public FetchedPages getFetchedPage(String dominio);
    public FetchedPages retrieveAndDelete();
    public boolean fetchedPageIsEmpty();
}
