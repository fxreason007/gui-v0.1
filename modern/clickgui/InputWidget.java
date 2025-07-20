package exalted.zxc.ui.modern.clickgui;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.matrix.MatrixStack;

import exalted.zxc.ui.modern.clickgui.animation.Animation;
import exalted.zxc.utils.render.ColorUtils;
import exalted.zxc.utils.render.RenderUtils;


public class InputWidget extends Rect {
    private String text = "";
    private boolean focused = false;
    private int cursorPosition = 0;
    private int selectionEnd = 0;
    private long lastBlink = 0;
    private boolean blink = true;
    private Animation focusAnimation = new Animation().setSpeed(300);
    
    public InputWidget(float x, float y, float width, float height) {
        super(x, y, width, height);
    }
    
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        focusAnimation.setDirection(focused);
        

        RenderUtils.Render2D.drawRoundedRect(x, y, width, height, 4, 
                ColorUtils.rgba(20, 20, 25, 200 + (int)(55 * focusAnimation.get())));

        String visibleText = text.isEmpty() ? (focused ? "" : "Введите текст...") : text;
        mc.fontRenderer.drawString(matrixStack, visibleText, x + 5, y + height/2 - mc.fontRenderer.FONT_HEIGHT/2, 
                ColorUtils.rgba(255, 255, 255, 200));

        if (focused && (System.currentTimeMillis() - lastBlink > 500)) {
            blink = !blink;
            lastBlink = System.currentTimeMillis();
        }
        
        if (focused && blink) {
            float cursorX = x + 5 + mc.fontRenderer.getStringWidth(text.substring(0, cursorPosition));
            RenderUtils.Render2D.drawRect(cursorX, y + 4, 1, height - 8, 
                    ColorUtils.rgba(255, 255, 255, 200));
        }
    }
    
    public void mouseClicked(double mouseX, double mouseY, int button) {
        boolean wasHovered = isHovered(mouseX, mouseY);
        focused = wasHovered;
        
        if (wasHovered && button == 0) {
            float textX = x + 5;
            float clickX = (float) mouseX - textX;
            
            cursorPosition = 0;
            float currentWidth = 0;
            
            for (int i = 0; i < text.length(); i++) {
                float charWidth = mc.fontRenderer.getStringWidth(String.valueOf(text.charAt(i)));
                if (currentWidth + charWidth / 2 > clickX) {
                    break;
                }
                currentWidth += charWidth;
                cursorPosition = i + 1;
            }
            
            selectionEnd = cursorPosition;
        }
    }
    
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!focused) return;
        
        switch (keyCode) {
            case GLFW.GLFW_KEY_BACKSPACE:
                if (cursorPosition > 0) {
                    text = text.substring(0, cursorPosition - 1) + text.substring(cursorPosition);
                    cursorPosition--;
                    selectionEnd = cursorPosition;
                }
                break;
                
            case GLFW.GLFW_KEY_DELETE:
                if (cursorPosition < text.length()) {
                    text = text.substring(0, cursorPosition) + text.substring(cursorPosition + 1);
                }
                break;
                
            case GLFW.GLFW_KEY_LEFT:
                if (cursorPosition > 0) {
                    cursorPosition--;
                    if ((modifiers & GLFW.GLFW_MOD_SHIFT) == 0) {
                        selectionEnd = cursorPosition;
                    }
                }
                break;
                
            case GLFW.GLFW_KEY_RIGHT:
                if (cursorPosition < text.length()) {
                    cursorPosition++;
                    if ((modifiers & GLFW.GLFW_MOD_SHIFT) == 0) {
                        selectionEnd = cursorPosition;
                    }
                }
                break;
                
            case GLFW.GLFW_KEY_HOME:
                cursorPosition = 0;
                if ((modifiers & GLFW.GLFW_MOD_SHIFT) == 0) {
                    selectionEnd = cursorPosition;
                }
                break;
                
            case GLFW.GLFW_KEY_END:
                cursorPosition = text.length();
                if ((modifiers & GLFW.GLFW_MOD_SHIFT) == 0) {
                    selectionEnd = cursorPosition;
                }
                break;
        }
    }
    
    public void charTyped(char codePoint, int modifiers) {
        if (!focused) return;
        
        if (codePoint >= 32 && codePoint != 127) {
            text = text.substring(0, cursorPosition) + codePoint + text.substring(cursorPosition);
            cursorPosition++;
            selectionEnd = cursorPosition;
        }
    }
    
    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
        this.cursorPosition = text.length();
        this.selectionEnd = this.cursorPosition;
    }
    
    public boolean isFocused() {
        return focused;
    }
    
    public void setFocused(boolean focused) {
        this.focused = focused;
    }
    
    public void setFocused2(boolean focused) {
        this.focused = focused;
    }
} 