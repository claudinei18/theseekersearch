package com.theseeker.crawler.entities.vocabularioDAO;

import com.theseeker.crawler.entities.RejectedURL;
import com.theseeker.crawler.entities.Vocabulario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by claudinei on 09/05/17.
 */
@Repository
public class VocabularioDAO {

    @Autowired
    EntityManager em;

    @Transactional
    public void insert(Vocabulario v){
        if(! exists(v)){
            em.persist(v);
        }
    }

    public boolean exists(Vocabulario v){
        List<Object> o = em.createQuery("SELECT v FROM Vocabulario v where v.termo = :termo")
                .setParameter("termo", v.getTermo()).getResultList();

        return (! o.isEmpty());
    }
}
