package com.theseeker.crawler.entities.fetchedPagesDAO;

import com.theseeker.crawler.entities.FetchedPages;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;

/**
 * Created by claudinei on 28/03/17.
 */
@Repository
public class FetchedPagesDAOImpl implements FetchedPagesDAO {

    @PersistenceContext
    protected EntityManager em;

    @Override
    public String getContent(String dominio) {
        Query query = em.createQuery("select fp.content from FetchedPages fp where fp.dominio = :dominio").setParameter("dominio", dominio);
        String result;
        try{
            result = (String) query.getSingleResult();
        }catch (Exception e){
            result = null;
        }
        return result;
    }

    @Override
    public FetchedPages getFetchedPage(String dominio) {
        Query query = em.createQuery("select fp from FetchedPages fp where fp.dominio = :dominio").setParameter("dominio", dominio);
        FetchedPages result;
        try{
            result = (FetchedPages) query.getSingleResult();
        }catch (Exception e){
            result = null;
        }
        return result;
    }

    @Override
    public String getIdDNS(String dominio) {
        Query query = em.createQuery("select fp.ip from FetchedPages fp where fp.dominio = :dominio").setParameter("dominio", dominio);
        String result;
        try{
            result = (String) query.getSingleResult();
        }catch (Exception e){
            result = null;
        }
        return result;
    }

    @Transactional
    public FetchedPages retrieveAndDelete(){
        Query query = em.createNativeQuery("SELECT MIN(fpp.id) FROM fetchedpages fpp");

        FetchedPages fp = null;
        BigInteger id = null;
        id = (BigInteger) query.getSingleResult();
        if(id != null){
            fp = em.find(FetchedPages.class, id);
            em.remove(fp);
        }
        return fp;
    }

    @Transactional
    public void insertFetPag(FetchedPages fp){
        System.out.println("Inserindo fetchedpages: " + fp.getDominio());
        em.persist(fp);
    }

    @Override
    public boolean fetchedPageIsEmpty(){
        Query q = em.createQuery ("SELECT count(x) FROM FetchedPages x");
        Number result = (Number) q.getSingleResult ();
        Number zero = 0;
        if(result.equals(zero)){
            return true;
        }else{
            return false;
        }
    }
}
