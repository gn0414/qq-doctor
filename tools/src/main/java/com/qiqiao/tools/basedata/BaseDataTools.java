package com.qiqiao.tools.basedata;
import com.qiqiao.model.basedata.domain.FirstLevelData;
import com.qiqiao.model.basedata.domain.SecondLevelData;
import lombok.Cleanup;
import lombok.SneakyThrows;
import net.sourceforge.pinyin4j.PinyinHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Simon
 */
public class BaseDataTools {

    //SecondLevelData

    public static FirstLevelData initLevelData(String url){
        FirstLevelData firstLevelData = new FirstLevelData();
        List<SecondLevelData> secondLevelDataList = new ArrayList<>();
        firstLevelData.setSecondLevelDataList(secondLevelDataList);
        File directory = new File(url);
        File[] fileList = directory.listFiles();
        for (File file : Objects.requireNonNull(fileList)) {
            secondLevelDataList.add(getSecondLevel(file));
        }
        return firstLevelData;
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

    @SneakyThrows
    public static SecondLevelData getSecondLevel(File file){
        //获得子级层级数据对象
        SecondLevelData secondLevelData = new SecondLevelData();
        //将文件名设置为层级名
        secondLevelData.setName((file.getName().substring(0, file.getName().length() - 4)));
        //定义IO流
        @Cleanup BufferedReader br = new BufferedReader(new FileReader(file));
        //设置获取的内部内容
        if (br.readLine() != null) {
            secondLevelData.setSecondLevel(doGetSecondLevel(br));
        }
        //返回结果
        return secondLevelData;

    }

    @SneakyThrows
    public static Map<String, Map<String, List<String>>> doGetSecondLevel(BufferedReader br){
        //定义子层级Map
        Map<String,Map<String,List<String>>> innerSecondLevel;
        //定义索引Map
        Map<String,List<String>> innerLevelData;
        //判断是否有内层级
        if(br.readLine().charAt(br.readLine().length() - 1) != '：'){
            innerLevelData = getInnerLevelData(br);
            innerSecondLevel = Collections.singletonMap("",innerLevelData);
        }else {
            innerSecondLevel = getInnerLevelData(br,true);
        }
        return innerSecondLevel;
    }
    @SneakyThrows
    public static Map<String,List<String>> getInnerLevelData( BufferedReader br){
        //索引值Map
        Map<String,List<String>> innerLevelData = new HashMap<>(26);
        //读取行内容
        String line;
        try {
            while ((line = br.readLine()) != null){
                String firstLetter = getFirstLetter(line);
                //判断是否存在索引的value然后做处理
                if (innerLevelData.containsKey(firstLetter)){
                    List<String> valueList = innerLevelData.get(firstLetter);
                    valueList.add(line);
                }else{
                    List<String> valueList = new ArrayList<>(20);
                    valueList.add(line);
                    innerLevelData.put(firstLetter,valueList);
                }
            }
            innerLevelData = innerLevelData.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .collect(
                            Collectors.toMap(
                                    Map.Entry::getKey,
                                    Map.Entry::getValue,
                                    (oldVal, newVal) -> oldVal,
                                    LinkedHashMap::new
                            )
                    );
        }finally {
            br.close();
        }
        return innerLevelData;
    }

    @SneakyThrows
    public static Map<String,Map<String,List<String>>> getInnerLevelData(BufferedReader br,boolean more){
        //定义子层级Map
        Map<String,Map<String,List<String>>> innerSecondLevel = new HashMap<>(20);
        //定义索引Map
        Map<String,List<String>> innerLevelData = new HashMap<>();
        //读取行内容
        String line;
        //索引列表
        List<String> keyList = new ArrayList<>(26);
        //索引列表值
        List<Map<String,List<String>>> valueList = new ArrayList<>();
        try {
            while ((line = br.readLine()) != null) {
                if (line.charAt(line.length() - 1) == '：') {
                    keyList.add(line.substring(0, line.length() - 1));
                    if (!innerLevelData.isEmpty()) {
                        valueList.add(innerLevelData);
                        innerLevelData = new HashMap<>();
                    }
                    continue;
                }
                String firstLetter = getFirstLetter(line);
                if (innerLevelData.containsKey(firstLetter)) {
                    List<String> strings = innerLevelData.get(firstLetter);
                    strings.add(line);
                } else {
                    List<String> list = new ArrayList<>();
                    list.add(line);
                    innerLevelData.put(firstLetter, list);
                }
            }
            valueList.add(innerLevelData);
            for (int i = 0; i < keyList.size(); i++) {
                Map<String, List<String>> value = valueList.get(i);
                if (!value.isEmpty()) {
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
                    innerSecondLevel.put(keyList.get(i), sortedMap);
                }
                innerSecondLevel.put(keyList.get(i), value);
            }
        }finally {
            br.close();
        }
        return innerSecondLevel;
    }
}
