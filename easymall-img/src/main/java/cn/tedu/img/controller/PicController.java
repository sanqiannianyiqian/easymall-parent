package cn.tedu.img.controller;

import cn.tedu.img.service.PicService;
import com.jt.common.vo.PicUploadResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Mangmang
 * @date 2020/2/11 22:16
 */
@RestController
public class PicController {
    @Autowired
    private PicService ps;
    //接收请求参数，调用业务层实现图片上传
    @RequestMapping("pic/upload")
    public PicUploadResult picUpload(MultipartFile pic){
        return ps.upload(pic);
    }
}
