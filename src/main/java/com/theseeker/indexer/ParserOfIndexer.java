package com.theseeker.indexer;

import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.*;
import com.theseeker.crawler.entities.OrderedURL;
import com.theseeker.crawler.entities.Pages;
import com.theseeker.crawler.entities.RejectedURL;
import com.theseeker.crawler.entities.Vocabulario;
import com.theseeker.crawler.entities.orderedURLsDAO.OrderedURLDAO;
import com.theseeker.crawler.entities.rejectedURL.rejectedURLDAO;
import com.theseeker.crawler.entities.vocabularioDAO.VocabularioDAO;
import com.theseeker.util.compress.Compress;
import com.theseeker.util.entities.Log;
import com.theseeker.util.entities.LogDAO.LogDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

/**
 * Created by claudinei on 05/05/17.
 */

@Component
public class ParserOfIndexer {

    @Autowired
    rejectedURLDAO rejectedURLDAO;

    @Autowired
    LogDAO logDAO;

    @Autowired
    OrderedURLDAO orderedURLDAO;

    @Autowired
    VocabularioDAO vocabularioDAO;

    public ParserOfIndexer() {

    }

    public List<EntitiesResult> getListEntitiesResult(String conteudo) {
        NaturalLanguageUnderstanding service = new NaturalLanguageUnderstanding(
                NaturalLanguageUnderstanding.VERSION_DATE_2017_02_27,
                "b845dcb3-1e77-4eb1-bf43-3cd7d71f9067",
                "Pd7kWrDmARew"
        );

        try {
            EntitiesOptions entities = new EntitiesOptions.Builder().limit(10000).build();
            Features features = new Features.Builder().entities(entities).build();
            AnalyzeOptions parameters = new AnalyzeOptions.Builder().text(conteudo).features(features).build();
            AnalysisResults results = service.analyze(parameters).execute();
            return results.getEntities();
        } catch (Exception e) {
            return null;
        }

    }

