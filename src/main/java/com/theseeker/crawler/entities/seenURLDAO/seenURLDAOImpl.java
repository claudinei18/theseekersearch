package com.theseeker.crawler.entities.seenURLDAO;

import com.theseeker.crawler.entities.Pages;
import com.theseeker.crawler.entities.seenURL;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by claudinei on 28/03/17.
 */
@Repository
public class seenURLDAOImpl implements seenURLDAO {

    @PersistenceContext
    protected EntityManager em;

    @Transactional
    public void insertURL(seenURL sl){
        List<Object> o = em.createQuery("SELECT t FROM seenURL t where t.dominio = :dominio")
                .setParameter("dominio", sl.getDominio()).getResultList();

        if(o.isEmpty()){
            em.persist(sl);
        }
    }
}
