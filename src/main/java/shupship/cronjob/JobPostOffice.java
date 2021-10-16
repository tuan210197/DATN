//package shupship.cronjob;
//
//import org.apache.commons.collections4.CollectionUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import shupship.dao.PostOfficeDao;
//import shupship.repo.IPostOfficeRepository;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//@Component
//public class JobPostOffice {
//
//    private final Logger logger = LoggerFactory.getLogger(JobPostOffice.class);
//
//    @Autowired
//    PostOfficeDao postOfficeDao;
//
//    @Autowired
//    IPostOfficeRepository postOfficeRepository;
//
//    public void syncPostOffice(){
//        try{
//            logger.info("Start sync POstOffice");
//            List<PostOfficeDto> officeDtoList = postOfficeDao.getListPostOffice();
//            Iterable<PostOffice> lisPostOffices = postOfficeRepository.findAll();
//
//            Map<String, PostOfficeDto> postOfficeDtoMap = postOfficeDao.getMapPostOfficeDto(lisPostOffices);
//            Map<String, PostOffice> postOfficeMap = postOfficeDao.getMapPostoffice(lisPostOffices);
//
//            List<PostOffice> listToUpdate = new ArrayList<>();
//            List<PostOffice> listToSave = new ArrayList<>();
//
//            for (PostOfficeDto model : officeDtoList){
//                PostOfficeDto postOfficeDto = postOfficeDtoMap.get(model.getPostCode());
//                boolean check = model.equals(postOfficeDto);
//
//                if(!check){
//                    PostOffice postOffice = postOfficeMap.get(model.getPostCode());
//                    if (postOffice != null){
//                        postOffice.setPostCode(model.getPostCode());
//                        postOffice.setPostName(model.getPostName());
//                        postOffice.setPostPhone(model.getPostPhone());
//                        postOffice.setLat(model.getLat());
//                        postOffice.setLng(model.getLng());
//                        postOffice.setProvinceCode(model.getProvinceCode());
//                        postOffice.setDistrictCode(model.getDistrictCode());
//                        postOffice.setWardCode(model.getWardCode());
//                        logger.info("Update " + model.getPostCode());
//                        listToUpdate.add(postOffice);
//                    } else {
//                        PostOffice postOffice1 = new PostOffice();
//                        postOffice1.setPostCode(model.getPostCode());
//                        postOffice1.setPostName(model.getPostName());
//                        postOffice1.setPostPhone(model.getPostPhone());
//                        postOffice1.setLat(model.getLat());
//                        postOffice1.setLng(model.getLng());
//                        postOffice1.setProvinceCode(model.getProvinceCode());
//                        postOffice1.setDistrictCode(model.getDistrictCode());
//                        postOffice1.setWardCode(model.getWardCode());
//                        postOffice1.setCreatedBy(-1L);
//                        logger.info("Add " + model.getPostCode());
//                        listToSave.add(postOffice1);
//                    }
//                }
//            }
//            if (CollectionUtils.isNotEmpty(listToUpdate)) {
//                postOfficeRepository.saveAll(listToUpdate);
//                logger.info("Update done");
//            }
//            if (CollectionUtils.isNotEmpty(listToSave)) {
//                postOfficeRepository.saveAll(listToSave);
//                logger.info("Add done");
//            }
//            logger.info("Done sync PostOffice");
//
//        } catch (Exception e){
//            logger.info("Sync PostOffice fail: " + e.getMessage());
//        }
//
//    }
//
//}
