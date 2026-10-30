package com.brazcubas.apsii.config;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Testes de unidade pura para {@link DateTimeUtils#parseUtc(String)}.
 *
 * <p>Cobre os tres formatos de data aceitos (borda de entrada) e uma entrada
 * invalida (caminho triste). Classe de regra pura que a suite inicial do
 * starter nao protegia. Padrao Arrange-Act-Assert, um comportamento por teste.</p>
 */
class DateTimeUtilsTest {

    @Test
    void parsesInstantWithZuluSuffix() {
        // Arrange
        String value = "2026-08-20T19:00:00Z";

        // Act
        Instant parsed = DateTimeUtils.parseUtc(value);

        // Assert
        assertEquals(Instant.parse("2026-08-20T19:00:00Z"), parsed);
    }

    @Test
    void parsesOffsetDateTimeConvertingToUtc() {
        // Arrange: 16:00 no fuso -03:00 equivale a 19:00 em UTC
        String value = "2026-08-20T16:00:00-03:00";

        // Act
        Instant parsed = DateTimeUtils.parseUtc(value);

        // Assert
        assertEquals(Instant.parse("2026-08-20T19:00:00Z"), parsed);
    }

    @Test
    void parsesLocalDateTimeAsUtc() {
        // Arrange: data e hora sem fuso devem ser interpretadas como UTC
        String value = "2026-08-20T19:00:00";

        // Act
        Instant parsed = DateTimeUtils.parseUtc(value);

        // Assert
        assertEquals(Instant.parse("2026-08-20T19:00:00Z"), parsed);
    }

    @Test
    void rejectsInvalidInput() {
        // Caminho triste: texto que nao representa uma data deve lancar excecao
        assertThrows(DateTimeParseException.class, () -> DateTimeUtils.parseUtc("not-a-date"));
    }
}
