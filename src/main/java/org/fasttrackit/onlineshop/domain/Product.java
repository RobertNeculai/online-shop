package org.fasttrackit.onlineshop.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;
@Entity

public class Product {
    @Id
    @GeneratedValue
    private long id;
    private String description;
    @NotNull
    private String name;
    @NotNull
    private double price;
    @NotNull
    private int quantity;
    private String imageUrl;

    @ManyToMany(mappedBy = "products")
    private Set<Cart> carts=new HashSet<>();
    // nu includem fetch.type=lazy in .toString

    public Set<Cart> getCarts() {
        return carts;
    }

    public void setCarts(Set<Cart> carts) {
        this.carts = carts;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Product.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("description='" + description + "'")
                .add("name='" + name + "'")
                .add("price=" + price)
                .add("quantity=" + quantity)
                .add("imageUrl='" + imageUrl + "'")
                .toString();
    }
}
