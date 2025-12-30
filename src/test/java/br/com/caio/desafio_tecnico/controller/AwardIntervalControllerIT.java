package br.com.caio.desafio_tecnico.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
import br.com.caio.desafio_tecnico.repository.MovieRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(properties = "app.csv.path=csv/movielist-no-tie.csv")
class AwardIntervalControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MovieRepository repository;

    @Test
    void shouldReturn200AndValidContract() throws Exception {
        mockMvc.perform(get("/producers/awards-intervals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.min").isArray())
                .andExpect(jsonPath("$.max").isArray())
                .andExpect(jsonPath("$.min[0].producer").exists());
    }

    @Test
    void shouldReturnExpectedIntervalsWithoutTie() throws Exception {
        MvcResult result = mockMvc.perform(get("/producers/awards-intervals"))
                .andExpect(status().isOk())
                .andReturn();

        AwardsIntervalResponse actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                AwardsIntervalResponse.class
        );

        List<AwardInterval> expectedMin = List.of(
                new AwardInterval("Producer Three", 1, 2007, 2008)
        );
        List<AwardInterval> expectedMax = List.of(
                new AwardInterval("Producer Two", 4, 2001, 2005)
        );

        assertThat(actual.min()).containsExactlyInAnyOrderElementsOf(expectedMin);
        assertThat(actual.max()).containsExactlyInAnyOrderElementsOf(expectedMax);
    }

    @Test
    void shouldLoadCsvOnStartup() {
        assertThat(repository.findAllByWinnerTrueOrderByYearAsc()).isNotEmpty();
    }

}
