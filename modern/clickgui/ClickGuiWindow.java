package exalted.zxc.ui.modern.clickgui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.matrix.MatrixStack;

import exalted.zxc.control.Manager;
import exalted.zxc.module.TypeList;
import exalted.zxc.module.api.Module;
import exalted.zxc.utils.IMinecraft;
import exalted.zxc.utils.SoundUtils;
import net.minecraft.util.math.MathHelper;


public class ClickGuiWindow extends Rect implements IMinecraft {
    
    private final ArrayList<Module> modules = new ArrayList<>();
    private TypeList currentCategory = TypeList.Combat;
    private Module openedModule = null;
    private ClickGuiRenderer renderer;
    private boolean drag = false;
    private float dragX, dragY;
    private InputWidget input;
    private float scroll = 0;
    private boolean searching = false;
    
    public ClickGuiWindow(float x, float y, float width, float height) {
        super(x, y, width, height);
        this.renderer = new ClickGuiRenderer();
        

    }
    
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if (drag) {
            int screenWidth = mc.getMainWindow().getScaledWidth();
            int screenHeight = mc.getMainWindow().getScaledHeight();
            x = (float) MathHelper.clamp(mouseX + dragX, -140, screenWidth - this.getWidth() + 140);
            y = (float) MathHelper.clamp(mouseY + dragY, -140, screenHeight - this.getHeight() + 140);
        }

        renderer.render(matrixStack, mouseX, mouseY, partialTicks);

        if (input != null) {
            input.render(matrixStack, mouseX, mouseY, partialTicks);
        }
    }
    
    public boolean clicked(double mouseX, double mouseY, int button) {
        if (input != null) {
            input.mouseClicked(mouseX, mouseY, button);
        }

        if (mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + 30 && button == 0) {
            drag = true;
            dragX = (float) (x - mouseX);
            dragY = (float) (y - mouseY);
            return true;
        }

        float iconSize = 20;
        float iconSpacing = 25;
        float iconX = x + 9;

        for (TypeList category : TypeList.values()) {
            float iconY = y + 40 + (category.ordinal() * iconSpacing);

            if (mouseX >= iconX && mouseX <= iconX + iconSize && mouseY >= iconY && mouseY <= iconY + iconSize) {
                if (currentCategory != category) {
                    currentCategory = category;
                    scroll = 0;
                    SoundUtils.playSound("click.wav", 50, false);
                }
                return true;
            }
        }

        float moduleX = x + 35;
        float moduleY = y + 40;
        float moduleWidth = width - 45;
        float moduleHeight = height - 50;

        if (mouseX >= moduleX && mouseY >= moduleY && mouseX < moduleX + moduleWidth && mouseY < moduleY + moduleHeight) {
            List<Module> categoryModules = Manager.FUNCTION_MANAGER.getFunctions().stream()
                    .filter(m -> m.category == currentCategory)
                    .toList();

            float modY = moduleY + 5 + scroll;
            for (Module module : categoryModules) {
                if (mouseX >= moduleX + 5 && mouseY >= modY && mouseX < moduleX + moduleWidth - 5 && mouseY < modY + 20) {
                    if (button == 0) {
                        module.toggle();
                        SoundUtils.playSound("click.wav", 50, false);
                    } else if (button == 1) {
                        openedModule = module;
                        SoundUtils.playSound("moduleopen.wav", 50, false);
                    }
                    return true;
                }
                modY += 25;
            }
        }
        
        return false;
    }
    
    public boolean released(double mouseX, double mouseY, int button) {
        if (button == 0) {
            drag = false;
        }
        return false;
    }
    
    public boolean dragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        return false;
    }
    
    public boolean scrolled(double mouseX, double mouseY, double delta) {
        float moduleX = x + 35;
        float moduleY = y + 40;
        float moduleWidth = width - 45;
        float moduleHeight = height - 50;
        
        if (mouseX >= moduleX && mouseY >= moduleY && mouseX < moduleX + moduleWidth && mouseY < moduleY + moduleHeight) {
            scroll += delta * 15;

            List<Module> categoryModules = Manager.FUNCTION_MANAGER.getFunctions().stream()
                    .filter(m -> m.category == currentCategory)
                    .toList();
            
            float contentHeight = categoryModules.size() * 25 + 10;
            float maxScroll = Math.min(0, moduleHeight - contentHeight);
            
            scroll = MathHelper.clamp(scroll, maxScroll, 0);
            return true;
        }
        
        return false;
    }
    
    public boolean charTyped(char codePoint, int modifiers) {
        if (input != null) {
            input.charTyped(codePoint, modifiers);
            return true;
        }
        return false;
    }
    
    public void tick() {
    }
    
    public boolean pressed(int keyCode, int scanCode, int modifiers) {
        if (input != null) {
            input.keyPressed(keyCode, scanCode, modifiers);
            return true;
        }
        return false;
    }
    
    public void close() {
        ClickGuiRenderer.opening.setDirection(false);
        SoundUtils.playSound("guiclose.wav", 62, false);
    }
    
    public TypeList getCurrentCategory() {
        return currentCategory;
    }

    public InputWidget getInput() {
        return input;
    }
    
    public void setInput(InputWidget input) {
        this.input = input;
    }
    
    public boolean isSearching() {
        return searching;
    }
    
    public void setSearching(boolean searching) {
        this.searching = searching;
    }
    
    public float getScroll() {
        return scroll;
    }
} 