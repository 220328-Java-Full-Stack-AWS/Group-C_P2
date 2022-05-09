/**
 * Author(s): @Arun Mohan
 * Contributor(s):
 * Purpose: Model class used to represent an Order to be persisted in the database.
 */

package com.revature.TeamCP2.models;

import com.revature.TeamCP2.interfaces.Model;

import javax.persistence.*;

@Entity
@Table(name = "order", schema="public")
public class Order implements Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    // foreign key to the cart table
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cart_id", referencedColumnName = "id")
    public Cart cart;

    // foreign key to the order detail
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_detail_id", referencedColumnName = "id")
    public OrderDetail detail;

    // table columns
    @Column(name = "cart_id");
    private Long cartId;

    @Column(name = "order_detail_id");
    private Long detailId;

    @Column(name = "date_created");
    private String dateCreated;

    // constructors

    public Order() {
    }

    public Order(Long id, Long cartId, Long detailId, String dateCreated) {
        this.id = id;
        this.cartId = cartId;
        this.detailId = detailId;
        this.dateCreated = dateCreated;
    }

    // foreign key methods

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public OrderDetail getDetail() {
        return detail;
    }

    public void setDetail(OrderDetail detail) {
        this.detail = detail;
    }

    // getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }
}
