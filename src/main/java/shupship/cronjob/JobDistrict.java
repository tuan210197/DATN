package shupship.cronjob;


import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import shupship.dao.DistrictDao;
import shupship.domain.dto.DistrictDto;
import shupship.domain.model.District;
import shupship.repo.IDistrictRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class JobDistrict {
    private final Logger logger = LoggerFactory.getLogger(JobDistrict.class);

    @Autowired
    DistrictDao districtDao;

    @Autowired
    IDistrictRepository districtRepository;

    //    @Scheduled(cron = "0 1 * ? * *")
//    @Scheduled(cron = "0 16 18 17 8 ?")
    public void syncDistrict(){
        try {
            logger.info("Start sync District");

            List<DistrictDto> listDis = districtDao.getListDistrict();
            Iterable<District> list = districtRepository.findAll();

            Map<Long, DistrictDto> lisDistrictDtoMap = districtDao.getDistrictMap(list);
            Map<String, District> districtMap = districtDao.getMapDistrict(list);

            List<District> listToUpdate = new ArrayList<>();
            List<District> listToSave = new ArrayList<>();

            for (DistrictDto model : listDis){
                DistrictDto districtDto = lisDistrictDtoMap.get(model.getDistrictId());
                boolean check = model.equals(districtDto);

                if (!check){
                   District district = districtMap.get(model.getDistrictCode());
                   if (district != null){
                       district.setDistrictId(model.getDistrictId());
                       district.setDistrictCode(model.getDistrictCode());
                       district.setDistrictName(model.getDistrictName());
                       district.setFormattedAddress(model.getFormattedAddress());
                       district.setLat(model.getLat());
                       district.setLng(model.getLng());
                       district.setProvinceCode(model.getProvinceCode());
                       listToUpdate.add(district);
                       logger.info("Update " + model.getDistrictName());
                   }else {
                       District district1 = new District();
                       district1.setDistrictId(model.getDistrictId());
                       district1.setFormattedAddress(model.getFormattedAddress());
                       district1.setDistrictName(model.getDistrictName());
                       district1.setDistrictCode(model.getDistrictCode());
                       district1.setLat(model.getLat());
                       district1.setLng(model.getLng());
                       district1.setProvinceCode(model.getProvinceCode());
                       district1.setCreatedBy(-1L);
                       listToSave.add(district1);
                       logger.info("Add " + model.getDistrictCode());
                   }
                }
            }
            if (CollectionUtils.isNotEmpty(listToUpdate)) {
                districtRepository.saveAll(listToUpdate);
                logger.info("Update done");
            }
            if (CollectionUtils.isNotEmpty(listToSave)) {
                districtRepository.saveAll(listToSave);
                logger.info("Add done");
            }
            logger.info("Done sync District");
        } catch (Exception e) {
            logger.info("Sync District fail: " + e.getMessage());
        }
    }
}
