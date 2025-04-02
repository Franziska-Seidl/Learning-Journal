package com.academy.learning_journal_team3;

import com.academy.learning_journal_team3.entity.TeachingClass;
import com.academy.learning_journal_team3.entity.Topic;
import com.academy.learning_journal_team3.entity.User;
import com.academy.learning_journal_team3.repository.TeachingClassRepository;
import com.academy.learning_journal_team3.repository.TopicsRepository;
import com.academy.learning_journal_team3.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableScheduling
public class LearningJournalTeam3Application {

	public static void main(String[] args) {
		SpringApplication.
				run(LearningJournalTeam3Application.class, args);
	}

        @Bean
        public CommandLineRunner demo(TeachingClassRepository teachingClassRepository, UserRepository userRepository,
									  TopicsRepository topicsRepository, PasswordEncoder passwordEncoder) {

		return (args) -> {
			if (teachingClassRepository.count() == 0 && userRepository.count() == 0 && topicsRepository.count() == 0) {
				System.out.println("Hallöchen");

				TeachingClass teachingClass1 = TeachingClass.builder().name("Schulklasse 1").build();
				TeachingClass teachingClass2 = TeachingClass.builder().name("Schulklasse 2").build();
				TeachingClass teachingClass3 = TeachingClass.builder().name("Schulklasse 3").build();
				teachingClassRepository.save(teachingClass1);
				teachingClassRepository.save(teachingClass2);
				teachingClassRepository.save(teachingClass3);

				User user1 = User.builder().firstName("Andrey").lastName("Makarov").email("am@am.com")
						.password(passwordEncoder.encode("123456Am")).role("ADMIN").build();
				userRepository.save(user1);
				User user2 = User.builder().firstName("Franziska").lastName("Seidl").email("fs@fs.com")
						.password(passwordEncoder.encode("123456Fs")).role("USER").build();
				userRepository.save(user2);
				User user3 = User.builder().firstName("Verena").lastName("Nowak").email("vn@vn.com")
						.password(passwordEncoder.encode("123456Vn")).role("USER").build();
				userRepository.save(user3);

				Topic topic1 = Topic.builder().name("SQL").build();
				Topic topic2 = Topic.builder().name("Java").build();
				Topic topic3 = Topic.builder().name("BWL").build();
				Topic topic4 = Topic.builder().name("Netzwerktechnik").build();
				topicsRepository.save(topic1);
				topicsRepository.save(topic2);
				topicsRepository.save(topic3);
				topicsRepository.save(topic4);
			}
		};
	}
}


