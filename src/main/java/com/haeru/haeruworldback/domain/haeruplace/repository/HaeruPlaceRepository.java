package com.haeru.haeruworldback.domain.haeruplace.repository;

import com.haeru.haeruworldback.domain.haeruplace.entity.Area;
import com.haeru.haeruworldback.domain.haeruplace.entity.HaeruPlace;
import com.haeru.haeruworldback.domain.haeruplace.entity.MarineCollection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HaeruPlaceRepository extends JpaRepository<HaeruPlace, Long> {

    List<HaeruPlace> findByArea(Area area);
}
