package com.bloggingAplication.blog.Service.impl;

import com.bloggingAplication.blog.Service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService{

    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException{

        String name=file.getOriginalFilename();

        String randomId= UUID.randomUUID().toString();
        String fileName1=randomId.concat(name.substring(name.lastIndexOf(".")));

        String filePath=path+ File.separator +fileName1;

        File f=new File(path);
        if(!f.exists()){
            f.mkdir();
        }

        Files.copy(file.getInputStream(),Paths.get(filePath));

        return fileName1;
    }

    @Override
    public InputStream getResources(String path, String fileName) throws FileNotFoundException{

        String fullPath=path+File.separator+fileName;
        InputStream in=new FileInputStream(fullPath);
        return in;
    }
}
