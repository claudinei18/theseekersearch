package com.theseeker.crawler.entities.rejectedURL;

import com.theseeker.crawler.entities.OrderedURL;
import com.theseeker.crawler.entities.RejectedURL;
import com.theseeker.crawler.entities.queuedURL;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;

/**
 * Created by claudinei on 28/03/17.
 */
@Repository
public class rejectedURLDAOImpl implements rejectedURLDAO {

    @PersistenceContext
    protected EntityManager em;

    @Transactional
    public void insertURL(RejectedURL rurl){
        if(! exists(rurl)){
            em.persist(rurl);
        }
    }

    public boolean exists(RejectedURL rurl){
        List<Object> o = em.createQuery("SELECT t FROM RejectedURL t where t.dominio = :dominio")
                .setParameter("dominio", rurl.getDominio()).getResultList();

        return (! o.isEmpty());
    }
}
