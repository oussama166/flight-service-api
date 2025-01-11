package org.jetblue.jetblue.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetblue.jetblue.Models.DAO.Airline;
import org.jetblue.jetblue.Repositories.AirlineRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@ExtendWith(MockitoExtension.class)
class AirlineControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AirlineRepo airlineRepo;

    @InjectMocks
    private AirlineController airlineController;

    private JacksonTester<Airline> jsonAirline;


    @BeforeEach
    void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());
        this.mockMvc = MockMvcBuilders.standaloneSetup(airlineController).build();
    }


    @Test
    void canCreateNewAirline() throws Exception {
        MockHttpServletResponse res = mockMvc.perform(
                post("/setAirline").contentType(MediaType.ALL_VALUE).content(
                        jsonAirline.write(
                                new Airline().builder()
                                        .airlineName("Aer Lingus UK")
                                        .airlineCode("EUK")
                                        .airlineUrl("https://www.aerlingus.com/html/en-IE/home.html")
                                        .airlineLogoLink("https://mediacentre.aerlingus.com/contents/archives/459/108/images/thumb1200x628_width/aerlingus_459108618666105_thumb.png")
                                        .colFormation(5)
                                        .rowFormation(70)
                                        .build()
                        ).getJson())).andReturn().getResponse();

        assertThat(res.getStatus()).isEqualTo(200);
    }

    @Test
    void canGetAirlineByTheName() throws Exception {
        MockHttpServletResponse res = mockMvc.perform(get("/getAirline/EUK").accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        assertThat(res.getStatus()).isEqualTo(200);
        assertThat(res.getContentAsString()).isEqualTo(
                jsonAirline.write(
                        new Airline().builder()
                                .airlineName("Aer Lingus UK")
                                .airlineCode("EUK")
                                .airlineUrl("https://www.aerlingus.com/html/en-IE/home.html")
                                .airlineLogoLink("https://mediacentre.aerlingus.com/contents/archives/459/108/images/thumb1200x628_width/aerlingus_459108618666105_thumb.png")
                                .colFormation(5)
                                .rowFormation(70)
                                .build()
                ).toString()
        );

    }
    @Test
    void canGetAllAirlines() throws Exception {
        MockHttpServletResponse res = mockMvc.perform(
          get("/getAllAirlines").accept(MediaType.ALL)
        ).andReturn().getResponse();

        assertThat(res.getStatus()).isEqualTo(200);
    }


}