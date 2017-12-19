package slackhubapi.demo.Controllers;

import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.*;
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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.lang.reflect.Array;
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

    final Message mockMessage = new Message(0L, "Hello World", new Date(1513626638760L), 22l );

    private Message createFakeMessage(Message message) throws Exception {
        //Create a fake message for testing purposes
        String mockMessageJson = "{\"messageId\":88,\"message\":\"Hello World\",\"messageTimeStamp\":1513626638760,\"userId\":22}";
        URI location = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/messages").build().toUri();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(location);

        Mockito.when(messageController.createMessage(Mockito.any(Message.class)))
                .thenReturn(new ResponseEntity<>(message, responseHeaders, HttpStatus.CREATED));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/messages")
                .accept(MediaType.APPLICATION_JSON).content(mockMessageJson).contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //End Creating a message and adding it.
        return message;
    }

    @Test
    public void listAllMessages() throws Exception {

        URI location = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/messages").build().toUri();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(location);

        ArrayList<Message> messages = new ArrayList<>();
        messages.add(createFakeMessage(mockMessage));

        messages.add(createFakeMessage(new Message(1l, "Hello Jeff", new Date(1513626638769L), 56l)));

        //Testing list All Messages with message from above.
        Mockito.when(messageController.listAllMessages()).thenReturn(new ResponseEntity<List<Message>>(messages, responseHeaders, HttpStatus.OK));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/messages").accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String expected = "[{\"messageId\":0,\"message\":\"Hello World\",\"messageTimeStamp\":1513626638760,\"userId\":22}," +
                "{\"messageId\":1,\"message\":\"Hello Jeff\",\"messageTimeStamp\":1513626638769,\"userId\":56}]";
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
        URI location = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/messages").build().toUri();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(location);

        Mockito.when((messageController.getMessageById(1L)))

    }

    @Test
    public void updateMessageById() throws Exception {
    }

    @Test
    public void deleteMessageById() throws Exception {
    }

}