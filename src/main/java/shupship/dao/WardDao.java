package shupship.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import shupship.common.Constants;
import shupship.domain.dto.AddressMapDto;
import shupship.domain.dto.MapDto;
import shupship.domain.dto.WardDto;
import shupship.domain.model.Ward;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class WardDao {
    private final Logger logger = LoggerFactory.getLogger(WardDao.class);

    @Autowired
    RestTemplate restTemplate;

    public List<WardDto> getListWard() {
        List<WardDto> list = new ArrayList<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String objects = restTemplate.getForObject(Constants.API_WARD, String.class);
            MapDto res = objectMapper.readValue(objects, MapDto.class);
            List<AddressMapDto> list1 = objectMapper.convertValue(res.getData(), new TypeReference<ArrayList<AddressMapDto>>() {
            });
            if (CollectionUtils.isNotEmpty(list1)) {
                for (AddressMapDto data : list1) {
                    WardDto model = new WardDto();
                    model.setWardId(data.getId());
                    model.setWardCode(data.getCode());
                    model.setWardName(data.getComponents().get(0).getName());
                    model.setFormattedAddress(data.getFormattedAddress());
                    if (data.getGeometry() != null){
                        model.setLat(data.getGeometry().getLocation().getLat());
                        model.setLng(data.getGeometry().getLocation().getLng());
                    }
                    if (data.getComponents().size() >= 3){
                        model.setProvinceCode(data.getComponents().get(2).getCode());
                        model.setDistrictCode(data.getComponents().get(1).getCode());
                    }
                    list.add(model);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Query fail getListWard: " + e.getMessage());
        }
        return list;
    }

    public Map<String, WardDto> getDistrictMap(Iterable<Ward> wards) {
        Map<String, WardDto> list = new HashMap<>();
        try {
            for (Ward model : wards) {
                WardDto wardDto = new WardDto();
                wardDto.setProvinceCode(model.getProvinceCode());
                wardDto.setDistrictCode(model.getDistrictCode());
                wardDto.setWardName(model.getWardName());
                wardDto.setWardCode(model.getWardCode());
                wardDto.setWardId(model.getWardId());
                wardDto.setLat(model.getLat());
                wardDto.setLng(model.getLng());
                wardDto.setFormattedAddress(model.getFormattedAddress());
                list.put(model.getWardId(), wardDto);
            }
        } catch (Exception e) {
            logger.info("Map District fail: " + e.getMessage());
        }
        return list;
    }

    public Map<String, Ward> getMapDistrict(Iterable<Ward> wards) {
        try {
            Map<String, Ward> liWardtMap = new HashMap<>();
            for (Ward model : wards) {
                if (StringUtils.isNotEmpty(model.getWardCode())) {
                    liWardtMap.put(model.getWardCode(), model);
                }
            }
            return liWardtMap;
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return null;
    }
}
