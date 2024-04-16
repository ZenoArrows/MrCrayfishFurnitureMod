package com.mrcrayfish.furniture.client.gui.screen.components;

import com.mrcrayfish.furniture.client.gui.screen.EditValueContainerScreen;
import com.mrcrayfish.furniture.tileentity.IValueContainer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

/**
 * Author: MrCrayfish
 */
public class ToggleComponent extends ValueComponent
{
    private Button button;
    private boolean state;

    public ToggleComponent(IValueContainer.Entry entry, Font font)
    {
        super(entry.getId(), entry.getName(), font);
        button = Button.builder(Component.translatable("cfm.gui.off"), button -> this.setState(!state))
                .width(EditValueContainerScreen.WIDTH - EditValueContainerScreen.PADDING * 2).build();
        this.setState(Boolean.valueOf(entry.getValue()));
    }

    @Override
    public void render(GuiGraphics graphics, int x, int y, int mouseX, int mouseY, float partialTicks)
    {
        super.render(graphics, x, y, mouseX, mouseY, partialTicks);
        button.setX(x);
        button.setY(y + 10);
        button.render(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public AbstractWidget getWidget()
    {
        return button;
    }

    @Override
    public String getValue()
    {
        return Boolean.toString(state);
    }

    @Override
    public IValueContainer.Entry toEntry()
    {
        return new IValueContainer.Entry(id, getValue());
    }

    public void setState(boolean state)
    {
        this.state = state;
        if(state)
        {
            button.setMessage(Component.translatable("gui.cfm.on"));
        }
        else
        {
            button.setMessage(Component.translatable("gui.cfm.off"));
        }
    }
}
