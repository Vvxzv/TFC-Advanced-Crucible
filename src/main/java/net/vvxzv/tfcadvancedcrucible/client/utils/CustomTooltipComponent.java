package net.vvxzv.tfcadvancedcrucible.client.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.ArrayList;
import java.util.List;

public abstract class CustomTooltipComponent {
    private int posX;
    private int posY;
    private final int xSize;
    private final int ySize;

    protected CustomTooltipComponent(int posX, int posY) {
        this(posX,posY,1,1);
    }

    protected CustomTooltipComponent(int posX, int posY, int xSize, int ySize){
        this.posX = posX;
        this.posY = posY;
        this.xSize = xSize;
        this.ySize = ySize;
    }

    public abstract Component[] getComponents();
    public abstract boolean hasTooltip();

    public void draw(GuiGraphics guiGraphics, int guiX, int guiY, int mouseX, int mouseY) {
        Font font = Minecraft.getInstance().font;
        List<FormattedCharSequence> list = new ArrayList<>();
        if(!hasTooltip()){
            return;
        }

        if(isMouseOver(guiX,guiY,mouseX,mouseY)) {
            for (Component component : getComponents()) {
                list.add(component.getVisualOrderText());
            }
            guiGraphics.renderTooltip(font,list,mouseX,mouseY);
        }
    }

    public boolean isMouseOver(int guiX, int guiY, double mouseX, double mouseY) {
        return mouseX >= guiX + posX && mouseX <= guiX + posX + xSize && mouseY >= guiY + posY && mouseY <= guiY + posY + ySize;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }
}
