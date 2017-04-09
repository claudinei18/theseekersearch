package com.theseeker.crawler.entities.seenURLDAO;

import com.theseeker.crawler.entities.Pages;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by claudinei on 28/03/17.
 */
@Repository
public class SeelURLDAOImpl implements seelURLDAO {

    @PersistenceContext
    protected EntityManager em;

    @Override
    public void insertPage(Pages page){
        em.persist(page);
    }
}
