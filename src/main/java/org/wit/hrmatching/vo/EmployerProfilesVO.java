package org.wit.hrmatching.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
