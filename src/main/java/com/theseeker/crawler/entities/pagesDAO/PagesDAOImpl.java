package com.theseeker.crawler.entities.pagesDAO;

import com.theseeker.crawler.entities.Pages;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by claudinei on 28/03/17.
 */
@Repository
public class PagesDAOImpl implements PagesDAO {

    @PersistenceContext
    protected EntityManager em;

    @Override
    public void insertPage(Pages page){
        em.persist(page);
    }
}
