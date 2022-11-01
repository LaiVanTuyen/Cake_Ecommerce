package com.codefresher.services;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class StorageFileService {
    @Autowired
    private Cloudinary cloudinary;
    public String storageFile(MultipartFile file){
        if(!file.isEmpty()) {
            try {
                Map uploadRes = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap());
                return (String) uploadRes.get("url");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public void deleteFile(String url){
        String imageName = url.split("/")[url.split("/").length-1];
        String id = imageName.split("\\.")[0];
        try {
            cloudinary.uploader().destroy(id, ObjectUtils.emptyMap());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
