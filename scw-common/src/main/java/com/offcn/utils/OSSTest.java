package com.offcn.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class OSSTest {
    /**
     * OSS 使用步骤 阿里云
     * 1）、引入SDK
     * 2）、配置好相应的属性
     */
    public static void main(String[] args)throws IOException {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = "http://oss-cn-beijing.aliyuncs.com";
        // 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
        String accessKeyId = "LTAI4G5uBtoeBqiiooN3cjpi";
        String accessKeySecret = "xEWoD30WEUgWmwuzIgTPjpWppNONmo";
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 上传文件流。
        InputStream inputStream = new FileInputStream(new File("C:\\Users\\ASUS\\Pictures\\Saved Pictures\\60d2f4fe0275d790-60de8deab87e73ea-f8802d1d23ad802df30204860d96786e.jpg"));
        ossClient.putObject("offcn24555", "pic/wangtao.jpg", inputStream);
        // 关闭OSSClient。
        ossClient.shutdown();

        System.out.println("测试完成");
    }
}
