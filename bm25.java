import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * Created by claudinei on 22/05/17.
 */
public class bm25 {

    public static HashMap<String, Double> BM25(String termo) {
        HashMap<String, Double> resp = new HashMap<String, Double>();

        String termWithoutTrash = termo.replaceAll("[^a-zA-Z ]", "").toLowerCase();

        String nameSHA = getFileName(termWithoutTrash);

        List<String> listOfUrls = new ArrayList<String>();
        HashMap<String, Integer> urlFreq = new HashMap<String, Integer>();

        long N;
        long ni = 0;

        try {
            String filePaginas = System.getProperty("user.dir") + "/database/paginas/";
            N = Files.list(Paths.get(filePaginas)).count();

            String fileName = System.getProperty("user.dir") + "/database/termos/" + nameSHA;
            BufferedReader brQueuedURL = new BufferedReader(new FileReader(fileName));
            String read = null;

            while ((read = brQueuedURL.readLine()) != null) {
                if(read.startsWith("http://dbpedia.org")){
                    String dbPediaContent = getDbPedia(read);
                    System.out.println(dbPediaContent);
                }
                else if (!read.startsWith("http://dbpedia.org") &&
                        !read.startsWith("http://null") &&
                        read.startsWith("http")) {
                    listOfUrls.add(read);
                    String x = read;
                    read = brQueuedURL.readLine();

                    urlFreq.put(x, Integer.valueOf(read));
                    ni++;
                }
            }
            brQueuedURL.close();

            double log = ((N - ni + 0.5) / (ni + 0.5));
            log = Math.log(log) / Math.log(2);


            for (String s : listOfUrls) {
                Integer fij = urlFreq.get(s);

                int K1 = 1;
                int b = 1;
                double avg = 220.58;

                String pageSHA = getFileName(s);
                BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/database/paginas/" + pageSHA));
                String line = null;
                line = br.readLine();
                line = br.readLine();

                br.close();

                Integer lenPage = Integer.valueOf(line);

                Double Bij = (((K1 + 1) * fij) / (K1 * ((1 - b) + b * (lenPage / avg))) + fij);
                Bij = Bij * log;

                resp.put(s, Bij);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resp;
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

    public static String getDbPedia(String termo){
        String fileName = getFileName(termo);
        String resp = "";
        try{
            BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/database/paginas/" + fileName));
            String line = null;
            line = br.readLine();
            line = br.readLine();
            line = br.readLine();
            line = br.readLine();
            resp = line;
        }catch (Exception e){
            e.printStackTrace();
        }
        return resp;
    }

    public static void main(String[] args){
        HashMap<String, Double> hash = BM25("gordon cummins");
        for(Map.Entry<String, Double> entry : hash.entrySet()){
            System.out.printf("Key : %s and Value: %s %n", getFileName(entry.getKey()), entry.getValue());
        }
    }
}
