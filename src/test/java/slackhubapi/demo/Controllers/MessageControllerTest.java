package slackhubapi.demo.Controllers;

import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import slackhubapi.demo.Models.*;
import slackhubapi.demo.Repositories.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@WebMvcTest(value = MessageController.class, secure = false)
public class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageRepository messageRepository;

    @MockBean
    private MessageController messageController;

    Date date = new Date(1513626638760L);

    Message mockMessage = new Message(0L, "Hello World", date, 0L );

    String mockMessageJson = "{\"messageId\":0,\"message\":\"Hello World\",\"messageTimeStamp\":null,\"userId\":22}";



//    @Test
//    public void listAllMessages() throws Exception {
//        Mockito.when(messageController.createMessage(Mockito.any(Message.class))).thenReturn(mockMessage);
//       messageController.createMessage(mockMessage);
//
//       RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/messages").accept(MediaType.APPLICATION_JSON);
//       MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//      String expected = "{\"messageId\":0,\"message\":\"Hello World\",\"messageTimeStamp\":date,\"userId\":0}";
//      // String expected = "";
//       JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
//    }

    @Test
    public void createMessage() throws Exception {
        Mockito.when(messageController.createMessage(Mockito.any(Message.class))).thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/messages")
        .accept(MediaType.APPLICATION_JSON).content(mockMessageJson).contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals("http://localhost:8080/messages", response.getHeader(HttpHeaders.LOCATION));



    }

    @Test
    public void getMessageById() throws Exception {
    }

    @Test
    public void updateMessageById() throws Exception {
    }

    @Test
    public void deleteMessageById() throws Exception {
    }

}