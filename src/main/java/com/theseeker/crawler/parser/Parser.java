package com.theseeker.crawler.parser;

import com.theseeker.crawler.entities.FetchedPages;
import com.theseeker.crawler.entities.Pages;
import com.theseeker.crawler.filter.Filter;
import com.theseeker.crawler.urlManager.urlManager;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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

    ExecutorService executorService1;
    ExecutorService executorService2;

    public Parser(){

    }


    public static List getLinksFromPage(String page) throws IOException {
        Document doc = Jsoup.parse(page);
        Elements es = doc.select("a[href]");
        List links = new ArrayList<String>();


        for(Element e: es){
            String link = e.attr("abs:href");
            if( (!link.endsWith(".png"))  &&
                    (!link.endsWith(".jpeg")) &&
                    (!link.endsWith(".jpg"))  &&
                    (!link.endsWith(".css"))  &&
                    (!link.endsWith(".png"))  &&
                    (!link.contains(".svg"))  &&
                    (!link.endsWith(".js")) ){
                links.add(link);
            }
        }
        return links;

    }

    public String getTextoDoHtml(String page){
        Document doc = Jsoup.parse(page);
        return doc.body().text();
    }

    public void parseFetchedPage(FetchedPages fp){

        BasicThreadFactory factory = new BasicThreadFactory.Builder()
                .namingPattern("myspringbean-thread-%d").build();

        executorService1 =  Executors.newSingleThreadExecutor(factory);
        executorService1.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("GetLinksFromPage: " + fp.getDominio());
                    um.recebendoUrl(getLinksFromPage(fp.getConteudo()));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        BasicThreadFactory factory2 = new BasicThreadFactory.Builder()
                .namingPattern("myspringbean-thread-%d").build();

        executorService2 =  Executors.newSingleThreadExecutor(factory2);
        executorService2.execute(new Runnable() {
            @Override
            public void run() {
                String texto = getTextoDoHtml(fp.getConteudo());
                Pages page = new Pages(fp.getIp(), fp.getDominio(), fp.getTitulo(), texto);
                System.out.println("Filtrando: " + page);
                filter.filtrar(page);
            }
        });

    }
}
