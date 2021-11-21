package com.example.reservisland.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CONFIG")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "key", "value"})
public class Config {

    @Id
    @Column(name = "ID_CONFIG")
    private long id;

    @Column(name = "KEY")
    private String key;

    @Column(name = "VALUE")
    private String value;
}
