package com.pacal.share.controller;

import com.pacal.share.annotation.NeedLogin;
import com.pacal.share.common.BaseResponse;
import com.pacal.share.entity.vo.UploadVO;
import com.pacal.share.service.storage.UploadBizService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadController {

    @Resource
    UploadBizService uploadBizService;

    /** 文件上传 (登录后可调用) */
    @NeedLogin
    @PostMapping("/upload/file")
    public BaseResponse<UploadVO> upload(@RequestParam("file") MultipartFile file) {
        return BaseResponse.success( uploadBizService.upload( file ) );
    }
}