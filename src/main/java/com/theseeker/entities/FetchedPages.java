package com.theseeker.entities;

import javax.persistence.*;
import java.math.BigInteger;

/**
 * Created by claudinei on 28/03/17.
 */
@Entity
@Table(name = "fetchedpages")
@SequenceGenerator(name = "seq_fetchedpages", sequenceName = "fetchedpages_seq", initialValue = 1, allocationSize = 1)
public class FetchedPages {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_fetchedpages")
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

    public FetchedPages(){

    }

    public FetchedPages(String ip, String dominio, String titulo, String conteudo) {
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
                "DNS[id=%d, domain='%s', titulo='%s', conteudo='%s', ip='%s']",
                id, dominio, titulo, conteudo, ip);
    }
}
