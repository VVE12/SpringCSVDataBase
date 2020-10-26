package com.project.springcsvdatabase.model;

import javax.persistence.*;

@Entity
@Table(name = "price_item")
public class PriceItem {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "vendor")
    private String vendor;

    @Column(name = "number")
    private String number;

    @Column(name = "searchvendor")
    private String searchVendor;

    @Column(name = "searchnumber")
    private String searchNumber;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private float price;

    @Column(name = "count")
    private int count;

    public PriceItem() {
    }

    public PriceItem(String id, String vendor, String number, String searchVendor, String searchNumber, String description, float price, int count) {
        this.id = id;
        this.vendor = vendor;
        this.number = number;
        this.searchVendor = searchVendor;
        this.searchNumber = searchNumber;
        this.description = description;
        this.price = price;
        this.count = count;
    }

    @Override
    public String toString() {
        return "PriceItem{" +
                "id=" + id +
                ", vendor='" + vendor + '\'' +
                ", number='" + number + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", count=" + count +
                '}';
    }
}
