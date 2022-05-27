package net.clotfelter.duncan.ShoppingCartDemo.entities;

import lombok.Data;
import lombok.ToString;
import net.clotfelter.duncan.ShoppingCartDemo.entities.products.Product;

import javax.persistence.*;
import java.util.*;

@Data
@Entity
@Table(name="carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany(orphanRemoval = true)
    private Set<Product> products = new HashSet<>();

    @ToString.Include
    double getTotal() {
        return products.stream().mapToDouble(p -> (p.getPrice() * p.getQuantity())).reduce(0, Double::sum);
    }
}