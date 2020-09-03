package com.cos.instagram.test;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.instagram.config.auth.LoginUserAnnotation;
import com.cos.instagram.config.auth.dto.LoginUser;
import com.cos.instagram.domain.comment.Comment;
import com.cos.instagram.domain.comment.CommentRepository;
import com.cos.instagram.domain.follow.Follow;
import com.cos.instagram.domain.follow.FollowRepository;
import com.cos.instagram.domain.image.Image;
import com.cos.instagram.domain.image.ImageRepository;
import com.cos.instagram.domain.like.Likes;
import com.cos.instagram.domain.like.LikesRepository;
import com.cos.instagram.domain.tag.Tag;
import com.cos.instagram.domain.tag.TagRepository;
import com.cos.instagram.domain.user.User;
import com.cos.instagram.domain.user.UserRepository;
import com.cos.instagram.domain.user.UserRole;

@RestController
public class TestApiController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ImageRepository imageRepository;
	
	@Autowired
	private TagRepository tagRepository;
	
	@Autowired
	private FollowRepository followRepository;
	
	@Autowired
	private LikesRepository likesRepository;
	
	@Autowired
	private CommentRepository commentRepository;
	
	
	@PostMapping("/test/api/join")
	public User join(@RequestBody User user) {
		user.setRole(UserRole.USER); // USER

		User userEntity = userRepository.save(user); // userEntity : 영속화된 user
		return userEntity;
	}
	
	@PostMapping("/test/api/image/{caption}")
	public String image(@PathVariable String caption) {
		
		User userEntity = userRepository.findById(1).get();

		// 1. Image가 먼저 insert 되어야 Tag를 넣을 수 있음 
		Image image = Image.builder()
				.location("외국")
				.caption(caption)
				.imageUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRNistxLCzQi8CK_iqM_C2TPbAaXFzJzDi1Eg13PkvzhZe7fLu0&s")
				.user(userEntity) // 지금은 강제로, 넣을 때는 session (principal.id)
				.build();
		// -> 영속화가 아직 되지않은 Image 따라서 Tag 안의 image에는 값이 없는 상태!
		
		Image imageEntity = imageRepository.save(image); // imageEntity : 영속화됨 한번 select되어서 persistence 안에 들어가 있음

		// 2. Tag insert
		List<Tag> tags = new ArrayList<>();
		Tag tag1 = Tag.builder()
				.name("#외국")
				.image(imageEntity) // 위에 영속화된 imageEntity가 들어와서 매칭됨
				.build();
		Tag tag2 = Tag.builder()
				.name("#여행")
				.image(imageEntity)
				.build();
		
		tags.add(tag1);
		tags.add(tag2);
		
		tagRepository.saveAll(tags);
		
		// List<Image> images = imageRepository.findAll();
		
		// Image imageEntity = imageRepository.findById(1).get();
		// return images; // Message Converter의 Jackson 발동
		// images 호출할 때 getter 호출하면서 User, Tag 리턴도 동시에 함
		// Image 모델 안에 Tag 호출하고 Tag에서는 Image를 또 호출하기 떄문에 무한으로 생성되는 오류 발생
		
		return "Image Insert 확인";
	}
	
	@GetMapping("/test/api/image/list")
	public List<Image> imageList() {
		return imageRepository.findAll();
	}
	
	@GetMapping("/test/api/tag/list")
	public List<Tag> tagList() {
		return tagRepository.findAll();
	}
	
	@PostMapping("/test/api/follow/{fromUserId}/{toUserId}")
	public String follow(@PathVariable int fromUserId, @PathVariable int toUserId) {
		User fromUserEntity = userRepository.findById(fromUserId).get();
		User toUserEntity = userRepository.findById(toUserId).get();
		
		Follow follow = Follow.builder()
				.fromUser(fromUserEntity)
				.toUser(toUserEntity)
				.build();
		
		followRepository.save(follow);
		// http://localhost:8080/test/api/follow/1/2
		
		return fromUserEntity.getUsername() + "님이 " + toUserEntity.getUsername() + "을 팔로우 하였습니다.";
	}
	
	@PostMapping("/test/api/image/{imageId}/likes")
	public String imageLikes(@PathVariable int imageId) {
		Image imageEntity = imageRepository.findById(imageId).get();
		User userEntity = userRepository.findById(3).get();
		Likes likes = Likes.builder()
				.image(imageEntity)
				.user(userEntity)
				.build();
		
		likesRepository.save(likes);
		return userEntity.getUsername() + "님이 " + imageEntity.getId() + "번 사진을 좋아합니다.";
	}
	
	@GetMapping("/test/annotation")
	public String feed(@CosAnnotation String string) {
		System.out.println("CosAnnotation : " + string);
		return string;
	}
	
	
}
