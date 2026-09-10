package com.zdkk.speed.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;

@Data
@Schema(description = "员工登录时传递的数据模型")
public class EmployeeLoginDTO {
    @Schema(description = "员工用户名", example = "admin")
    private String username;
    @Schema(description = "员工密码", example = "123123")
    private String password;
}
