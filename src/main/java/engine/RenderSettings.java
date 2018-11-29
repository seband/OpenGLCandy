package engine;

import java.util.EnumMap;
import java.util.Map;

/**
 * Singleton Settings Handler
 */
public class RenderSettings {

    /**
     * Settings used for turning on and off renderings
     */
    public enum RenderSetting{
        LIGHT,
        SSAO,
        SHADOWS,
        GODRAYS,
        DISPLACEMENT
    }

    private static Map<RenderSetting, Boolean> settings = new EnumMap<>(RenderSetting.class);
    static {
        settings.put(RenderSetting.LIGHT, true);
        settings.put(RenderSetting.SSAO, true);
        settings.put(RenderSetting.SHADOWS, true);
        settings.put(RenderSetting.GODRAYS, true);
        settings.put(RenderSetting.DISPLACEMENT, true);
    }

    /**
     * Get render setting
     * @param setting to retrieve
     * @return value of setting
     */
    public static boolean getSetting(RenderSetting setting){
        return settings.get(setting);
    }

    /**
     * Update status of setting
     * @param setting to update
     * @param value new value of the setting
     */
    public static void setSetting(RenderSetting setting, boolean value){
        settings.replace(setting, value);
    }
}
