package com.github7.classloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ClassLoader extends java.lang.ClassLoader {
    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        //1.根据类名称找到name对应的.class文件
        String fileLocation =  "F:\\\\LRhttp\\webapps\\WEB-INF\\classes\\"+name.replace("/","")+".class";
        File file = new File(fileLocation);
        if (file.exists()) {
            //2.读取文件内容。
            byte[] buf = new byte[8192];
            int len = 0;
            try {
                len = new FileInputStream(fileLocation).read(buf);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //3.调用defineClass 转为Class<?>
            return defineClass(name.replaceAll("/",""), buf, 0, len);
        } else {
            System.out.println("动态类，文件没有找到");
            return null;
        }
    }
}