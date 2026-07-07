package com.inv.invmaster001.service;

import com.inv.invmaster001.dto.request.settings.CreateSettingsRequest;
import com.inv.invmaster001.dto.request.settings.UpdateSettingsRequest;
import com.inv.invmaster001.dto.response.settings.SettingsResponse;
import com.inv.invmaster001.entity.Company;
import com.inv.invmaster001.entity.Settings;
import com.inv.invmaster001.exception.SettingsAlreadyExistsException;
import com.inv.invmaster001.exception.SettingsNotFoundException;
import com.inv.invmaster001.repository.SettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@Transactional
public class SettingsService {


    private final SettingsRepository settingsRepository;

    public SettingsResponse getSettings(Company company) {

        Settings settings =
                settingsRepository
                        .findByCompanyIdAndDeletedAtIsNull(company.getId())
                        .orElseThrow(() ->
                                new SettingsNotFoundException(
                                        "Settings not found"
                                )
                        );


        return mapToResponse(settings);
    }

    public SettingsResponse createSettings(
            Company company,
            CreateSettingsRequest request
    ) {


        if (settingsRepository.findByCompanyIdAndDeletedAtIsNull(company.getId()).isPresent()) {
            throw new SettingsAlreadyExistsException("Settings already exists for company");
        }


        Settings settings = Settings.builder()
                .company(company)
                .gstPercentage(request.getGstPercentage())
                .cgstPercentage(request.getCgstPercentage())
                .sgstPercentage(request.getSgstPercentage())
                .vehicleNumbers(request.getVehicleNumbers())
                .defaultProfitMargin(request.getDefaultProfitMargin())
                .currency(request.getCurrency())
                .invoicePrefix(request.getInvoicePrefix())
                .financialYear(request.getFinancialYear())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();


        return mapToResponse(settingsRepository.save(settings));
    }



    public SettingsResponse updateSettings(Company company,
            UpdateSettingsRequest request) {


        Settings settings = settingsRepository
                .findByCompanyIdAndDeletedAtIsNull(company.getId())
                .orElseThrow(() ->
                        new SettingsNotFoundException("Settings not found")
                );

        settings.setGstPercentage(request.getGstPercentage());
        settings.setCgstPercentage(request.getCgstPercentage());
        settings.setSgstPercentage(request.getSgstPercentage());
        settings.setVehicleNumbers(request.getVehicleNumbers());
        settings.setDefaultProfitMargin(request.getDefaultProfitMargin());
        settings.setCurrency(request.getCurrency());
        settings.setInvoicePrefix(request.getInvoicePrefix());
        settings.setFinancialYear(request.getFinancialYear());
        settings.setUpdatedAt(LocalDateTime.now());

        return mapToResponse(settingsRepository.save(settings));
    }




    public void deleteSettings(Company company) {

        Settings settings = settingsRepository
                .findByCompanyIdAndDeletedAtIsNull(company.getId())
                .orElseThrow(() ->
                        new SettingsNotFoundException("Settings not found")
                );


        settingsRepository.delete(settings);

    }


    private SettingsResponse mapToResponse(Settings settings) {

        return SettingsResponse.builder()
                .id(settings.getId())
                .gstPercentage(settings.getGstPercentage())
                .cgstPercentage(settings.getCgstPercentage())
                .sgstPercentage(settings.getSgstPercentage())
                .vehicleNumbers(settings.getVehicleNumbers())
                .defaultProfitMargin(settings.getDefaultProfitMargin())
                .currency(settings.getCurrency())
                .invoicePrefix(settings.getInvoicePrefix())
                .financialYear(settings.getFinancialYear())
                .build();

    }
}
