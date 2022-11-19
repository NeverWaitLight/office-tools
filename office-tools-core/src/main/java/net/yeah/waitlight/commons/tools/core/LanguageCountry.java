package net.yeah.waitlight.commons.tools.core;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;


@Getter
@AllArgsConstructor
public enum LanguageCountry {
    EN_US("en", "US"),
    ZH_CN("zh", "CN"),
    ZH_TW("zh", "TW");

    private final String country;
    private final String language;

    private static LanguageCountry systemLanguageCountry = null;

    public static LanguageCountry getSystemLanguageCountry() {
        if (Objects.isNull(systemLanguageCountry)) {
            String language = System.getProperty("user.language").toUpperCase();
            String country = System.getProperty("user.country").toUpperCase();

            systemLanguageCountry = Arrays.stream(LanguageCountry.values())
                    .filter(cl -> (language + "_" + country).equals(cl.name()))
                    .findFirst()
                    .orElseThrow();
        }
        return systemLanguageCountry;
    }
}
