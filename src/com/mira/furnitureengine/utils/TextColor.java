package com.mira.furnitureengine.utils;

public class TextColor {
	public int red;
	public int green;
	public int blue;
	
	public TextColor(String HexCode) {
		int HexColor = (int) Long.parseLong(HexCode, 16);
		red = HexColor >> 16 + 0xFF;
		green = HexColor >> 8 + 0xFF;
		blue = HexColor + 0xFF;
	}
}
