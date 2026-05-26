package com.nitnem.tracker.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSession {

    private UserState state;

    private NitnemUnit nitnemUnit;

    private String nitnemName;

    private Integer targetCount;

    private Integer durationDays;

    private Integer completedCount;

    public boolean isNew() {
        return this.state == UserState.IDLE;
    }
}