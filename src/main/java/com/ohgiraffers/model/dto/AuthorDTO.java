package com.ohgiraffers.model.dto;

public class AuthorDTO {
    private int authorId;
    private String authorName;
    private String awarded;

    public AuthorDTO() {}

    public AuthorDTO(String authorName) {
        this.authorName = authorName;
    }

    public AuthorDTO(int authorId, String authorName) {
        this.authorId = authorId;
        this.authorName = authorName;
    }

    public int getAuthorId() { return authorId; }
    public void setAuthorId(int authorId) { this.authorId = authorId; }

    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }

    public String getAwarded() { return awarded; }
    public void setAwarded(String awarded) { this.awarded = awarded; }

    @Override
    public String toString() {
        return "AuthorDTO{" +
                "authorId=" + authorId +
                ", authorName='" + authorName + '\'' +
                ", awarded='" + awarded + '\'' +
                '}';
    }
}
