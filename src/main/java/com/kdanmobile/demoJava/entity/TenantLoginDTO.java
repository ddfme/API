package com.kdanmobile.demoJava.entity;

import lombok.*;

/**
 * @author ComPDFKit-WPH 2022/9/15
 */
@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TenantLoginDTO {

    private String accessToken;

    private String tokenType;

    private String expiresIn;

    private String scope;

}
