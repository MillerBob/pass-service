package com.pass.service.common.util;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pass.service.common.exception.BizExceptionEnum;
import com.pass.service.common.exception.BussinessException;

public class FileUtil {

    private static Logger log = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 判断路径是否存在，如果不存在则创建
     * 
     * 
     * @param dir
     */
    public static void mkdirs(String dir) {
        if (StringUtils.isEmpty(dir)) {
            return;
        }

        File file = new File(dir);
        if (file.isDirectory()) {
            return;
        } else {
            file.mkdirs();
        }
    }


    /**
     * NIO way
     */
    public static byte[] toByteArray(String filename) {

        File f = new File(filename);
        if (!f.exists()) {
            log.error("文件未找到！" + filename);
            throw new BussinessException(BizExceptionEnum.FILE_NOT_FOUND);
        }
        FileChannel channel = null;
        FileInputStream fs = null;
        try {
            fs = new FileInputStream(f);
            channel = fs.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
            while ((channel.read(byteBuffer)) > 0) {
                // do nothing
                // System.out.println("reading");
            }
            return byteBuffer.array();
        } catch (IOException e) {
            throw new BussinessException(BizExceptionEnum.FILE_READING_ERROR);
        } finally {
            try {
                channel.close();
            } catch (IOException e) {
                throw new BussinessException(BizExceptionEnum.FILE_READING_ERROR);
            }
            try {
                fs.close();
            } catch (IOException e) {
                throw new BussinessException(BizExceptionEnum.FILE_READING_ERROR);
            }
        }
    }
    
    /* @param instreams 二进制流
     * @param imgPath 图片的保存路径
     * @param imgName 图片的名称
     * @return
     *      1：保存正常
     *      0：保存失败
     */
    public static int saveToImgByInputStream(InputStream instreams,String imgPath,String imgName) throws FileNotFoundException{
   
        int stateInt = 1;
        File file=new File(imgPath,imgName);//可以是任何图片格式.jpg,.png等
        FileOutputStream fos=new FileOutputStream(file);
        if(instreams != null){
            try {
                                  
                byte[] b = new byte[1024];
                int nRead = 0;
                while ((nRead = instreams.read(b)) != -1) {
                    fos.write(b, 0, nRead);
                }
                               
            } catch (Exception e) {
                stateInt = 0;
                throw new BussinessException(BizExceptionEnum.FILE_READING_ERROR);
            } finally {
                try {
                    fos.flush();
                    fos.close();
             } catch (IOException e) {
                 e.printStackTrace();
             } 
            }
        }
        return stateInt;
    }  

    public static String subFileName(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }

        String fileName = name.substring(0, name.indexOf("."));
        return fileName;
    }
    
	// 下载图片到本地
	public static void downloadPicture(String urlString, String newName) {
		URL url = null;
		try {
			url = new URL(urlString);
			DataInputStream dataInputStream = new DataInputStream(url.openStream());
			FileOutputStream fileOutputStream = new FileOutputStream(new File(newName));

			byte[] buffer = new byte[1024];
			int length;

			while ((length = dataInputStream.read(buffer)) > 0) {
				fileOutputStream.write(buffer, 0, length);
			}

			dataInputStream.close();
			fileOutputStream.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		FileUtil.downloadPicture("http://p2-q.mafengwo.net/s1/M00/25/D8/wKgBm04WS8bW8tYQAAP8w-Lp-eM61.jpeg?imageMogr2%2Fthumbnail%2F%21690x370r%2Fgravity%2FCenter%2Fcrop%2F%21690x370%2Fquality%2F100", "D:\\myimage.jpg");
	}
}
