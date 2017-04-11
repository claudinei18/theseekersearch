package com.theseeker.crawler.entities.seenURLDAO;

import com.theseeker.crawler.entities.seenURL;

/**
 * Created by claudinei on 28/03/17.
 */
public interface seenURLDAO {
    public void insertURL(seenURL sl);
    public boolean exists(seenURL sl);
}
