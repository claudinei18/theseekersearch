package com.theseeker.util.compress;

import java.io.*;
import java.util.zip.*;

/**
 * Created by claudinei on 08/05/17.
 */
public class Compress {
    static final int BUFFER = 2048;

    public static void zip(String conteudo, String filename) {
        try {

            ByteArrayInputStream x = new ByteArrayInputStream(conteudo.getBytes());

            FileOutputStream dest = new FileOutputStream(filename);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));

            byte data[] = new byte[BUFFER];

            BufferedInputStream origin = new BufferedInputStream(x, BUFFER);

            ZipEntry entry = new ZipEntry(filename);

            out.putNextEntry(entry);
            int count;
            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                out.write(data, 0, count);
            }

            x.close();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String unzip(String filename) {
        String resp = "";
        try {

            FileInputStream fis = new FileInputStream(filename);
            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
            ZipEntry entry;

            while ((entry = zis.getNextEntry()) != null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(zis));
                String aux = "";
                while ((aux = br.readLine()) != null) {
                    resp += aux;
                }
                System.out.println(resp);
            }
            zis.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resp;
    }
}