package com.theseeker.crawler.entities.dnsDAO;

import com.theseeker.crawler.entities.DNS;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * Created by claudinei on 28/03/17.
 */
public interface DNSDao {
    public DNS getDNS(String dominio) throws DataAccessException;
    public void insertDNS(DNS dns);
    public void remove(DNS dns);
    public boolean getRobots(String dominio);
    public List<DNS> getRobots() throws DataAccessException;
    public List<DNS> getWithoutRobots();
    public List<DNS> getDNSPages();
}
