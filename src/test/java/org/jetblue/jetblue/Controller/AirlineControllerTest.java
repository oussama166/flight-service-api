package org.jetblue.jetblue.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetblue.jetblue.Config.SecurityConfiguration;
import org.jetblue.jetblue.Mapper.Airline.AirlineMapper;
import org.jetblue.jetblue.Mapper.Airline.AirlineRequest;
import org.jetblue.jetblue.Mapper.Airline.AirlineResponse;
import org.jetblue.jetblue.Models.DAO.Airline;
import org.jetblue.jetblue.Service.AirlineService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AirlineController.class)
@Import(SecurityConfiguration.class)
@ExtendWith(MockitoExtension.class)
class AirlineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
    @MockBean
    AirlineService airlineService;


    @InjectMocks
    private AirlineController controller;

    @Autowired
    private ObjectMapper mapper;

    static AirlineRequest airline;

    @BeforeAll
    static void setUp() {
        airline = new AirlineRequest("JetBlue", "JET", "www.jetblue.com", 5, 5, "www.jetblue.com");
    }


    @Test
    @DisplayName("Should return 404 when the airline insertion fails")
    void shouldReturn404WhenAirlineInsertionFails() throws Exception {
        when(airlineService.setAirline(AirlineMapper.toAirline(airline))).thenReturn(null);

        mockMvc.perform(post("/setAirline").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(airline))).andExpect(status().isNotFound()); // Expect 404 instead of 200
    }

    @Test
    @DisplayName("Should return 200 with airlineResponse payload when the airline insertion success")
    void testSetAirline_ValidRequest() throws Exception {
        AirlineRequest airlineRequest = new AirlineRequest("Test Airline", "TA", "TestUrl", 5, 5, "www.jetblue.com");
        AirlineResponse airlineResponse = new AirlineResponse("Test Airline", "TA", "TestUrl", "UrlTest", 5, 5);


        when(airlineService.setAirline(any(Airline.class))).thenReturn(airlineResponse);

        mockMvc.perform(
                post("/setAirline")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(airlineRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(airlineResponse)))
                .andDo(print());
    }


    @Test
    @DisplayName("Should return 200 with null payload when the airline insertion failed")
    void shouldReturn200WhenAirlineIsInsertionIsFailed() throws Exception {

        when(airlineService.setAirline(AirlineMapper.toAirline(airline))).thenReturn(null);


        mockMvc.perform(
                post("/setAirline")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(airline)))
                .andExpect(status().isNotFound())
                .andDo(print());

    }

    @Test
    @DisplayName("Should return 200 OK when airline is found")
    void should_return_200_when_airline_is_found() throws Exception {
        AirlineResponse airline = new AirlineResponse("Royal Air Maroc", "RAM", "WWW.google.com", "WWW.google.com", 2, 5

        );

        when(airlineService.getAirline("RAM")).thenReturn(airline);

        mockMvc.perform(get("/getAirline/{airline}", "RAM").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().string(mapper.writeValueAsString(airline)));
    }


    @Test
    @DisplayName("Should return 400 Bad request when the airline not found")
    void should_return_400_when_airline_not_found() throws Exception {
        AirlineResponse airline = new AirlineResponse("JetBlue", "JET", "www.jetblue.com", "www.jetblue.com", 5, 5);

        when(airlineService.getAirline("JET")).thenReturn(null);

        mockMvc.perform(get("/getAirline/{airline}", "JET").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andExpect(content().string("Airline not found!!!"));
    }
}
