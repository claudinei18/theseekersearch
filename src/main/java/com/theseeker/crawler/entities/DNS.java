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

    @Column(name="priority")
    private int priority;


    public DNS(){

    }

    public DNS(String dominio, String ip, long time, boolean robots, int priority) {
        this.dominio = dominio;
        this.ip = ip;
        this.time = time;
        this.robots = robots;
        this.priority = priority;
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

    public boolean getRobots() {
        return robots;
    }

    public void setRobots(boolean robots) {
        this.robots = robots;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return String.format(
                "DNS[domain='%s', ip='%s', lta='%d']",
                dominio, ip, time);
    }
}