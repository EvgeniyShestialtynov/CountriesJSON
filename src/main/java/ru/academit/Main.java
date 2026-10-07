package ru.academit;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            java.io.InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("countries.json");

            if (inputStream == null) {
                throw new java.io.FileNotFoundException("Файл countries.json не найден в папке resources!");
            }

            List<Country> countries = mapper.readValue(inputStream, new TypeReference<List<Country>>() {
                    }
            );

            long totalPopulation = 0;

            for (Country country : countries) {
                totalPopulation += country.getPopulation();
            }

            System.out.println("Суммарное население равно " + totalPopulation + " человек");

            Set<String> currencyNames = new HashSet<>();

            for (Country country : countries) {
                if (country.getCurrencies() != null) {
                    for (Currency currency : country.getCurrencies()) {
                        if (currency.getName() != null) {
                            currencyNames.add(currency.getName());
                        }
                    }
                }
            }

            System.out.println("Перечень валют: " + currencyNames);

            List<Country> countriesUpperOneMillionPopulation = new ArrayList<>();

            for (Country country : countries) {
                if (country.getPopulation() >= 1000000) {
                    countriesUpperOneMillionPopulation.add(country);
                }
            }

            mapper.writeValue(new File("result.json"), countriesUpperOneMillionPopulation);
            System.out.println("Создан файл JSON со странами с населением более 1 000 000 человек");
        } catch (Exception e) {
            System.out.println("Произошла ошибка при работе с JSON: " + e.getMessage());
        }
    }
}