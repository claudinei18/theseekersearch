package com.theseeker.crawler.entities.queuedURLDAO;

import com.theseeker.crawler.entities.queuedURL;

/**
 * Created by claudinei on 28/03/17.
 */
public interface queuedURLDAO {
    public void insertURL(queuedURL sl);
    public boolean queuedURLIsEmpty();
    public queuedURL retrieveAndDelete();
}
