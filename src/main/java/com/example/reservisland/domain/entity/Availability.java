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
import java.time.LocalDate;

@Entity
@Table(name = "AVAILABILITY")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "date", "reservations"})
public class Availability {

    @Id
    @Column(name = "ID_AVAILABILITY")
    @GeneratedValue(generator = "sq_availability_id", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "sq_availability_id", sequenceName = "sq_availability_id", allocationSize = 1)
    private long id;

    @Column(name = "DATE")
    private LocalDate date;

    @Column(name = "RESERVATIONS")
    private Integer reservations;
}
