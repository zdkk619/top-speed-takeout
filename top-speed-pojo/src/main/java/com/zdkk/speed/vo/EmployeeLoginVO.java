package com.zdkk.speed.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "员工登录成功后返回的数据格式")
public class EmployeeLoginVO {
    @Schema(description = "员工id")
    private Long id;
    @Schema(description = "员工用户名")
    private String userName;
    @Schema(description = "员工姓名")
    private String name;
    @Schema(description = "jwt令牌")
    private String token;
}
