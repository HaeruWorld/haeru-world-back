package com.haeru.haeruworldback.domain.haeruplace.repository;

import com.haeru.haeruworldback.domain.Area;
import com.haeru.haeruworldback.domain.haeruplace.entity.HaeruPlace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HaeruPlaceRepository extends JpaRepository<HaeruPlace, Long> {

    List<HaeruPlace> findByArea(Area area);
}
