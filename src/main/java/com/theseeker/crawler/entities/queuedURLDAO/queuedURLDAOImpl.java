package com.theseeker.crawler.entities.queuedURLDAO;

import com.theseeker.crawler.entities.FetchedPages;
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
public class queuedURLDAOImpl implements queuedURLDAO {

    @PersistenceContext
    protected EntityManager em;

    @Transactional
    public void insertURL(queuedURL sl){
        List<Object> o = em.createQuery("SELECT t FROM queuedURL t where t.dominio = :dominio")
                .setParameter("dominio", sl.getDominio()).getResultList();

        if(o.isEmpty()){
            em.persist(sl);
        }
    }

    @Transactional
    public queuedURL retrieveAndDelete(){
        Query query = em.createNativeQuery("SELECT MIN(qurl.id) FROM queuedURL qurl");

        queuedURL qurl = null;
        BigInteger id = null;
        id = (BigInteger) query.getSingleResult();
        if(id != null){
            qurl = em.find(queuedURL.class, id);
            em.remove(qurl);
        }
        return qurl;
    }

    @Override
    public boolean queuedURLIsEmpty(){
        Query q = em.createQuery ("SELECT count(x) FROM queuedURL x");
        Number result = (Number) q.getSingleResult ();
        Number zero = 0;
        if(result.equals(zero)){
            return true;
        }else{
            return false;
        }
    }
}
