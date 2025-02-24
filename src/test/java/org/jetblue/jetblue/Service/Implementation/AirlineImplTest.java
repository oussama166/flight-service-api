package org.jetblue.jetblue.Service.Implementation;

import org.jetblue.jetblue.Mapper.Airline.AirlineMapper;
import org.jetblue.jetblue.Mapper.Airline.AirlineResponse;
import org.jetblue.jetblue.Mapper.Airplane.AirplaneMapper;
import org.jetblue.jetblue.Models.DAO.Airline;
import org.jetblue.jetblue.Repositories.AirlineRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the AirlineImpl service class.<br>
 * <p>
 * This class contains tests for the following methods in the AirlineImpl class:<br>
 * - setAirline: Ensures a new airline can be saved when it doesn't exist already.<br>
 * - getAirline: Retrieves airline information by airline code or name.<br>
 */
@ExtendWith(MockitoExtension.class)
class AirlineImplTest {

    @Mock
    private AirlineRepo airlineRepo;

    @Mock
    private AirplaneMapper airplaneMapper;

    @InjectMocks
    private AirlineImpl airlineService;

    private Airline airline;

    /**
     * Setup method to initialize a new Airline object before each test.<br>
     * This ensures we have a consistent object for all test cases.<br>
     */
    @BeforeEach
    void setUp() {
        airline = Airline.builder()
                .airlineName("JetBlue Airline")
                .airlineCode("JET")
                .airlineLogoLink("https://jetblue.org")
                .airlineUrl("https://jetblue.org")
                .build();
    }


    /// ============================== Test cases of the setAirline function ============================== ///

    /**
     * Tests that the airline information is saved correctly when it does not already exist in the database.<br>
     * - Mocks the repository to return an empty result for the airline's name.<br>
     * - Verifies that the airline is saved and the correct airline name is returned.<br>
     */
    @Test
    @DisplayName(value = "Should return the airline information saved by the user")
    void setAirline_test_should_return_name_of_airline() {
        when(airlineRepo.findByAirlineName(airline.getAirlineName())).thenReturn(Optional.empty());

        AirlineResponse actual = airlineService.setAirline(airline);

        assertNotNull(actual);
        assertEquals(airline.getAirlineName(), actual.airlineName());


        verify(airlineRepo, times(1)).save(airline);
    }

    /**
     * Tests that the service returns null when the airline already exists in the database.<br>
     * This ensures that duplicate airlines are not saved.<br>
     */
    @Test
    @DisplayName(value = "Should return null when the airline already exist in the database")
    void setAirline_test_should_return_null_when_the_airline_exist() {
        when(airlineRepo.findByAirlineName(airline.getAirlineName())).thenReturn(Optional.of(airline));

        AirlineResponse actual = airlineService.setAirline(airline);

        assertNull(actual);
        verify(airlineRepo, times(1)).findByAirlineName(airline.getAirlineName());
    }

    /// ============================== Test cases of the setAirline function ============================== ///

    /**
     * getAirline
     *
     * @param String airlineCode_Name
     * @retun airline
     */

    @Test
    @DisplayName(value = "Should return null when the airline name is null or undefined")
    void getAirline_test_should_return_null_when_airline_name_is_null_or_undefined() {
        AirlineResponse actual = airlineService.getAirline(null);
        assertNull(actual);
    }

    @Test
    @DisplayName(value = "Should return null when the airline name is blank or empty string")
    void getAirline_test_should_return_null_when_airline_name_is_blank_or_empty_string() {
        AirlineResponse actual = airlineService.getAirline("");
        assertNull(actual);
    }

    @Test
    @DisplayName(value = "Should return the airline info depend on the airline code that exist")
    void getAirline_should_return_airline_depend_on_airline_code_exist() {
        when(airlineRepo.findByAirlineCode(airline.getAirlineCode())).thenReturn(Optional.of(airline));

        AirlineResponse actual = airlineService.getAirline(airline.getAirlineCode());

        assertNotNull(actual);
        assertSame(airline, actual);

        verify(airlineRepo, times(1)).findByAirlineCode(airline.getAirlineCode());
    }

    @Test
    @DisplayName(value = "Should return null when the airline code is not exist in data base")
    void getAirline_should_return_airline_depend_on_airline_code_not_exist() {
        when(airlineRepo.findByAirlineCode(airline.getAirlineCode())).thenReturn(Optional.empty());

        AirlineResponse actual = airlineService.getAirline(airline.getAirlineCode());

        assertNull(actual);
        verify(airlineRepo, times(1)).findByAirlineCode(airline.getAirlineCode());
    }
}