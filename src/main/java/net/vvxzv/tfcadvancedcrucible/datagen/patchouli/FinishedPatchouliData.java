package net.vvxzv.tfcadvancedcrucible.datagen.patchouli;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public interface FinishedPatchouliData {
    void serializeCategoriesData(JsonObject pJson);

    void serializeEntriesData(Map<String, JsonObject> map);

    default JsonObject serializeCategories() {
        JsonObject jsonobject = new JsonObject();
        this.serializeCategoriesData(jsonobject);
        return jsonobject;
    }

    default Map<String, JsonObject> serializeEntries() {
        Map<String, JsonObject> map = new HashMap<>();
        this.serializeEntriesData(map);
        return map;
    }

    String getModId();
    String getCategoryName();
}
