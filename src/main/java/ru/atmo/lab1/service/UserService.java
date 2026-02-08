package ru.atmo.lab1.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import ru.atmo.lab1.entity.User;
import ru.atmo.lab1.repository.UserRepository;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    /**
     * Сохранение пользователя
     */
    @Transactional(rollbackFor = Exception.class)
    public void save(User user) {
        userRepository.save(user);
    }


    /**
     * Создание пользователя
     */
    @Transactional(rollbackFor = Exception.class)
    public void create(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new IllegalStateException("Пользователь с таким именем уже существует");
        }
        save(user);
    }

    /**
     * Получение пользователя по имени пользователя
     *
     * @return пользователь
     */
    @Transactional(readOnly = true)
    public User getByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) throw new EntityNotFoundException("Пользователь не найден");
        return user;
    }

    /**
     * Получение пользователя по имени пользователя
     * <p>
     * Нужен для Spring Security
     *
     * @return пользователь
     */
    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    /**
     * Получение текущего пользователя
     *
     * @return текущий пользователь
     */
    public User getCurrentUser() {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }
}
