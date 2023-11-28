package com.myspring.safechannel.cloudnary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class UploadService {
	final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private Cloudinary cloudinary;

	public String uploadFile(MultipartFile file) throws IOException {
		// Upload the file to Cloudinary
		Map<String, Object> params = ObjectUtils.asMap("transformation",
				new Transformation().width(500).height(500).crop("limit"), "public_id", "books");
		log.info("Cloudinary API: {}", cloudinary.api().toString());
		Map uploadResult = cloudinary.uploader().upload(file.getBytes(), params);
		log.info(uploadResult.toString());

		// Get the URL of the uploaded image
		return (String) uploadResult.get("url");
	}

}
