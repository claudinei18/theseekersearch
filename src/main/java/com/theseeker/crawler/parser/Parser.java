package com.theseeker.crawler.parser;

import com.theseeker.crawler.entities.FetchedPages;
import com.theseeker.crawler.entities.Pages;
import com.theseeker.crawler.entities.RejectedURL;
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

import com.theseeker.crawler.entities.RejectedURL;
import com.theseeker.crawler.entities.rejectedURL.rejectedURLDAO;

/**
 * Created by claudinei on 29/03/17.
 */
@Component
public class Parser {

    @Autowired
    private urlManager um;

    @Autowired
    private Filter filter;

    @Autowired
    rejectedURLDAO rejectedURLDAO;

    ExecutorService executorService1;
    ExecutorService executorService2;

    public Parser(){

    }


    public List getLinksFromPage(String page) throws IOException {
        Document doc = Jsoup.parse(page);
        Elements es = doc.select("a[href]");
        List links = new ArrayList<String>();


        for(Element e: es){
            String link = e.attr("abs:href");
            if(     (!link.contains(".svg"))  &&
                    (!link.endsWith(".asx"))  &&     // Windows video
                    (!link.endsWith((".bmp")) &&     // bitmap image
                    (!link.endsWith(".css"))  &&     // Cascading Style Sheet
                    (!link.endsWith(".doc"))  &&     // Microsoft Word (mostly)
                    (!link.endsWith(".docx")) &&     // Microsoft Word
                    (!link.endsWith(".flv"))  &&     // Old Flash video format
                    (!link.endsWith(".gif"))  &&     // GIF image
                    (!link.endsWith(".jpeg")) &&     // JPEG image
                    (!link.endsWith(".jpg"))  &&     // JPEG image
                    (!link.endsWith(".mid"))  &&     // MIDI file
                    (!link.endsWith(".mov"))  &&     // Quicktime movie
                    (!link.endsWith(".mp3"))  &&     // MP3 audio
                    (!link.endsWith(".ogg"))  &&     // .ogg format media
                    (!link.endsWith(".pdf"))  &&     // PDF files
                    (!link.endsWith(".png"))  &&     // image
                    (!link.endsWith(".ppt"))  &&     // powerpoint
                    (!link.endsWith(".ra"))   &&     // real media
                    (!link.endsWith(".ram"))  &&     // real media
                    (!link.endsWith(".rm"))   &&     // real media
                    (!link.endsWith(".swf"))  &&     // Flash files
                    (!link.endsWith(".txt"))  &&     // plain text
                    (!link.endsWith(".wav"))  &&     // WAV format sound
                    (!link.endsWith(".wma"))  &&     // Windows media audio
                    (!link.endsWith(".wmv"))  &&     // Windows media video
                    (!link.endsWith(".xml"))  &&     // XML files
                    (!link.endsWith(".zip"))  &&     // ZIP files
                    (!link.endsWith(".m4a"))  &&     // MP4 audio
                    (!link.endsWith(".m4v"))  &&     // MP4 video
                    (!link.endsWith(".mov"))  &&     // Quicktime movie
                    (!link.endsWith(".mp4"))  &&     // MP4 video or audio
                    (!link.endsWith(".m4b"))  &&	 // MP4 video or audio
                    (!link.endsWith(".js")) )){
                links.add(link);
            }else{
                RejectedURL rurl = new RejectedURL(link, "", "Robots");
                rejectedURLDAO.insertURL(rurl);
            }
        }
        return links;

    }

    public String getTextoDoHtml(String page){
        Document doc = Jsoup.parse(page);
        return doc.body().text();
    }

    public void parseFetchedPage(FetchedPages fp){

        try {
            System.out.println("PARSER RECEBEU: " + fp.getDominio());
            um.recebendoUrl(getLinksFromPage(fp.getConteudo()), fp.getDominio());
        } catch (IOException e) {
            e.printStackTrace();
        }

        String texto = getTextoDoHtml(fp.getConteudo());
        Pages page = new Pages(fp.getIp(), fp.getDominio(), fp.getTitulo(), texto);
        System.out.println("Filtrando: " + page);
        filter.filtrar(page);

        /*try {
            um.recebendoUrl(getLinksFromPage(fp.getConteudo()), fp.getDominio());
        } catch (IOException e) {
            e.printStackTrace();
        }

        String texto = getTextoDoHtml(fp.getConteudo());
        Pages page = new Pages(fp.getIp(), fp.getDominio(), fp.getTitulo(), texto);
        filter.filtrar(page);*/

        /*BasicThreadFactory factory = new BasicThreadFactory.Builder()
                .namingPattern("callurlmanager-thread-%d").build();

        executorService1 =  Executors.newSingleThreadExecutor(factory);
        executorService1.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("PARSER RECEBEU: " + fp.getDominio());
                    um.recebendoUrl(getLinksFromPage(fp.getConteudo()), fp.getDominio());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        BasicThreadFactory factory2 = new BasicThreadFactory.Builder()
                .namingPattern("callfilter-thread-%d").build();

        executorService2 =  Executors.newSingleThreadExecutor(factory2);
        executorService2.execute(new Runnable() {
            @Override
            public void run() {
                String texto = getTextoDoHtml(fp.getConteudo());
                Pages page = new Pages(fp.getIp(), fp.getDominio(), fp.getTitulo(), texto);
                System.out.println("Filtrando: " + page);
                filter.filtrar(page);
            }
        });*/

    }
}
