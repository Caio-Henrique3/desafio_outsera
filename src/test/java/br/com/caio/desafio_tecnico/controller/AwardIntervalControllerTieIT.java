package br.com.caio.desafio_tecnico.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.caio.desafio_tecnico.controller.dto.response.AwardsIntervalResponse;
import br.com.caio.desafio_tecnico.domain.dto.AwardInterval;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(properties = "app.csv.path=csv/movielist-tie.csv")
class AwardIntervalControllerTieIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnExpectedIntervalsWithTie() throws Exception {
        MvcResult result = mockMvc.perform(get("/producers/awards-intervals"))
                .andExpect(status().isOk())
                .andReturn();

        AwardsIntervalResponse actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                AwardsIntervalResponse.class
        );

        List<AwardInterval> expectedMin = List.of(
                new AwardInterval("Producer One", 1, 2000, 2001),
                new AwardInterval("Producer Two", 1, 2005, 2006)
        );
        List<AwardInterval> expectedMax = List.of(
                new AwardInterval("Producer Three", 4, 2000, 2004),
                new AwardInterval("Producer Four", 4, 2002, 2006)
        );

        assertThat(actual.min()).containsExactlyInAnyOrderElementsOf(expectedMin);
        assertThat(actual.max()).containsExactlyInAnyOrderElementsOf(expectedMax);
    }

}
