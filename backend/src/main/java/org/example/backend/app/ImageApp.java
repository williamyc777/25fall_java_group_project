package org.example.backend.app;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * 该类用于处理图片上传下载相关的请求
 * @author Shinomiya
 * @version 1.0
 */
@RestController
@RequestMapping("/image")
public class ImageApp {
    private static final Logger logger = Logger.getLogger(ImageApp.class.getName());

    @Value("${image-server.path}")
    String basePath;

    // 使用相对路径，避免硬编码IP地址导致的连接超时
    private final String IMAGE_PATH = "/image/";

    /**
     * 上传图片
     * @param file 上传的图片文件
     * @return 上传成功后的图片url
     * @throws Exception 上传失败时抛出异常
     */
    @PostMapping("/upload")
    public String uploadImage(@RequestParam("file") MultipartFile file) throws Exception{
        File dir = new File(basePath);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new Exception("Failed to create directory");
            }
        }
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename = UUID.randomUUID() + suffix;
        Path path = Paths.get(basePath, filename);
        logger.info("Image saved, url: " + path);
        file.transferTo(path.toAbsolutePath());
        return IMAGE_PATH + filename;
    }

    /**
     * 下载图片
     * @param fileURL 图片url
     * @param response HttpServletResponse
     * @throws Exception 下载失败时抛出异常
     */
    @GetMapping("/download")
    public void downloadImage(@RequestParam("url") String fileURL, HttpServletResponse response) throws Exception {
        File file = Paths.get(basePath, fileURL).toFile();
        //输入流，通过输入流读取文件内容
        FileInputStream fileInputStream = new FileInputStream(file);
        //输出流，通过输出流将文件写回浏览器，在浏览器展示图片
        ServletOutputStream outputStream = response.getOutputStream();
        response.setContentType("image/" + fileURL.substring(fileURL.lastIndexOf(".") + 1));

        int len;
        byte[] bytes = new byte[1024];
        while((len = fileInputStream.read(bytes))!= -1){
            outputStream.write(bytes,0,len);
            outputStream.flush();
        }
        //关闭资源
        outputStream.close();
        fileInputStream.close();
    }

    /**
     * 通过url下载图片
     * @param url 图片文件名（路径变量）
     * @param response HttpServletResponse
     * @throws Exception 下载失败时抛出异常
     */
    @GetMapping("{url:.+}")
    public void getImage(@PathVariable String url, HttpServletResponse response) throws Exception {
        // URL解码，处理空格等特殊字符
        try {
            url = URLDecoder.decode(url, StandardCharsets.UTF_8);
        } catch (Exception e) {
            logger.warning("Failed to decode URL: " + url);
        }
        
        // 移除查询参数（如 ?t=1234567890）
        int queryIndex = url.indexOf('?');
        if (queryIndex >= 0) {
            url = url.substring(0, queryIndex);
        }
        
        logger.info("Getting image: " + url + ", basePath: " + basePath);
        
        // 从文件系统中读取图片
        File file = Paths.get(basePath, url).toFile();
        logger.info("Image file path: " + file.getAbsolutePath() + ", exists: " + file.exists());
        if (!file.exists() || !file.isFile()) {
            logger.warning("Image not found: " + file.getAbsolutePath());
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("Image not found: " + url);
            return;
        }
        
        // 输入流，通过输入流读取文件内容
        FileInputStream fileInputStream = new FileInputStream(file);
        // 输出流，通过输出流将文件写回浏览器，在浏览器展示图片
        ServletOutputStream outputStream = response.getOutputStream();
        
        // 根据文件扩展名设置Content-Type
        String contentType = "image/jpeg"; // 默认类型
        int lastDotIndex = url.lastIndexOf(".");
        if (lastDotIndex > 0 && lastDotIndex < url.length() - 1) {
            String extension = url.substring(lastDotIndex + 1).toLowerCase();
            if (extension.equals("jpg") || extension.equals("jpeg")) {
                contentType = "image/jpeg";
            } else if (extension.equals("png")) {
                contentType = "image/png";
            } else if (extension.equals("gif")) {
                contentType = "image/gif";
            } else if (extension.equals("webp")) {
                contentType = "image/webp";
            } else {
                contentType = "image/" + extension;
            }
        }
        response.setContentType(contentType);

        int len;
        byte[] bytes = new byte[1024];
        while((len = fileInputStream.read(bytes))!= -1){
            outputStream.write(bytes,0,len);
            outputStream.flush();
        }
        //关闭资源
        outputStream.close();
        fileInputStream.close();
    }

}
