package com.academy.learning_journal_team3.service;

import com.academy.learning_journal_team3.entity.User;
import com.academy.learning_journal_team3.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class PasswordResetService {
    @Autowired
    private UserService userService;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String ALL_ALLOWED = LOWERCASE + UPPERCASE + DIGITS;
    private static final int PASSWORD_LENGTH = 12;
    private static final SecureRandom RANDOM = new SecureRandom();

//    @Value("${app.base-url}")
//    private String baseUrl;

    @Autowired
    private UserRepository userRepository;

    public void sendPasswordResetEmail(String email) {
        System.out.println("email: " + email);
        User user = userService.findByEmail(email);
        System.out.println("user: " + user);

        String temporaryPassword = generatePassword();
        System.out.println("temporaryPassword: " + temporaryPassword);
        System.out.println("passwordEncoder: " + passwordEncoder.encode(temporaryPassword));

        user.setPassword(passwordEncoder.encode(temporaryPassword));
        userRepository.save(user);

        sendResetPasswordEmail(email, temporaryPassword);
    }

    private void sendResetPasswordEmail(String email, String temporaryPassword) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Temporäres Passwort");
        mailMessage.setText("Ihr temporäres Passwort: " + temporaryPassword +
                "\nBitte ändern Sie es nach dem Einloggen.");

        mailSender.send(mailMessage);
    }

    public static String generatePassword() {
        StringBuilder password = new StringBuilder();

        // Добавляем по одному обязательному символу из каждого набора - Wir fügen jeweils ein obligatorisches Zeichen aus jedem Satz hinzu
        password.append(LOWERCASE.charAt(RANDOM.nextInt(LOWERCASE.length())));
        password.append(UPPERCASE.charAt(RANDOM.nextInt(UPPERCASE.length())));
        password.append(DIGITS.charAt(RANDOM.nextInt(DIGITS.length())));

        // Заполняем оставшиеся символы случайными из всех разрешенных -Wir füllen die verbleibenden Symbole mit zufälligen aus allen erlaubten
        for (int i = 3; i < PASSWORD_LENGTH; i++) {
            password.append(ALL_ALLOWED.charAt(RANDOM.nextInt(ALL_ALLOWED.length())));
        }

        // Перемешиваем символы для большей случайности - Wir mischen die Symbole für mehr Zufälligkeit.
        return shuffleString(password.toString());
    }

    private static String shuffleString(String input) {
        char[] characters = input.toCharArray();
        for (int i = characters.length - 1; i > 0; i--) {
            int j = RANDOM.nextInt(i + 1);
            char temp = characters[i];
            characters[i] = characters[j];
            characters[j] = temp;
        }
        return new String(characters);
    }
}