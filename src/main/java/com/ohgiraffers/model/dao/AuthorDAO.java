package com.ohgiraffers.model.dao;

import com.ohgiraffers.model.dto.AuthorDTO;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.ohgiraffers.common.JDBCTemplate.close;

public class AuthorDAO {

    private Properties prop = new Properties();

    public AuthorDAO() {
        try {
            prop.loadFromXML(new FileInputStream("src/main/java/com/ohgiraffers/mapper/author-query.xml"));
        } catch (IOException e) {
            throw new RuntimeException("author-query.xml 파일을 읽는데 실패했습니다.", e);
        }
    }

    // 전체 작가 조회
    public List<AuthorDTO> selectAllAuthors(Connection con) {
        Statement stmt = null;
        ResultSet rset = null;
        List<AuthorDTO> list = new ArrayList<>();

        String query = prop.getProperty("selectAllAuthors");

        try {
            stmt = con.createStatement();
            rset = stmt.executeQuery(query);

            while (rset.next()) {
                AuthorDTO author = new AuthorDTO();
                author.setAuthorId(rset.getInt("author_id"));
                author.setAuthorName(rset.getString("author_name"));
                author.setAwarded(rset.getString("awarded"));
                list.add(author);
            }
        } catch (SQLException e) {
            throw new RuntimeException("전체 작가 조회 중 오류 발생", e);
        } finally {
            close(rset);
            close(stmt);
        }

        return list;
    }

    // ID로 작가 조회
    public AuthorDTO selectAuthorById(Connection con, int authorId) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        AuthorDTO author = null;

        String query = prop.getProperty("selectAuthorById");

        try {
            pstmt = con.prepareStatement(query);
            pstmt.setInt(1, authorId);
            rset = pstmt.executeQuery();

            if (rset.next()) {
                author = new AuthorDTO();
                author.setAuthorId(rset.getInt("author_id"));
                author.setAuthorName(rset.getString("author_name"));
                author.setAwarded(rset.getString("awarded"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("작가 조회 중 오류 발생", e);
        } finally {
            close(rset);
            close(pstmt);
        }

        return author;
    }

    // 작가 삽입 (author_name NOT NULL 컬럼, awarded는 null 가능)
    public int insertAuthor(Connection con, AuthorDTO author) {
        PreparedStatement pstmt = null;
        int result = 0;

        String query = prop.getProperty("insertAuthor");

        try {
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, author.getAuthorName());
            pstmt.setString(2, author.getAwarded());

            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("작가 삽입 중 오류 발생", e);
        } finally {
            close(pstmt);
        }

        return result;
    }

    // 작가 수정 (author_name만 수정)
    public int updateAuthor(Connection con, AuthorDTO author) {
        PreparedStatement pstmt = null;
        int result = 0;

        String query = prop.getProperty("updateAuthor");

        try {
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, author.getAuthorName());
            pstmt.setInt(2, author.getAuthorId());

            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("작가 수정 중 오류 발생", e);
        } finally {
            close(pstmt);
        }

        return result;
    }

    // 작가 삭제
    public int deleteAuthor(Connection con, int authorId) {
        PreparedStatement pstmt = null;
        int result = 0;

        String query = prop.getProperty("deleteAuthor");

        try {
            pstmt = con.prepareStatement(query);
            pstmt.setInt(1, authorId);

            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("작가 삭제 중 오류 발생", e);
        } finally {
            close(pstmt);
        }

        return result;
    }
}
