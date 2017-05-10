package com.theseeker.crawler.dns;

import com.sun.org.apache.xml.internal.security.c14n.Canonicalizer;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import com.theseeker.crawler.entities.OrderedURL;
import com.theseeker.crawler.entities.orderedURLsDAO.OrderedURLDAO;
import com.theseeker.crawler.entities.queuedURL;
import com.theseeker.crawler.entities.seenURL;
import com.theseeker.crawler.fetcher.Fetcher;
import com.theseeker.util.entities.Log;
import com.theseeker.util.entities.LogDAO.LogDAO;
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

    @Autowired
    LogDAO logDAO;

    public static InetAddress getIp(String dominio) {
        try {
            URL url = new URL(dominio);
            if (url != null) {
                String host = url.getHost();
                if (host != null) {
                    try{
                        InetAddress ip = InetAddress.getByName(host);
                        return ip;
                    }catch (Exception e){

                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @PostConstruct
    public void refreshCache() {
        new Thread(t1).start();
    }

    @PostConstruct
    public void refreshCache2() {
        new Thread(t2).start();
    }

    private Runnable t2 = new Runnable() {
        public void run() {
            while (true) {
//                System.out.println("get do ordered");
                List<OrderedURL> listOrdered = orderedURLDAO.getList();

                Date now = new Date();
                long nowLong = now.getTime();

                for (OrderedURL ourl : listOrdered) {
                    String url = ourl.getUrl();

                    /*String[] aux = url.split("//");
                    String x = aux[0] + "//" + getDomain(url);
                    System.out.println("75" + x);*/


                    String urlCanonica = URLCanonicalizer.getCanonicalURL(url);
                    if (urlCanonica != null) {
                        String dom = getDomain(urlCanonica);
                        if (dom != null) {
                            InetAddress ip = getIp(ourl.getUrl());
                            if (ip != null) {
                                DNS dns = new DNS(dom, ip.getHostAddress().toString(), nowLong, false, ourl.getPriority());
                                try {
                                    long antes = System.currentTimeMillis();
                                    boolean resp = dnsDAO.insertDNS(dns);
                                    long depois = System.currentTimeMillis();
                                    long diff = depois - antes;
                                    if(resp){
                                        Log log = new Log("DNS", "insert", new Date(), diff, dom);
                                        logDAO.insert(log);
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                }

//                System.out.println("fim do ordered");

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    };

    private Runnable t1 = new Runnable() {
        public void run() {
            while (true) {
                System.out.println("get do dns");
                List<DNS> listDNS = dnsDAO.getRobots();
                /*System.out.println("get do ordered");
                List<OrderedURL> listOrdered = orderedURLDAO.getList();*/

                Date now = new Date();
//                long nowLong = now.getTime();
                long nowLong = System.currentTimeMillis();

                /*for (OrderedURL ourl : listOrdered) {
                    String url = ourl.getUrl();
                    InetAddress ip = getIp(ourl.getUrl());

                    *//*String[] aux = url.split("//");
                    String x = aux[0] + "//" + getDomain(url);
                    System.out.println("75" + x);*//*

                    String dom = getDomain(URLCanonicalizer.getCanonicalURL(url));

                    DNS dns = new DNS(URLCanonicalizer.getCanonicalURL(url), ip.getHostAddress().toString(), nowLong, dnsDAO.getRobots(dom));

                    dnsDAO.insertDNS(dns);
                }*/


                for (DNS dns : listDNS) {

                    if (nowLong - dns.getTime() >= 30000) {

                        String dominio = dns.getDominio();

                        String urlCanonica = URLCanonicalizer.getCanonicalURL(dominio);
                        if (urlCanonica != null) {
                            String dom = getDomain(urlCanonica);
                            if (dom != null) {
                                InetAddress ip = getIp(dominio);
                                if(ip != null){
                                    DNS newDns = new DNS(dom, ip.getHostAddress().toString(), nowLong, dnsDAO.getRobots(dom), dns.getPriority());
                                    try {
                                        dnsDAO.remove(dns);
                                        long antes = System.currentTimeMillis();
                                        boolean resp = dnsDAO.insertDNS(newDns);
                                        long depois = System.currentTimeMillis();
                                        long diff = depois - antes;

                                        Log log = new Log("DNS", "update", new Date(), diff, dom);
                                        logDAO.insert(log);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                            }
                        }
                    }
                }

                //System.out.println("fim do dns");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    };

    public String getDomain(String url) {
        URI uri = null;
        String[] aux = url.split("://");

        String domain;
        try {
            uri = new URI(url);
            domain = uri.getHost();
        } catch (URISyntaxException e) {
/*            System.out.println(url);
            e.printStackTrace();*/
            return null;
        }
        if (uri == null) {
            return null;
        } else {
            if(domain == null){
                return url;
            }else{
                return domain.startsWith("www.") ? aux[0] + "://" + domain.substring(4) : aux[0] + "://" + domain;
            }
        }
    }


    /*public String getDomain(String url) {
        URI uri = null;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
//            System.out.println(url);
            e.printStackTrace();
            return null;
        }
        if (uri == null) {
            return null;
        } else {
            System.out.println(uri.getHost());
            return uri.getHost();
        }
    }*/

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
