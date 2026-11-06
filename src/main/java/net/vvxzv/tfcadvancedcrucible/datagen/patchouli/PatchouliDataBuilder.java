package net.vvxzv.tfcadvancedcrucible.datagen.patchouli;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PatchouliDataBuilder implements FinishedPatchouliData {
    private final String modid;
    private final String categoryName;
    private CategoryBuilder categoryBuilder = null;
    private final List<EntriesBuilder> listOfEntries = new ArrayList<>();

    public PatchouliDataBuilder(ResourceLocation category) {
        this.modid = category.getNamespace();
        this.categoryName = category.getPath();
    }

    public PatchouliDataBuilder category(String title, String description, ItemLike icon, int sortnum) {
        this.categoryBuilder = new CategoryBuilder(title, description, icon, sortnum);
        return this;
    }

    public PatchouliDataBuilder addEntry(EntriesBuilder builder) {
        this.listOfEntries.add(builder);
        return this;
    }

    @Override
    public void serializeCategoriesData(JsonObject pJson) {
        if(this.categoryBuilder != null) {
            pJson.addProperty("name", this.categoryBuilder.name);
            pJson.addProperty("description", this.categoryBuilder.description);
            pJson.addProperty("icon", BuiltInRegistries.ITEM.getKey(this.categoryBuilder.icon.asItem()).toString());
            pJson.addProperty("sortnum", this.categoryBuilder.sortnum);
        }
    }

    @Override
    public void serializeEntriesData(Map<String, JsonObject> map) {
        if(!this.listOfEntries.isEmpty()) {
            for (EntriesBuilder builder: this.listOfEntries) {
                JsonObject object = new JsonObject();
                object.addProperty("category", this.modid + ":" + this.categoryName);
                object.addProperty("name", builder.getName());
                object.addProperty("icon", BuiltInRegistries.ITEM.getKey(builder.icon.asItem()).toString());
                object.add("pages", builder.getPages());
                map.put(builder.getLocationName(), object);
            }
        }
    }

    @Override
    public String getModId() {
        return this.modid;
    }

    @Override
    public String getCategoryName() {
        return this.categoryName;
    }

    public record CategoryBuilder(String name, String description, ItemLike icon, int sortnum) {
    }

    public static class EntriesBuilder {
        private final String name;
        private final String title;
        private final ItemLike icon;
        private String category;
        private final JsonArray pages = new JsonArray();
        private boolean read_by_default = true;

        public EntriesBuilder(ResourceLocation nameLocation, String title, ItemLike icon) {
            this.name = nameLocation.getPath();
            this.title = title;
            this.icon = icon;
        }

        public String getName() {
            return this.title;
        }

        public String getLocationName() {
            return this.name;
        }

        public ItemLike getIcon() {
            return this.icon;
        }


        public String getCategory() {
            return category;
        }

        public EntriesBuilder setCategory(String category) {
            this.category = category;
            return this;
        }

        public JsonArray getPages() {
            return pages;
        }

        public EntriesBuilder setReadByDefault(boolean b) {
            this.read_by_default = b;
            return this;
        }

        public boolean isReadByDefault() {
            return read_by_default;
        }

        public EntriesBuilder addTextPage(Text text) {
            JsonObject object = new JsonObject();
            object.addProperty("type", "patchouli:text");
            object.addProperty("text", text.getText());
            this.pages.add(object);
            return this;
        }

        public EntriesBuilder addTextPage(String title, Text text) {
            JsonObject object = new JsonObject();
            object.addProperty("type", "patchouli:text");
            object.addProperty("title", title);
            object.addProperty("text", text.getText());
            this.pages.add(object);
            return this;
        }

        public EntriesBuilder addImagePage(String text, List<ResourceLocation> pngs, boolean border) {
            JsonObject object = new JsonObject();
            object.addProperty("type", "patchouli:image");
            object.addProperty("text", text);
            object.addProperty("border", border);
            JsonArray array = new JsonArray();
            for (ResourceLocation png: pngs) {
                array.add(png.toString());
            }
            object.add("images", array);
            this.pages.add(object);
            return this;
        }

        public EntriesBuilder addMultiblockPage(String name, String text, List<List<String>> patterns, Map<Character, BlockState> key) {
            JsonObject object = new JsonObject();
            object.addProperty("type", "patchouli:multiblock");
            object.addProperty("name", name);
            object.addProperty("text", text);

            JsonObject multiblockObject = new JsonObject();

            JsonArray patternArray = new JsonArray();
            for (List<String> pattern: patterns) {
                JsonArray stringArray = new JsonArray();
                for (String string: pattern) {
                    stringArray.add(string);
                }
                patternArray.add(stringArray);
            }
            multiblockObject.add("pattern", multiblockObject);

            JsonObject keyObject = new JsonObject();
            for(Map.Entry<Character, BlockState> entry : key.entrySet()) {
                keyObject.addProperty(String.valueOf(entry.getKey()), entry.getValue().toString());
            }

            multiblockObject.add("mapping", keyObject);
            object.add("multiblock", multiblockObject);

            this.pages.add(object);
            return this;
        }
    }

    public static class Text {
        private String text = "";

        public static Text of() {
            return new Text();
        }

        public static Text of(String text) {
            return of().add(text);
        }

        public Text add(String text) {
            this.text += text;
            return this;
        }

        public Text addThing(String text) {
            this.text += this.format(text, "thing");
            return this;
        }

        public Text addChatFormatting(String text, ChatFormatting format) {
            String code = String.valueOf(format.getChar());
            this.text += this.format(text, code);
            return this;
        }

        public Text br() {
            this.text += "$(br)";
            return this;
        }

        public Text br2() {
            this.text += "$(br2)";
            return this;
        }

        private String format(String text, String format) {
            return "$("+ format + ")" + text + "$()";
        }

        public String getText() {
            return this.text;
        }
    }
}
