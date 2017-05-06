package com.theseeker.crawler.entities.orderedURLsDAO;

import com.theseeker.crawler.entities.DNS;
import com.theseeker.crawler.entities.FetchedPages;
import com.theseeker.crawler.entities.OrderedURL;
import org.aspectj.weaver.ast.Or;
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

    public int getMaiorPriority(){
        int priority = 0;
        Query query = em.createQuery("SELECT MAX(ourl.priority) FROM OrderedURL ourl");

        Object o = query.getSingleResult();

        if( o != null ){
            priority = (int) o;
        }

        return priority;
    }

    public boolean exists(OrderedURL ourl){
        List<Object> o = em.createQuery("SELECT t FROM OrderedURL t where t.url = :url")
                .setParameter("url", ourl.getUrl()).getResultList();

        return (! o.isEmpty());
    }

    @Transactional
    public List<OrderedURL> getList(){
        int maiorPriority = 0;
        maiorPriority = getMaiorPriority();
        Query query = em.createQuery("select d from OrderedURL d where d.priority = :priority")
                .setParameter("priority", maiorPriority);
        query.setMaxResults(70);
        List<OrderedURL> resultList = query.getResultList();

        int size = resultList.size();
        System.out.println(size);
        if(size < 100){
            query = em.createQuery("select d from OrderedURL d");
            query.setMaxResults(100 - size);

            List<OrderedURL> resultLit2 = query.getResultList();
            for(int i = 0; i < resultLit2.size(); i++){
                resultList.add(resultLit2.get(i));
            }

        }

        return resultList;
    }



    @Transactional
    public List<OrderedURL> getListToRobots(){
        int maiorPriority = 0;
        maiorPriority = getMaiorPriority();
        Query query = em.createQuery("select d from OrderedURL d where d.priority = :priority")
                .setParameter("priority", maiorPriority);
        query.setMaxResults(70);
        List<OrderedURL> resultList = query.getResultList();

        int size = resultList.size();
        System.out.println(size);
        if(size < 100){
            query = em.createQuery("select d from OrderedURL d");
            query.setMaxResults(100 - size);

            List<OrderedURL> resultLit2 = query.getResultList();
            for(int i = 0; i < resultLit2.size(); i++){
                resultList.add(resultLit2.get(i));
            }
        }

        for(int i = 0; i < resultList.size(); i++){
            em.remove(resultList.get(i));
        }

        return resultList;
    }


    @Transactional
    public void remove(OrderedURL ourl){
        em.remove(em.contains(ourl) ? ourl : em.merge(ourl));
    }
}
