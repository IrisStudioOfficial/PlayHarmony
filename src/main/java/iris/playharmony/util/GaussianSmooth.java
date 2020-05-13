package iris.playharmony.util;

public class GaussianSmooth {

    public static void gaussianSmooth(float[] src, float[] dest) {

        for(int i = 0;i < src.length;i++) {

            float value = src[i];

            float prev = i == 0 ? value : src[i - 1];

            float next = i == src.length - 1 ? value : src[i + 1];

            float smoothed = getAverage(prev, value, next);

            dest[i] = smoothed;
        }

    }

    private static float getAverage(float... values) {

        float sum = 0.0f;

        for(int i = 0;i < Math.min(10, values.length);i++) {
            sum += values[i];
        }

        return sum / values.length;
    }

}
