package com.theseeker.util.entities.LogDAO;

import com.theseeker.util.entities.Log;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by claudinei on 08/05/17.
 */
@Repository
public class LogDAO {

    @PersistenceContext
    protected EntityManager em;

    @Transactional
    public void insert(Log log){
        em.persist(log);
    }
}
