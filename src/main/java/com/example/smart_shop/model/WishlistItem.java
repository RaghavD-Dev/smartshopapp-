package com.example.smart_shop.model;

import jakarta.persistence.*;

@Entity
@Table(name = "wishlist_items")
public class WishlistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Product product;

    private boolean priceDropAlert;
    private boolean purchaseGoal;

    public Long getId() { return id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public boolean isPriceDropAlert() { return priceDropAlert; }
    public void setPriceDropAlert(boolean priceDropAlert) { this.priceDropAlert = priceDropAlert; }

    public boolean isPurchaseGoal() { return purchaseGoal; }
    public void setPurchaseGoal(boolean purchaseGoal) { this.purchaseGoal = purchaseGoal; }
}
