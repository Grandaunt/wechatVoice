package www.branch.com.contentproviderdemo.Utils;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

/**
 * 工具类
 */
public class BmpToByteArray {
    /**
     * 将bmp转换为字节数组
     * @param bmp
     * @param needRecycle
     * @return
     */
    public static byte[] bmpToByteArray(final Bitmap bmp, boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}