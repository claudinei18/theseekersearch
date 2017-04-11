package com.theseeker.crawler.entities;

import javax.persistence.*;
import java.math.BigInteger;

/**
 * Created by claudinei on 28/03/17.
 */
@Entity
@Table(name = "dns")
public class DNS {
    @Id
    @Column(name="dominio")
    private String dominio;

    @Column(name = "ip")
    private String ip;

    @Column(name="lasttimeaccess")
    private long time;

    @Column(name="robots")
    private boolean robots;


    public DNS(){

    }

    public DNS(String dominio, String ip, long time, boolean robots) {
        this.dominio = dominio;
        this.ip = ip;
        this.time = time;
        this.robots = robots;
    }

    public String getDominio() {
        return dominio;
    }

    public void setDominio(String dominio) {
        this.dominio = dominio;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isRobots() {
        return robots;
    }

    public void setRobots(boolean robots) {
        this.robots = robots;
    }

    @Override
    public String toString() {
        return String.format(
                "DNS[domain='%s', ip='%s', lta='%d']",
                dominio, ip, time);
    }
}