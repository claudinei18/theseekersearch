package com.theseeker.crawler.entities;

/**
 * Created by claudinei on 11/04/17.
 */

import javax.persistence.*;
import java.math.BigInteger;
@Entity
@Table(name = "rejectedURL")
@SequenceGenerator(name = "rejectedURL_seq", sequenceName = "rejectedURL_seq", initialValue = 1, allocationSize = 1)
public class RejectedURL {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rejectedURL_seq")
    @Column(name="id")
    private BigInteger id;

    @Id
    @Column(name="dominio")
    private String dominio;

    @Column(name="ip")
    private String ip;

    @Column(name="quemrejeitou")
    private String quemRejeitou;

    public RejectedURL(){

    }

    public RejectedURL(String dominio, String ip, String quemRejeitou) {
        this.dominio = dominio;
        this.ip = ip;
        this.quemRejeitou = quemRejeitou;

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

    public String getQuemRejeitou() {
        return quemRejeitou;
    }

    public void setQuemRejeitou(String quemRejeitou) {
        this.quemRejeitou = quemRejeitou;
    }

    @Override
    public String toString() {
        return String.format(
                "rejectedURL[id=%d, domain='%s', ip='%s']",
                id, dominio, ip);
    }
}



