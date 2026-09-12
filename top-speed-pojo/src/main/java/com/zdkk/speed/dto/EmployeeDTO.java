package com.zdkk.speed.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "添加员工时传递的数据模型")
public class EmployeeDTO {
    @Schema(description = "id")
    private Long id;

    @Schema(description = "身份证")
    @NotBlank
    private String idNumber;

    @Schema(description = "姓名")
    @NotBlank
    private String name;

    @Schema(description = "电话号")
    @NotBlank
    private String phone;

    @Schema(description = "性别")
    @NotBlank
    private String sex;

    @Schema(description = "用户名")
    @NotBlank
    private String username;
}
