package net.vvxzv.tfcadvancedcrucible.datagen.tfcdata;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

public interface FinishedTFCData {
    void serializeData(JsonObject pJson);

    default JsonObject serialize() {
        JsonObject jsonobject = new JsonObject();
        this.serializeData(jsonobject);
        return jsonobject;
    }

    ResourceLocation getId();
}
