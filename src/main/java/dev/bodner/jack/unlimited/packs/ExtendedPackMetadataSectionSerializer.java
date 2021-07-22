package dev.bodner.jack.unlimited.packs;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.metadata.pack.PackMetadataSectionSerializer;
import net.minecraft.util.GsonHelper;

public class ExtendedPackMetadataSectionSerializer extends PackMetadataSectionSerializer {

    @Override
    public PackMetadataSection fromJson(JsonObject jsonObject) {
        Component component = Component.Serializer.fromJson(jsonObject.get("description"));
        if (component == null) {
            throw new JsonParseException("Invalid/missing description!");
        } else {
            int i = GsonHelper.getAsInt(jsonObject, "pack_format");
            if (jsonObject.get("object_mode") != null && jsonObject.get("object_mode").getAsBoolean()){
                return new ExtendedPackMetadataSection(component, i, true);
            }
            else {
                return new ExtendedPackMetadataSection(component, i);
            }
        }
    }
}
