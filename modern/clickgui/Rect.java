package exalted.zxc.ui.modern.clickgui;

import exalted.zxc.utils.IMinecraft;


public class Rect implements IMinecraft {
    protected float x, y, width, height;
    
    public Rect(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    public Rect() {
        this(0, 0, 0, 0);
    }
    
    public float getX() {
        return x;
    }
    
    public Rect x(float x) {
        this.x = x;
        return this;
    }
    
    public float getY() {
        return y;
    }
    
    public Rect y(float y) {
        this.y = y;
        return this;
    }
    
    public float getWidth() {
        return width;
    }
    
    public Rect width(float width) {
        this.width = width;
        return this;
    }
    
    public float getHeight() {
        return height;
    }
    
    public Rect height(float height) {
        this.height = height;
        return this;
    }
    
    public boolean isHovered(double mouseX, double mouseY) {
        return mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
    }
} 