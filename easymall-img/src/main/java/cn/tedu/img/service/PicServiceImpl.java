package cn.tedu.img.service;

import com.jt.common.utils.UUIDUtil;
import com.jt.common.utils.UploadUtil;
import com.jt.common.vo.PicUploadResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @author Mangmang
 * @date 2020/2/11 21:32
 */
@Service
public class PicServiceImpl implements PicService {

    @Override
    public PicUploadResult upload(MultipartFile pic) {
         /*
            图片上传实现的流程
            1.对上传的文件做校验是否合法
                1.1判断后缀是否属于图片 jpg,png,gif...
            2.生成存储路径 d:/static/upload/1/d/d/3/d/3/d/3/
            3.图片重命名，防止上传同一个图片名称，存储到文件夹
                d:/static/upload/1/d/d/3/d/3/d/3/重命名.png
            4.生成对应可以访问到该文件的url地址，通过nginx静态访问
                http://image.jt.com/upload/1/d/d/3/d/3/d/3/重命名.png
            5.封装数据，返回对象 url包装返回给ajax使用
            整个流程过程任何一个位置出现异常，都会导致图片上传失败
            try catch包含所有代码
         */
        //PicUploadResult 表示图片上传是否成功，成功携带url
        PicUploadResult result=new PicUploadResult();//error=0 url=null
        //编写上传图片具体逻辑
        try {
            //从pic参数中拿到文件原名称，解析后缀.png,.jpg..判断合法
            //拿到原名称
            String oName = pic.getOriginalFilename();//qige.png
            String extName = oName.substring(oName.lastIndexOf("."));//后缀
            //判断合法，正则表达式
            //通过后缀判断，截取后缀
            if(!extName.matches(".(png|gif|jpg)$")){
                //说明 后缀截取值不满足要求，不是图片，不合法
                result.setError(1);
                return result;
            }
            //根据图片，生成一个多级路径/upload/1/d/3/d/3/d/3/d/,调用一个工具类
            String dir = UploadUtil.getUploadPath(oName, "upload");
            //根据生成的dir，创建磁盘d:/static/upload/1/d/3/d/3/d/3/d//
            String path="E:/java/Nginxstatic/"+dir+"/";
            File _dir=new File(path);//代表文件夹内存对象
            //如果没有这个文件夹需要创建多级目录
            if(!_dir.exists()){
                _dir.mkdirs();
            }
            //重命名图片，存储图片到多级目录下
            String nName= UUIDUtil.getUUID()+extName;//3kd3-3md23dk434dfs23-3.jpg
            pic.transferTo(new File(path+nName));

            //根据上传的图片，生成可以url访问图片
            String url="http://image.jt.com/"+dir+"/"+nName;
            //http://image.jt.com/upload/5/e/d/5/4/5/e/b/5f0d34dc-157f-49ba-ad39-1b28927ba6ae_1005714.jpg
            result.setUrl(url);
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            //出现异常了上传图片表示失败
            result.setError(1);
            return result;
        }

    }
}
