package com.cos.instagram.domain.image;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SqlResultSetMapping;

import org.hibernate.annotations.CreationTimestamp;

import com.cos.instagram.domain.tag.Tag;
import com.cos.instagram.domain.user.User;
import com.cos.instagram.web.dto.UserProfileImageRespDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@SqlResultSetMapping(name = "UserProfileImageRespDtoMapping", classes = @ConstructorResult(targetClass = UserProfileImageRespDto.class, columns = {
		@ColumnResult(name = "id", type = Integer.class), // Object 매핑 X
		@ColumnResult(name = "imageUrl", type = String.class), @ColumnResult(name = "likeCount", type = Integer.class),
		@ColumnResult(name = "commentCount", type = Integer.class) }))

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Image {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String location;
	private String caption; // 사진 설명
	private String imageUrl; // 이미지 경로

	// Image를 select하면 한 명의 User가 딸려옴 - 부하↓ 기본적인 전략이 Image를 select하면 User를 조인해서 들고옴
	// 어차피 한 명이니까
	@ManyToOne(fetch = FetchType.EAGER) // Many : Image, One : User 관계 어노테이션 걸기
	@JoinColumn(name = "userId") // 정해준 컬럼명으로 들어감
	private User user; // Object 해당 객체를 select 해옴. 실제 DB에는 user가 안들어가고 1이 들어감 → 내부적 조인

	// Image를 select하면 여러 개의 Tag가 딸려옴 - 부하↑
	@OneToMany(mappedBy = "image", fetch = FetchType.LAZY) // One : Image, Many : Tag
	@JsonIgnoreProperties({ "image" }) // Jackson한테 내리는 명령 : 여기로부터 Tag 접근하면 Tag 안의 image는 getter 호출 하지마 → 무한 생성 막기
	private List<Tag> tags;

	@CreationTimestamp
	private Timestamp createDate;

}
