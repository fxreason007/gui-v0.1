package exalted.zxc.ui.modern.clickgui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;

import exalted.zxc.control.Manager;
import exalted.zxc.module.TypeList;
import exalted.zxc.module.api.Module;
import exalted.zxc.ui.midnight.StyleManager;
import exalted.zxc.ui.modern.clickgui.animation.Animation;
import exalted.zxc.utils.IMinecraft;
import exalted.zxc.utils.font.Fonts;
import exalted.zxc.utils.render.ColorUtils;
import exalted.zxc.utils.render.RenderUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

/**
 * Это типо ОКАК МАРАБУ 2 ЙОУ
 */

public class ClickGuiRenderer implements IMinecraft {

    public static Animation opening = new Animation().setSpeed(300);
    public static Animation tooltip = new Animation().setSpeed(300);

    private Color backgroundColor;

    private ClickGuiWindow window;


    public ClickGuiRenderer() {
        backgroundColor = new Color(20, 20, 25, 220);

    }

    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.window = mc.currentScreen instanceof ClickGuiScreen ? ((ClickGuiScreen)mc.currentScreen).getWindow() : null;
        
        if (window == null) return;

        renderWindow(matrixStack, mouseX, mouseY, partialTicks);
    }

    private String getCategoryIcon(TypeList category) {
        switch (category) {
            case Combat: return "combat.png";
            case Movement: return "movement.png";
            case Render: return "render.png";
            case Player: return "player.png";
            case Display: return "display.png";
            default: return "misc.png";
        }
    }
    
    private void renderWindow(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
            float x = window.getX();
            float y = window.getY();
            float width = window.getWidth();
            float height = window.getHeight();

            float stripeWidth = 40;
            float stripeX = x;
            float stripeY = y;
            float stripeHeight = height;

            RenderUtils.Render2D.drawRoundedRect(stripeX, stripeY, stripeWidth, stripeHeight,6, ColorUtils.rgba(0,0,0,255));
            RenderUtils.Render2D.drawRect(stripeX + stripeWidth - 5, stripeY + 0.5f, 5, stripeHeight - 1, ColorUtils.rgba(0,0,0,255));

            RenderUtils.Render2D.drawRoundedRect(x, y, width, height, 6, ColorUtils.rgba(backgroundColor.getRed(), backgroundColor.getGreen(), backgroundColor.getBlue(), (int)(backgroundColor.getAlpha() * opening.get())));

            ResourceLocation watermarkTexture = new ResourceLocation("minecraft", "newcode/images/all/logo/watermark.png");

            float watermarkSize = 32;
            float watermarkX = x + 2.7f;
            float watermarkY = y + 2.7f;

            RenderUtils.Render2D.drawImage(watermarkTexture, watermarkX, watermarkY, watermarkSize, watermarkSize, ColorUtils.rgba(255,255,255,255));

            float separatorWidth = 22;
            float separatorHeight = 1;
            float separatorX = x + 8;
            float separatorY = y + 32;

            RenderUtils.Render2D.drawRoundedRect(separatorX, separatorY, separatorWidth, separatorHeight, 0.5f, ColorUtils.rgba(100,100,100,150));

            float iconSize = 20;
            float iconSpacing = 25;
            float iconX = x + 9;

            for (TypeList category :  TypeList.values()) {
                float iconY = y + 40 + (category.ordinal() * iconSpacing);

                String iconName = getCategoryIcon(category);
                ResourceLocation iconTexture = new ResourceLocation("minecraft", "newcode/images/all/gui/" + iconName);

                int iconColor = (window.getCurrentCategory() == category) ? ColorUtils.rgba(255,255,255,255) : ColorUtils.rgba(150,150,150,180);

                RenderUtils.Render2D.drawImage(iconTexture, iconX, iconY, iconSize, iconSize, iconColor);
            }

            float moduleAreaX = x + stripeWidth;
            float moduleAreaY = y;
            float moduleAreaWidth = stripeWidth * 3;
            float moduleAreaHeight = height;

            RenderUtils.Render2D.drawRoundedRect(moduleAreaX,moduleAreaY,moduleAreaWidth,moduleAreaHeight,6,ColorUtils.rgba(20,20,25,200));

    }

    public boolean isDone() {
        return opening.isDone();
    }
} 
