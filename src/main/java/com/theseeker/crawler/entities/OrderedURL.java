package com.theseeker.crawler.entities;

import javax.persistence.*;
import java.math.BigInteger;

/**
 * Created by claudinei on 11/04/17.
 */
@Entity
@Table(name = "orderedurl")
@SequenceGenerator(name = "orderedurl_generator", sequenceName = "orderedurl_seq", initialValue = 1, allocationSize = 1)
public class OrderedURL {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orderedurl_generator")
    @Column(name="id")
    private BigInteger id;

    @Column(name="url")
    private String url;

    @Column(name = "priority")
    private int priority;

    public OrderedURL(){

    }

    public OrderedURL(String url, int priority) {
        this.url = url;
        this.priority = priority;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
                "orderedurl[id=%d, url='%s', priority='%d']",
                id, url, priority);
    }
}