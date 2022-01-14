package com.rlg.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;

import java.io.InputStream;

/**
 * 阿里雲oos工具類
 */
public class AliyunUtil {
    private static String endpoint = "https://oss-cn-hangzhou.aliyuncs.com";
    // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
    private static String accessKeyId = "LTAI5t8gV1LVrXmdnxARezY3";
    private static String accessKeySecret = "L4a8GSCrM7TKCFKmYZ07A8Vb0Y4h51";
    // 填写Bucket名称。
    private static String bucketName = "meinian-rlg";

    //上传文件
    public static void uploadAliyun(InputStream is, String fileName){
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 依次填写Bucket名称（例如examplebucket）和Object完整路径（例如exampledir/exampleobject.txt）。Object完整路径中不能包含Bucket名称。
        ossClient.putObject(bucketName, "pic/"+fileName, is);

        // 关闭OSSClient。
        ossClient.shutdown();
    }

    //删除文件
    public static void deleteFileFromAliyun(String fileName){
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 删除文件或目录。如果要删除目录，目录必须为空。
        ossClient.deleteObject(bucketName, "pic/"+fileName);

        // 关闭OSSClient。
        ossClient.shutdown();
    }
}
