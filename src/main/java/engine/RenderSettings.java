package engine;

import java.util.EnumMap;
import java.util.Map;

/**
 * Singleton Settings Handler
 */
public class RenderSettings {

    public enum RenderSetting{
        LIGHT,
        SSAO,
        SHADOWS,
        GODRAYS
    }

    private static Map<RenderSetting, Boolean> settings = new EnumMap<>(RenderSetting.class);
    static {
        settings.put(RenderSetting.LIGHT, true);
        settings.put(RenderSetting.SSAO, true);
        settings.put(RenderSetting.SHADOWS, true);
        settings.put(RenderSetting.GODRAYS, true);
    }

    /**
     * Get render setting
     * @param setting to retrieve
     * @return value of setting
     */
    public static boolean getSetting(RenderSetting setting){
        return settings.get(setting);
    }

    public static void setSetting(RenderSetting setting, boolean value){
        settings.replace(setting, value);
    }
}
