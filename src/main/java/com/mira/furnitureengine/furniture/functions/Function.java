package com.mira.furnitureengine.furniture.functions;

import java.util.HashMap;

public interface Function {
    String getType();

    void execute(HashMap<String, Object> args) throws IllegalArgumentException;
}
