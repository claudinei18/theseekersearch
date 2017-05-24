package com.theseeker.crawler.fetcher;


import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.*;
import com.theseeker.crawler.dns.DNSUtil;
import com.theseeker.crawler.entities.DNS;
import com.theseeker.crawler.entities.FetchedPages;
import com.theseeker.crawler.entities.dnsDAO.DNSDao;
import com.theseeker.crawler.entities.fetchedPagesDAO.FetchedPagesDAO;
import com.theseeker.crawler.entities.queuedURL;
import com.theseeker.crawler.entities.seenURL;
import com.theseeker.crawler.entities.seenURLDAO.seenURLDAO;
import com.theseeker.crawler.writeReader.Writer;
import com.theseeker.util.entities.Log;
import com.theseeker.util.entities.LogDAO.LogDAO;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;
import java.util.List;

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
    LogDAO logDAO;

    @Autowired
    private seenURLDAO seenURLDAO;

    public Fetcher() {

    }

    public Document getHtmlContent(String dominio, String ip){
        Document doc = null;
        try{
            doc = Jsoup.connect(dominio)
                    .userAgent("TheSeeker1.0")
                    .header("Accept-Language", "en")
                    .timeout(3000)
                    .get();

            if(dominio.startsWith("http://dbpedia.org")){
                Element p = doc.select("p").first();
                String text = p.text(); //some bold text
                String html = "<html><head><title>First parse</title></head>"
                        + "<body>" + text + "</body></html>";
                doc = Jsoup.parse(html);
            }
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

    public void start(queuedURL qurl) {
//        System.out.println("FETCHER PROCESSANDO: " + qurl.getDominio());

        //Acessar através do IP

        //Acessando atraves do LINK


        long antes = System.currentTimeMillis();
        Document doc = getHtmlContent(qurl.getDominio(), qurl.getIp());
        long depois = System.currentTimeMillis();
        long diff = depois - antes;

        Log log = new Log("Fetcher", "fetch", new Date(), diff, qurl.getDominio());
        logDAO.insert(log);

        NaturalLanguageUnderstanding service = new NaturalLanguageUnderstanding(
                NaturalLanguageUnderstanding.VERSION_DATE_2017_02_27,
                "b845dcb3-1e77-4eb1-bf43-3cd7d71f9067",
                "Pd7kWrDmARew"
        );

        EntitiesOptions entities = new EntitiesOptions.Builder().limit(10000).build();
        Features features = new Features.Builder().entities(entities).build();
        AnalyzeOptions parameters = new AnalyzeOptions.Builder().url(qurl.getDominio()).features(features).build();
        AnalysisResults results = service.analyze(parameters).execute();
        List<EntitiesResult> a = results.getEntities();

        if(doc != null){
            FetchedPages fp = new FetchedPages(qurl.getIp(), qurl.getDominio(), doc.title(), doc.html());

            //Chamando o Writer para escrever no banco de dados
            writer.writerInFetchedPages(fp);
        }
    }
}
