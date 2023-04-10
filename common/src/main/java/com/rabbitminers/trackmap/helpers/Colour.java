package com.rabbitminers.trackmap.helpers;

import com.simibubi.create.foundation.utility.Color;

public class Colour extends Color {
    public Colour(int r, int g, int b) {
        super(r, g, b);
    }

    public Colour(int r, int g, int b, int a) {
        super(r, g, b, a);
    }

    public Colour(float r, float g, float b, float a) {
        super(r, g, b, a);
    }

    public Colour(int rgba) {
        super(rgba);
    }

    public Colour(int rgb, boolean hasAlpha) {
        super(rgb, hasAlpha);
    }

    public Colour (Color color) {
        super(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }
}
