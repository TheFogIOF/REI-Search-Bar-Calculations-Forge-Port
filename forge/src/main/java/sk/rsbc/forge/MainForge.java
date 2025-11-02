package sk.rsbc.forge;

import me.shedaniel.rei.api.client.REIRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import sk.rsbc.CalculatorSearch;
import sk.rsbc.Main;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Main.MOD_ID)
public final class MainForge {
    public MainForge() {
        // Submit our event bus to let Architectury API register our content on the right time.
        EventBuses.registerModEventBus(Main.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        // Run our common setup.
        Main.init();
        MinecraftForge.EVENT_BUS.addListener(MainForge::onScreenInit);
    }

    private static void onScreenInit(ScreenEvent.Init.Post event) {
        Screen screen = event.getScreen();
        if (screen instanceof AbstractContainerScreen<?> handled) {
            // Для Forge используем событие отрисовки экрана
            MinecraftForge.EVENT_BUS.addListener((ScreenEvent.Render.Post renderEvent) -> {
                if (renderEvent.getScreen() == screen) {
                    onScreenRender(renderEvent, handled);
                }
            });
        }
    }

    private static void onScreenRender(ScreenEvent.Render.Post event, AbstractContainerScreen<?> handled) {
        Minecraft minecraft = Minecraft.getInstance();
        Font font = minecraft.font;

        int centerX = handled.width / 2;
        int bottomY = handled.height;
        int textPosX = centerX - 94;
        int textPosY = bottomY - 32;

        // Получаем текстовое поле поиска REI (адаптируйте под вашу версию REI для Forge)
        String text = CalculatorSearch.format(REIRuntime.getInstance().getSearchTextField().getText());

        if (text.contains("=")) {
            if (!minecraft.player.isCreative()) {
                textPosX += 10;
            }
            event.getGuiGraphics().drawString(font, Component.literal(text), textPosX, textPosY, 0xFF55FF55, false);
        }
    }
}
