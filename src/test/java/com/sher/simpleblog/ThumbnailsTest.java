package com.sher.simpleblog;

import net.coobird.thumbnailator.Thumbnails;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Title ThumbnailsTest
 * @Package com.sher.simpleblog
 * @Description TODO
 * @Author sher
 * @Date 2020/07/31 10:06 AM
 */
public class ThumbnailsTest {

    public static void main(String[] args) throws IOException {
        InputStream pic =
                Thumbnails.class.getClassLoader().getResourceAsStream("static/images/login.jpg");
        Thumbnails.of(pic)
                .scale(1)
                .outputQuality(0.5)
                .toFile("50.jpg");
    }
}
