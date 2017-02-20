package ru.kit.stabiloapps.dynamicTest.model;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

/**
 * Created by mikha on 23.01.2017.
 */
public class MyCircle extends Circle {

    private Paint defaultColor;
    private double holdTime = 0.0;
    private boolean isPassFigure = false;

    public MyCircle(double radius) {
        super(radius);
    }

    public MyCircle(double radius, Paint fill) {
        super(radius, fill);
    }

    public MyCircle() {
    }

    public MyCircle(double centerX, double centerY, double radius) {
        super(centerX, centerY, radius);
    }

    public MyCircle(double centerX, double centerY, double radius, Paint fill) {
        super(centerX, centerY, radius, fill);
        this.defaultColor = fill;
    }

    public MyCircle(double centerX, double centerY, double radius, Paint fill, boolean isVisible) {
        super(centerX, centerY, radius, fill);
        this.defaultColor = fill;
        setVisible(isVisible);
    }

    public boolean isIntersect(Circle figure) {
        double x = this.getCenterX();
        double y = this.getCenterY();

        double dx = x - figure.getCenterX();
        double dy = y - figure.getCenterY();
        double destination = Math.sqrt(dx * dx + dy * dy);
        double destination2 = Math.max(this.getRadius(), figure.getRadius());

        return destination * 1.4 <= destination2;
    }


    public void draw(double x, double y, double width, double height) {
        if (x - getRadius() <= 0) {
            setCenterX(getRadius() + 10);
        } else if (x + getRadius() >= width) {
            setCenterX(width - getRadius() - 10);
        } else {
            setCenterX(x);
        }

        if (y - getRadius() <= 0) {
            setCenterY(getRadius());
        } else if (y + getRadius() >= height) {
            setCenterY(height - getRadius());
        } else {
            setCenterY(y);
        }
    }

    public Paint getDefaultColor() {
        return defaultColor;
    }

    public double getHoldTime() {
        return holdTime;
    }

    public void setHoldTime(double holdTime) {
        this.holdTime = holdTime;
    }

    public boolean isPassFigure() {
        return isPassFigure;
    }

    public void setPassFigure(boolean isPassFigure) {
        this.isPassFigure = isPassFigure;
        setVisible(!isPassFigure);
    }

    @Override
    public String toString() {
        return "MyCircle{" + super.toString() +
                "defaultColor=" + defaultColor +
                ", holdTime=" + holdTime +
                ", isPassFigure=" + isPassFigure +
                '}';
    }
}
