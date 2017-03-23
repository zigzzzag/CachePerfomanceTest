package org.zigzzzag;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "Branch")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Branch {

    @Id
    @SequenceGenerator(name = "seq", sequenceName = "cache_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String data1;

    @Column(nullable = false)
    private String data2;

    @Column(nullable = false)
    private String data3;

    @Column(nullable = false)
    private String data4;

    @Column(nullable = false)
    private String data5;

    @Column(nullable = false)
    private Date shelfLife;

    public Branch() {
        // empty constructor
    }

    Branch name(String name) {
        this.name = name;
        return this;
    }

    Branch type(String type) {
        this.type = type;
        return this;
    }

    Branch data1(String data1) {
        this.data1 = data1;
        return this;
    }

    Branch data2(String data2) {
        this.data2 = data2;
        return this;
    }

    Branch data3(String data3) {
        this.data3 = data3;
        return this;
    }

    Branch data4(String data4) {
        this.data4 = data4;
        return this;
    }

    Branch data5(String data5) {
        this.data5 = data5;
        return this;
    }

    Branch shelfLife(Date shelfLife) {
        this.shelfLife = shelfLife;
        return this;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData1() {
        return data1;
    }

    public void setData1(String data1) {
        this.data1 = data1;
    }

    public String getData2() {
        return data2;
    }

    public void setData2(String data2) {
        this.data2 = data2;
    }

    public String getData3() {
        return data3;
    }

    public void setData3(String data3) {
        this.data3 = data3;
    }

    public String getData4() {
        return data4;
    }

    public void setData4(String data4) {
        this.data4 = data4;
    }

    public String getData5() {
        return data5;
    }

    public void setData5(String data5) {
        this.data5 = data5;
    }

    public Date getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(Date shelfLife) {
        this.shelfLife = shelfLife;
    }
}
