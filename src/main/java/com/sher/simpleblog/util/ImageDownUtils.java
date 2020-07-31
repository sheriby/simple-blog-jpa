package com.sher.simpleblog.util;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;

/**
 * @Title ImageDownUtils
 * @Package com.sher.simpleblog.util
 * @Description download cover image and compress it
 * @Author sher
 * @Date 2020/07/27 4:33 PM
 */
@Component
public class ImageDownUtils {

    @Value(value = "${images.uploadPath}")
    private String uploadPath;

    public InputStream getImageStream(String urlstr) {
        InputStream inputStream = null;
        BufferedInputStream bufferedInputStream = null;
        try {
            URL url = new URL(urlstr);
            URLConnection connection = url.openConnection();
            inputStream = connection.getInputStream();

            bufferedInputStream = new BufferedInputStream(Objects.requireNonNull(inputStream));
            return bufferedInputStream;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bufferedInputStream;
    }

    public void downloadImage(String urlstr, String dest) {
        File file = new File(dest);
        if (file.exists()) {
            return;
        }

        BufferedInputStream bufferedInputStream = (BufferedInputStream) getImageStream(urlstr);
        BufferedOutputStream bufferedOutputStream = null;

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bufferedOutputStream = new BufferedOutputStream(fileOutputStream);

            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = bufferedInputStream.read(buffer)) != -1) {
                bufferedOutputStream.write(buffer, 0, len);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
                if (bufferedOutputStream != null) {
                    bufferedOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String downloadImage(String urlstr) {
        String[] split = urlstr.split("/");
        String filename = split[split.length - 1];
        String dest = uploadPath + "cover/" + filename;
        downloadImage(urlstr, dest);
        return "/cover/" + filename;
    }


    public void compressImg(URL url, String dest) {
        try {
            Thumbnails.of(url).scale(1).outputQuality(0.5f).toFile(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String DownloadAndCompressImg(String urlstr) {
        String[] split = urlstr.split("/");
        String filename = split[split.length - 1];
        if (filename.endsWith("png")) {
            int length = filename.length();
            filename = filename.substring(0, length - 3) + "jpg";
        }
        try {
            String dest = uploadPath + "cover/" + filename;
            compressImg(new URL(urlstr), dest);
            return "/cover/" + filename;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
