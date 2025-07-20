package exalted.zxc.ui.modern.clickgui;

import exalted.zxc.utils.IMinecraft;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TranslationTextComponent;


public class ModernClickGui implements IMinecraft {
    
    private static ClickGuiScreen screen;
    
    public static void openGui() {
        if (screen == null) {
            screen = new ClickGuiScreen();
        }
        
        ClickGuiRenderer.opening.setDirection(true);
        ClickGuiRenderer.opening.reset();
        
        Minecraft.getInstance().displayGuiScreen(screen);
    }
    
    public static void closeGui() {
        if (screen != null) {
            ClickGuiRenderer.opening.setDirection(false);
            screen.getWindow().close();
        }
    }
    
    public static boolean isGuiOpen() {
        return Minecraft.getInstance().currentScreen instanceof ClickGuiScreen;
    }
} 