    public void start(Pages page) {

        boolean contemTermo = false;


        long antes = System.currentTimeMillis();
        List<EntitiesResult> list = getListEntitiesResult(page.getConteudo());
        long depois = System.currentTimeMillis();
        long diff = depois - antes;

        Log log = new Log("ParserOfIndex", "watson", new Date(), diff, page.getDominio());
        logDAO.insert(log);

        String termosDaPagina = "";

        if (list != null) {
            try {
                termosDaPagina += page.getDominio() + "\n";
                antes = System.currentTimeMillis();
                for (EntitiesResult e : list) {
                    String type = e.getType();
                    if (type.equals("Person") ||
                            type.equals("Location") ||
                            type.equals("GeographicFeature")) {

                        BufferedWriter bw = null;
                        FileWriter fw = null;

                        String name = e.getText();
                        name = name.replaceAll("[^a-zA-Z ]", "").toLowerCase();

                        String nameSHA = getFileName(name);
                        String fileName = System.getProperty("user.dir") + "/database/termos/" + nameSHA;

                        FileLock lock = null;
                        FileChannel channel = null;
                        try {

                            String conteudo = "";
                            // if file doesnt exists, then create it
                            Path path = Paths.get(fileName);

                            if (Files.notExists(path)) {

                                File file = new File(fileName);

                                // Get a file channel for the file
                                channel = new RandomAccessFile(file, "rw").getChannel();

                                // Use the file channel to create a lock on the file.
                                // This method blocks until it can retrieve the lock.
                                lock = channel.lock();

                                file.createNewFile();

                                String termo = e.getText();

                                termo = termo.replaceAll("[^a-zA-Z ]", "").toLowerCase();
                                conteudo += termo + "\n";

                                Vocabulario v = new Vocabulario(termo);
                                vocabularioDAO.insert(v);

                                termosDaPagina += termo + "\n";

                                if (e.getDisambiguation() != null) {
                                    if (e.getDisambiguation().getDbpediaResource() != null) {
                                        OrderedURL ourl = new OrderedURL(e.getDisambiguation().getDbpediaResource(), 12);
                                        orderedURLDAO.insert(ourl);

                                        conteudo += e.getDisambiguation().getDbpediaResource() + "\n";
                                    } else {
                                        conteudo += "http://null" + "\n";
                                    }
                                } else {
                                    conteudo += "http://null" + "\n";
                                }
                                conteudo += e.getType() + "\n";
                                conteudo += page.getDominio() + "\n";
                                conteudo += e.getCount() + "\n";
                                conteudo += e.getRelevance().toString() + "\n";

                                // true = append file
                                fw = new FileWriter(file.getAbsoluteFile());
                                bw = new BufferedWriter(fw);

                                bw.write(conteudo);
                                bw.newLine();

                                try {
                                    if (bw != null)
                                        bw.close();

                                    if (fw != null)
                                        fw.close();

                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }

                                // Release the lock - if it is not null!
                                if (lock != null) {
                                    try {
                                        lock.release();
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    }
                                }

                                // Close the file
                                try {
                                    if (channel != null)
                                        channel.close();
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }


                            } else {
                                conteudo += page.getDominio() + "\n";
                                conteudo += e.getCount() + "\n";
                                conteudo += e.getRelevance().toString() + "\n\n";

                                // true = append file
                                fw = new FileWriter(path.toFile(), true);
                                bw = new BufferedWriter(fw);

                                bw.write(conteudo);
                                bw.newLine();

                                try {
                                    if (bw != null)
                                        bw.close();

                                    if (fw != null)
                                        fw.close();

                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            }
                            contemTermo = true;

                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }

                    }
                }
                depois = System.currentTimeMillis();
                diff = depois - antes;

                log = new Log("ParserOfIndex", "watson(" + list.size() + ")", new Date(), diff, page.getDominio());
                logDAO.insert(log);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        if (contemTermo) {
            String name = page.getDominio();
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
                    conteudo += page.getConteudo().length() + "\n";
                    conteudo += page.getDominio() + "\n";
                    conteudo += page.getConteudo() + "\n";

                    antes = System.currentTimeMillis();

                    Compress.zip(conteudo, fileName + ".zip");

                    depois = System.currentTimeMillis();
                    diff = depois - antes;

                    log = new Log("ParserOfIndexer", "compress", new Date(), diff, page.getDominio());
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

        } else {
            RejectedURL rurl = new RejectedURL(page.getDominio(), "", "Indexer");
            rejectedURLDAO.insertURL(rurl);
        }

        if(list != null){
            if(list.size() > 0 && contemTermo){
                try {
                    BufferedWriter bw3 = null;
                    FileWriter fw3 = null;

                    FileLock lock3 = null;
                    FileChannel channel3 = null;

                    String name = page.getDominio();
                    String nameSHA = getFileName(name);
                    String fileTermosPorPagina = System.getProperty("user.dir") + "/database/termosPorPagina/" +  nameSHA + ".txt";
                    File fileVocab = new File(fileTermosPorPagina);

                    // Get a file channel for the file
                    channel3 = new RandomAccessFile(fileVocab, "rw").getChannel();

                    // Use the file channel to create a lock on the file.
                    // This method blocks until it can retrieve the lock.
                    lock3 = channel3.lock();


                    // true = append file
                    fw3 = new FileWriter(fileVocab.getAbsoluteFile(), true);
                    bw3 = new BufferedWriter(fw3);

                    bw3.write(termosDaPagina);
                    bw3.newLine();

                    try {
                        if (bw3 != null)
                            bw3.close();

                        if (fw3 != null)
                            fw3.close();

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                    // Release the lock - if it is not null!
                    if (lock3 != null) {
                        try {
                            lock3.release();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }

                    // Close the file
                    try {
                        if (channel3 != null)
                            channel3.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
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

    public boolean writeToFile(String name, String conteudo) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(name));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        try {
            writer.write(conteudo);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public synchronized void insertInVocabulario(String termo) {
        BufferedWriter bw2 = null;
        FileWriter fw2 = null;

        String fileNameVocab = System.getProperty("user.dir") + "/database/vocabulario.txt";
        File fileVocab = new File(fileNameVocab);


        // true = append file
        try {
            fw2 = new FileWriter(fileVocab.getAbsoluteFile(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        bw2 = new BufferedWriter(fw2);

        try {
            bw2.write(termo);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            bw2.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (bw2 != null)
                bw2.close();

            if (fw2 != null)
                fw2.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
