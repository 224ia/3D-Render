package Util;

public final class FpsCounter {
    private static long startTime = -1;
    private static int frames = 0;
    private static float changeTime = 0;
    private static final float FPS_UPDATE_TIME = 0.4f;

    private static float deltaTime;
    private static int fps;

    public static float getDeltaTime() {
        return deltaTime;
    }

    public static int getFps() {
        return fps;
    }

    private FpsCounter() {}

    public static void start(long time) {
        if (startTime == -1) {
            startTime = time;
            Logger.info("FpsCounter was started");
        } else {
            Logger.warn("FpsCounter has already been started");
        }
    }

    public static void fpsCount() {
        long now = System.nanoTime();
        deltaTime = (now - startTime) / 1_000_000_000f;
        changeTime += deltaTime;
        frames += 1;
        startTime = now;
        if (changeTime >= FPS_UPDATE_TIME) {
            fps = (int) (frames / changeTime);
            changeTime = 0;
            frames = 0;
        }
    }
}
