package com.kawika.smart_survey.utils;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

/*
 * Created by 121 on 18-06-2016.
 */
public class TempFile {
    public static String createTemporaryFile(String part, String ext) {
        File tempDir = Environment.getExternalStorageDirectory();
        tempDir = new File(tempDir.getAbsolutePath() + "/.Test");
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }
        return tempDir + "/" + part + ext;
    }

    public static File getFilePath() throws Exception {
        final String s = createTemporaryFile("image", ".jpg");
        File photo = new File(s);
        if (photo.exists()) {
            photo.delete();
        }
        return photo;
    }
    public static String getFilePathImage(){
        String s = createTemporaryFile(getTime(), ".jpg");
        File photo = new File(s);
        if (photo.exists()) {
            photo.delete();
        }
        return s;
    }

    /**
     * @return current time in millisecond format
     */
    public static String getTime() {
        return String.valueOf(System.currentTimeMillis());
    }

    /**
     * image transfer
     * @param sourceImagePath
     * @param destinationImagePath
     * @return
     */
    public static boolean copyImage(String sourceImagePath, String destinationImagePath) {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            if (sd.canWrite()) {
//                String sourceImagePath = "/path/to/source/file.jpg";
//                String destinationImagePath = "/path/to/destination/file.jpg";
                File source = new File(data, sourceImagePath);
                File destination = new File(sd, destinationImagePath);
                if (source.exists()) {
                    FileChannel src = new FileInputStream(source).getChannel();
                    FileChannel dst = new FileOutputStream(destination).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}
