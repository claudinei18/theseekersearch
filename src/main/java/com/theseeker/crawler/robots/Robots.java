package com.theseeker.crawler.robots;

import com.theseeker.crawler.entities.*;
import com.theseeker.crawler.entities.orderedURLsDAO.OrderedURLDAO;
import com.theseeker.crawler.fetcher.Fetcher;
import com.theseeker.util.entities.Log;
import com.theseeker.util.entities.LogDAO.LogDAO;
import com.theseeker.util.robots.NoRobotClient;
import com.theseeker.util.robots.NoRobotException;
import com.theseeker.util.url.URLCanonicalizer;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import com.theseeker.crawler.entities.dnsDAO.DNSDao;
import com.theseeker.crawler.entities.queuedURLDAO.queuedURLDAO;
import com.theseeker.crawler.entities.RejectedURL;
import com.theseeker.crawler.entities.rejectedURL.rejectedURLDAO;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by claudinei on 11/04/17.
 */
@Component
public class Robots {

    @Autowired
    DNSDao dnsDAO;

    @Autowired
    queuedURLDAO queuedURLDAO;

    @Autowired
    rejectedURLDAO rejectedURLDAO;

    @Autowired
    OrderedURLDAO orderedURLDAO;

    @Autowired
    LogDAO logDAO;


    ExecutorService executorService;

    /*@PostConstruct
    public void resolveRobots() throws URISyntaxException, IOException, NoRobotException {

        BasicThreadFactory factory = new BasicThreadFactory.Builder()
                .namingPattern("dnsthread-%d").build();

        executorService = Executors.newSingleThreadExecutor(factory);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                while(true){
                    List<DNS> listDNS = dnsDAO.getRobots();

                    for(DNS dns: listDNS){
                        dnsDAO.remove(dns);
                        dns.setRobots(true);
                        dnsDAO.setTime(dns);

                        String[] x = dns.getDominio().split("/");
                        try {
                            downloadRobots(x[0] + "//" + x[2] + "/robots.txt");
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        String urlRestante = "";
                        for(int i = 3; i < x.length; i++){
                            urlRestante += "/" + x[i];
                        }


                        try {
                            Path path = Paths.get(System.getProperty("user.dir") + "/src/main/resources/robotsData/" + getDomainName(dns.getDominio()));
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }


                        String hardCode = null;
                        hardCode = "file:///"+new File(System.getProperty("user.dir") + "/src/main/resources/robotsData/").getAbsoluteFile()+ "/";
                        String base = "";
                        try {
                            base = hardCode + getDomainName(dns.getDominio()) + "/";
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }


                        NoRobotClient nrc = new NoRobotClient("SeekerRobot-1.0");
                        try {
                            nrc.parse( new URL(base) );
                        } catch (NoRobotException e) {
                            e.printStackTrace();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        try {
//                            System.out.println( nrc.isUrlAllowed( new URL(base+urlRestante) ) ) ;
                            if(nrc.isUrlAllowed( new URL(base+urlRestante ))){
                                queuedURL qurl = new queuedURL(dns.getDominio(), dns.getIp());
                                queuedURLDAO.insertURL(qurl);
                            }else{
                                RejectedURL rurl = new RejectedURL(dns.getDominio(), dns.getIp(), "Robots");
                                rejectedURLDAO.insertURL(rurl);
                            }
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        });

    }*/

//    @PostConstruct
//    public void robotsOfDns() {
//        new Thread(t1).start();
//    }

    @PostConstruct
    public void t1() {
        new Thread(downloadRobots).start();
    }

    @PostConstruct
    public void t2() {
        new Thread(sendToQueued).start();
    }

