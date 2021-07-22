package dev.bodner.jack.unlimited.mixin;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.bodner.jack.unlimited.json.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.worldselection.WorldSelectionList;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.LevelSummary;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;

@Mixin(WorldSelectionList.class)
public abstract class RegisterAlt {

    private final Minecraft minecraft = Minecraft.getInstance();


    @Inject(method = "setSelected", at = @At(value = "TAIL"))
    private void thing(WorldSelectionList.WorldListEntry worldListEntry, CallbackInfo ci) throws NoSuchFieldException, IllegalAccessException, IOException {
        Field field = WorldSelectionList.WorldListEntry.class.getDeclaredField("summary");
        field.setAccessible(true);
        LevelSummary summary = (LevelSummary) field.get(worldListEntry);
        LevelStorageSource.LevelStorageAccess levelStorageAccess = this.minecraft.getLevelSource().createAccess(summary.getLevelId());
        File packsDir =  levelStorageAccess.getLevelPath(LevelResource.DATAPACK_DIR).toFile();
        JsonParser parser = new JsonParser();
        for (File pack : packsDir.listFiles()){
            System.out.println(pack);
            if (pack.isDirectory()){
                for (File packFile : pack.listFiles()){
                    System.out.println(packFile);
                    if (packFile.getName().equals("pack.mcmeta")){
                        JsonObject meta = parser.parse(new FileReader(packFile)).getAsJsonObject();
                        System.out.println(meta);
                        if (meta.get("pack").getAsJsonObject().has("object_mode") && meta.get("pack").getAsJsonObject().get("object_mode").getAsBoolean()){
                            System.out.println("true");
                            File blocks = new File(pack, "objects" + File.separator + "block");
                            System.out.println(blocks);
                            if (blocks.exists()) {
                                for (File file : (blocks.listFiles())) {
                                    System.out.println(file);
                                    JsonObject obj = parser.parse(new FileReader(file)).getAsJsonObject();
                                    Util.blockFromJson(obj);
                                }
                            }
                        }
                        System.out.println("false");
                    }
                }
            }
        }
    }
}
