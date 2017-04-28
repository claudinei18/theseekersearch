package com.theseeker.crawler.urlManager;

import com.theseeker.crawler.merger.Merger;
import com.theseeker.util.url.URLCanonicalizer;
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

    public urlManager(){

    }

    public boolean isHtml(String dominio) throws IOException {
        boolean resp = false;

        dominio = URLCanonicalizer.getCanonicalURL(dominio);
        URL url = null;
        try{
            url = new URL(dominio);
        }catch (Exception e){
        }

        if(url != null) {
            HttpURLConnection urlc = (HttpURLConnection)url.openConnection();
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
            }
        }
        return resp;
    }

    public void recebendoUrl(List urls, String origemDaLista) throws IOException {
        System.out.println("URLMANAGER RECEBEU: " + origemDaLista);
        List htmlUrl = new ArrayList<String>();
        List noHtmlUrl = new ArrayList<String>();

        for (Object element : urls) {
            if(element != null){
                if( isHtml( element.toString() )){
                    htmlUrl.add(element);

                }else{
                    noHtmlUrl.add(element);
                }
            }
        }

        m.execute(htmlUrl, origemDaLista);
    }
}
