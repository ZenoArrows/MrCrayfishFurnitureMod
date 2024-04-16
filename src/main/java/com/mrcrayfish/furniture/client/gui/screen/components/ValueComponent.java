package com.mrcrayfish.furniture.client.gui.screen.components;

import com.mrcrayfish.furniture.tileentity.IValueContainer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;

import java.awt.*;

/**
 * Author: MrCrayfish
 */
public abstract class ValueComponent
{
    protected String id;
    protected String name;
    protected Font font;

    public ValueComponent(String id, String name, Font font)
    {
        this.id = id;
        this.name = name;
        this.font = font;
    }

    public void render(GuiGraphics graphics, int x, int y, int mouseX, int mouseY, float partialTicks)
    {
        graphics.drawString(font, name, x, y, Color.WHITE.getRGB());
    }

    public abstract AbstractWidget getWidget();

    public String getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public abstract String getValue();

    public abstract IValueContainer.Entry toEntry();
}
