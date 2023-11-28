package com.myspring.safechannel.book;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)

public class Book {

	@Id
	@GeneratedValue
	private Integer id;
	private String author;
	private String genre;
	private String title;

	private String coverImage;
	private String description;
	private Integer publicationYear;
	private String isbn;
	private String language;
	private String pages;
	private Integer publisher;
	private Double rating;
	private Integer reviews;
	private String formats;
	private String price;
	private String purchaseLink;
	private Boolean featured;

	private Boolean bestseller;
	private String releaseDate;

	@Enumerated(EnumType.STRING)
	private AgeGroup ageGroup;

	@CreatedDate
	@Column(nullable = false, updatable = false)
	private LocalDateTime createDate;

	@LastModifiedBy
	@Column(insertable = false)
	private LocalDateTime lastModified;

	@CreatedBy
	@Column(nullable = false, updatable = false)
	private String createdBy;

	@LastModifiedBy
	@Column(insertable = false)
	private String lastModifiedBy;

	@Transient
	private String currentUser;

	@PrePersist
	protected void onCreate() {
		this.createDate = LocalDateTime.now();
		this.createdBy = this.currentUser;
	}

	@PreUpdate
	protected void onUpdate() {
		this.lastModified = LocalDateTime.now();
		this.lastModifiedBy = this.currentUser;
	}

	public void setCurrentUser(String currentUser) {
		this.currentUser = currentUser;
	}

//    
//    private Integer getCurrentUserId() {
//        return 1; // Replace this with your logic
//    }

}
