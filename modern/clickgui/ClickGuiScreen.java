package exalted.zxc.ui.modern.clickgui;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import exalted.zxc.utils.IMinecraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.TranslationTextComponent;


public class ClickGuiScreen extends Screen implements IMinecraft {
    
    private final ClickGuiWindow window;

    public ClickGuiScreen() {
        super(new TranslationTextComponent("Click Gui"));
        float 
        width = 488,
        height = 318;

        int screenWidth = mc.getMainWindow().getScaledWidth();
        int screenHeight = mc.getMainWindow().getScaledHeight();

        float x = screenWidth / 2f - width / 2f;
        float y = screenHeight / 2f - height / 2f;
        

        
        this.window = new ClickGuiWindow(x, y, width, height);

        this.window.x(x);
        this.window.y(y);
        
        ClickGuiRenderer.opening.reset();
    }
    
    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.runAsFancy(() -> {
            window.render(matrixStack, mouseX, mouseY, partialTicks);
            super.render(matrixStack, mouseX, mouseY, partialTicks);
        });
        
        boolean up = GLFW.glfwGetKey(mc.getMainWindow().getHandle(), GLFW.GLFW_KEY_UP) == GLFW.GLFW_PRESS;
        boolean down = GLFW.glfwGetKey(mc.getMainWindow().getHandle(), GLFW.GLFW_KEY_DOWN) == GLFW.GLFW_PRESS;
        float offset = .1f / Math.max((float) Minecraft.debugFPS, 5) * 500;
        
        if (up || down)
            this.mouseScrolled(mouseX, mouseY, up ? offset : down ? -offset : 0);
    }
    
    @Override
    public boolean isPauseScreen() {
        return false;
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        window.clicked(mouseX, mouseY, button);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        window.released(mouseX, mouseY, button);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        window.dragged(mouseX, mouseY, button, dragX, dragY);
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        this.window.pressed(keyCode, scanCode, modifiers);
        if ((keyCode == GLFW.GLFW_KEY_ESCAPE) && ClickGuiRenderer.opening.isDone()) {
            this.window.close();
            mc.displayGuiScreen(null);
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
    
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        window.scrolled(mouseX, mouseY, delta);
        return super.mouseScrolled(mouseX, mouseY, delta);
    }
    
    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        window.charTyped(codePoint, modifiers);
        return super.charTyped(codePoint, modifiers);
    }
    
    @Override
    public void tick() {
        window.tick();
        super.tick();
    }
    
    public ClickGuiWindow getWindow() {
        return window;
    }
} 