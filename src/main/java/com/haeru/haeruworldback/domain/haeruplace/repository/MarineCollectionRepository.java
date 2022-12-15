package com.haeru.haeruworldback.domain.haeruplace.repository;

import com.haeru.haeruworldback.domain.haeruplace.entity.MarineCollection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarineCollectionRepository extends JpaRepository<MarineCollection, Long> {
    MarineCollection findByName(String name);
}
