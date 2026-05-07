package com.nitnem.tracker.repository;

import com.nitnem.tracker.entity.NitnemEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface NitnemEntryRepository extends JpaRepository<NitnemEntry, Long> {
    List<NitnemEntry> findByName(String nitnemName);

    @Query("SELECT COALESCE(SUM(ne.completedCount), 0) FROM NitnemEntry ne WHERE ne.nitnem.id = :nitnemId AND ne.date = :date")
    Integer getTodayCompletedCount(Long nitnemId, LocalDate date);
}
