package com.sky.service.impl;

import com.aliyun.oss.common.utils.HttpUtil;
import com.sky.service.CommonService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;

@Service
public class CommonServiceImpl implements CommonService {
    //用yml文件中获取文件上传路径
    @Value("${file.uploadFolder}")
    private String upload_path;

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        String oldFilename = file.getOriginalFilename();//获取原始文件名
        String suffix = oldFilename.substring(oldFilename.lastIndexOf("."));//获取文件后缀：.jpg、.png
        String uuid = UUID.randomUUID().toString();
        String newFilename = uuid + suffix;//文件新名称
        //创建yml文件中指定的上传文件夹目录
        File folder = new File(upload_path + newFilename);
        if (!folder.getParentFile().exists()) {
            folder.getParentFile().mkdirs();
        }
        //文件写入该文件夹下
        file.transferTo(folder);

        //获取服务器ip
        InetAddress inetAddress = InetAddress.getLocalHost();
        String ip = inetAddress.getHostAddress();
        //返回保存的url，根据url可查看文件或下载文件
        String fileUrl = "http://" + ip + ":8080" + "/api/file/" + newFilename;
        return fileUrl;
    }
}
