package com.nitnem.tracker.model;

import com.nitnem.tracker.entity.Nitnem;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSession {

    private UserState state;

    private Nitnem nitnem;

    private NitnemUnit nitnemUnit;

    private Long nitnemId;

    private String nitnemName;

    private Integer targetCount;

    private Integer durationDays;

    private Integer completedCount;

    private String lastProcessedMessage;

    private LocalDateTime lastProcessedAt;

    public boolean isNew() {
        return this.state == UserState.IDLE;
    }
}