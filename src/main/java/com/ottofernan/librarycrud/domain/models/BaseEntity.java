package com.ottofernan.librarycrud.domain.models;

import javax.persistence.*;

@MappedSuperclass
public abstract class BaseEntity {

    public BaseEntity() {
    }

    public BaseEntity(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
