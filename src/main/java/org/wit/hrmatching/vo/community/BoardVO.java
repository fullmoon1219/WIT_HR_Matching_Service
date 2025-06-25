package org.wit.hrmatching.vo.community;

import lombok.Data;

@Data
public class BoardVO {
    private Long id;           // 게시판 ID
    private String code;       // 게시판 코드 (ex: free, qna)
    private String name;       // 게시판 이름 (ex: 자유게시판)
    private String icon;        // 게시판 아이콘
    private String description; // 게시판 설명 (선택)
}
