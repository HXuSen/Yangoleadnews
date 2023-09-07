package com.yango.minio.test;

import com.yango.file.service.FileStorageService;
import com.yango.minio.MinIOApplication;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;


/**
 * ClassName: MinIOTest
 * Package: com.yango.minio.test
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/8/28-17:00
 */

@SpringBootTest(classes = MinIOApplication.class)
@RunWith(SpringRunner.class)
public class MinIOTest {
    @Autowired
    private FileStorageService fileStorageService;

    //@Test
    //public void test() throws FileNotFoundException {
    //    FileInputStream fis = new FileInputStream("D:\\AllRepository\\leadnews\\list.html");
    //    String path = fileStorageService.uploadHtmlFile("", "list.html", fis);
    //    System.out.println("path = " + path);
    //}
    public static void main(String[] args) {
        FileInputStream fis = null;
        try {
            //minio client
            MinioClient minioClient = MinioClient.builder()
                    .credentials("minio", "minio123")
                    .endpoint("http://192.168.162.101:9000")
                    .build();
            //upload
            fis = new FileInputStream("D:\\AllRepository\\code_study_relate\\1.back-end\\17.黑马头条\\day02-app端文章查看，静态化freemarker,分布式文件系统minIO\\资料\\模板文件\\plugins\\css\\index.css");
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .object("plugins/css/index.css")//文件名
                    .contentType("text/css")//文件类型
                    .bucket("leadnews")//桶名
                    .stream(fis,fis.available(),-1)//文件流
                    .build();
            minioClient.putObject(putObjectArgs);
            //System.out.println("http://192.168.162.101:9000/leadnews/list.html");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
