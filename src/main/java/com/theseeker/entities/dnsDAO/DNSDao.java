package com.theseeker.entities.dnsDAO;

import com.theseeker.entities.DNS;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * Created by claudinei on 28/03/17.
 */
public interface DNSDao {
    public List<DNS> getDNS() throws DataAccessException;
    public DNS getDNS(String dominio) throws DataAccessException;
    public void insertDNS(DNS dns);
}
