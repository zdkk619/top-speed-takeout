package com.zdkk.speed.controller.admin;

import com.zdkk.speed.constant.MessageConstant;
import com.zdkk.speed.result.Result;
import com.zdkk.speed.utils.AliOssUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/admin/common")
@Slf4j
@Tag(name = "通用管理", description = "通用管理相关接口")
public class CommonController {
    @Autowired
    private AliOssUtil aliOssUtil;


    /**
     * 上传文件
     * @param file
     * @return
     */
    @PostMapping("/upload")
    @Operation(summary = "上传文件", description = "上传文件")
    public Result<String> upload(MultipartFile file) {
        log.info("上传文件：{}", file);
        try {
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String objectName = UUID.randomUUID().toString() + extension;
            String filePath = aliOssUtil.upload(file.getBytes(), objectName);
            return Result.success(filePath);
        } catch (IOException e) {
            log.error("上传文件失败：{}", e.getMessage());
        }
        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}
