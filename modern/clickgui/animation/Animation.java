package exalted.zxc.ui.modern.clickgui.animation;

import exalted.zxc.utils.IMinecraft;
import exalted.zxc.utils.render.animation.easing.Easing;


public class Animation implements IMinecraft {
    private long startTime = System.currentTimeMillis();
    private int speed = 300;
    private double size = 1;
    private boolean forward = true;
    private Easing easing = Easing.EASE_OUT_SINE;

    public boolean finished(boolean forward) {
        return isDone() && (forward ? this.forward : !this.forward);
    }
    
    public boolean finished() {
        return isDone() && this.forward;
    }
    
    public boolean isDone() {
        return getElapsedTime() >= speed;
    }
    
    public Animation setDirection(boolean forward) {
        if (this.forward != forward) {
            this.forward = forward;
            startTime = (long) (System.currentTimeMillis() - (size - Math.min(size, getElapsedTime())));
        }
        return this;
    }
    
    public Animation finish() {
        startTime = (long) (System.currentTimeMillis() - speed);
        return this;
    }
    
    public Animation setEasing(Easing easing) {
        this.easing = easing;
        return this;
    }
    
    public Animation setSpeed(int speed) {
        this.speed = speed;
        return this;
    }
    
    public Animation setSize(float size) {
        this.size = size;
        return this;
    }
    
    public long getElapsedTime() {
        return System.currentTimeMillis() - startTime;
    }
    
    public float getLinear() {
        if (forward) {
            if (isDone()) {
                return (float) size;
            }
            return (float) (getElapsedTime() / (double) speed * size);
        } else {
            if (isDone()) {
                return 0.0f;
            }
            return (float) ((1 - getElapsedTime() / (double) speed) * size);
        }
    }
    
    public float get() {
        if (forward) {
            if (isDone()) {
                return (float) size;
            }
            return (float) (easing.apply(getElapsedTime() / (double) speed) * size);
        } else {
            if (isDone()) {
                return 0.0f;
            }
            return (float) ((1 - easing.apply(getElapsedTime() / (double) speed)) * size);
        }
    }
    
    public float reversed() {
        return 1 - get();
    }
    
    public void reset() {
        startTime = System.currentTimeMillis();
    }
} 