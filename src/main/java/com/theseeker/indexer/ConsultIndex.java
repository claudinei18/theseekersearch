package com.theseeker.indexer;

import com.theseeker.util.compress.Compress;
import com.theseeker.util.entities.Log;
import com.theseeker.util.entities.LogDAO.LogDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * Created by claudinei on 08/05/17.
 */
@Component
public class ConsultIndex {

    @Autowired
    LogDAO logDAO;

    public String consultar(String termo){
        String shaOfTermo = getFileName(termo);
        String fileName = System.getProperty("user.dir") + "/database/termos/" + shaOfTermo;

        Path file = Paths.get(fileName);
        String conteudo = null;

        Charset charset = Charset.forName("US-ASCII");
        try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                conteudo += line + "\n";
            }
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
        return conteudo;
    }

    @PostConstruct
    public void teste(){
        teste2("Zac Efron");
        teste2("Barack Obama");
        teste2("Donald Trump");
        teste2("Kaley Cuoco");
        teste2("Katy Perry");
        teste2("Lady Gaga");
        teste2("Osama Bin Laden");
        teste2("Ellen DeGeneres");
        teste2("Isaac Newton");
        teste2("Albert Einstein");

        teste2("Paris");
        teste2("New York");
        teste2("Brazil");
        teste2("Oz");
        teste2("Barcelona");
        teste2("Tokyo");
        teste2("Brasilia");
        teste2("Canada");
        teste2("Sao Paulo");
        teste2("Brooklyn");

        teste2("Colosseum");
        teste2("Hogwarts");
        teste2("Eifel Tower");
        teste2("Central Park");
        teste2("Belo Horizonte");
        teste2("Contagem");
        teste2("Betim");
        teste2("Campinas");
        teste2("Half Blood Camp");
        teste2("Christ the Redeemer");
    }

    public void teste2(String termo){
        System.out.println("CONSULTANDO");

        long antes = System.currentTimeMillis();
        String resp = consultar(termo);
        long depois = System.currentTimeMillis();
        long diff = depois - antes;

        Log log = new Log("ConsultIndex", "consult", new Date(), diff, termo);
        logDAO.insert(log);

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
