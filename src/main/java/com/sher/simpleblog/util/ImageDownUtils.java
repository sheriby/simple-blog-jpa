package com.sher.simpleblog.util;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;

/**
 * @Title ImageDownUtils
 * @Package com.sher.simpleblog.util
 * @Description download cover image
 * @Author sher
 * @Date 2020/07/27 4:33 PM
 */
public class ImageDownUtils {

    private static String uploadPath = "/home/sher/upload/cover/";

    public static void downloadImage(String urlstr, String dest) {
        File file = new File(dest);
        if (file.exists()) {
            return;
        }

        InputStream inputStream = null;
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;

        try {
            URL url = new URL(urlstr);
            URLConnection connection = url.openConnection();
            inputStream = connection.getInputStream();

            bufferedInputStream = new BufferedInputStream(Objects.requireNonNull(inputStream));

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

    public static String downloadImage(String urlstr) {
        String[] split = urlstr.split("/");
        String filename = split[split.length - 1];
        String dest = uploadPath + filename;
        downloadImage(urlstr, dest);
        return "cover/" + filename;
    }
}
