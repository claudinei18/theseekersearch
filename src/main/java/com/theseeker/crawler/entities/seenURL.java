package com.theseeker.crawler.entities;

import javax.persistence.*;
import java.math.BigInteger;

/**
 * Created by claudinei on 28/03/17.
 */
@Entity
@Table(name = "seenurl")
@SequenceGenerator(name = "seq_seenurl", sequenceName = "seenurl_seq", initialValue = 1, allocationSize = 1)
public class seenURL {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_seenurl")
    @Column(name="id")
    private BigInteger id;

    @Id
    @Column(name="dominio")
    private String dominio;

    @Column(name="ip")
    private String ip;

    public seenURL(){

    }

    public seenURL(String dominio, String ip) {
        this.dominio = dominio;
        this.ip = ip;
    }

    public seenURL(String dominio) {
        this.dominio = dominio;
        this.ip = "";
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
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

    @Override
    public String toString() {
        return String.format(
                "seenURL[id=%d, domain='%s']",
                id, dominio);
    }
}
