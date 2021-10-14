package shupship.cronjob;


import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import shupship.dao.WardDao;
import shupship.domain.dto.WardDto;
import shupship.domain.model.Ward;
import shupship.repo.IWardRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class JobWard {
    private final Logger logger = LoggerFactory.getLogger(JobWard.class);

    @Autowired
    WardDao wardDao;

    @Autowired
    IWardRepository wardRepository;

//    @Scheduled(cron = "30 20 17 17 8 ?")
    public void syncWard() {
        try {
            logger.info("Start sync Ward");
            List<WardDto> listWard = wardDao.getListWard();
            Iterable<Ward> list = wardRepository.findAll();

            Map<String, WardDto> listWardDtoMap = wardDao.getDistrictMap(list);
            Map<String, Ward> listWardMap = wardDao.getMapDistrict(list);

            List<Ward> listToUpdate = new ArrayList<>();
            List<Ward> listToSave = new ArrayList<>();

            for (WardDto model : listWard) {
                WardDto wardDto = listWardDtoMap.get(model.getWardId());
                boolean check = model.equals(wardDto);
                if (!check) {
                    Ward ward = listWardMap.get(model.getWardCode());
                    if (ward != null) {
                        ward.setWardId(model.getWardId());
                        ward.setWardCode(model.getWardCode());
                        ward.setWardName(model.getWardName());
                        ward.setFormattedAddress(model.getFormattedAddress());
                        ward.setProvinceCode(model.getProvinceCode());
                        ward.setDistrictCode(model.getDistrictCode());
                        ward.setLat(model.getLat());
                        ward.setLng(model.getLng());
                        listToUpdate.add(ward);
                        logger.info("Update" + model.getWardName());
                    } else {
                        Ward ward1 = new Ward();
                        ward1.setWardId(model.getWardId());
                        ward1.setWardCode(model.getWardCode());
                        ward1.setWardName(model.getWardName());
                        ward1.setFormattedAddress(model.getFormattedAddress());
                        ward1.setProvinceCode(model.getProvinceCode());
                        ward1.setDistrictCode(model.getDistrictCode());
                        ward1.setLat(model.getLat());
                        ward1.setLng(model.getLng());
                        ward1.setCreatedBy(-1L);
                        logger.info("Add" + model.getWardCode());
                        listToSave.add(ward1);
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(listToUpdate)) {
                wardRepository.saveAll(listToUpdate);
                logger.info("Update done");
            }
            if (CollectionUtils.isNotEmpty(listToSave)) {
                wardRepository.saveAll(listToSave);
                logger.info("Add done");
            }
            logger.info("Done sync Ward");
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Sync Povince fail: " + e.getMessage());
        }
    }
}
