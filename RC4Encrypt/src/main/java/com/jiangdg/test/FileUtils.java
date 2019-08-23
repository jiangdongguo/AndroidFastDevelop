package com.jiangdg.test;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/** 创建文件
 *
 * Created by jiangdongguo on 2019/8/18.
 */

public class FileUtils {

    private static BufferedOutputStream outputStream;

    public static void createfile(String path){
        File file = new File(path);
        if(file.exists()){
            file.delete();
        }
        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(file));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void releaseFile(){
        try {
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void putFileStream(byte[] data,int offset,int length){
        if(outputStream != null) {
            try {
                outputStream.write(data,offset,length);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void putFileStream(byte[] data){
        if(outputStream != null) {
            try {
                outputStream.write(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
