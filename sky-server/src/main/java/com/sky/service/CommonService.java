package com.sky.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
public interface CommonService {
    String uploadFile(MultipartFile file) throws IOException;
}
