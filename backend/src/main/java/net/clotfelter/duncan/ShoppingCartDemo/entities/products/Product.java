package net.clotfelter.duncan.ShoppingCartDemo.entities.products;

import lombok.Data;
import net.clotfelter.duncan.ShoppingCartDemo.entities.products.Book;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Data
@Entity
@Table(name="products")
public class Product {
    @Id
    @Column(name = "product_id", unique=true, nullable=false)
    private int id;

    @Column(name="product_name", nullable=false)
    private String name;

    @Column(name="product_price", nullable=false)
    private double price;

    @Column(name="product_quantity", nullable=false)
    @ColumnDefault("1")
    private int quantity;
}