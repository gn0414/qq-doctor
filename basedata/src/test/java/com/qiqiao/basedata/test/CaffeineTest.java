package com.qiqiao.basedata.test;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.qiqiao.model.basedata.domain.Disease;
import com.qiqiao.model.basedata.domain.DiseaseDepartment;
import com.qiqiao.model.basedata.domain.DiseaseInnerDepartment;
import net.sourceforge.pinyin4j.PinyinHelper;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CaffeineTest {

    /*
    * caffeine使用基础测试
    * success
    * */
    @Test
    void testBasicOops(){
        //构建cache对象
        Cache<String, String> cache = Caffeine.newBuilder().build();
        //存入数据
        cache.put("simon","赵熙蒙");
        //取数据
        String name = cache.get("simon", key -> {
            return "安子哥";
        });
        System.out.println(name);
    }
    /*
    * 读取疾病数据集测试
    * */
    @Test
    void getDataFromTxt(){
        Disease disease = new Disease();
        String directoryPath = "C:\\Users\\Simon\\Desktop\\qq-doctor-project\\data\\disease";
        File directory = new File(directoryPath);
        File[] fileList = directory.listFiles();
        for (File file : fileList) {
            DiseaseDepartment department = new DiseaseDepartment();
            department.setDepartName(file.getName().substring(0,file.getName().length()-4));
            List<DiseaseInnerDepartment> diseaseInnerDepartments = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {


                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }






    }

    /*
    * 测试词语获取首字母大写方法
    * */
    @Test
    void getLetterTest(){

        //测试非汉字型开头
        System.out.println(getFirstLetter("2型糖尿病"));
        //测试汉字型开头
        System.out.println(getFirstLetter("感冒"));

    }
    /*
    * 汉字首字母获取方法
    * */
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
}
