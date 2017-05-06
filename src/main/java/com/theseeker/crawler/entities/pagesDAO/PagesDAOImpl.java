package com.theseeker.crawler.entities.pagesDAO;

import com.theseeker.crawler.entities.OrderedURL;
import com.theseeker.crawler.entities.Pages;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by claudinei on 28/03/17.
 */
@Repository
public class PagesDAOImpl implements PagesDAO {

    @PersistenceContext
    protected EntityManager em;

    @Transactional
    public void insertPage(Pages page){
        if(! exists(page)){
            em.persist(page);
        }
    }

    @Transactional
    public boolean exists(Pages p){
        List<Object> o = em.createQuery("SELECT t FROM Pages t where t.dominio = :dominio")
                .setParameter("dominio", p.getDominio()).getResultList();

        return (! o.isEmpty());
    }

    @Transactional
    public List<Pages> getPagesToIndexer(){
        List<Pages> list = em.createQuery("SELECT p FROM Pages p")
                .setMaxResults(100)
                .getResultList();

        for(Pages p: list){
            em.remove(p);
        }

        return list;

    }
}
