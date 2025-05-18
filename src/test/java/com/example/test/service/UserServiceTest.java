package com.example.test.service;

import com.example.test.entity.User;
import com.example.test.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest
{
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User sampleUser;

    @BeforeEach
    public void setup()
    {
        sampleUser = User.builder().id(1L).username("usertest1").password("passtest1").build();
    }

    @Test
    public void createUser_ShouldReturnSavedUser()
    {
        when(userRepository.save(sampleUser)).thenReturn(sampleUser);

        User result = userService.create(sampleUser);

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("usertest1");
        verify(userRepository, times(1)).save(sampleUser);
    }

    @Test
    public void getAll_ShouldReturnListOfUsers()
    {
        List<User> users = Arrays.asList(sampleUser);
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAll();

        assertThat(result).hasSize(1);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void getById_ShouldReturnUser()
    {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));

        Optional<User> result = userService.getById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("usertest1");
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    public void getById_ShouldReturnEmpty_WhenUserNotFound()
    {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<User> result = userService.getById(999L);

        assertThat(result).isEmpty();
        verify(userRepository, times(1)).findById(999L);
    }

    @Test
    public void updateUser_ShouldReturnUpdatedUser()
    {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);

        User update = User.builder().username("updateduser").password("newpassword").build();

        User result = userService.update(1L, update);

        assertThat(result.getUsername()).isEqualTo("updateduser");
        assertThat(result.getPassword()).isEqualTo("newpassword");
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void update_ShouldReturnNull_WhenUserNotFound()
    {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        User updated = userService.update(999L, sampleUser);

        assertThat(updated).isNull();
        verify(userRepository, times(1)).findById(999L);
        verify(userRepository, never()).save(any());
    }

    @Test
    public void deleteUser_ShouldCallRepositoryDelete()
    {
        userService.delete(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }
}
