package dev.bodner.jack.unlimited.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

import java.lang.reflect.Field;

public class Util {

    //todo: finish adding block properties
    public static void blockFromJson(JsonObject json) {
        ResourceLocation resourceLocation;
        BlockBehaviour.Properties properties;

        Material material = Material.STONE;
        MaterialColor materialColor;
        float hardness;
        float blastResistance;
        boolean instantBreak;
        boolean conductsRedstone;
        boolean suffocates;
        boolean validSpawn;
        boolean blocksView;
        float jumpFactor;
        float lightLevel;
        boolean noCollision;
        boolean noDrops;
        boolean noOcclusion;
        boolean requiresTool;
        SoundType soundType;
        float speedFactor;

        try{
            if (json.get("name") == null){
                throw new Exception("Field \"name\" is required");
            }
            else {
                String[] name = json.get("name").getAsString().split(":");
                if (name.length != 2){
                    throw new Exception("\"name\" must be a namespace and a name separated by :");
                }
                else {
                    resourceLocation = new ResourceLocation(name[0], name[1]);
                }
            }

            JsonElement baseJson = json.get("base");
            if (baseJson != null) {
                try {
                    Field field = Material.class.getField(baseJson.getAsString());
                    material = (Material) field.get(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            properties = BlockBehaviour.Properties.of(material);

            JsonElement colorJson = json.get("map_color");
            if (colorJson != null){
                try{
                    Field field = MaterialColor.class.getField(colorJson.getAsString());
                    materialColor = (MaterialColor) field.get(null);
                    properties.color(materialColor);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            JsonElement hardnessJson = json.get("hardness");
            if (hardnessJson != null){
                try{
                    hardness = hardnessJson.getAsFloat();
                    properties.destroyTime(hardness);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            JsonElement blastResJson = json.get("blast_resist");
            try{
                blastResistance = blastResJson.getAsFloat();
                properties.explosionResistance(blastResistance);
            } catch (Exception e){
                e.printStackTrace();
            }


            Block block = new Block(properties);

            Registry.register(Registry.BLOCK, resourceLocation, block);
            Registry.register(Registry.ITEM, resourceLocation, new BlockItem(block, new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
