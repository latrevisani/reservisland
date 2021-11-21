package com.example.reservisland.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "RESERVATION")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "visitor", "bookingId", "arrivalDate", "departureDate"})
public class Reservation {

    @Id
    @Column(name = "ID_RESERVATION")
    @GeneratedValue(generator = "sq_reservation_id", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "sq_reservation_id", sequenceName = "sq_reservation_id", allocationSize = 1)
    private long id;

    @JoinColumn(name = "id_visitor")
    @ManyToOne(fetch = FetchType.LAZY)
    private Visitor visitor;

    @Column(name = "BOOKING_ID")
    String bookingId;

    @Column(name = "ARRIVAL_DATE")
    private LocalDate arrivalDate;

    @Column(name = "DEPARTURE_DATE")
    private LocalDate departureDate;
}
