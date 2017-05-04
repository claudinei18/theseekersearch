package com.theseeker.crawler.fetcher;


import com.theseeker.crawler.dns.DNSUtil;
import com.theseeker.crawler.entities.DNS;
import com.theseeker.crawler.entities.FetchedPages;
import com.theseeker.crawler.entities.dnsDAO.DNSDao;
import com.theseeker.crawler.entities.fetchedPagesDAO.FetchedPagesDAO;
import com.theseeker.crawler.entities.queuedURL;
import com.theseeker.crawler.entities.seenURL;
import com.theseeker.crawler.entities.seenURLDAO.seenURLDAO;
import com.theseeker.crawler.writeReader.Writer;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetAddress;

import org.jsoup.Jsoup;

/**
 * Created by claudinei on 28/03/17.
 */
@Component
@Scope("prototype")
public class Fetcher {

    @Autowired
    private DNSDao dnsDao;

    @Autowired
    private FetchedPagesDAO fpDao;

    @Autowired
    private Writer writer;

    @Autowired
    private seenURLDAO seenURLDAO;

    public Fetcher() {

    }

    public Document getHtmlContent(String dominio, String ip) throws IOException {
        Document doc = null;
        try{
            doc = Jsoup.connect(dominio)
                    .userAgent("TheSeeker1.0")
                    .header("Accept-Language", "en")
                    .timeout(3000)
                    .get();

            Element taglang = doc.select("html").first();
//            System.out.println(taglang.attr("lang"));
            if( taglang.attr("lang").startsWith("en") || taglang.attr("lang").equals("") ){
                seenURL sl = new seenURL(dominio, ip);
                seenURLDAO.insertURL(sl);
            }else{
                doc = null;
            }
        }catch (Exception e) {
            /*System.out.println("ERRO: Não conseguiu coletar com o JSOUP. " + dominio);
            e.printStackTrace();*/
        }

        return doc;
    }

    private boolean alreadyCollected(String dominio) {
        if (fpDao.getIdDNS(dominio) != null) {
            return true;
        } else {
            return false;
        }
    }

    public void start(queuedURL qurl) throws IOException {
//        System.out.println("FETCHER PROCESSANDO: " + qurl.getDominio());

        //Acessar através do IP

        //Acessando atraves do LINK
        Document doc = getHtmlContent(qurl.getDominio(), qurl.getIp());
        if(doc != null){
            FetchedPages fp = new FetchedPages(qurl.getIp(), qurl.getDominio(), doc.title(), doc.html());

            //Chamando o Writer para escrever no banco de dados
            writer.writerInFetchedPages(fp);
        }
    }
}
