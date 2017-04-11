package com.theseeker.crawler.entities;

import javax.persistence.*;
import java.math.BigInteger;

/**
 * Created by claudinei on 28/03/17.
 */
@Entity
@Table(name = "queuedURL")
@SequenceGenerator(name = "seq_queuedURL", sequenceName = "queuedURL_seq", initialValue = 1, allocationSize = 1)
public class queuedURL {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_queuedURL")
    @Column(name="id")
    private BigInteger id;

    @Column(name="dominio")
    private String dominio;

    @Column(name="ip")
    private String ip;

    public queuedURL(){

    }

    public queuedURL(String dominio, String ip) {
        this.dominio = dominio;
        this.ip = ip;
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
                "queuedURL[id=%d, domain='%s', ip='%s']",
                id, dominio);
    }
}

