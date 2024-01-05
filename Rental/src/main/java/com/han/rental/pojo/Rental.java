package com.han.rental.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "rental")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "rental_uid", nullable = false, unique = true)
    private UUID rentalUid;

    @Column(nullable = false, length = 80)
    private String username;

    @Column(name = "payment_uid", nullable = false)
    private UUID paymentUid;


    /*@ManyToOne
    @JoinColumn(name = "car_uid", nullable = false)*/
    @Column(name = "car_uid", nullable = false)
    private UUID carUid;

    @Column(name = "date_from")
    private LocalDate dateFrom;

    @Column(name = "date_to")
    private LocalDate dateTo;

    @Column(columnDefinition = "VARCHAR(20) CHECK (status IN ('IN_PROGRESS', 'FINISHED', 'CANCELED'))", nullable = false)
    private String status;
}
