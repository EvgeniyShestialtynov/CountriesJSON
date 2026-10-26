package ru.academit.shestialtynov.countriesjson.countriesjson_main;

import ru.academit.shestialtynov.countriesjson.countriesjson.Country;
import ru.academit.shestialtynov.countriesjson.countriesjson.Currency;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.io.InputStream;

public class Main {
    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("countries.json");

        if (inputStream == null) {
            throw new FileNotFoundException("Файл countries.json не найден в папке resources!");
        }

        List<Country> countries = mapper.readValue(inputStream, new TypeReference<>() {
        });

        long totalPopulation = countries.stream()
                .mapToLong(Country::getPopulation)
                .sum();
        System.out.println("Суммарное население равно " + totalPopulation + " человек");

        Set<String> currencyNames = countries.stream()
                .filter(c -> c.getCurrencies() != null)
                .flatMap(c -> c.getCurrencies().stream())
                .map(Currency::getName)
                .map(name -> name != null ? name.toUpperCase().trim() : null)
                .collect(Collectors.toSet());
        System.out.println("Перечень валют: " + currencyNames);

        mapper.writeValue(
                new File("result.json"),
                countries.stream()
                        .filter(p -> p.getPopulation() >= 1000000)
                        .collect(Collectors.toList())
        );
        System.out.println("Создан файл JSON со странами с населением более 1 000 000 человек");
    }
}