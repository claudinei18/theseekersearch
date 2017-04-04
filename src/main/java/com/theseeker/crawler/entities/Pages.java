package com.theseeker.crawler.entities;

/**
 * Created by claudinei on 04/04/17.
 */

import javax.persistence.*;
import java.math.BigInteger;

@Entity
@Table(name = "pages")
@SequenceGenerator(name = "seq_pages", sequenceName = "pages_seq", initialValue = 1, allocationSize = 1)
public class Pages {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_pages")
    @Column(name="id")
    private BigInteger id;

    @Column(name="ip")
    private String ip;

    @Column(name="dominio")
    private String dominio;

    @Column(name="titulo")
    private String titulo;

    @Column(name="conteudo")
    private String conteudo;

    public Pages(){

    }

    public Pages(String ip, String dominio, String titulo, String conteudo) {
        this.ip = ip;
        this.dominio = dominio;
        this.titulo = titulo;
        this.conteudo = conteudo;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDominio() {
        return dominio;
    }

    public void setDominio(String dominio) {
        this.dominio = dominio;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    @Override
    public String toString() {
        return String.format(
                "Pages[id=%d, domain='%s', titulo='%s', conteudo='%s', ip='%s']",
                id, dominio, titulo, conteudo, ip);
    }
}

