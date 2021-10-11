package com.waracle.cake_manager.service;

import com.waracle.cake_manager.dto.CakeDto;
import com.waracle.cake_manager.model.Cake;

import java.util.List;

public interface StartupRunnerService {
    List<CakeDto> fetchJsonCakeData();

    List<Cake> saveCakeData(List<CakeDto> cakeDtos);
}
