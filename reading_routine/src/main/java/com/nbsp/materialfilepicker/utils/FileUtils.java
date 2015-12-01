package com.nbsp.materialfilepicker.utils;

import android.os.Environment;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Dimorinny on 24.10.15.
 */
public class FileUtils {
    public static List<File> getFileListByDirPath(String path) {
        File directory = new File(path);
        List<File> resultFiles = new ArrayList<>();

        // TODO: filter here
        File[] files = directory.listFiles();
        if (files != null && files.length > 0) {
            for (File f : files) {
                if (f.isHidden()) {
                    continue;
                }
                resultFiles.add(f);
            }

            Collections.sort(resultFiles, new FileComparator());
        }

        return resultFiles;
    }

    public static String cutLastSegmentOfPath(String path) {
        return path.substring(0, path.lastIndexOf("/"));
    }

    public static String getReadableFileSize(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    //获取APP路径
    public static String getAppPath() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return "";
        }
        File root = Environment.getExternalStorageDirectory();
        File file = new File(root.getAbsolutePath() + "/ReadingRoutine");
        if (!file.exists()) file.mkdir();
        return file.getAbsolutePath();
    }

    public static String getCachePath() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return "";
        }
        File file = new File(getAppPath() + "/cache");
        if (!file.exists()) file.mkdir();
        return file.getAbsolutePath();
    }
}
