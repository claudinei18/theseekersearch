package com.theseeker.crawler.dns;

import com.sun.org.apache.xml.internal.security.c14n.Canonicalizer;
import com.theseeker.crawler.entities.OrderedURL;
import com.theseeker.crawler.entities.orderedURLsDAO.OrderedURLDAO;
import com.theseeker.crawler.entities.queuedURL;
import com.theseeker.crawler.entities.seenURL;
import com.theseeker.crawler.fetcher.Fetcher;
import com.theseeker.util.url.URLCanonicalizer;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.theseeker.crawler.entities.dnsDAO.DNSDao;
import com.theseeker.crawler.entities.DNS;
import java.lang.reflect.Field;

/**
 * Created by claudinei on 28/03/17.
 */
@Component
public class DNSUtil {
    ExecutorService executorService;

    @Autowired
    DNSDao dnsDAO;

    @Autowired
    OrderedURLDAO orderedURLDAO;

    public static InetAddress getIp(String dominio) {
        try {
            InetAddress ip = InetAddress.getByName(new URL(dominio).getHost());
            return ip;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @PostConstruct
    public void refreshCache(){
        new Thread(t1).start();
    }

    private Runnable t1 = new Runnable() {
        public void run() {
            try{
                while(true){
                    System.out.println("start-dnsutil");
                    List<DNS> listDNS = dnsDAO.getRobots();
                    List<OrderedURL> listOrdered = orderedURLDAO.getList();

                    Date now = new Date();
                    long nowLong = now.getTime();

                    for(OrderedURL ourl: listOrdered){
                        String url = ourl.getUrl();
                        InetAddress ip = getIp(ourl.getUrl());

                        DNS dns = new DNS(URLCanonicalizer.getCanonicalURL(url), ip.getHostAddress().toString(), nowLong, dnsDAO.getRobots(URLCanonicalizer.getCanonicalURL(url)));
                        dnsDAO.remove(dns);
                        dnsDAO.setTime(dns);
                    }

                    for(DNS dns: listDNS){
                        if(nowLong - dns.getTime() >= 3000000){
                            String dominio = dns.getDominio();
                            InetAddress ip = getIp(dominio);

                            DNS newDns = new DNS(URLCanonicalizer.getCanonicalURL(dominio), ip.getHostAddress().toString(), nowLong, dnsDAO.getRobots(URLCanonicalizer.getCanonicalURL(dominio)));
                            dnsDAO.remove(newDns);
                            dnsDAO.setTime(newDns);
                        }
                    }
                    System.out.println("fim-dnsutil");
                    Thread.sleep(30000);
                }
            } catch (Exception e){}

        }
    };

    public String getDomain(String url){
        URI uri = null;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return uri.getHost();
    }

    /*@PostConstruct
    public void refreshCache(){
        BasicThreadFactory factory = new BasicThreadFactory.Builder()
                .namingPattern("dnsthread-%d").build();

        executorService = Executors.newSingleThreadExecutor(factory);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                while(true){
                    List<DNS> listDNS = dnsDAO.getRobots();
                    List<OrderedURL> listOrdered = orderedURLDAO.getList();

                    Date now = new Date();
                    long nowLong = now.getTime();

                    for(OrderedURL ourl: listOrdered){
                        String url = ourl.getUrl();
                        InetAddress ip = getIp(ourl.getUrl());
                        DNS dns = new DNS(URLCanonicalizer.getCanonicalURL(url), ip.getHostAddress().toString(), nowLong, dnsDAO.getRobots(URLCanonicalizer.getCanonicalURL(url)));
                        dnsDAO.remove(dns);
                        dnsDAO.setTime(dns);
                    }

                    for(DNS dns: listDNS){
                        if(nowLong - dns.getTime() >= 300){
                            String dominio = dns.getDominio();

                            InetAddress ip = getIp(dominio);

                            DNS newDns = new DNS(URLCanonicalizer.getCanonicalURL(dominio), ip.getHostAddress().toString(), nowLong, dnsDAO.getRobots(URLCanonicalizer.getCanonicalURL(dominio)));
                            dnsDAO.remove(newDns);
                            dnsDAO.setTime(newDns);
                        }
                    }
                }
            }
        });
    }*/

}
