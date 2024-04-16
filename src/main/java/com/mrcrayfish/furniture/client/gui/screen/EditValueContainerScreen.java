package com.mrcrayfish.furniture.client.gui.screen;

import com.mrcrayfish.furniture.client.gui.screen.components.TextFieldComponent;
import com.mrcrayfish.furniture.client.gui.screen.components.ToggleComponent;
import com.mrcrayfish.furniture.client.gui.screen.components.ValueComponent;
import com.mrcrayfish.furniture.network.PacketHandler;
import com.mrcrayfish.furniture.network.message.C2SMessageUpdateValueContainer;
import com.mrcrayfish.furniture.tileentity.IValueContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: MrCrayfish
 */
public class EditValueContainerScreen extends Screen
{
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation("cfm:textures/gui/value_container.png");

    public static final int WIDTH = 176;
    public static final int PADDING = 10;
    public static final int VALUE_HEIGHT = 35;

    private List<ValueComponent> values = new ArrayList<>();
    private IValueContainer valueContainer;
    private int containerHeight;

    public EditValueContainerScreen(Component title, IValueContainer valueContainer)
    {
        super(title);
        this.valueContainer = valueContainer;
    }

    @Override
    public void init()
    {
        values.clear();
        valueContainer.getEntries().forEach(entry ->
        {
            if(entry.getType() != null)
            {
                ValueComponent component;
                switch(entry.getType())
                {
                    case TEXT_FIELD:
                        component = new TextFieldComponent(entry, font);
                        if(this.getCurrentFocusPath() == null)
                            this.setInitialFocus(component.getWidget());
                        break;
                    case TOGGLE:
                        component = new ToggleComponent(entry, font);
                        break;
                    default:
                        return;
                }
                this.addWidget(component.getWidget());
                values.add(component);
            }
        });
        this.containerHeight = values.size() * VALUE_HEIGHT + 10 * 2;

        int startX = (this.width - WIDTH) / 2;
        int startY = (this.height - this.containerHeight) / 2;

        this.addRenderableWidget(Button.builder(Component.literal("X"), button -> this.onClose())
                .pos(startX + WIDTH + 5, startY + 5).size(20, 20)
                .createNarration(component -> Component.translatable("gui.button.cfm.back")).build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks)
    {
        int startX = (this.width - WIDTH) / 2;
        int startY = (this.height - this.containerHeight) / 2;

        graphics.blitNineSliced(GUI_TEXTURE, startX, startY, WIDTH, values.size() * VALUE_HEIGHT + 15, 5, WIDTH, 20, 0, 0);

        for (int i = 0; i < values.size(); i++)
        {
            values.get(i).render(graphics, startX + PADDING, startY + i * VALUE_HEIGHT + 10, mouseX, mouseY, partialTicks);
        }
        super.render(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public void onClose()
    {
        PacketHandler.getPlayChannel().sendToServer(new C2SMessageUpdateValueContainer(values, valueContainer));
        super.onClose();
    }
}
