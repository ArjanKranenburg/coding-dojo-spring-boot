package com.assignment.weather.service.openweather;

import com.assignment.weather.exception.InternalServerException;
import com.assignment.weather.exception.LocationNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class RestTemplateResponseErrorHandlerTest {

    @Mock
    private ClientHttpResponse httpResponseMock;

    private RestTemplateResponseErrorHandler errorHandler;

    @BeforeEach
    public void initialize() {

        errorHandler = new RestTemplateResponseErrorHandler();
    }

    @Test
    void when_response_has_a_client_error_then_hasError_is_true() throws IOException {

        // prepare
        when(httpResponseMock.getStatusCode()).thenReturn(HttpStatus.NOT_FOUND);

        // act & check
        assertTrue(errorHandler.hasError(httpResponseMock));
    }

    @Test
    void when_response_has_a_server_error_then_hasError_is_true() throws IOException {

        // prepare
        when(httpResponseMock.getStatusCode()).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR);

        // act & check
        assertTrue(errorHandler.hasError(httpResponseMock));
    }

    @Test
    void when_response_is_OK_then_hasError_is_false() throws IOException {

        // prepare
        when(httpResponseMock.getStatusCode()).thenReturn(HttpStatus.OK);

        // act & check
        assertFalse(errorHandler.hasError(httpResponseMock));
    }

    @Test
    void when_response_has_not_found_status_then_handleError_throws_LocationNotFoundException() throws IOException {

        // prepare
        when(httpResponseMock.getStatusCode()).thenReturn(HttpStatus.NOT_FOUND);

        // act & check
        assertThrows(LocationNotFoundException.class, () -> errorHandler.handleError(httpResponseMock));
    }

    @Test
    void when_response_has_other_erroneous_status_then_handleError_throws_InternalServerException() throws IOException {

        // prepare
        when(httpResponseMock.getStatusCode()).thenReturn(HttpStatus.GATEWAY_TIMEOUT);

        // act & check
        assertThrows(InternalServerException.class, () -> errorHandler.handleError(httpResponseMock));
    }
}
