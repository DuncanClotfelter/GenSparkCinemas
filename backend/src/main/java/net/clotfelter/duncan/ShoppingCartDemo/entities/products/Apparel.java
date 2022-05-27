package net.clotfelter.duncan.ShoppingCartDemo.entities.products;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Data
@Entity
@Inheritance(strategy= InheritanceType.JOINED)
public class Apparel extends Product {
    @Column(name = "product_apparel_type")
    private String type;

    @Column(name = "product_apparel_brand")
    private String brand;

    @Column(name = "product_apparel_design")
    private String design;
}
