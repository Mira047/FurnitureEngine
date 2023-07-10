package com.mira.furnitureengine.furniture.functions.internal.fmanip;

import com.mira.furnitureengine.FurnitureEngine;
import com.mira.furnitureengine.furniture.functions.Function;

import java.util.HashMap;

public class MoveFunction implements Function {
    @Override
    public String getType() {
        return "MOVE";
    }

    @Override
    public void execute(HashMap<String, Object> args) throws IllegalArgumentException {
        // get position
        String pos = (String) args.get("position");

        if(pos == null) pos = "LOCAL"; Position position;

        try {
            position = Position.valueOf(pos.toUpperCase());
        } catch (IllegalArgumentException e) {
            FurnitureEngine.getInstance().getLogger().warning("Invalid position in MOVE function: " + pos);

            return;
        }


    }
}
