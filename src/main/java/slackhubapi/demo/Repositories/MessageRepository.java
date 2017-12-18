package slackhubapi.demo.Repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import slackhubapi.demo.Models.Message;

public interface MessageRepository  extends JpaRepository<Message, Long> {

}
