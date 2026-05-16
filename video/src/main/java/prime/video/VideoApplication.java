package prime.video;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VideoApplication {

	public static void main(String[] args) {
		System.out.println("###################################################");
		System.out.println("Starting VideoApplication...");
		System.out.println("Working Directory: " + System.getProperty("user.dir"));
		System.out.println("###################################################");
		SpringApplication.run(VideoApplication.class, args);
	}

}
