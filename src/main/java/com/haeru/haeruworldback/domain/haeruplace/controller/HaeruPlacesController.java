package com.haeru.haeruworldback.domain.haeruplace.controller;

import com.haeru.haeruworldback.domain.haeruplace.dto.HaeruPlaceDetail;
import com.haeru.haeruworldback.domain.haeruplace.dto.HaeruPlacesResponse;
import com.haeru.haeruworldback.domain.haeruplace.service.HaeruPlaceDetailService;
import com.haeru.haeruworldback.domain.haeruplace.service.HaeruPlaceService;
import com.haeru.haeruworldback.global.model.BaseResponseBody;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = { "https://baru-haeru.vercel.app/*" }, maxAge = 6000)
@RestController
@Slf4j
@RequestMapping("/haeruPlaces")
@RequiredArgsConstructor
public class HaeruPlacesController {

    private final HaeruPlaceService haeruPlaceService;
    private final HaeruPlaceDetailService haeruPlaceDetailService;

    @GetMapping
    @ApiOperation(value = "해루질 장소 리스트 조회", notes = "선택한 어종, 지역을 기준으로 해루질 장소 리스트를 조회합니다.")
    public HttpEntity<BaseResponseBody> getHaeruPlacesList(@RequestParam String marineCollections, @RequestParam String area) {
        HaeruPlacesResponse response = haeruPlaceService.getHaeruPlacesList(marineCollections, area);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "success", response));
    }

    @GetMapping("/{haeruPlaceId}")
    @ApiOperation(value = "해루질 장소 상세 조회", notes = "선택한 해루질 장소 상세정보를 조회합니다.")
    public HttpEntity<BaseResponseBody> getHaeruPlacesDetail(@PathVariable Long haeruPlaceId) {
        HaeruPlaceDetail response = haeruPlaceDetailService.getHaeruPlacesDetail(haeruPlaceId);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "success", response));
    }



}
