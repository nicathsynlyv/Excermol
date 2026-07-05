package com.example.Excermol.Service;

import com.example.Excermol.Service.impl.UserServiceImpl;
import com.example.Excermol.entity.User;
import com.example.Excermol.entity.dtos.UserRequestDto;
import com.example.Excermol.entity.dtos.UserResponseDto;
import com.example.Excermol.enums.UserRole;
import com.example.Excermol.enums.UserStatus;
import com.example.Excermol.exception.EmailAlreadyExistsException;
import com.example.Excermol.exception.UserNotFoundException;
import com.example.Excermol.mapper.UserMapper;
import com.example.Excermol.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserServiceImplTest Unit Tests")
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserRequestDto requestDto;
    private UserResponseDto responseDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setFullName("Nicat Aliyev");
        user.setEmail("nicat@example.com");
        user.setPassword("password123");
        user.setStatus(UserStatus.ACTIVE);
        user.setRole(UserRole.USER);

        requestDto = new UserRequestDto();
        requestDto.setFullName("Nicat Aliyev");
        requestDto.setEmail("nicat@example.com");
        requestDto.setPassword("password123");
        requestDto.setStatus(UserStatus.ACTIVE);
        requestDto.setRole(UserRole.USER);

        responseDto = new UserResponseDto();
        responseDto.setId(1L);
        responseDto.setFullName("Nicat Aliyev");
        responseDto.setEmail("nicat@example.com");
        responseDto.setStatus(UserStatus.ACTIVE);
        responseDto.setRole(UserRole.USER);
    }

    // ---------- getAll ----------

    @Test
    void getAll_success() {
        List<User> users = List.of(user);
        List<UserResponseDto> responseDtos = List.of(responseDto);

        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toResponseList(users)).thenReturn(responseDtos);

        List<UserResponseDto> result = userService.getAll();

        assertEquals(1, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getAll_emptyList() {
        when(userRepository.findAll()).thenReturn(List.of());
        when(userMapper.toResponseList(List.of())).thenReturn(List.of());

        List<UserResponseDto> result = userService.getAll();

        assertTrue(result.isEmpty());
    }

    // ---------- getById ----------

    @Test
    void getById_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toResponse(user)).thenReturn(responseDto);

        UserResponseDto result = userService.getById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getById_notFound_throwsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getById(1L));
    }

    // ---------- deleteById ----------

    @Test
    void deleteById_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);

        userService.deleteById(1L);

        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void deleteById_notFound_throwsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.deleteById(1L));

        verify(userRepository, never()).delete(any());
    }

    // ---------- createUser ----------

    @Test
    void createUser_success() {
        when(userRepository.existsByEmail("nicat@example.com")).thenReturn(false);
        when(userMapper.toEntity(requestDto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(responseDto);

        UserResponseDto result = userService.createUser(requestDto);

        assertNotNull(result);
        assertEquals(responseDto.getEmail(), result.getEmail());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void createUser_emailAlreadyExists_throwsException() {
        when(userRepository.existsByEmail("nicat@example.com")).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> userService.createUser(requestDto));

        verify(userRepository, never()).save(any());
    }

    // ---------- searchUsers ----------

    @Test
    void searchUsers_success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> userPage = new PageImpl<>(List.of(user));

        when(userRepository.findByFullNameContainingIgnoreCase("Nicat", pageable)).thenReturn(userPage);
        when(userMapper.toResponse(user)).thenReturn(responseDto);

        Page<UserResponseDto> result = userService.searchUsers("Nicat", pageable);

        assertEquals(1, result.getTotalElements());
        verify(userRepository, times(1)).findByFullNameContainingIgnoreCase("Nicat", pageable);
    }

    @Test
    void searchUsers_noMatch_emptyPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> emptyPage = new PageImpl<>(List.of());

        when(userRepository.findByFullNameContainingIgnoreCase("xyz", pageable)).thenReturn(emptyPage);

        Page<UserResponseDto> result = userService.searchUsers("xyz", pageable);

        assertTrue(result.isEmpty());
    }

    // ---------- updateUser ----------

    @Test
    void updateUser_success_sameEmail_noEmailCheck() {
        requestDto.setEmail("nicat@example.com"); // same as existing
        requestDto.setPassword(null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(responseDto);

        UserResponseDto result = userService.updateUser(1L, requestDto);

        assertNotNull(result);
        verify(userRepository, never()).existsByEmail(any());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateUser_success_emailChanged_notTaken() {
        requestDto.setEmail("new@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(responseDto);

        userService.updateUser(1L, requestDto);

        assertEquals("new@example.com", user.getEmail());
        verify(userRepository, times(1)).existsByEmail("new@example.com");
    }

    @Test
    void updateUser_emailChanged_alreadyTaken_throwsException() {
        requestDto.setEmail("taken@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail("taken@example.com")).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> userService.updateUser(1L, requestDto));

        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUser_notFound_throwsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(1L, requestDto));

        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUser_passwordProvided_updatesPassword() {
        requestDto.setPassword("newPassword123");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(responseDto);

        userService.updateUser(1L, requestDto);

        assertEquals("newPassword123", user.getPassword());
    }

    @Test
    void updateUser_passwordBlank_passwordUnchanged() {
        requestDto.setPassword("   ");
        String originalPassword = user.getPassword();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(responseDto);

        userService.updateUser(1L, requestDto);

        assertEquals(originalPassword, user.getPassword());
    }

    @Test
    void updateUser_passwordNull_passwordUnchanged() {
        requestDto.setPassword(null);
        String originalPassword = user.getPassword();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(responseDto);

        userService.updateUser(1L, requestDto);

        assertEquals(originalPassword, user.getPassword());
    }

    // ---------- updateLastLogin ----------

    @Test
    void updateLastLogin_userExists_updatesTimestamp() {
        when(userRepository.findByEmail("nicat@example.com")).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        userService.updateLastLogin("nicat@example.com");

        assertNotNull(user.getLastLogin());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateLastLogin_userNotFound_doesNothing() {
        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        userService.updateLastLogin("unknown@example.com");

        verify(userRepository, never()).save(any());
    }

    // ---------- findActiveUsers ----------

    @Test
    void findActiveUsers_success() {
        when(userRepository.findByStatus(UserStatus.ACTIVE)).thenReturn(List.of(user));
        when(userMapper.toResponseList(List.of(user))).thenReturn(List.of(responseDto));

        List<UserResponseDto> result = userService.findActiveUsers();

        assertEquals(1, result.size());
        verify(userRepository, times(1)).findByStatus(UserStatus.ACTIVE);
    }

    @Test
    void findActiveUsers_emptyList() {
        when(userRepository.findByStatus(UserStatus.ACTIVE)).thenReturn(List.of());
        when(userMapper.toResponseList(List.of())).thenReturn(List.of());

        List<UserResponseDto> result = userService.findActiveUsers();

        assertTrue(result.isEmpty());
    }

    // ---------- getActiveUserCount ----------

    @Test
    void getActiveUserCount_success() {
        when(userRepository.countByStatus(UserStatus.ACTIVE)).thenReturn(5L);

        Long result = userService.getActiveUserCount();

        assertEquals(5L, result);
    }

    @Test
    void getActiveUserCount_zero() {
        when(userRepository.countByStatus(UserStatus.ACTIVE)).thenReturn(0L);

        Long result = userService.getActiveUserCount();

        assertEquals(0L, result);
    }

    // ---------- existsByEmail ----------

    @Test
    void existsByEmail_true() {
        when(userRepository.existsByEmail("nicat@example.com")).thenReturn(true);

        boolean result = userService.existsByEmail("nicat@example.com");

        assertTrue(result);
    }

    @Test
    void existsByEmail_false() {
        when(userRepository.existsByEmail("unknown@example.com")).thenReturn(false);

        boolean result = userService.existsByEmail("unknown@example.com");

        assertFalse(result);
    }

    // ---------- findByStatus ----------

    @Test
    void findByStatus_success() {
        when(userRepository.findByStatus(UserStatus.INACTIVE)).thenReturn(List.of(user));
        when(userMapper.toResponseList(List.of(user))).thenReturn(List.of(responseDto));

        List<UserResponseDto> result = userService.findByStatus(UserStatus.INACTIVE);

        assertEquals(1, result.size());
        verify(userRepository, times(1)).findByStatus(UserStatus.INACTIVE);
    }

    @Test
    void findByStatus_emptyList() {
        when(userRepository.findByStatus(UserStatus.INACTIVE)).thenReturn(List.of());
        when(userMapper.toResponseList(List.of())).thenReturn(List.of());

        List<UserResponseDto> result = userService.findByStatus(UserStatus.INACTIVE);

        assertTrue(result.isEmpty());
    }

    // ---------- findByRole ----------

    @Test
    void findByRole_success() {
        when(userRepository.findByRole(UserRole.ADMIN)).thenReturn(List.of(user));
        when(userMapper.toResponseList(List.of(user))).thenReturn(List.of(responseDto));

        List<UserResponseDto> result = userService.findByRole(UserRole.ADMIN);

        assertEquals(1, result.size());
        verify(userRepository, times(1)).findByRole(UserRole.ADMIN);
    }

    @Test
    void findByRole_emptyList() {
        when(userRepository.findByRole(UserRole.ADMIN)).thenReturn(List.of());
        when(userMapper.toResponseList(List.of())).thenReturn(List.of());

        List<UserResponseDto> result = userService.findByRole(UserRole.ADMIN);

        assertTrue(result.isEmpty());
    }
}
