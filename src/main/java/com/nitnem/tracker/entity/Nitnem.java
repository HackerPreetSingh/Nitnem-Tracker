package com.nitnem.tracker.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "nitnem")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Nitnem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            nullable = false
    )
    private String name;

    @Column(
            nullable = false
    )
    private Integer targetCount;

    @Column(
            nullable = false
    )
    private Integer durationDays;

    @Column(
            nullable = false
    )
    private LocalDate startDate;

    @Column(
            nullable = false
    )
    private Boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;
}