package com.theseeker.crawler.entities.orderedURLsDAO;

import com.theseeker.crawler.entities.DNS;
import com.theseeker.crawler.entities.OrderedURL;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * Created by claudinei on 28/03/17.
 */
public interface OrderedURLDAO {
    public void insert(OrderedURL ourl);
    public List<OrderedURL> getList();

}
