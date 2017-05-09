package com.theseeker.crawler.entities.dnsDAO;

import com.theseeker.crawler.entities.DNS;
import com.theseeker.crawler.entities.OrderedURL;
import com.theseeker.crawler.entities.queuedURL;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.net.URI;
import java.net.URISyntaxException;
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

    public int getMaiorPriority(){
        int priority = 0;
        Query query = em.createQuery("SELECT MAX(d.priority) FROM DNS d");

        Object o = query.getSingleResult();

        if( o != null ){
            priority = (int) o;
        }

        return priority;
    }

    @Override
    public List<DNS> getRobots() throws DataAccessException {
        Query query = em.createQuery("select d from DNS d where d.priority = :priority")
                .setParameter("priority", getMaiorPriority());
        query.setMaxResults(100);

        List<DNS> resultList = query.getResultList();

        int size = resultList.size();
        if(size < 100){
            query = em.createQuery("select d from DNS d");
            query.setMaxResults(100 - size);

            List<DNS> resultLit2 = query.getResultList();
            for(int i = 0; i < resultLit2.size(); i++){
                resultList.add(resultLit2.get(i));
            }

        }

        return resultList;
    }

    @Override
    public List<DNS> getWithoutRobots(){
        Query query = em.createQuery("select d from DNS d where d.robots = false");
        query.setMaxResults(100);

        List<DNS> resultList = query.getResultList();

        return resultList;
    }

    public List<DNS> getDNSPages(){
        Query query = em.createQuery("select d from DNS d where d.robots = true and d.priority = :priority")
                .setParameter("priority", getMaiorPriority());
        query.setMaxResults(100);

        List<DNS> resultList = query.getResultList();

        int size = resultList.size();
        if(size < 100){
            query = em.createQuery("select d from DNS d where d.robots = true");
            query.setMaxResults(100 - size);

            List<DNS> resultLit2 = query.getResultList();
            for(int i = 0; i < resultLit2.size(); i++){
                resultList.add(resultLit2.get(i));
            }

        }

        return resultList;
    }


    @Override
    public DNS getDNS(String dominio) throws DataAccessException {
        /*System.out.println(dominio);
        dominio = getDomain(dominio);
        System.out.println(dominio);*/

        Query query = em.createQuery("select d from DNS d where d.dominio = :dominio", DNS.class).setParameter("dominio", dominio);
        query.setMaxResults(1);
        DNS result;
        try{
            result = (DNS) query.getSingleResult();
        }catch (Exception e){
            e.printStackTrace();
            result = null;
        }
        return result;
    }

    @Transactional
    public void remove(DNS dns){
//        DNS aux = getDNS(getDomain(dns.getDominio()));
//        if(aux != null){
            Query query = em.createQuery(
                    "DELETE FROM DNS d WHERE d.dominio = :dominio").setParameter("dominio", dns.getDominio());
            query.executeUpdate();
//        }
    }

    public String getDomain(String url) {
        URI uri = null;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return uri.getHost();
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
    public boolean insertDNS(DNS dns){
//        System.out.println(dns.toString());
        boolean resp = false;
        if(!exists(dns)){
            resp = true;
            em.persist(dns);
        }
        return resp;
    }

    public boolean exists(DNS dns){
        List<Object> o = em.createQuery("SELECT t FROM DNS t where t.dominio = :dominio")
                .setParameter("dominio", dns.getDominio()).getResultList();

        return (! o.isEmpty());
    }

}
