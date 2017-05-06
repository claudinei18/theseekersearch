package com.theseeker.indexer;

import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.*;
import com.theseeker.crawler.entities.Pages;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Created by claudinei on 05/05/17.
 */

@Component
public class ParserOfIndexer {

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




        List<EntitiesResult> list = getListEntitiesResult(page.getConteudo());
        if (list != null) {

            for (EntitiesResult e : list) {
                String type = e.getType();
                if (type.equals("Person") ||
                        type.equals("Location")) {

                    BufferedWriter bw = null;
                    FileWriter fw = null;

                    String name = e.getText();
                    String nameSHA = getFileName(name);
                    String fileName = System.getProperty("user.dir") + "/database/termos/" + nameSHA;

                    FileLock lock = null;
                    FileChannel channel = null;
                    try {

                        File file = new File(fileName);

                        // Get a file channel for the file
                        channel = new RandomAccessFile(file, "rw").getChannel();

                        // Use the file channel to create a lock on the file.
                        // This method blocks until it can retrieve the lock.
                        lock = channel.lock();

                        /*
                           use channel.lock OR channel.tryLock();
                        */

                        /*// Try acquiring the lock without blocking. This method returns
                        // null or throws an exception if the file is already locked.
                        try {
                            lock = channel.tryLock();
                        } catch (OverlappingFileLockException e2) {
                            // File is already locked in this thread or virtual machine
//                            e2.printStackTrace();
                        }*/


                        String conteudo = "aa";
                        // if file doesnt exists, then create it
                        if (!file.exists()) {
                            file.createNewFile();

                            conteudo += "notExists" + "\n";
                            String termo = e.getText();
                            conteudo += termo + "\n";
                            conteudo += e.getDisambiguation().getDbpediaResource() + "\n";
                            conteudo += e.getType() + "\n";
                            conteudo += "texto do site do watson" + "\n\n";
                            conteudo += page.getDominio() + "\n";
                            conteudo += e.getRelevance().toString() + "\n\n";

                            // true = append file
                            fw = new FileWriter(file.getAbsoluteFile());
                            bw = new BufferedWriter(fw);

                            bw.write(conteudo);
                            bw.newLine();


                        } else {
                            conteudo += "Exists " + e.getText() + "\n";
                            conteudo += page.getDominio() + "\n";
                            conteudo += e.getRelevance().toString() + "\n\n";

                            // true = append file
                            fw = new FileWriter(file.getAbsoluteFile(), true);
                            bw = new BufferedWriter(fw);

                            bw.write(conteudo);
                            bw.newLine();
                        }


                    } catch (IOException e2) {
                        e2.printStackTrace();
                    } finally {
                        try {
                            if (bw != null)
                                bw.close();

                            if (fw != null)
                                fw.close();

                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                        // Release the lock - if it is not null!
                        if( lock != null ) {
                            try {
                                lock.release();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }

                        // Close the file
                        try {
                            channel.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }

                /*System.out.println(b.getText());
                System.out.println(b.getCount());
                System.out.println(b.getRelevance());
                System.out.println(b.getType());
                System.out.println(b.getDisambiguation().getDbpediaResource());
                System.out.println(b.getDisambiguation().getSubtype());*/
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

        return sb.toString() + ".txt";
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
}
