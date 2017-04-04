package com.theseeker.crawler.parser;

import com.theseeker.crawler.entities.FetchedPages;
import com.theseeker.crawler.filter.Filter;
import com.theseeker.crawler.urlManager.urlManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by claudinei on 29/03/17.
 */
@Component
public class Parser {

    @Autowired
    private urlManager um;

    @Autowired
    private Filter filter;

    public Parser(){

    }


    public static List getLinksFromPage(String page) throws IOException {
        List link = new ArrayList<String>();
        String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern p = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(page);
        while(m.find()) {
            if( (!m.group(1).endsWith(".png"))  &&
                    (!m.group(1).endsWith(".jpeg")) &&
                    (!m.group(1).endsWith(".jpg"))  &&
                    (!m.group(1).endsWith(".js")) ){
                link.add(m.group(1));
            }
        }
        return link;
    }

    public String getTextoDoHtml(String page){
        Document doc = Jsoup.parse(page);
        return doc.body().text();
    }

    public void parseFetchedPage(FetchedPages fp) throws IOException {
        um.recebendoUrl(getLinksFromPage(fp.getConteudo()));
        filter.filtrar(getTextoDoHtml(fp.getConteudo()));
    }
}
