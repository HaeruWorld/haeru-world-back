package com.haeru.haeruworldback.domain.haeruplace.service;

import com.haeru.haeruworldback.domain.Area;
import com.haeru.haeruworldback.domain.haeruplace.dto.HaeruPlaceDetailDto;
import com.haeru.haeruworldback.domain.haeruplace.dto.MarineCollectionDto;
import com.haeru.haeruworldback.domain.haeruplace.entity.HaeruPlace;
import com.haeru.haeruworldback.domain.haeruplace.exception.HaeruPlaceNotFoundException;
import com.haeru.haeruworldback.domain.haeruplace.repository.HaeruPlaceRepository;
import com.haeru.haeruworldback.domain.haeruplace.repository.MarineCollectionRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HaeruPlaceDetailService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final HaeruPlaceRepository haeruPlaceRepository;
    private final MarineCollectionRepository marineCollectionRepository;

    @Value("${khoaServiceKey}")
    private String khoaServiceKey;

    // 해루질 장소 상세 조회
    public HaeruPlaceDetailDto getHaeruPlacesDetail(Long haeruPlaceId) {
        HaeruPlace findHaeruPlace = haeruPlaceRepository.findById(haeruPlaceId).orElseThrow(HaeruPlaceNotFoundException::new);

        List<MarineCollectionDto> marineCollectionDtos = Arrays.stream(findHaeruPlace.getMarineCollections().split(","))
                .map(name -> marineCollectionRepository.findByName(name).toDto())
                .collect(Collectors.toList());
        List<String> canHaeruTime = canHaeruTimes(findHaeruPlace.getArea().toString());

        return findHaeruPlace.haeruPlaceDetailDto(marineCollectionDtos, canHaeruTime);
    }

    // 해루질 가능 시간
    public List<String> canHaeruTimes(String area) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lawStartTime = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 9, 0, 0);
        LocalDateTime lawEndTime = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 17, 0, 0);

        // 해당 지역의 저조 시간
        LocalDateTime lowTideTime = getLowTideTime(lawStartTime, lawEndTime, areaLowTideTimes(area, now));

        // 법정 시간 비교
        LocalDateTime startTime = lowTideTime.minusHours(1).isBefore(lawStartTime) ? lawStartTime : lowTideTime.minusHours(1);
        LocalDateTime endTime = lowTideTime.plusHours(1).isAfter(lawEndTime) ? lawEndTime : lowTideTime.plusHours(1);

        return List.of(mappingTimeFormat(startTime), mappingTimeFormat(endTime));
    }

    private String mappingTimeFormat(LocalDateTime time) {
        return time.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    private LocalDateTime getLowTideTime(LocalDateTime lawStartTime, LocalDateTime lawEndTime, List<String> tphTimes) {
        return tphTimes.stream()
                .map(time -> LocalDateTime.parse(time, DATE_TIME_FORMATTER))
                .filter(time -> isBetweenLawTime(lawStartTime, lawEndTime, time))
                .findFirst()
                .orElseGet(null);
    }

    private boolean isBetweenLawTime(LocalDateTime lawStartTime, LocalDateTime lawEndTime, LocalDateTime dateTime) {
        return (dateTime.minusHours(1).isAfter(lawStartTime) && dateTime.minusHours(1).isBefore(lawEndTime))
                || (dateTime.plusHours(1).isAfter(lawStartTime) && dateTime.plusHours(1).isBefore(lawEndTime));
    }

    // 해당지역 저조 시간 구하기
    private List<String> areaLowTideTimes(String area, LocalDateTime now) {
        String areaCode = Area.valueOf(area).getAreaCode();
        String nowDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        String reqURL = "https://www.khoa.go.kr/api/oceangrid/tideObsPreTab/search.do?ServiceKey=" + khoaServiceKey + "&ObsCode="
                + areaCode + "&Date=" + nowDate + "&ResultType=json";
        String result = "";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("charset", "UTF-8");

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                result += line;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> lowTideTimes = new ArrayList<>();
        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObj = (JSONObject) parser.parse(result);
            JSONObject objResult = (JSONObject) jsonObj.get("result");
            JSONArray objData = (JSONArray) objResult.get("data");

            for (Object data : objData) {
                JSONObject object = (JSONObject) data;
                String hl_code = (String) object.get("hl_code");
                if (hl_code.equals("저조")) {
                    String time = (String) object.get("tph_time");
                    lowTideTimes.add(time);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return lowTideTimes;
    }

}
