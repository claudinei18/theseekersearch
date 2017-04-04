package com.theseeker.crawler.dns;

import java.net.InetAddress;
import java.net.URL;

/**
 * Created by claudinei on 28/03/17.
 */
public class DNSUtil {
    public static InetAddress getIp(String dominio) {
        try {
            InetAddress ip = InetAddress.getByName(new URL(dominio).getHost());

            return ip;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
