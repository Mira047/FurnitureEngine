package com.mira.furnitureengine.furniture.functions.internal.fmanip;

public enum Position {
    ABSOLUTE, // Absolute world space
    RELATIVE, // Relative to the origin
    LOCAL // Relative to the furniture (Same as relative, but inherits rotation)
}
