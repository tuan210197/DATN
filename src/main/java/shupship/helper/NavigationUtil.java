package shupship.helper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Iterator;

public class NavigationUtil {
    private NavigationUtil() {
        // Private constructor to hide the implicit public one
    }


    public static NavigationInfo generateNavigationInfo(Pageable pageable, Page<?> page, String baseUrl) {

        NavigationInfo navigationInfo = new NavigationInfo();

        try {
            // Get sort information from Pageable
            String sortParams = sortParams(pageable.getSort());

            // Actual page's information
            int pageNumber = page.getNumber();
            int pageSize = page.getSize();
            int totalPage = page.getTotalPages();

            // next link
            if ((pageNumber + 2) <= totalPage) {
                navigationInfo.setNext(generateUri(baseUrl, pageNumber + 2, pageSize, sortParams));
            }

            // prev link
            if (pageNumber + 1 > 1) {
                navigationInfo.setPrevious(generateUri(baseUrl, pageNumber, pageSize, sortParams));
            }

            // last and first link
            int lastPage = 1;
            if (totalPage > 0) {
                lastPage = totalPage;
            }

            navigationInfo.setLast(generateUri(baseUrl, lastPage, pageSize, sortParams));
            navigationInfo.setFirst(generateUri(baseUrl, 1, pageSize, sortParams));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return navigationInfo;
    }

    private static String generateUri(String baseUrl, int page, int size, String sort) {
        return UriComponentsBuilder.fromUriString(baseUrl).queryParam("page", page).queryParam("size", size).toUriString() + sort;
    }

    private static String sortParams(Sort sort) {
        StringBuilder sortParamStr = new StringBuilder();
        if (sort != null) {
            Iterator<Sort.Order> it = sort.iterator();
            while (it.hasNext()) {
                Sort.Order value = it.next();
                sortParamStr.append("&sort=")
                        .append(value.getProperty())
                        .append(",")
                        .append(value.getDirection().toString().toLowerCase());
            }
        }
        return sortParamStr.toString();
    }
}
