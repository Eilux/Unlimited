package dev.bodner.jack.unlimited.mixin;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.bodner.jack.unlimited.json.Util;
import dev.bodner.jack.unlimited.packs.ExtendedPackMetadataSection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.AbstractPackResources;
import net.minecraft.server.packs.FilePackResources;
import net.minecraft.server.packs.FolderPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.world.level.DataPackConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;

@Mixin(MinecraftServer.class)
public abstract class RegisterBlocks {

    @Inject(method = "getSelectedPacks", at = @At(value = "TAIL"))
    private static void PackMixin(PackRepository packRepository, CallbackInfoReturnable<DataPackConfig> cir) throws IOException, IllegalAccessException, NoSuchFieldException {
        JsonParser parser = new JsonParser();
        for (Pack pack : packRepository.getSelectedPacks()){
            ExtendedPackMetadataSection meta = (ExtendedPackMetadataSection) pack.open().getMetadataSection(ExtendedPackMetadataSection.SERIALIZER);
            if (meta != null && meta.isObjectMode()){
                Field field = AbstractPackResources.class.getDeclaredField("file");
                field.setAccessible(true);
                if (pack.open() instanceof FilePackResources file){

                }
                else if (pack.open() instanceof FolderPackResources folder){
                    File blocks = new File(((File) field.get(folder)), "objects" + File.separator + "block");
                    File items = new File(((File) field.get(folder)), "objects" + File.separator + "item");

                    if (blocks.exists()) {
                        for (File file : (blocks.listFiles())) {
                            JsonObject obj = parser.parse(new FileReader(file)).getAsJsonObject();
                            Util.blockFromJson(obj);
                        }
                    }

                    //todo: add json items
                    if (items.exists()){
                        for (File file : items.listFiles()) {

                        }
                    }
                }
            }
        }
    }
}
