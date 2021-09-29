package com.waracle.cake_manager.dto;

import java.util.ArrayList;
import java.util.List;

public class Cakes {
    private List<CakeDto> cakes = new ArrayList<>();

    public List<CakeDto> getCakes() {
        return cakes;
    }

    public void setCakes(List<CakeDto> cakes) {
        this.cakes = cakes;
    }

    @Override
    public String toString() {
        return "Cakes{" +
                "cakes=" + cakes +
                '}';
    }
}
