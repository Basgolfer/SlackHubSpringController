package slackhubapi.demo.Controllers;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import slackhubapi.demo.Models.Message;
import slackhubapi.demo.Repositories.MessageRepository;

import javax.xml.ws.Response;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    @RequestMapping(value = "/messages", method = RequestMethod.GET)
    public ResponseEntity<List<Message>>listAllMessages(){
        List<Message> allMessages =  messageRepository.findAll();
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(location);
        return new ResponseEntity<>(allMessages, responseHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "/messages", method = RequestMethod.POST)
    public ResponseEntity<Message> createMessage(@RequestBody Message newMessage){
        Message message = messageRepository.saveAndFlush(newMessage);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(message.getMessageId()).toUri();

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(location);

        return new ResponseEntity<>(message, responseHeaders, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/messages/{messageId}", method = RequestMethod.GET)
    public ResponseEntity<Message> getMessageById(@PathVariable Long messageId){
        Message messageToReturn = messageRepository.findOne(messageId);
        if (messageToReturn == null) {
            return new ResponseEntity<>( HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(messageToReturn, HttpStatus.FOUND);
    }

    @RequestMapping(value = "/messages/{messageId}", method = RequestMethod.PUT)
    public ResponseEntity<Message> updateMessageById(@PathVariable Long messageId, @RequestBody Message message) {
        Message existingMessage = getMessageById(messageId).getBody();
        BeanUtils.copyProperties(message, existingMessage);
        messageRepository.saveAndFlush(existingMessage);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
//
//    @RequestMapping(value = "messages/{messageId}", method = RequestMethod.DELETE)
//    public Message deleteMessageById(@PathVariable Long messageId) {
//        Message existingMessage = getMessageById(messageId);
//        messageRepository.delete(existingMessage);
//        return existingMessage;
//    }
}

