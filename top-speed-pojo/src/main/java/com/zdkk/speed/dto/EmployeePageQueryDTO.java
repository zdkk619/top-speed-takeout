package com.zdkk.speed.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "分页查询员工请求参数")
public class EmployeePageQueryDTO implements Serializable {
    @Schema(description = "员工姓名")
    private String name;
    @Schema(description = "当前页码")
    private int page;
    @Schema(description = "每页大小")
    private int pageSize;
}
