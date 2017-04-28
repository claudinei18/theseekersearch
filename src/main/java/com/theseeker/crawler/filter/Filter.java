package com.theseeker.crawler.filter;

import com.theseeker.crawler.entities.Pages;
import com.theseeker.crawler.entities.pagesDAO.PagesDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by claudinei on 28/03/17.
 */
@Component
public class Filter {

    @Autowired
    PagesDAO pagesDAO;

    public void filtrar(Pages page){
        pagesDAO.insertPage(page);
    }

}
