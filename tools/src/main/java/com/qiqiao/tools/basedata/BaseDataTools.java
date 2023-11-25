package com.qiqiao.tools.basedata;
import com.qiqiao.model.basedata.domain.*;
import com.qiqiao.model.basedata.finals.BaseDataFinal;
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

    /**
     * 获取索引数据的方法
     * @param url 文件夹路径
     * @return IndexLevelData
     */
    @SneakyThrows
    public static BaseIndexLevelData initIndexData(String url,String key){
        BaseIndexLevelData indexLevelData = null;
        if (key.equals(BaseDataFinal.INSPECT_DATA_KEY)){
           indexLevelData = new Inspect();
        }else{
            indexLevelData = new Treatment();
        }
        Map<String,List<String>> data = null;
        File directory = new File(url);
        File[] fileList = directory.listFiles();
        assert fileList != null;
        File file = fileList[0];
        @Cleanup BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        if ((line = br.readLine()) != null){
            data = getInnerLevelData(br,line);
        }
        indexLevelData.setData(data);
        return indexLevelData;
    }
    /**
     * 获得顶层数据方法（文件夹多文件）
     * @param url 文件夹路径
     * @return FirstLevelData
     */
    public static BaseFirstLevelData initLevelData(String url, String key){
        BaseFirstLevelData firstLevelData = null;
        if (key.equals(BaseDataFinal.DISEASE_DATA_KEY)){
            firstLevelData = new Disease();
        }else{
            firstLevelData = new Vaccine();
        }
        List<SecondLevelData> secondLevelDataList = new ArrayList<>();
        firstLevelData.setSecondLevelDataList(secondLevelDataList);
        File directory = new File(url);
        File[] fileList = directory.listFiles();
        for (File file : Objects.requireNonNull(fileList)) {
            secondLevelDataList.add(getSecondLevel(file));
        }
        return firstLevelData;
    }

    /**
     *
     * @param word 识别语句
     * @return String
     */
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

    /**
     *
     * @param file 读取文件
     * @return SecondLevelData
     */
    @SneakyThrows
    public static SecondLevelData getSecondLevel(File file){
        //获得子级层级数据对象
        SecondLevelData secondLevelData = new SecondLevelData();
        //将文件名设置为层级名
        secondLevelData.setName((file.getName().substring(0, file.getName().length() - 4)));
        //定义IO流
        @Cleanup BufferedReader br = new BufferedReader(new FileReader(file));
        //设置获取的内部内容
        String line;
        if ((line = br.readLine()) != null) {
            secondLevelData.setSecondLevel(doGetSecondLevel(br,line));
        }else {
            secondLevelData.setSecondLevel(Collections.emptyMap());
        }
        //返回结果
        return secondLevelData;

    }

    /**
     *
     * @param br 文件流缓冲区
     * @param line 读取的第一行内容
     * @return Map<String, Map<String, List<String>>>
     */

    @SneakyThrows
    public static Map<String, Map<String, List<String>>> doGetSecondLevel(BufferedReader br,String line){
        //定义子层级Map
        Map<String,Map<String,List<String>>> innerSecondLevel;
        //定义索引Map
        Map<String,List<String>> innerLevelData;
        //判断是否有内层级
        if(line.charAt(line.length() - 1) != '：'){
            innerLevelData = getInnerLevelData(br,line);
            innerSecondLevel = Collections.singletonMap("",innerLevelData);
        }else {
            innerSecondLevel = getInnerLevelData(br,line ,true);
        }
        return innerSecondLevel;
    }

    /**
     *
     * @param br 文件流缓冲区
     * @param line 判断行内容
     * @return Map<String,List<String>>
     */
    @SneakyThrows
    public static Map<String,List<String>> getInnerLevelData( BufferedReader br,String line){
        //索引值Map
        Map<String,List<String>> innerLevelData = new HashMap<>(26);
        //读取行内容
        try {
            do {
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
            } while ((line = br.readLine()) != null);
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

    /**
     *
     * @param br 文件流缓冲区
     * @param line 判断行内容
     * @param more 是否有子层
     * @return Map<String,Map<String,List<String>>>
     */

    @SneakyThrows
    public static Map<String,Map<String,List<String>>> getInnerLevelData(BufferedReader br,String line,boolean more){
        //定义子层级Map
        Map<String,Map<String,List<String>>> innerSecondLevel = new HashMap<>(20);
        //定义索引Map
        Map<String,List<String>> innerLevelData = new HashMap<>(26);
        //索引列表
        List<String> keyList = new ArrayList<>(26);
        //索引列表值
        List<Map<String,List<String>>> valueList = new ArrayList<>();
        try {
            do {
                if (line.charAt(line.length() - 1) == '：') {
                    keyList.add(line.substring(0, line.length() - 1));
                    if (!innerLevelData.isEmpty()) {
                        valueList.add(innerLevelData);
                        innerLevelData = new HashMap<>(26);
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
            } while ((line = br.readLine()) != null);
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
