package com.theseeker.crawler.parser;

import com.theseeker.crawler.entities.FetchedPages;
import com.theseeker.crawler.entities.Pages;
import com.theseeker.crawler.filter.Filter;
import com.theseeker.crawler.urlManager.urlManager;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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
        List link = new ArrayList<String>();
        String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern p = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(page);
        while(m.find()) {
            if( (!m.group(1).endsWith(".png"))  &&
                    (!m.group(1).endsWith(".jpeg")) &&
                    (!m.group(1).endsWith(".jpg"))  &&
                    (!m.group(1).endsWith(".css"))  &&
                    (!m.group(1).endsWith(".png"))  &&
                    (!m.group(1).contains(".svg"))  &&
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

    public void parseFetchedPage(FetchedPages fp){

        BasicThreadFactory factory = new BasicThreadFactory.Builder()
                .namingPattern("myspringbean-thread-%d").build();

        executorService1 =  Executors.newSingleThreadExecutor(factory);
        executorService1.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    inserirUrlsDaPagina(fp);
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
                filtrarPagina(page);
            }
        });

    }

    public void inserirUrlsDaPagina(FetchedPages fp) throws IOException {
        um.recebendoUrl(getLinksFromPage(fp.getConteudo()));
    }

    public void filtrarPagina(Pages page){
        filter.filtrar(page);
    }
}
