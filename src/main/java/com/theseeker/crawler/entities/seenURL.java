package com.theseeker.crawler.entities;

import javax.persistence.*;
import java.math.BigInteger;

/**
 * Created by claudinei on 28/03/17.
 */
@Entity
@Table(name = "fetchedpages")
@SequenceGenerator(name = "seq_fetchedpages", sequenceName = "fetchedpages_seq", initialValue = 1, allocationSize = 1)
public class seenURL {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_fetchedpages")
    @Column(name="id")
    private BigInteger id;

    @Column(name="dominio")
    private String dominio;

    public seenURL(){

    }

    public seenURL(String dominio) {
        this.dominio = dominio;
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

    @Override
    public String toString() {
        return String.format(
                "DNS[id=%d, domain='%s']",
                id, dominio);
    }
}
