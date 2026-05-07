package com.nitnem.tracker.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "nitnem_entry")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NitnemEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //date when nitnem is being logged
    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private Integer completedCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "nitnem_id",
            nullable = false
    )
    private Nitnem nitnem;

    //date when nitnem is done
    @Column(nullable = false)
    private LocalDate doneAt;
}
