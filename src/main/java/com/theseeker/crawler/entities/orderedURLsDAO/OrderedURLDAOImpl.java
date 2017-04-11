package com.theseeker.crawler.entities.orderedURLsDAO;

import com.theseeker.crawler.entities.DNS;
import com.theseeker.crawler.entities.FetchedPages;
import com.theseeker.crawler.entities.OrderedURL;
import org.springframework.dao.DataAccessException;
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
public class OrderedURLDAOImpl implements OrderedURLDAO {

    @PersistenceContext
    protected EntityManager em;


    @Transactional
    public void insert(OrderedURL ourl) {
        if(!(exists(ourl))){
            em.persist(ourl);
        }
    }

    public boolean exists(OrderedURL ourl){
        List<Object> o = em.createQuery("SELECT t FROM OrderedURL t where t.url = :url")
                .setParameter("url", ourl.getUrl()).getResultList();

        return (! o.isEmpty());
    }

    @Transactional
    public List<OrderedURL> getList(){
        Query query = em.createQuery("select d from OrderedURL d");
        List<OrderedURL> resultList = query.getResultList();
        for(OrderedURL o: resultList){
            em.remove(o);
        }
        return resultList;
    }
}
