package ssn.codebreakers.pecsinstructor.helpers;

import android.graphics.ImageFormat;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.util.Range;
import android.util.Size;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CameraUtils {
    public static String getFrontCameraId(CameraManager manager)
    {
        try
        {
            String cameraIDs[] = manager.getCameraIdList();
            for (String cameraId : cameraIDs)
            {
                CameraCharacteristics cameraCharacteristics = manager.getCameraCharacteristics(cameraId);
                if (cameraCharacteristics != null && cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT)
                    return cameraId;
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static Size[] getPreviewSizes(CameraCharacteristics characteristics)
    {
        Size jpegSizes[] = null;
        try{
            if(characteristics!=null)
            {
                jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.JPEG);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return jpegSizes;
    }

    public static Size chooseVideoSize(Size[] choices, int resolution, double aspectRatio)
    {
        List<Size> sameAspectRatio = new ArrayList<>();
        List<Size> anyAspectRatio = new ArrayList<>();
        for(int i=0; i<choices.length; i++)
        {
            Size size = new Size(choices[i].getHeight(), choices[i].getWidth());
            if (size.getWidth() <= resolution)
            {
                anyAspectRatio.add(size);
                if(size.getWidth() == size.getHeight() * aspectRatio)
                    sameAspectRatio.add(size);
            }
        }
        if(!sameAspectRatio.isEmpty())
            return Collections.max(sameAspectRatio, new CompareSizesByArea());
        if(!anyAspectRatio.isEmpty())
            return Collections.max(anyAspectRatio, new CompareSizesByArea());
        return choices[choices.length - 1];
    }

    public static Size chooseOptimalSize(Size[] choices, Size aspectRatio, int resolution)
    {
        // Collect the supported resolutions that are at least as big as the preview Surface
        List<Size> exactAspectRatio = new ArrayList<>();
        List<Size> anyAspectRatio = new ArrayList<>();
        int height = aspectRatio.getWidth();
        int width = aspectRatio.getHeight();
        for (Size option : choices)
        {
            //option = new Size(option.getHeight(), option.getWidth());
            System.out.println("available "+ option.getWidth()+":"+option.getHeight());
            if (option.getWidth() <= width && option.getHeight() <= height)
            {
                anyAspectRatio.add(option);
                if(option.getHeight() == option.getWidth() * height / width && option.getHeight() <= resolution)
                    exactAspectRatio.add(option);
            }
        }
        System.out.println("exact count" +exactAspectRatio.size());
        System.out.println("anyAspectRatio count" +anyAspectRatio.size());
        // Pick the smallest of those, assuming we found any
        if (!exactAspectRatio.isEmpty())
            return Collections.max(exactAspectRatio, new CompareSizesByArea());
        if (!anyAspectRatio.isEmpty())
            return Collections.max(anyAspectRatio, new CompareSizesByArea());
        return choices[0];
    }

    public static Range chooseFPS(Range<Integer>[] ranges)
    {
        if(ranges == null || ranges.length <= 0)
            return null;
        int targetFPS = 24;
        for (Range<Integer> r : ranges)//for some devices fps is multiples of 1000
        {
            if(r.getUpper()>1000)
                targetFPS = targetFPS * 1000;
        }

        Range<Integer> bestRange = ranges[0];
        for (Range<Integer> r : ranges)
        {
            System.out.println("available fps "+r.getLower()+":"+r.getUpper());
            int upper = r.getUpper();
            int lower = r.getLower();
            if (Math.abs(upper - targetFPS) <= Math.abs(bestRange.getUpper() - targetFPS))
            {
                bestRange = r;
            }
            if(targetFPS>=lower && targetFPS <= upper)
                return new Range<>(targetFPS, targetFPS);
        }
        return bestRange;
    }

    //compare based on area
    static class CompareSizesByArea implements Comparator<Size>
    {
        @Override
        public int compare(Size lhs, Size rhs) {
            // We cast here to ensure the multiplications won't overflow
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() - (long) rhs.getWidth() * rhs.getHeight());
        }

    }
}
