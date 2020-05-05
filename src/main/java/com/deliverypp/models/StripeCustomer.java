package com.deliverypp.models;

import javax.persistence.*;

@Entity
@Table(name = "stripe_customers")
public class StripeCustomer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String stripeCustomerId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStripeCustomerId() {
        return stripeCustomerId;
    }

    public StripeCustomer setStripeCustomerId(String stripeCustomerId) {
        this.stripeCustomerId = stripeCustomerId;
        return this;
    }

    public User getUser() {
        return user;
    }

    public StripeCustomer setUser(User user) {
        this.user = user;
        return this;
    }
}
