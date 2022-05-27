package net.clotfelter.duncan.ShoppingCartDemo.entities.products;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Data
@Entity
@Inheritance(strategy= InheritanceType.JOINED)
public class Book extends Product {
    @Column(name = "product_book_genre")
    private String genre;

    @Column(name = "product_book_author")
    private String author;

    @Column(name = "product_book_publisher")
    private String publisher;
}
