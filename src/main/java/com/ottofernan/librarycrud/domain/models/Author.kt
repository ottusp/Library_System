package com.ottofernan.librarycrud.domain.models

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.Entity
import javax.persistence.ManyToMany
import javax.persistence.Table

@Entity
@Table(name = "Authors")
class Author: Person() {

    @JsonIgnore
    @ManyToMany(mappedBy = "authors")
    var books = mutableSetOf<Book>(Book())

    override fun toString(): String =
            "$firstName $lastName"
}