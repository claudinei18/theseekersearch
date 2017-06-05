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
import java.util.TreeMap;
import java.util.Iterator;
import java.util.Set;
import java.util.*;

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
                    if(dbPediaContent != null){
                        System.out.println(dbPediaContent);
                    }
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
            resp.put(termo, 0.0);
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
//            e.printStackTrace();
            return null;
        }
        return resp;
    }

    public static void teste(String termo){
        System.out.println(termo);
        System.out.println(getFileName(termo));
        HashMap<String, Double> hash = BM25(termo);
        Map<String, Double> treeMap = new TreeMap<String, Double>(hash);
//        printMap(treeMap, termo);
        treeMap = sortByValue(treeMap);
        printMap(treeMap, termo);

        /*if(hash != null){
            for(Map.Entry<String, Double> entry : hash.entrySet()){
                System.out.printf("Key : %s and Value: %s %n", entry.getKey(), entry.getValue());
                System.out.println(getFileName(entry.getKey()));
            }
        }*/
    }

    public static void main(String[] args){
/*
Albert Einsten
Cristiano Ronaldo
Justin Bieber
Donald Trump
Kendrick Lamar
*/
/*
New York City
Washington
London
Hong Kong
Thailand
*/
        //teste("thailand");
        System.out.println(getFileName("cristiano ronaldo"));
        /*
        teste("barack obama sr");
        teste("china");
        teste("arnold schwarzenegger");
        teste("bahamas");
        teste("washington");*/
    }

    public static void printMap(Map<String,Double> map, String termo) {
        System.out.println("PRINTMAP " + termo);
        Set s = map.entrySet();
        Iterator it = s.iterator();
        while ( it.hasNext() ) {
            Map.Entry entry = (Map.Entry) it.next();
            String key = (String) entry.getKey();
            Double value = (Double) entry.getValue();
            System.out.println(key + " => " + value);
        }//while
        System.out.println("========================");
    }//printMap

    /*private static <K, V> Map<K, V> sortByValue(Map<K, V> map) {
        List<Entry<K, V>> list = new LinkedList<>(map.entrySet());
        Collections.sort(list, new Comparator<Object>() {
            @SuppressWarnings("unchecked")
            public int compare(Object o1, Object o2) {
                return ((Comparable<V>) ((Map.Entry<K, V>) (o1)).getValue()).compareTo(((Map.Entry<K, V>) (o2)).getValue());
            }
        });

        Map<K, V> result = new LinkedHashMap<>();
        for (Iterator<Entry<K, V>> it = list.iterator(); it.hasNext();) {
            Map.Entry<K, V> entry = (Map.Entry<K, V>) it.next();
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }*/

    public static <K, V extends Comparable<? super V>> Map<K, V>
    sortByValue( Map<K, V> map )
    {
        List<Map.Entry<K, V>> list =
                new LinkedList<Map.Entry<K, V>>( map.entrySet() );
        Collections.sort( list, new Comparator<Map.Entry<K, V>>()
        {
            public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
            {
                return (o1.getValue()).compareTo( o2.getValue() );
            }
        } );

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list)
        {
            result.put( entry.getKey(), entry.getValue() );
        }
        return result;
    }

}
