package cn.itcast.reggie.controller;

import cn.itcast.reggie.common.Result;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.UUID;

@Controller
@RequestMapping("/common")
public class CommonController {

    @Value("${reggie.image.path}")
    private String imgePath;

    /**
     * 上传图片
     * @Param:
     * @return:
     */
    @PostMapping("/upload")
    @ResponseBody
    //1.如何接收前端传递的文件数据  MultipartFile 对象
    public Result uploadFile(@RequestParam("file") MultipartFile imageFile)throws Exception{

        //将文件上传到服务器指定位置
        //1.先有一个目录存放文件，在系统中创建一个目录  C:\Users\Windows\Desktop\demo\img
        //2.将图片存储的目录位置放入到配置文件 application.yml
        /**
         * reggie:
         *   image:
         *     path: D:/BaiduNetdiskDownload/img
         */
        //3.从配置文件中将图片所存储目录读进来   @Value:可以从applicatoin.yml读取指定可以的value值

        //4. 将文件拷贝到指定的目录下
        //张三: 美女.jpg     李四: 美女.jpg    两个人都在上传， 不管谁把谁给覆盖，都不满足要求
        //4.1 将图片重新命名   美女.jpg       werqewrwerqwesadfa.jpg  wqeqweqefdsfadf.jpg
        String filename = imageFile.getOriginalFilename(); //文件的名称  美女.jpg   1.jpg
        String uuid = UUID.randomUUID().toString(); // uuid不重复  4e1e960d-2db8-11ed-ada5-f8e43b8321a7
        String ext = filename.substring(filename.lastIndexOf("."));//从filename最后一个点开始截取 ， .jpg
        String finalFilename = uuid+ext; //4e1e960d-2db8-11ed-ada5-f8e43b8321a7.jpg
        //  C:/Users/Windows/Desktop/demo/img/4e1e960d-2db8-11ed-ada5-f8e43b8321a7.jpg
        //将文件拷贝到指定位置
        imageFile.transferTo(new File(imgePath+"/"+finalFilename));

        return Result.success(finalFilename);
    }

    /**
     * 下载图片
     * @Param:
     * @return:
     */
    @GetMapping("/download")
    public void downloadFile(String name, HttpServletResponse response) throws Exception{
        //1.组装文件的具体位置
        String filepath = imgePath+"/"+name;
        //2.构建输入流
        InputStream in = new FileInputStream(filepath);
        if(in==null){
            throw new RuntimeException("文件不存在");
        }
        // http:  request:请求对象，封装了所有和请求相关的内容  response 响应对象，封装所有和响应相关的内容
        //3.设置响应的文件类型(MIME TYPE：image/jpeg)   jpg文件
        response.setContentType("image/jpeg");
        //4.构建输出流
        ServletOutputStream out = response.getOutputStream();

        //5.  将输入流的内容写入到输出流， 输出流响应到前端，前端使用 <img/>标签就能将图片展示在页面上。

        // commmons-io包下的工具类 IOUtils
            //5.1 pom.xml添加对应的依赖
            /**
             *         <dependency>
             *             <groupId>org.apache.commons</groupId>
             *             <artifactId>commons-io</artifactId>
             *             <version>1.3.2</version>
             *         </dependency>
             */
            //5.2使用IOUtils工具类
        IOUtils.copy(in,out);
        //6.关闭流
        out.close();
        in.close();
    }
}
