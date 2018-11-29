package engine;

import engine.model.Texture;
import utils.TextureLoader;

import java.io.File;
import java.util.EnumMap;
import java.util.HashMap;
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
        DISPLACEMENT,
        LUT
    }
    public enum LUT{
        SUNNY,
        DARK
    }
    private static Texture activeLUT;

    private static Map<RenderSetting, Boolean> settings = new EnumMap<>(RenderSetting.class);
    private static Map<LUT, Texture> luts =  new EnumMap<>(LUT.class);
    static {
        settings.put(RenderSetting.LIGHT, true);
        settings.put(RenderSetting.SSAO, true);
        settings.put(RenderSetting.SHADOWS, true);
        settings.put(RenderSetting.GODRAYS, true);
        settings.put(RenderSetting.DISPLACEMENT, true);
        settings.put(RenderSetting.LUT, false);
        try {
            luts.put(LUT.SUNNY, TextureLoader.loadTexture(new File("textures/lut2.png")));
            luts.put(LUT.DARK, TextureLoader.loadTexture(new File("textures/lut3.png")));
            activeLUT = luts.get(LUT.SUNNY);
        } catch (TextureLoader.TextureLoadException e) {
            e.printStackTrace();
        }
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

    public static Texture getActiveLUT() {
        return activeLUT;
    }

    public static void setLut(LUT lut){
        activeLUT = luts.get(lut);
    }
}