    private Runnable downloadRobots = new Runnable() {
        @Override
        public void run() {
            while (true) {
                List<DNS> listDNS = dnsDAO.getWithoutRobots();

                for (DNS dns : listDNS) {

                    try {
                        String[] x = dns.getDominio().split("/");

                        String url = x[0] + "//" + x[2] + "/robots.txt";
                        long antes = System.currentTimeMillis();
                        boolean aux = downloadRobots(dns.getDominio());
                        long depois = System.currentTimeMillis();
                        long diff = depois - antes;

                        Log log = new Log("Robots", "download", new Date(), diff, url);
                        logDAO.insert(log);


                        if (aux) {
                            dns.setRobots(true);
                        }
                        dns.setRobots(true);
                        dnsDAO.remove(dns);
                        dnsDAO.insertDNS(dns);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };


    private Runnable sendToQueued = new Runnable() {
        @Override
        public void run() {
            while (true) {
//                List<DNS> listDNS = dnsDAO.getDNSPages();
                List<OrderedURL> list = orderedURLDAO.getListToRobots();
                System.out.println("get do ordered" + list.size());

                for (OrderedURL ourl : list) {
                    try {
                        String url = ourl.getUrl();
                        String dominio = null;

                        dominio = getDomainName(url);


                        if (dominio != null) {

                            String[] aux = dominio.split("://");

                            if (dnsDAO.getRobots(dominio) == false) {
                                long antes = System.currentTimeMillis();
                                downloadRobots(ourl.getUrl());
                                long depois = System.currentTimeMillis();
                                long diff = depois - antes;
                                Log log = new Log("Robots", "download", new Date(), diff, dominio);
                                logDAO.insert(log);

                                Date d = new Date();
                                DNS dns = new DNS(dominio, "123", d.getTime(), true, ourl.getPriority());
                                dnsDAO.insertDNS(dns);
                            }

                            if (aux.length > 1) {
                                dominio = aux[1];
                            }

                            String[] x = ourl.getUrl().split("/");

                            if (x.length > 1) {
                                String urlRestante = "";
                                for (int i = 3; i < x.length; i++) {
                                    urlRestante += "/" + x[i];
                                }

                                String hardCode = null;
                                hardCode = "file:///" + new File(System.getProperty("user.dir") + "/src/main/resources/robotsData/").getAbsoluteFile() + "/";
                                String base = "";


                                base = hardCode + dominio + "/";

                                NoRobotClient nrc = new NoRobotClient("SeekerRobot-1.0");

                                nrc.parse(new URL(base));

                                long antes = System.currentTimeMillis();
                                boolean allow = nrc.isUrlAllowed(new URL(base + urlRestante));
                                long depois = System.currentTimeMillis();
                                long diff = depois - antes;

                                Log log = new Log("Robots", "consult", new Date(), diff, base + urlRestante);
                                logDAO.insert(log);

                                if (allow) {
                                    queuedURL qurl = new queuedURL(ourl.getUrl(), "123");
                                    queuedURLDAO.insertURL(qurl);
                                } else {
                                    RejectedURL rurl = new RejectedURL(ourl.getUrl(), "123", "Robots");
                                    rejectedURLDAO.insertURL(rurl);
                                }
//                                }
                            }
                        }
                    } catch (Exception e) {
                        System.out.println(ourl.getUrl());
                        e.printStackTrace();
                    }


                }
//                System.out.println("finalizou");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };


    /*private Runnable t1 = new Runnable() {
        public void run() {
            try {
                while (true) {
                    System.out.println("Começou");
                    List<DNS> listDNS = dnsDAO.getRobots();

                    for (DNS dns : listDNS) {

                        if (dns.getRobots() == true) {
                            String[] x = dns.getDominio().split("/");

                            dnsDAO.remove(dns);
                            String aux = getDomainName(x[0] + "//" + x[2]);
                            System.out.println(aux);
                            dns.setDominio(aux);
                            dns.setRobots(true);
                            dnsDAO.insertDNS(dns);

                        } else {


                            try {
                                String[] x = dns.getDominio().split("/");

                                downloadRobots(x[0] + "//" + x[2] + "/robots.txt");

                                String urlRestante = "";
                                for (int i = 3; i < x.length; i++) {
                                    urlRestante += "/" + x[i];
                                }

                                Path path = Paths.get(System.getProperty("user.dir") + "/src/main/resources/robotsData/" + getDomainName(dns.getDominio()));

                                String hardCode = null;
                                hardCode = "file:///" + new File(System.getProperty("user.dir") + "/src/main/resources/robotsData/").getAbsoluteFile() + "/";
                                String base = "";

                                base = hardCode + getDomainName(dns.getDominio()) + "/";
//                            System.out.println(base);

                                NoRobotClient nrc = new NoRobotClient("SeekerRobot-1.0");

//                            System.out.println(base);
                                nrc.parse(new URL(base));

                                if (nrc.isUrlAllowed(new URL(base + urlRestante))) {
                                    queuedURL qurl = new queuedURL(dns.getDominio(), dns.getIp());
                                    queuedURLDAO.insertURL(qurl);
                                } else {
                                    RejectedURL rurl = new RejectedURL(dns.getDominio(), dns.getIp(), "Robots");
                                    rejectedURLDAO.insertURL(rurl);
                                }

                                System.out.println(dns.getDominio());

                                dnsDAO.remove(dns);
                                String aux = getDomainName(x[0] + "//" + x[2]);
                                System.out.println(aux);
                                dns.setDominio(aux);
                                dns.setRobots(true);
                                dnsDAO.insertDNS(dns);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        *//*String[] x = dns.getDominio().split("/");
                        try {
                            downloadRobots(x[0] + "//" + x[2] + "/robots.txt");
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        String urlRestante = "";
                        for(int i = 3; i < x.length; i++){
                            urlRestante += "/" + x[i];
                        }


                        try {
                            Path path = Paths.get(System.getProperty("user.dir") + "/src/main/resources/robotsData/" + getDomainName(dns.getDominio()));
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }


                        String hardCode = null;
                        hardCode = "file:///"+new File(System.getProperty("user.dir") + "/src/main/resources/robotsData/").getAbsoluteFile()+ "/";
                        String base = "";
                        try {
                            base = hardCode + getDomainName(dns.getDominio()) + "/";
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }


                        NoRobotClient nrc = new NoRobotClient("SeekerRobot-1.0");
                        try {
                            nrc.parse( new URL(base) );
                        } catch (NoRobotException e) {
                            e.printStackTrace();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        try {
//                            System.out.println( nrc.isUrlAllowed( new URL(base+urlRestante) ) ) ;
                            if(nrc.isUrlAllowed( new URL(base+urlRestante ))){
                                queuedURL qurl = new queuedURL(dns.getDominio(), dns.getIp());
                                queuedURLDAO.insertURL(qurl);
                            }else{
                                RejectedURL rurl = new RejectedURL(dns.getDominio(), dns.getIp(), "Robots");
                                rejectedURLDAO.insertURL(rurl);
                            }
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }*//*

                    }
                    Thread.sleep(3000);
                    System.out.println("Finalizou");
                }
            } catch (Exception e) {
            }

        }
    };*/


    public boolean downloadRobots(String url) throws URISyntaxException, IOException {
        boolean resp = false;

        String dominio = getDomainNameToRobots(url);

        String[] aux = url.split("/");
        if(aux.length > 2){
            String urlToConnect = aux[0] + "//" + aux[2] + "/robots.txt";

            String hardCode = "file:///" + new File(System.getProperty("user.dir") + "/src/main/resources/robotsData/").getAbsoluteFile() + "/";

            Path path = Paths.get(System.getProperty("user.dir") + "/src/main/resources/robotsData/" + dominio);

            if (!(Files.exists(path))) {
                Files.createDirectories(path);

                Path path2 = Paths.get(System.getProperty("user.dir") + "/src/main/resources/robotsData/" + dominio + "/" + "robots.txt");
                Files.createFile(path2);

                try {
                    PrintWriter writer = new PrintWriter(path2.toString(), "UTF-8");

                    try (BufferedReader in = new BufferedReader(
                            new InputStreamReader(new URL(urlToConnect).openStream()))) {
                        String line = null;
                        while ((line = in.readLine()) != null) {
                            writer.println(line);
                        }
                        resp = true;
                    } catch (IOException e) {

                    }


                    writer.close();
                } catch (IOException e) {

                }
            }
        }

        return resp;
    }

    public String getDomainName(String url) {
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
            if (domain == null) {
                return url;
            } else {
                return domain.startsWith("www.") ? aux[0] + "://" + domain.substring(4) : aux[0] + "://" + domain;
            }
        }
    }

    public String getDomainNameToRobots(String url) throws URISyntaxException {
        URI uri = new URI(url);
        String domain = uri.getHost();
        if (domain == null) {
            return url;
        } else {
            return domain.startsWith("www.") ? domain.substring(4) : domain;
        }
    }
}
