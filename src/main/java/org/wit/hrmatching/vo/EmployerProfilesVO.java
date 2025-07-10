package org.wit.hrmatching.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

@Alias(value = "EmployerProfilesVO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployerProfilesVO {

    private Long userId;           // user_id (foreign key, employer user)
    private String companyName;    // company_name
    private String businessNumber; // business_number
    private String address;        // address
    private String phoneNumber;    // phone_number
    private String homepageUrl;    // homepage_url
    private String industry;       // industry
    private Integer foundedYear;   // founded_year
    private String companySize;    // company_size
    private String ceoName;
    private String email;
    private String password;       // 화면단에서 입력한 비밀번호, userPassword와 일치 비교
    private String logoUrl;        // 화면단에서 입력한 비밀번호, userPassword와 일치 비교
    private String profileImage;
}
