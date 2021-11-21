package com.example.reservisland.business.service;

import com.example.reservisland.domain.repository.ConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.reservisland.config.Constants.MAXIMUM_OCCUPATION;

@Service
public class ConfigService {

    @Autowired
    private ConfigRepository configRepository;

    public int getMaximumOccupation() {
        var configMaxOccupation = configRepository.findByKey(MAXIMUM_OCCUPATION);

        try {
            return Integer.parseInt(configMaxOccupation.getValue());
        } catch (NullPointerException | NumberFormatException e) {
            return 0;
        }
    }
}
