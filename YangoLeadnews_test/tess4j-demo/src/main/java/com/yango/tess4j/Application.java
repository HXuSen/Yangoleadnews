package com.yango.tess4j;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;

/**
 * ClassName: Application
 * Package: com.yango.tess4j
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/8/30-16:45
 */
public class Application {
    public static void main(String[] args) throws TesseractException {
        //创建实例
        Tesseract tesseract = new Tesseract();
        //设置字体库路径
        tesseract.setDatapath("D:\\AllRepository\\leadnews");
        //设置语言
        tesseract.setLanguage("chi_sim");

        File file = new File("D:\\AllRepository\\leadnews\\OCR测试.png");
        //识别图片
        String result = tesseract.doOCR(file).replaceAll("\\r|\\n","-");
        System.out.println(result);
    }
}
