package slackhubapi.demo.Controllers;

import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;
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

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@WebMvcTest(value = MessageController.class, secure = false)
public class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

//    @MockBean
//    private MessageRepository messageRepository;

    @MockBean
    private MessageController messageController;

    final Date date = new Date(1513626638760L);

    final Message mockMessage = new Message(0L, "Hello World", date, 22l );


    @Test
    public void listAllMessages() throws Exception {
        //Create a fake message for testing purposes
        String mockMessageJson = "{\"messageId\":88,\"message\":\"Hello World\",\"messageTimeStamp\":1513626638760,\"userId\":22}";
        URI location = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/messages").build().toUri();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(location);

        Mockito.when(messageController.createMessage(Mockito.any(Message.class)))
                .thenReturn(new ResponseEntity<>(mockMessage, responseHeaders, HttpStatus.CREATED));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/messages")
                .accept(MediaType.APPLICATION_JSON).content(mockMessageJson).contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        ArrayList<Message> messages = new ArrayList<>();
        messages.add(mockMessage);

        //End Creating a message and adding it.

        //Testing list All Messages with message from above.
        Mockito.when(messageController.listAllMessages()).thenReturn(new ResponseEntity<List<Message>>(messages, responseHeaders, HttpStatus.OK));
        requestBuilder = MockMvcRequestBuilders.get("/messages").accept(MediaType.APPLICATION_JSON);
        result = mockMvc.perform(requestBuilder).andReturn();
        String expected = "[{\"messageId\":0,\"message\":\"Hello World\",\"messageTimeStamp\":1513626638760,\"userId\":22}]";
        System.out.println("Expected = " + expected + "\n");
        System.out.println("result = " + result.getResponse().getContentAsString());
        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

    @Test
    public void createMessage() throws Exception {
        String mockMessageJson = "{\"messageId\":88,\"message\":\"Hello World\",\"messageTimeStamp\":1513626638760,\"userId\":22}";
        URI location = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/messages").build().toUri();

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(location);

        Mockito.when(messageController.createMessage(Mockito.any(Message.class)))
        .thenReturn(new ResponseEntity<>(mockMessage, responseHeaders, HttpStatus.CREATED));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/messages")
        .accept(MediaType.APPLICATION_JSON).content(mockMessageJson).contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals("http://localhost:8080/messages", response.getHeader(HttpHeaders.LOCATION));
        String expected = "{\"messageId\":0,\"message\":\"Hello World\",\"messageTimeStamp\":1513626638760,\"userId\":22}";
        System.out.println("Expected = " + expected);
        System.out.println("Result = " + result.getResponse().getContentAsString());
        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
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