package com.theseeker.crawler.urlManager;

import com.theseeker.crawler.entities.RejectedURL;
import com.theseeker.crawler.entities.rejectedURL.rejectedURLDAO;
import com.theseeker.crawler.merger.Merger;
import com.theseeker.util.url.URLCanonicalizer;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.theseeker.crawler.entities.seenURLDAO.seenURLDAO;
import com.theseeker.crawler.entities.seenURL;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by claudinei on 04/04/17.
 */
@Component
public class urlManager {

    @Autowired
    Merger m;

    int countLinksOff= 0;

    public urlManager() {

    }

    public boolean isHtml(String dominio) {
        boolean resp = false;

        dominio = URLCanonicalizer.getCanonicalURL(dominio);
        URL url = null;
        try {
            url = new URL(dominio);
        } catch (Exception e) {
            /*System.out.println(dominio);
            e.printStackTrace();*/
        }

        if (url != null) {
            try {
                Connection.Response res = Jsoup.connect(dominio).timeout(1000).execute();
                String contentType = res.contentType();

                Document doc = Jsoup.parse(res.toString());
                Element taglang = doc.select("html").first();

                if (contentType != null) {
                    if (contentType.startsWith("text/html") &&
                            (taglang.attr("lang").startsWith("en") || taglang.attr("lang").equals("")) ) {
                        resp = true;
                    }else{

                    }
                }

            /*HttpURLConnection urlc = (HttpURLConnection)url.openConnection();
            urlc.setAllowUserInteraction( false );
            urlc.setDoInput( true );
            urlc.setDoOutput( false );
            urlc.setUseCaches( true );
            urlc.setRequestMethod("HEAD");

            urlc.setRequestProperty("Accept-Language", "en");

            try{
                urlc.connect();
                String contentType = urlc.getContentType();

                if(contentType != null){
                    if (contentType.startsWith("text/html")) {
                        resp = true;
                    }
                }
            }catch (Exception e){
                System.out.println("Erro ao conectar e requisitar o contentType da pagina: " + dominio);
                e.printStackTrace();
            }*/
            } catch (Exception e) {
                System.out.println("Erro ao requisitar content-type " + dominio);
                countLinksOff++;
            }

        }
        return resp;
    }

    public void recebendoUrl(List urls, String origemDaLista) throws IOException {
//        System.out.println("URLMANAGER RECEBEU: " + origemDaLista);
        List htmlUrl = new ArrayList<String>();
        List noHtmlUrl = new ArrayList<String>();

        /*for(Object e: urls){
            System.out.println(e.toString());
        }*/

        for (Object element : urls) {
            if (element != null) {/*
                if(countLinksOff > 50){
                    break;
                }*/
                /*if (isHtml(element.toString())) {
                    htmlUrl.add(element);

                } else {
                    noHtmlUrl.add(element);
                }*/

                htmlUrl.add(element);
            }
        }

        m.execute(htmlUrl, origemDaLista);
    }
}
