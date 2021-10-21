package shupship.util;

import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import shupship.domain.dto.AddressMapDto;
import shupship.domain.model.Users;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class CommonUtils {
    public static Users getCurrentUser() {
        Users user = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user == null) {
          ///  throw new ApplicationException(Applica);
        }
        return user;
    }

    public static String formatUSDouble(double num) {
        NumberFormat nf = NumberFormat.getInstance(new Locale(Locale.US.getLanguage(), Locale.US.getCountry()));
        return nf.format(num);
    }

    public static String maskPhone(String phoneNumber) {
        return maskString(phoneNumber, 3, 7, '*');
    }

    public static void main(String[] args) throws Exception {
        System.out.println(maskString("03682753879", 3, 7, '*'));
    }

    private static String maskString(String strText, int start, int end, char maskChar) {

        if (strText == null || strText.equals(""))
            return "";

        if (start < 0)
            start = 0;

        if (end > strText.length())
            end = strText.length();

//        if (start > end)
//            throw new Exception("End index cannot be greater than start index");

        int maskLength = end - start;

        if (maskLength == 0)
            return strText;

        String strMaskString = StringUtils.repeat(maskChar, maskLength);

        return StringUtils.overlay(strText, strMaskString, start, end);
    }

    private static final Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return true;
        }
        return !pattern.matcher(strNum).matches();
    }

    public static String convertPhone(String phone) {
        String converted = "";
        if (StringUtils.isNotEmpty(phone)) {
            if (StringUtils.startsWith(phone, "0"))
                converted = "84" + phone.substring(1);
            if (StringUtils.startsWith(phone, "84"))
                converted = phone;
            if (StringUtils.startsWith(phone, "+84"))
                converted = "84" + phone.substring(3);
            if (StringUtils.startsWith(phone, "9") || StringUtils.startsWith(phone, "5") || StringUtils.startsWith(phone, "3") || StringUtils.startsWith(phone, "7"))
                converted = "84" + phone;
        }
        return converted;
    }

    public static boolean isValidPhone(String phone) {
        if (StringUtils.isEmpty(phone))
            return false;

        boolean isValid = true;

        if (phone.startsWith("+84"))
            isValid = phone.length() == 12 || phone.length() == 13;

        if (!isNumeric(phone)) {
            if (phone.startsWith("84"))
                isValid = phone.length() == 11 || phone.length() == 12;
            else if (phone.startsWith("0"))
                isValid = phone.length() == 10 || phone.length() == 11;
            else if (phone.length() < 9)
                isValid = false;
            else if (phone.length() > 11)
                isValid = false;
            return isValid;

        } else return false;
    }

    public static AddressMapDto[] validAddress(String address) {

        final String uri = "https://location.okd.viettelpost.vn/location/v1.0/addresses?shortForm=true&system=VTP";
        RestTemplate restTemplate = new RestTemplate();

        MappingJackson2HttpMessageConverter jsonHttpMessageConverter = new MappingJackson2HttpMessageConverter();
        jsonHttpMessageConverter.getObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        restTemplate.getMessageConverters().add(jsonHttpMessageConverter);

        ArrayList<String> arrays = new ArrayList<>();
        arrays.add(address);
        return restTemplate.postForObject(uri, arrays, AddressMapDto[].class);
    }

    public static String generateUri(String baseUrl, String queryParam, String value) {
        if (StringUtils.isNotBlank(value)) {
            return UriComponentsBuilder.fromUriString(baseUrl).queryParam(queryParam, value).toUriString();
        }
        return baseUrl;
    }

    public static String generateUri(String baseUrl, List<NameValuePair> nameValuePairs) {
        for (NameValuePair nameValuePair : nameValuePairs) {
            baseUrl = generateUri(baseUrl, nameValuePair.getName(), nameValuePair.getValue());
        }
        return baseUrl;
    }

}
