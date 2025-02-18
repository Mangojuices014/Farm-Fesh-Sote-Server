package com.kira.farm_fresh_store.utils;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class Util {
    public String createNewID(String chars) {
        return chars + String.format("%03d", 1);
    }

    public String createIDFromLastID(String chars, Integer index, String lastID) {
        Integer IDNumber = Integer.parseInt(lastID.substring(index));
        IDNumber++;
        String newID = chars + String.format("%03d", IDNumber);
        return newID;
    }
    public  String generateRandomID() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        // Tạo 15 số ngẫu nhiên
        for (int i = 0; i < 15; i++) {
            sb.append(random.nextInt(10)); // Số từ 0-9
        }

        return sb.toString();
    }
}
