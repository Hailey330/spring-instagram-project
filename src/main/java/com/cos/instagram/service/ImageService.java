package com.cos.instagram.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.instagram.domain.image.Image;
import com.cos.instagram.domain.image.ImageRepository;
import com.cos.instagram.domain.tag.Tag;
import com.cos.instagram.domain.tag.TagRepository;
import com.cos.instagram.domain.user.User;
import com.cos.instagram.domain.user.UserRepository;
import com.cos.instagram.util.Utils;
import com.cos.instagram.web.dto.ImageReqDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ImageService {

	private final ImageRepository imageRepository;
	private final TagRepository tagRepository;
	private final UserRepository userRepository;
	
	@Value("${file.path}")
	private String uploadFolder;

	@Transactional // 저장하다가 오류나면 Rollback 해야함
	public void 사진업로드(ImageReqDto imageReqDto, int userId) {

		// 항상 DB와 동기화되는 것이 좋음
		User userEntity = userRepository.findById(userId).orElseThrow(null);
		UUID uuid = UUID.randomUUID();
		String imageFilename = uuid + "_" + imageReqDto.getFile().getOriginalFilename();
		Path imageFilePath = Paths.get(uploadFolder + imageFilename);
		try {
			Files.write(imageFilePath, imageReqDto.getFile().getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// 1. Image 저장
		Image image = imageReqDto.toEntity(imageFilename, userEntity); // 경로 넣으면 나중에 바뀌었을 때 다 바꿔야함. 어차피 서버에서는 경로를 알기 때문에 파일명만 명시함
		Image imageEntity = imageRepository.save(image); // imageEntity - DB와 동기화된 데이터(영속화)
		
		// 2. Tag 저장
		List<String> tagNames = Utils.tagParse(imageReqDto.getTags());
	
		for (String name : tagNames) {
			Tag tag = Tag.builder()
					.image(imageEntity)
					.name(name)
					.build();
			tagRepository.save(tag);
		}
	}
}
