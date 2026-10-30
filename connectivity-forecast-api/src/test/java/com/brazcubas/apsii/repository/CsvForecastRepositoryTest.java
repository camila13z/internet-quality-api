package com.brazcubas.apsii.repository;

import com.brazcubas.apsii.model.Forecast;
import com.brazcubas.apsii.model.PredictionModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.DefaultResourceLoader;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Prova, pela via publica do repositorio, como a leitura do campo opcional
 * model_confidence se comporta ao carregar o CSV. Nunca chama metodo privado:
 * exercita load(), findAll() e findByModelAndProbe(...). Este teste protege a
 * extracao da regra (parseOptionalConfidence) feita em parseRow.
 *
 * <p>Usa um CSV de teste dedicado (data-test/predictions-11-2.csv) e um duble
 * (Mockito) de ModelRepository, no mesmo padrao do teste de servico do starter.</p>
 */
@ExtendWith(MockitoExtension.class)
class CsvForecastRepositoryTest {

    private static final double DELTA = 1e-9;
    private static final String TEST_CSV = "classpath:data-test/predictions-11-2.csv";

    @Mock
    private ModelRepository modelRepository;

    private CsvForecastRepository repository;

    @BeforeEach
    void setUp() {
        // O duble conhece apenas model-a e model-b (model-zz do CSV e descartado).
        when(modelRepository.findAll()).thenReturn(List.of(model("model-a"), model("model-b")));
        repository = new CsvForecastRepository(new DefaultResourceLoader(), modelRepository, TEST_CSV);
        repository.load();
    }

    @Test
    void loadsOnlyRowsWithKnownModelAndValidConfidence() {
        // 4 linhas validas de model-a + 1 de model-b = 5.
        // Descartadas: confianca 1.5, texto "abc", -0.1 e o modelo desconhecido model-zz.
        assertEquals(5, repository.findAll().size());
    }

    @Test
    void keepsConfidenceBordersAndBlankOnModelATimeline() {
        List<Forecast> timeline = repository.findByModelAndProbe("model-a", 900001);

        assertEquals(4, timeline.size());
        // Ordenadas por prediction_for (00:00, 01:00, 02:00, 03:00).
        assertEquals(0.82, timeline.get(0).modelConfidence(), DELTA); // valor valido
        assertNull(timeline.get(1).modelConfidence());                // em branco -> null
        assertEquals(0.0, timeline.get(2).modelConfidence(), DELTA);  // borda inferior aceita
        assertEquals(1.0, timeline.get(3).modelConfidence(), DELTA);  // borda superior aceita
    }

    @Test
    void dropsRowsWithConfidenceOutOfRangeOrNonNumeric() {
        List<Forecast> timeline = repository.findByModelAndProbe("model-a", 900001);

        // As 3 linhas invalidas (1.5, "abc", -0.1) nao entram na timeline;
        // toda confianca presente fica null ou dentro de [0, 1].
        assertEquals(4, timeline.size());
        assertTrue(timeline.stream().allMatch(forecast ->
                forecast.modelConfidence() == null
                        || (forecast.modelConfidence() >= 0.0 && forecast.modelConfidence() <= 1.0)));
    }

    private static PredictionModel model(String id) {
        return new PredictionModel(id, "Modelo " + id, "Descricao de exemplo",
                "Grupo", "Teste", "1.0", true, null);
    }
}
