/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.gateway.controller;

import com.elcom.gateway.model.dto.IpInformation;
import com.elcom.gateway.repository.IpInformationRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.Valid;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Admin
 */
@RestController
public class ManagerIpController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    IpInformationRepository ipInformationRepository;

    /**
     * URL check (chú ý có giới hạn số lần kiểm tra trong 1 ngày)
     */
    private static final String BASE_URL = "http://whoer.net/en/checkwhois?host=";

    public ManagerIpController() {
    }

    @RequestMapping(value = "/redis-test", method = RequestMethod.GET)
    public ResponseEntity<String> redisTest() {

        String key = "my-key";
        String value = "my-value";
        redisTemplate.opsForValue().set(key, value);
        redisTemplate.opsForValue().set(key + "-2", null);
        redisTemplate.opsForValue().set(key + "-3", true);
        redisTemplate.opsForValue().set(key + "-4", 123456);

        System.out.println("Value of key [" + key + "]: " + redisTemplate.opsForValue().get(key));
        System.out.println("Value of key [" + key + "-1] (not set): " + redisTemplate.opsForValue().get(key + "-1"));
        System.out.println("Value of key [" + key + "-2] (set null): " + redisTemplate.opsForValue().get(key + "-2"));
        System.out.println("Value of key [" + key + "-3]: " + redisTemplate.opsForValue().get(key + "-3"));
        System.out.println("Value of key [" + key + "-4]: " + redisTemplate.opsForValue().get(key + "-4"));

        String keyLst = "my-key-lst";
        List<String> lst = new ArrayList<>();
        lst.add("A");
        lst.add("B");
        lst.add("C");
        redisTemplate.opsForList().rightPushAll(keyLst, lst);

        Long sizeLst = redisTemplate.opsForList().size(keyLst);
        System.out.println("Key [" + keyLst + "] contains : " + sizeLst + " element");
        if (sizeLst != null && !sizeLst.equals(0L)) {
            List<String> lstStr = (List<String>) redisTemplate.opsForList().range(keyLst, 0, -1);
            if (lstStr != null && !lstStr.isEmpty()) {
                for (String str : lstStr) {
                    System.out.println("item:" + str);
                }
                redisTemplate.delete(keyLst);
            }
        }

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @RequestMapping(value = "/redis-tst", method = RequestMethod.GET)
    public ResponseEntity<String> redisTest2() {

        // add Ip
        // String ipaddress = "123.25.30.136";
        String ipaddress = "94.228.222.47";
        try {
            synchronized (this) {
                if(checkIP(ipaddress) == true){
                     return new ResponseEntity<>("badrequest", HttpStatus.OK);
                } 
                
                
                String region = "Ha Noi";
                String city = "Ha Noi";
                String country = "Vietnam";
                String zipcode = "100000";
                IpInformation ipInformation = new IpInformation(ipaddress, region, city, country, zipcode);

                String keyLst = "listIP";
                List<IpInformation> lst = new ArrayList<>();
                lst.add(ipInformation);
                redisTemplate.opsForList().rightPushAll(keyLst, lst);
                Long sizeLst = redisTemplate.opsForList().size(keyLst);
                System.out.println("Key [" + keyLst + "] contains : " + sizeLst + " element");
                if (sizeLst != null && !sizeLst.equals(0L)) {
                    List<IpInformation> lstStr = redisTemplate.opsForList().range(keyLst, 0, -1);
                    for (int i = 0; i < sizeLst; i++) {
                        System.out.println("======== item " + i + "===============");
                        System.out.println("id : " + lstStr.get(i).getId());
                        System.out.println("ipaddress : " + lstStr.get(i).getIpaddress());
                        System.out.println("region : " + lstStr.get(i).getRegion());
                        System.out.println("city : " + lstStr.get(i).getCity());
                        System.out.println("country : " + lstStr.get(i).getCountry());
                        System.out.println("regizipcodeon : " + lstStr.get(i).getZipcode());
                    }
                    redisTemplate.delete(keyLst);

                }

            }
        } catch (IOException ex) {
            Logger.getLogger(ManagerIpController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    private Boolean checkIP(String ipaddress) throws IOException {
        Boolean ipBlackList = false;

        // Lấy document từ URL
        Document doc = Jsoup.connect(BASE_URL.concat(ipaddress)).get();

        // Lấy tất cả thẻ <a>
        Elements as = doc.select("a");
        for (Element element : as) {
            String linkHref = element.attr("href");
            if (linkHref.contains("spamhaus")) {
                ipBlackList = ("Yes".equalsIgnoreCase(element.text()));
                break;
            }
        }
        System.out.println("ip is black list : " + ipBlackList);
        return ipBlackList;
    }

}
