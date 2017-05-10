package com.theseeker.crawler.entities;

import javax.persistence.*;
import java.math.BigInteger;

/**
 * Created by claudinei on 09/05/17.
 */
@Entity
@SequenceGenerator(name = "seq_vocab", sequenceName = "vocab_seq", initialValue = 1, allocationSize = 1)
public class Vocabulario {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_vocab")
    @Column(name="id")
    private BigInteger id;

    @Column(name="termo")
    private String termo;

    public Vocabulario() {

    }

    public Vocabulario(String termo) {
        this.termo = termo;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getTermo() {
        return termo;
    }

    public void setTermo(String termo) {
        this.termo = termo;
    }
}
