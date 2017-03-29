package com.theseeker.filter;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by claudinei on 28/03/17.
 */
public class Filter {

    public static boolean isHtml(String dominio) throws IOException {
        boolean resp = false;
        URL url = new URL(dominio);
        URLConnection c = url.openConnection();
        String contentType = c.getContentType();
        System.out.println(dominio + " -> CONTENT-TYPE: " + contentType);

        if (contentType.startsWith("text/html")) {
            resp = true;
        }

        return resp;
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
                System.out.println(m.group(1));
                link.add(m.group(1));
            }
        }
        return link;
    }


}
