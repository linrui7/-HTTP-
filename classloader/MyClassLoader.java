package com.github7.classloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MyClassLoader extends ClassLoader {
    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {

        try {
                return super.findClass(name);
            } catch (ClassNotFoundException e) {

            //1.根据类名称找到name对应的.class文件
            //String fileLocation =  "F:\\\\LRhttp\\webapps\\WEB-INF\\classes\\"+name.replace("/","")+".class";
            String fileLocation = "F:\\\\httpProject\\target\\classes\\" + name.replace("/", "") + ".class";
            File file = new File(fileLocation);
            if (file.exists()) {
                //2.读取文件内容。
                byte[] buf = new byte[8192];
                int len = 0;
                try {
                    len = new FileInputStream(fileLocation).read(buf);
                } catch (IOException e1) {
                   throw new ClassNotFoundException("文件出错",e);
                }
                //3.调用defineClass 转为Class<?>
                System.out.println("类名" + name.replaceAll("/", ""));
                return defineClass(name.replaceAll("/", ""), buf, 0, len);
            } else {
               throw new ClassNotFoundException("类没找到");
            }
        }
    }
}