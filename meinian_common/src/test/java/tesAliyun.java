import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class tesAliyun {

    @Test
    public void tesAliyun(){

        try {

            File file = new File("D:/1.jpg");
            //init array with file length
            InputStream fis = new FileInputStream(file);
            // yourEndpoint填写Bucket所在地域对应的Endpoint。以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
            String endpoint = "https://oss-cn-hangzhou.aliyuncs.com";
// 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
            String accessKeyId = "LTAI5t8gV1LVrXmdnxARezY3";
            String accessKeySecret = "L4a8GSCrM7TKCFKmYZ07A8Vb0Y4h51";
// 填写Bucket名称。
            String bucketName = "meinian-rlg";
// 填写文件完整路径。文件完整路径中不能包含Bucket名称。
            String objectName = "pic/1sdfasf2.jpg";
// 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

// 填写网络流地址。

// 依次填写Bucket名称（例如examplebucket）和Object完整路径（例如exampledir/exampleobject.txt）。Object完整路径中不能包含Bucket名称。
            ossClient.putObject(bucketName, objectName, fis);

// 关闭OSSClient。
            ossClient.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void delete(){
        // yourEndpoint填写Bucket所在地域对应的Endpoint。以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
        String endpoint = "https://oss-cn-hangzhou.aliyuncs.com";
// 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = "LTAI5t8gV1LVrXmdnxARezY3";
        String accessKeySecret = "L4a8GSCrM7TKCFKmYZ07A8Vb0Y4h51";
// 填写Bucket名称。
        String bucketName = "meinian-rlg";
// 填写文件完整路径。文件完整路径中不能包含Bucket名称。
        String objectName = "pic/12.jpg";

// 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

// 删除文件或目录。如果要删除目录，目录必须为空。
        ossClient.deleteObject(bucketName, objectName);

// 关闭OSSClient。
        ossClient.shutdown();

    }


}
