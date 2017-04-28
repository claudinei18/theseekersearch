package com.theseeker.crawler.entities.dnsDAO;

import com.theseeker.crawler.entities.DNS;
import com.theseeker.crawler.entities.OrderedURL;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by claudinei on 28/03/17.
 */
@Repository
public class DNSDaoImpl implements DNSDao {

    @PersistenceContext
    protected EntityManager em;


    @Transactional
    public List<DNS> getDNS() throws DataAccessException {
        Query query = em.createQuery("select d from DNS d");
        List<DNS> resultList = query.getResultList();
        return resultList;
    }

    @Override
    public List<DNS> getRobots() throws DataAccessException {
        Query query = em.createQuery("select d from DNS d where d.robots = false");
        List<DNS> resultList = query.getResultList();
        return resultList;
    }


    @Override
    public DNS getDNS(String dominio) throws DataAccessException {
        Query query = em.createQuery("select d from DNS d where d.dominio = :dominio", DNS.class).setParameter("dominio", dominio);
        query.setMaxResults(1);
        DNS result;
        try{
            result = (DNS) query.getSingleResult();
        }catch (Exception e){
            result = null;
        }
        return result;
    }

    @Transactional
    public void setTime(DNS dns){
        em.persist(dns);
    }

    @Transactional
    public void remove(DNS dns){
        DNS aux = getDNS(dns.getDominio());
        if(aux != null){
            Query query = em.createQuery(
                    "DELETE FROM DNS d WHERE d.dominio = :dominio").setParameter("dominio", dns.getDominio());
            query.executeUpdate();
        }
    }

    @Override
    public boolean getRobots(String dominio) throws DataAccessException {
        Query query = em.createQuery("select d.robots from DNS d where d.dominio = :dominio").setParameter("dominio", dominio);
        query.setMaxResults(1);
        boolean result = false;
        try{
            result = (boolean) query.getSingleResult();
        }catch (Exception e){

        }
        return result;
    }

    @Transactional
    public List<DNS> retrieveAndDelete(){
        Query query = em.createQuery("select d from DNS d where d.robots = false");
        List<DNS> resultList = query.getResultList();
        for(DNS d: resultList){

        }
        return resultList;
    }

    @Transactional
    public void updateTime(DNS dns){
//        System.out.println(dns.getDominio());
        em.merge(dns);
    }

    @Transactional
    public void insertDNS(DNS dns){
//        System.out.println(dns.toString());
        em.persist(dns);
    }
}
