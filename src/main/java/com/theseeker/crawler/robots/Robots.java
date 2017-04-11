package com.theseeker.crawler.robots;

import com.theseeker.crawler.entities.DNS;
import com.theseeker.util.url.URLCanonicalizer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import com.theseeker.crawler.entities.dnsDAO.DNSDao;

import java.io.*;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * Created by claudinei on 11/04/17.
 */
@Component
public class Robots {

    @Autowired
    DNSDao dnsDAO;

    @PostConstruct
    public void resolveRobots() throws URISyntaxException, IOException {
        String url;

        List<DNS> listDNS = dnsDAO.getDNS();

        for(DNS dns: listDNS){
            dns.setRobots(true);
            dnsDAO.remove(dns);
            dnsDAO.setTime(dns);

            String[] x = dns.getDominio().split("/");
            downloadRobots(x[0] + "//" + x[2] + "/robots.txt");

        }
    }

    public void downloadRobots(String url) throws URISyntaxException, IOException {
        String dominio = getDomainName(url);
        String hardCode = "file:///"+new File(System.getProperty("user.dir") + "/src/main/resources/robotsData/").getAbsoluteFile()+"/";

        Path path = Paths.get(System.getProperty("user.dir") + "/src/main/resources/robotsData/" + dominio);

        if( !(Files.exists(path))){
            Files.createDirectories(path);

            Path path2 = Paths.get(System.getProperty("user.dir") + "/src/main/resources/robotsData/" + dominio + "/" + "robots.txt");
            Files.createFile(path2);

            try{
                PrintWriter writer = new PrintWriter(path2.toString(), "UTF-8");

                try(BufferedReader in = new BufferedReader(
                        new InputStreamReader(new URL(url).openStream()))) {
                    String line = null;
                    while((line = in.readLine()) != null) {
                        writer.println(line);
                    }
                } catch (IOException e) {

                }



                writer.close();
            } catch (IOException e) {

            }
        }
    }

    public String getDomainName(String url) throws URISyntaxException {
        URI uri = new URI(url);
        String domain = uri.getHost();
        return domain.startsWith("www.") ? domain.substring(4) : domain;
    }
}
