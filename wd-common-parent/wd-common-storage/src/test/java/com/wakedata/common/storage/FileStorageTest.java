package com.wakedata.common.storage;


import com.wakedata.common.storage.enums.BucketEnum;
import com.wakedata.common.storage.enums.FileTypeEnum;
import com.wakedata.common.storage.model.UploadRequest;
import com.wakedata.common.storage.model.UploadResult;
import com.wakedata.common.storage.service.FileStorageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.File;

/**
 * @Desc
 * @Author zkz
 * @Date 2021/12/13
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@SpringBootApplication
public class FileStorageTest {

    @Resource
    private FileStorageService fileStorageService;
    String filekey = "e4b704a0f86648e0a969db56518b800d";
    String filekey1 = "e4b704a0f86648e0a969db56518b800d1";
    String filekey2 = "e4b704a0f86648e0a969db56518b800d2";
    String filekey3 = "e4b704a0f86648e0a969db56518b800d3";
    @Test
    public void upload(){
        File file = new File("C:\\Users\\admin\\Desktop\\images.jpg");
//        File file = new File("C:\\Users\\admin\\Desktop\\demo.zip");

        UploadRequest request = UploadRequest.builder().fileKey(filekey1).bucket(BucketEnum.MATERIAL).fileTypeEnum(FileTypeEnum.PICTURE).genThumb(false).build();
//        UploadRequest face = UploadRequest.builder().fileKey(filekey2).bucket(BucketEnum.FACE).fileTypeEnum(FileTypeEnum.PICTURE).genThumb(true).build();
//        UploadRequest cert = UploadRequest.builder().fileKey(filekey3).bucket(BucketEnum.CERT).fileTypeEnum(FileTypeEnum.PICTURE).genThumb(true).build();
        UploadResult upload = fileStorageService.upload(request, file);
//        UploadResult upload2 = fileStorageService.upload(face, file);
//        UploadResult upload3 = fileStorageService.upload(cert, file);
        System.out.println(upload.toString());
//        System.out.println(upload2.toString());
//        System.out.println(upload3.toString());
        // 指定桶
//        UploadRequest request = UploadRequest.builder().bucket("face").build();
//        UploadResult upload2 = fileStorageService.upload(request, file);
//        System.out.println(upload2.toString());
//        // 缩略图
//        request.setGenThumb(true);
//        UploadResult upload3 = fileStorageService.upload(request, file);

//        System.out.println(upload3.toString());
    }

    @Test
    public void download(){
        File file = new File("C:\\Users\\admin\\Desktop\\d1.png");
        // 下载到指定文件，存在则覆盖
        fileStorageService.downloadFile( "d8af1abe0a214887a4d94cca0b22d8ca", file);

    }
    @Test
    public void getUrl(){
        UploadResult url = fileStorageService.getURL( "d8af1abe0a214887a4d94cca0b22d8ca");
        UploadResult url2 = fileStorageService.getURL(BucketEnum.FACE, filekey);
        UploadResult url3 = fileStorageService.getURL(BucketEnum.CERT, filekey);

        System.out.println("getUrl:" + url.toString());
        System.out.println("getUrl2:" + url2.toString());
        System.out.println("getUrl3:" + url3.toString());
    }
    @Test
    public void delete(){
        fileStorageService.deleteFile("d8af1abe0a214887a4d94cca0b22d8ca");
        fileStorageService.deleteFile(BucketEnum.MATERIAL, filekey);
        fileStorageService.deleteFile(BucketEnum.FACE, filekey);
        fileStorageService.deleteFile(BucketEnum.CERT, filekey);

    }



}
