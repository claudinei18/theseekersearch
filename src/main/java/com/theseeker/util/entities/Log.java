package com.theseeker.util.entities;

import com.fasterxml.jackson.databind.node.BigIntegerNode;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;

/**
 * Created by claudinei on 08/05/17.
 * date        TIMESTAMP ,
 * componente  TEXT ,
 * action      TEXT
 */
@Entity
@SequenceGenerator(name = "seq_log", sequenceName = "log_seq", initialValue = 1, allocationSize = 1)
@Table(name = "log")
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_log")
    @Column(name="id")
    private BigInteger id;

    @Column(name="componente")
    private String componente;

    @Column(name = "acao")
    private String acao;

    @Column(name="date")
    private Date date;

    @Column(name="tempogasto")
    private double tempogasto;

    @Column(name="url")
    private String url;

    public Log(){

    }

    public Log(String componente, String acao, Date date, double tempogasto, String url) {
        this.componente = componente;
        this.acao = acao;
        this.date = date;
        this.tempogasto = tempogasto;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getComponente() {
        return componente;
    }

    public void setComponente(String componente) {
        this.componente = componente;
    }

    public String getAcao() {
        return acao;
    }

    public void setAcao(String acao) {
        this.acao = acao;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getTempogasto() {
        return tempogasto;
    }

    public void setTempogasto(double tempogasto) {
        this.tempogasto = tempogasto;
    }

    @Override
    public String toString() {
        return String.format(
                "LOG[componente='%s', acao='%s', date='%s']",
                componente, acao, date);
    }
}