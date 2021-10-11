package com.waracle.cake_manager.service;

import com.waracle.cake_manager.dto.CakeDto;

import java.util.List;

public interface StartupRunnerService {
    List<CakeDto> fetchJsonCakeData();

    void saveCakeData(List<CakeDto> cakeDtos);
}
