package cn.tedu.img.service;

import com.jt.common.vo.PicUploadResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Mangmang
 * @date 2020/2/11 21:23
 */
public interface PicService {
    public PicUploadResult upload(MultipartFile pic);
}
