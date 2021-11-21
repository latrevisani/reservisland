package com.example.reservisland.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "VISITOR")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "email", "fullName"})
public class Visitor {

    @Id
    @Column(name = "ID_VISITOR")
    @GeneratedValue(generator = "sq_visitor_id", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "sq_visitor_id", sequenceName = "sq_visitor_id", allocationSize = 1)
    private long id;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "FULLNAME")
    private String fullName;
}
