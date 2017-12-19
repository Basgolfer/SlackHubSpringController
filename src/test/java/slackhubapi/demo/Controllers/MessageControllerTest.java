package slackhubapi.demo.Controllers;

import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.*;
import org.springframework.mock.web.MockHttpServletRequest;
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

    @MockBean
    private MessageController messageController;

    final Message mockMessage = new Message(0L, "Hello World", new Date(1513626638760L), 22l );

    private Message createFakeMessage(Message message) throws Exception {
        String mockMessageJson = "{\"messageId\":88,\"message\":\"Hello World\",\"messageTimeStamp\":1513626638760,\"userId\":22}";
        URI location = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/messages").build().toUri();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(location);

        Mockito.when(messageController.createMessage(Mockito.any(Message.class)))
                .thenReturn(new ResponseEntity<>(message, responseHeaders, HttpStatus.CREATED));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/messages")
                .accept(MediaType.APPLICATION_JSON).content(mockMessageJson).contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder).andReturn();

        return message;
    }

    @Test
    public void listAllMessagesTest() throws Exception {

        URI location = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/messages").build().toUri();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(location);

        ArrayList<Message> messages = new ArrayList<>();
        messages.add(createFakeMessage(mockMessage));

        messages.add(createFakeMessage(new Message(1L, "Hello Jeff", new Date(1513626638769L), 56L)));

        //Testing list All Messages with message from above.
        Mockito.when(messageController.listAllMessages()).thenReturn(new ResponseEntity<List<Message>>(messages, responseHeaders, HttpStatus.OK));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/messages").accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();

        String expected = "[{\"messageId\":0,\"message\":\"Hello World\",\"messageTimeStamp\":1513626638760,\"userId\":22}," +
                "{\"messageId\":1,\"message\":\"Hello Jeff\",\"messageTimeStamp\":1513626638769,\"userId\":56}]";
        System.out.println("Expected = " + expected + "\n");
        System.out.println("result = " + result.getResponse().getContentAsString());

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("http://localhost:8080/messages", response.getHeader(HttpHeaders.LOCATION));

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

    @Test
    public void createMessageTest() throws Exception {
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
        String expected = "{\"messageId\":0,\"message\":\"Hello World\",\"messageTimeStamp\":1513626638760,\"userId\":22}";
        System.out.println("Expected = " + expected);
        System.out.println("Result = " + result.getResponse().getContentAsString());

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals("http://localhost:8080/messages", response.getHeader(HttpHeaders.LOCATION));
        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

    @Test
    public void getMessageByIdTestMessageIdExists() throws Exception {
        String mockMessageJson = "{\"messageId\":0,\"message\":\"Hello Brian\",\"messageTimeStamp\":1513626638769,\"userId\":69}";
        URI location = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/messages/0").build().toUri();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(location);

        Message messageToGet = createFakeMessage(new Message(0L, "Hello Brian", new Date(1513626638769L), 69L));

        Mockito.when((messageController.getMessageById(0L)))
                .thenReturn(new ResponseEntity<>(messageToGet, responseHeaders, HttpStatus.FOUND));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/messages/0")
                .accept(MediaType.APPLICATION_JSON).content(mockMessageJson).contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String expected = mockMessageJson;

        assertEquals(HttpStatus.FOUND.value(), response.getStatus());
        assertEquals("http://localhost:8080/messages/0", response.getHeader(HttpHeaders.LOCATION));
        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

    @Test
    public void getMessageByIdTestMessageIdDoesNotExist() throws Exception {
        String mockMessageJson = "";
        URI location = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/messages/100").build().toUri();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(location);

        Message messageToGet = null;

        Mockito.when(messageController.getMessageById(100L))
                .thenReturn(new ResponseEntity<Message>(messageToGet, responseHeaders, HttpStatus.NO_CONTENT));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/messages/100")
                .accept(MediaType.APPLICATION_JSON).content(mockMessageJson).contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();

        String expected = mockMessageJson;

        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
        assertEquals("http://localhost:8080/messages/100", response.getHeader(HttpHeaders.LOCATION));
    }

    @Test
    public void updateMessageByIdTest() throws Exception {
        String mockMessageJson = "{\"messageId\":1,\"message\":\"Hello Brian\",\"messageTimeStamp\":1513626638769,\"userId\":85}";

        Message messageToUpdate = createFakeMessage(new Message(1L, "Hello Brian", new Date(1513626638769L), 85L));

        Message updatedMessage = new Message(1L, "Tariq is friggin awesome!", new Date(1513626638779L), 85L);

        String updatedMessageJson = "{\"messageId\":1,\"message\":\"Tariq is friggin awesome!\",\"messageTimeStamp\":1513626638779,\"userId\":85}";

        Mockito.when(messageController.updateMessageById(1L, updatedMessage))
                .thenReturn(new ResponseEntity<>(updatedMessage, HttpStatus.OK));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/messages/1")
                .accept(MediaType.APPLICATION_JSON).content(updatedMessageJson).contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();

        String expected = mockMessageJson;

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        expected = "{\"messageId\":1,\"message\":\"Tariq is friggin awesome!\",\"messageTimeStamp\":1513626638769,\"userId\":85}";
        System.out.println("expected = " + expected);
        System.out.println("Response = " + response.getContentAsString());
        JSONAssert.assertEquals(expected, response.getContentAsString(), false);
    }

    @Test
    public void deleteMessageById() throws Exception {
    }

}