package com.theseeker.crawler.fetcher;


import com.theseeker.crawler.dns.DNSUtil;
import com.theseeker.crawler.entities.DNS;
import com.theseeker.crawler.entities.FetchedPages;
import com.theseeker.crawler.entities.dnsDAO.DNSDao;
import com.theseeker.crawler.entities.fetchedPagesDAO.FetchedPagesDAO;
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
import java.net.URI;
import java.net.URISyntaxException;

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
                    .userAgent("TheSeeker2.2")
                    .header("Accept-Language", "en")
                    .timeout(3000)
                    .get();


            Element taglang = doc.select("html").first();

            if( taglang.attr("lang").startsWith("en") || taglang.attr("lang").equals("") ){
                System.out.println(taglang.attr("lang"));
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

    public void start(String dominio) throws IOException, URISyntaxException {
//        System.out.println("FETCHER PROCESSANDO: " + dominio);
        //Procuro se existe no banco de dns

        String[] aux = dominio.split("//");
        String main = aux[0] + "//" + getDomainName(dominio);

        DNS dns = dnsDao.getDNS(main);

        //Acessar através do IP

        //Acessando atraves do LINK
        Document doc = getHtmlContent(dominio, dns.getIp());
        if(doc != null){
            FetchedPages fp = new FetchedPages(dns.getIp(), dominio, doc.title(), doc.html());
            //Chamando o Writer para escrever no banco de dados
            writer.writerInFetchedPages(fp);
        }

    }

    public String getDomainName(String url) throws URISyntaxException {
        URI uri = new URI(url);
        String domain = uri.getHost();
        return domain.startsWith("www.") ? domain.substring(4) : domain;
    }

}
