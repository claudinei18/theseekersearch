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
import com.theseeker.util.compress.Compress;
import com.theseeker.util.entities.Log;
import com.theseeker.util.entities.LogDAO.LogDAO;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

                doc = null;

                String name = dominio;
                String nameSHA = getFileName(name);
                String fileName = System.getProperty("user.dir") + "/database/paginas/" + nameSHA;

                /*FileLock lock = null;
                FileChannel channel = null;

                try {
                    // Get a file channel for the file
                    channel = new RandomAccessFile(file, "rw").getChannel();

                    // Use the file channel to create a lock on the file.
                    // This method blocks until it can retrieve the lock.
                    lock = channel.lock();
                } catch (Exception e) {
                    e.printStackTrace();
                }*/

                Path path = Paths.get(fileName);

                if (Files.notExists(path)) {

                    BufferedWriter bw = null;
                    FileWriter fw = null;
                    try {
                        File file = new File(fileName);

                        fw = new FileWriter(file.getAbsoluteFile());
                        bw = new BufferedWriter(fw);

                        String conteudo = "";
                        Date date = new Date();
                        conteudo += date.toString() + "\n";
                        conteudo += text.length() + "\n";
                        conteudo += dominio + "\n";
                        conteudo += text + "\n";

                        long antes = System.currentTimeMillis();

                        Compress.zip(conteudo, fileName + ".zip");

                        long depois = System.currentTimeMillis();
                        long diff = depois - antes;

                        Log log = new Log("Fetcher", "compress", new Date(), diff, dominio);
                        logDAO.insert(log);

                        bw.write(conteudo);
                        bw.newLine();


                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (bw != null)
                                bw.close();

                            if (fw != null)
                                fw.close();

//                            lock.release();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }
            }else{
                Element taglang = doc.select("html").first();
//            System.out.println(taglang.attr("lang"));
                if( taglang.attr("lang").startsWith("en") || taglang.attr("lang").equals("") ){
                    seenURL sl = new seenURL(dominio, ip);
                    seenURLDAO.insertURL(sl);
                }else{
                    doc = null;
                }
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

        if(doc != null){
            FetchedPages fp = new FetchedPages(qurl.getIp(), qurl.getDominio(), doc.title(), doc.html());

            //Chamando o Writer para escrever no banco de dados
            writer.writerInFetchedPages(fp);
        }
    }

    public static String getFileName(String input) {
        MessageDigest mDigest = null;
        try {
            mDigest = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] result = mDigest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }
}
