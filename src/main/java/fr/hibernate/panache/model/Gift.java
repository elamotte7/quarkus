package fr.hibernate.panache.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.*;

@Entity
public class Gift  extends PanacheEntity {

    @Column(length = 40, unique = true)
    public String name;

    public Gift() {
    }

    public Gift(String name) {
        this.name = name;
    }
}