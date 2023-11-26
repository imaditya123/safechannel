package com.myspring.safechannel.aws;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/aws")
@RequiredArgsConstructor
public class AwsController {
	
	private final S3Util s3util;


    @PostMapping("/upload")
    public String uploadFile(@RequestPart(value = "file") MultipartFile file) {
        return this.s3util.uploadFile(file);
    }

    @DeleteMapping("/deleteFile")
    public String deleteFile(@RequestPart(value = "url") String fileUrl) {
        return this.s3util.deleteFileFromS3Bucket(fileUrl);
    }


}
