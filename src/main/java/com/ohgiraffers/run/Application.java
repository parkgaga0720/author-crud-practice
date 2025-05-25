package com.ohgiraffers.run;

import com.ohgiraffers.model.dao.AuthorDAO;
import com.ohgiraffers.model.dto.AuthorDTO;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

import static com.ohgiraffers.common.JDBCTemplate.*;

public class Application {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Connection con = getConnection();
        AuthorDAO authorDAO = new AuthorDAO();

        while (true) {
            System.out.println("====== 작가 관리 프로그램 ======");
            System.out.println("1. 작가 전체 조회");
            System.out.println("2. 작가 상세 조회");
            System.out.println("3. 작가 등록");
            System.out.println("4. 작가 수정");
            System.out.println("5. 작가 삭제");
            System.out.println("0. 종료");
            System.out.print("메뉴 선택: ");
            int menu = sc.nextInt();
            sc.nextLine(); // 버퍼 비우기

            try {
                switch (menu) {
                    case 1:
                        List<AuthorDTO> authors = authorDAO.selectAllAuthors(con);
                        for (AuthorDTO author : authors) {
                            System.out.printf("%d : %s (수상내역: %s)%n",
                                    author.getAuthorId(), author.getAuthorName(),
                                    author.getAwarded() == null ? "없음" : author.getAwarded());
                        }
                        break;

                    case 2:
                        System.out.print("조회할 작가 ID 입력: ");
                        int id = Integer.parseInt(sc.nextLine());
                        AuthorDTO author = authorDAO.selectAuthorById(con, id);
                        if (author != null) {
                            System.out.println("작가 ID: " + author.getAuthorId());
                            System.out.println("작가 이름: " + author.getAuthorName());
                            System.out.println("수상 내역: " + (author.getAwarded() == null ? "없음" : author.getAwarded()));
                        } else {
                            System.out.println("해당 ID의 작가가 없습니다.");
                        }
                        break;

                    case 3:
                        System.out.print("작가 이름 입력: ");
                        String name = sc.nextLine();
                        System.out.print("수상 내역 입력 (없으면 엔터): ");
                        String awarded = sc.nextLine();
                        if (awarded.isEmpty()) awarded = null;

                        AuthorDTO newAuthor = new AuthorDTO();
                        newAuthor.setAuthorName(name);
                        newAuthor.setAwarded(awarded);

                        int insertResult = authorDAO.insertAuthor(con, newAuthor);
                        System.out.println(insertResult > 0 ? "작가 등록 성공!" : "작가 등록 실패!");
                        break;

                    case 4:
                        System.out.print("수정할 작가 ID 입력: ");
                        int modId = Integer.parseInt(sc.nextLine());
                        AuthorDTO modAuthor = authorDAO.selectAuthorById(con, modId);
                        if (modAuthor == null) {
                            System.out.println("해당 ID의 작가가 없습니다.");
                            break;
                        }
                        System.out.print("작가 이름 (" + modAuthor.getAuthorName() + "): ");
                        String modName = sc.nextLine();
                        if (!modName.isEmpty()) modAuthor.setAuthorName(modName);

                        System.out.print("수상 내역 (" + (modAuthor.getAwarded() == null ? "없음" : modAuthor.getAwarded()) + "): ");
                        String modAwarded = sc.nextLine();
                        modAuthor.setAwarded(modAwarded.isEmpty() ? null : modAwarded);

                        int updateResult = authorDAO.updateAuthor(con, modAuthor);
                        System.out.println(updateResult > 0 ? "작가 수정 성공!" : "작가 수정 실패!");
                        break;

                    case 5:
                        System.out.print("삭제할 작가 ID 입력: ");
                        int delId = Integer.parseInt(sc.nextLine());
                        int deleteResult = authorDAO.deleteAuthor(con, delId);
                        System.out.println(deleteResult > 0 ? "작가 삭제 성공!" : "작가 삭제 실패!");
                        break;

                    case 0:
                        System.out.println("프로그램 종료");
                        close(con);
                        sc.close();
                        return;

                    default:
                        System.out.println("잘못된 메뉴 선택입니다.");
                }
            } catch (Exception e) {
                System.out.println("오류 발생: " + e.getMessage());
            }
        }
    }
}
