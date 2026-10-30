package com.brazcubas.apsii.service;

import com.brazcubas.apsii.config.ActivityRules.Activity;
import com.brazcubas.apsii.config.ActivityRules.Recommendation;
import com.brazcubas.apsii.config.QualityRules.Quality;
import com.brazcubas.apsii.service.RecommendationService.ActivityRecommendation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testes de unidade pura para {@link RecommendationService#forActivity}.
 *
 * <p>O alvo recebe apenas valores de enum, entao nao ha caminho triste: os
 * casos cobrem regras de dominio e limites de faixa de qualidade por atividade.
 * Padrao Arrange-Act-Assert, um comportamento por teste.</p>
 */
class RecommendationServiceTest {
    private final RecommendationService service = new RecommendationService();

    @Test
    void messagingIsSuitableEvenWhenConnectionIsUnstable() {
        // Regra de dominio: mensagens aceitam ate qualidade UNSTABLE.
        ActivityRecommendation result = service.forActivity(Activity.MESSAGING, Quality.UNSTABLE);

        assertTrue(result.suitable());
        assertEquals(Recommendation.PREPARE_OFFLINE, result.recommendation().code());
    }

    @Test
    void videoCallIsNotSuitableWhenConnectionIsUnstable() {
        // Limite de faixa: chamada de video exige qualidade GOOD; UNSTABLE nao atende.
        ActivityRecommendation result = service.forActivity(Activity.VIDEO_CALL, Quality.UNSTABLE);

        assertFalse(result.suitable());
    }

    @Test
    void videoCallIsSuitableAndRecommendsNormalUseWhenGood() {
        // Borda superior: com qualidade GOOD a atividade e adequada e o uso e normal.
        ActivityRecommendation result = service.forActivity(Activity.VIDEO_CALL, Quality.GOOD);

        assertTrue(result.suitable());
        assertEquals(Recommendation.NORMAL_USE, result.recommendation().code());
    }
}
