package com.HopeConnect.HC.DTO;

import com.HopeConnect.HC.models.Donation.Review;

import java.time.LocalDateTime;

public class ReviewDTO {
    private Integer rating;
    private String comment;
    private LocalDateTime reviewDate;

    public ReviewDTO(Review review) {
        this.rating = review.getRating();
        this.comment = review.getComment();
        this.reviewDate = review.getReviewDate();
    }

    // Getters and setters
    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(LocalDateTime reviewDate) {
        this.reviewDate = reviewDate;
    }
}
