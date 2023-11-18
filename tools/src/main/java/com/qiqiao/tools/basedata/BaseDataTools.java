package com.qiqiao.tools.basedata;
import com.qiqiao.model.basedata.domain.Disease;
import com.qiqiao.model.basedata.domain.DiseaseDepartment;
import net.sourceforge.pinyin4j.PinyinHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Simon
 */
public class BaseDataTools {

    public static Disease initDisease(){
        Disease disease = new Disease();
        List<DiseaseDepartment> diseaseDepartmentList = new ArrayList<>();
        disease.setDepartments(diseaseDepartmentList);
        String directoryPath = "C:\\Users\\Simon\\Desktop\\qq-doctor-project\\data\\disease";
        File directory = new File(directoryPath);
        File[] fileList = directory.listFiles();
        for (File file : fileList) {
            diseaseDepartmentList.add(getFileDepartment(file));
        }
        disease.setDepartments(diseaseDepartmentList);
        return disease;
    }

    public static String getFirstLetter(String word) {
        if (word == null || word.isEmpty()) {
            return "";
        }
        String[] pinyinArray;
        // 获取拼音数组（可能有多音字）
        pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word.charAt(0));
        // 获取首字母（取第一个拼音的第一个字母）
        if (pinyinArray != null && pinyinArray.length > 0) {
            String pinyin = pinyinArray[0];
            char firstLetter = pinyin.charAt(0);
            if (Character.isUpperCase(firstLetter)) {
                return String.valueOf(firstLetter);
            } else {
                return String.valueOf(Character.toUpperCase(firstLetter));
            }
        }
        return "#";
    }

    public static DiseaseDepartment getFileDepartment(File file) {
        DiseaseDepartment department = new DiseaseDepartment();
        department.setDepartName(file.getName().substring(0, file.getName().length() - 4));
        department.setInnerDepart(new HashMap<>());
        Map<String, Map<String, List<String>>> innerDepart = new HashMap<>();
        Map<String, List<String>> indexMap = new HashMap<>();
        List<String> keyList = new ArrayList<>();
        List<Map<String, List<String>>> valueList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            if (line == null) {
                return department;
            }
            if (line.charAt(line.length() - 1) != '：') {
                while ((line = br.readLine()) != null){
                    String firstLetter = getFirstLetter(line);
                    if (indexMap.containsKey(firstLetter)) {
                        List<String> strings = indexMap.get(firstLetter);
                        strings.add(line);
                    } else {
                        List<String> list = new ArrayList<>();
                        list.add(line);
                        indexMap.put(firstLetter, list);
                    }
                }
                Map<String, List<String>> sortedMap = indexMap.entrySet().stream()
                        .sorted(Map.Entry.comparingByKey())
                        .collect(
                                Collectors.toMap(
                                        Map.Entry::getKey,
                                        Map.Entry::getValue,
                                        (oldVal, newVal) -> oldVal,
                                        LinkedHashMap::new
                                )
                        );
                innerDepart.put("",sortedMap);
                department.setInnerDepart(innerDepart);
            } else {
                while ((line = br.readLine()) != null) {
                    if (line.charAt(line.length() - 1) == '：') {
                        keyList.add(line.substring(0, line.length() - 1));
                        if (!indexMap.isEmpty()) {
                            valueList.add(indexMap);
                            indexMap = new HashMap<>();
                        }
                        continue;
                    }
                    String firstLetter = getFirstLetter(line);
                    if (indexMap.containsKey(firstLetter)) {
                        List<String> strings = indexMap.get(firstLetter);
                        strings.add(line);
                    } else {
                        List<String> list = new ArrayList<>();
                        list.add(line);
                        indexMap.put(firstLetter, list);
                    }
                }
                valueList.add(indexMap);
                for (int i = 0; i < keyList.size(); i++) {
                    Map<String, List<String>> value = valueList.get(i);
                    if (value.size() != 0){
                        Map<String, List<String>> sortedMap = value.entrySet().stream()
                                .sorted(Map.Entry.comparingByKey())
                                .collect(
                                        Collectors.toMap(
                                                Map.Entry::getKey,
                                                Map.Entry::getValue,
                                                (oldVal, newVal) -> oldVal,
                                                LinkedHashMap::new
                                        )
                                );
                        innerDepart.put(keyList.get(i), sortedMap);
                    }
                    innerDepart.put(keyList.get(i),value);
                }
                department.setInnerDepart(innerDepart);
            }
            return department;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return department;
    }
}
