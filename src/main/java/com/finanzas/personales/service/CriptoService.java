package com.finanzas.personales.service;

import com.finanzas.personales.dto.response.CriptoDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class CriptoService {

    private final RestClient restClient;
    private final String baseUrl;
    private final String monedas;

    public CriptoService(
            @Value("${coingecko.base-url:https://api.coingecko.com/api/v3}") String baseUrl,
            @Value("${coingecko.monedas:bitcoin,ethereum,tether,binancecoin,solana,cardano}") String monedas) {
        this.baseUrl = baseUrl;
        this.monedas = monedas;
        this.restClient = RestClient.create();
    }

    /** Precios en USD de las principales criptomonedas, ordenadas por capitalización. */
    public List<CriptoDTO> obtenerCriptos() {
        return restClient.get()
                .uri(baseUrl + "/coins/markets?vs_currency=usd&order=market_cap_desc&ids={ids}", monedas)
                .retrieve()
                .body(new ParameterizedTypeReference<List<CriptoDTO>>() {});
    }
}