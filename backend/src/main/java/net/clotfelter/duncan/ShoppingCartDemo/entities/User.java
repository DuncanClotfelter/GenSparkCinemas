package net.clotfelter.duncan.ShoppingCartDemo.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="users")
public class User {
    @Id
    @Column(name = "user_id", unique = true, nullable = false)
    private int id;

    @OneToOne(orphanRemoval = true)
    private Cart cart;
}
