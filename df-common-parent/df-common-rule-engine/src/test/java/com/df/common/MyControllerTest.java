package com.df.common;

import com.df.common.MyController;
import com.df.common.UserReq;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class MyControllerTest {

    @InjectMocks
    private MyController myController;

    @Mock
    private MockHttpServletRequest mockHttpServletRequest;

    @Mock
    private MockHttpServletResponse mockHttpServletResponse;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testHandleRequest() throws ServletException, IOException {
        // Arrange
        String paramName = "paramName";
        String paramValue = "paramValue";
        when(mockHttpServletRequest.getParameter(paramName)).thenReturn(paramValue);

        // Act
        String result = myController.handleRequest(mockHttpServletRequest, mockHttpServletResponse);

        // Assert
        assertEquals("处理结果", result);
    }

    @Test
    public void testRequest() {
        // Arrange
        UserReq req = new UserReq();

        // Act
        String result = myController.request(req);

        // Assert
        assertEquals("{\"key1\":null,\"key2\":null}", result);
    }

    @Test
    public void testRequest1() {
        // Arrange
        String key1 = "key1";
        String key2 = "key2";

        // Act
        String result = myController.request1(key1, key2);

        // Assert
        assertEquals("key1key2", result);
    }

    @Test
    public void testRequest2() {
        // Arrange
        String key1 = "key1";
        String key2 = "key2";

        // Act
        String result = myController.request2(key1, key2);

        // Assert
        assertEquals("key1key2", result);
    }
}