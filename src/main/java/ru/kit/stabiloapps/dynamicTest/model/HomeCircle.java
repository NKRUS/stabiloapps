package ru.kit.stabiloapps.dynamicTest.model;

import javafx.scene.paint.Paint;

/**
 * Created by mikha on 10.02.2017.
 */
public class HomeCircle extends MyCircle{
    public HomeCircle(double radius) {
        super(radius);
    }

    public HomeCircle(double radius, Paint fill) {
        super(radius, fill);
    }

    public HomeCircle() {
    }

    public HomeCircle(double centerX, double centerY, double radius) {
        super(centerX, centerY, radius);
    }

    public HomeCircle(double centerX, double centerY, double radius, Paint fill) {
        super(centerX, centerY, radius, fill);
    }

    public HomeCircle(double centerX, double centerY, double radius, Paint fill, boolean isVisible) {
        super(centerX, centerY, radius, fill, isVisible);
    }

    @Override
    public void setPassFigure(boolean isPassFigure) {
        super.setPassFigure(false);
    }
}
