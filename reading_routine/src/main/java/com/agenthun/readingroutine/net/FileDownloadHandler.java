package com.agenthun.readingroutine.net;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.loopj.android.http.BinaryHttpResponseHandler;
import com.nbsp.materialfilepicker.utils.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import cz.msebera.android.httpclient.Header;

/**
 * @project ReadingRoutine
 * @authors agenthun
 * @date 15/11/30 上午11:07.
 */
public abstract class FileDownloadHandler extends BinaryHttpResponseHandler {
    public FileDownloadHandler(String[] allow) {
        super(allow);
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] binaryData) {
        String tempPath = FileUtils.getCachePath() + "/temp.jpg";
        File file = new File(tempPath);
        Bitmap bmp = BitmapFactory.decodeByteArray(binaryData, 0, binaryData.length);
        Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
        int quality = 100; //压缩比率
        try {
            if (file.exists()) file.delete();
            file.createNewFile();
            OutputStream stream = new FileOutputStream(file);
            bmp.compress(format, quality, stream);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        onDownloadSuccess();
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] binaryData, Throwable error) {
        String tempPath = FileUtils.getCachePath() + "/temp.jpg";
        File file = new File(tempPath);
        if (file.exists()) file.delete();
        onDownloadFailure();
    }

    public abstract void onDownloadSuccess();

    public abstract void onDownloadFailure();
}
