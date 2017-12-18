package slackhubapi.demo.Controllers;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import slackhubapi.demo.Models.Message;
import slackhubapi.demo.Repositories.MessageRepository;

import java.net.URI;
import java.util.List;

@RestController
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    @RequestMapping(value = "messages", method = RequestMethod.GET)
    public List<Message> listAllMessages(){
        return messageRepository.findAll();
    }

    @RequestMapping(value = "messages", method = RequestMethod.POST)
    public ResponseEntity<Message> createMessage(@RequestBody Message message){
        messageRepository.saveAndFlush(message);

        URI location = UriComponentsBuilder.fromPath("http://localhost:8080/messages").build().toUri();

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(location);

        return new ResponseEntity<>(message, responseHeaders, HttpStatus.CREATED);
    }

    @RequestMapping(value = "messages/{messageId}", method = RequestMethod.GET)
    public Message getMessageById(@PathVariable Long messageId){
        return messageRepository.findOne(messageId);
    }

    @RequestMapping(value = "messages/{messageId}", method = RequestMethod.PUT)
    public Message updateMessageById(@PathVariable Long messageId, @RequestBody Message message) {
        Message existingMessage = getMessageById(messageId);
        BeanUtils.copyProperties(message, existingMessage);
        return messageRepository.saveAndFlush(existingMessage);
    }

    @RequestMapping(value = "messages/{messageId}", method = RequestMethod.DELETE)
    public Message deleteMessageById(@PathVariable Long messageId) {
        Message existingMessage = getMessageById(messageId);
        messageRepository.delete(existingMessage);
        return existingMessage;
    }
}

