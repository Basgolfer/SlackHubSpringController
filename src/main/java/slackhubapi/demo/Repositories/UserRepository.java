package slackhubapi.demo.Repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import slackhubapi.demo.Models.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
