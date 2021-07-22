package dev.bodner.jack.unlimited.packs;

import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.metadata.pack.PackMetadataSectionSerializer;

public class ExtendedPackMetadataSection extends PackMetadataSection {
    private final boolean objectMode;
    public static final PackMetadataSectionSerializer SERIALIZER = new ExtendedPackMetadataSectionSerializer();

    public ExtendedPackMetadataSection(Component component, int i) {
        super(component, i);
        this.objectMode = false;
    }

    public ExtendedPackMetadataSection(Component component, int i, boolean objectMode) {
        super(component, i);
        this.objectMode = objectMode;
    }

    public boolean isObjectMode() {
        return objectMode;
    }
}
