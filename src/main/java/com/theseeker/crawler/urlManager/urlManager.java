package com.theseeker.crawler.urlManager;

import com.theseeker.crawler.merger.Merger;
import com.theseeker.util.url.URLCanonicalizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    public static boolean isHtml(String dominio) throws IOException {
        boolean resp = false;

        System.out.println("before" + dominio);
        dominio = URLCanonicalizer.getCanonicalURL(dominio);
        System.out.println("after" + dominio);
        URL url = new URL(dominio);



        HttpURLConnection urlc = (HttpURLConnection)url.openConnection();
        urlc.setAllowUserInteraction( false );
        urlc.setDoInput( true );
        urlc.setDoOutput( false );
        urlc.setUseCaches( true );
        urlc.setRequestMethod("HEAD");
        urlc.connect();
        String contentType = urlc.getContentType();
        if(contentType != null){
            if (contentType.startsWith("text/html")) {
                resp = true;
            }
        }
        return resp;
    }

    public void recebendoUrl(List urls) throws IOException {
        List htmlUrl = new ArrayList<String>();
        List noHtmlUrl = new ArrayList<String>();

        for (Object element : urls) {
            if( isHtml( element.toString() )){
                htmlUrl.add(element);
            }else{
                noHtmlUrl.add(element);
            }
        }

    }
}
