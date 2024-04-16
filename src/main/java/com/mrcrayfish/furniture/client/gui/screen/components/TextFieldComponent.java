package com.mrcrayfish.furniture.client.gui.screen.components;

import com.mrcrayfish.furniture.client.gui.screen.EditValueContainerScreen;
import com.mrcrayfish.furniture.tileentity.IValueContainer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.CommonComponents;

/**
 * Author: MrCrayfish
 */
public class TextFieldComponent extends ValueComponent
{
    private EditBox textField;

    public TextFieldComponent(IValueContainer.Entry entry, Font font)
    {
        super(entry.getId(), entry.getName(), font);
        textField = new EditBox(font, 0, 0, EditValueContainerScreen.WIDTH - EditValueContainerScreen.PADDING * 2, 20, CommonComponents.EMPTY);
        textField.setMaxLength(256);
        if(entry.getValue() != null)
        {
            textField.setValue(entry.getValue());
        }
    }

    @Override
    public void render(GuiGraphics graphics, int x, int y, int mouseX, int mouseY, float partialTicks)
    {
        super.render(graphics, x, y, mouseX, mouseY, partialTicks);
        textField.setX(x);
        textField.setY(y + 10);
        textField.renderWidget(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public AbstractWidget getWidget()
    {
        return textField;
    }

    @Override
    public String getValue()
    {
        return textField.getValue();
    }

    @Override
    public IValueContainer.Entry toEntry()
    {
        return new IValueContainer.Entry(id, getValue());
    }
}
