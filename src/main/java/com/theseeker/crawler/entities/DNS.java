package com.theseeker.crawler.entities;

import javax.persistence.*;
import java.math.BigInteger;

/**
 * Created by claudinei on 28/03/17.
 */
@Entity
@Table(name = "dns")
@SequenceGenerator(name = "dns_generator", sequenceName = "dns_seq", initialValue = 1, allocationSize = 1)
public class DNS {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dns_generator")
    @Column(name="id")
    private BigInteger id;

    @Column(name="dominio")
    private String dominio;

    @Column(name = "ip")
    private String ip;

    public DNS(){

    }

    public DNS(String dominio, String ip) {
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
                "DNS[id=%d, domain='%s', ip='%s']",
                id, dominio, ip);
    }
}