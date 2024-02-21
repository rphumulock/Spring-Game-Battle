package demosecurity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemosecurityApplication {

  private static final Logger logger = LoggerFactory.getLogger(DemosecurityApplication.class);


  public static void main(String[] args) {
    SpringApplication.run(DemosecurityApplication.class, args);
  }
}
