package investimentos.java.investimentosADR.service;

import investimentos.java.investimentosADR.controller.CreateUserDTO;
import investimentos.java.investimentosADR.entity.User;
import investimentos.java.investimentosADR.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;
    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;
    @Captor
    private ArgumentCaptor<UUID> UUIDArgumentCaptor;
    @Nested
    class createUser{

        @Test
        @DisplayName("Deve criar usuário com sucesso")
         void shouldCreateUserWithSuccess(){

            //Arrange
            var user = new User(
                    UUID.randomUUID(),
                    "username",
                    "email@gmail.com",
                    "807324",
                    Instant.now(),
                    null
            );
            doReturn(user).when(userRepository).save(userArgumentCaptor.capture());

            var input = new CreateUserDTO(
                    "userName",
                    "email@gmail.com",
                    "807324"
            );

            //Act
            var output = userService.createUser(input);

            //Assert
            assertNotNull(output);
            var userCaptured = userArgumentCaptor.getValue();
            assertEquals(input.userName(), userCaptured.getUserName());
            assertEquals(input.email(), userCaptured.getEmail());
            assertEquals(input.password(), userCaptured.getPassword());

        }

        @Test
        @DisplayName("Deve mostrar na tela que está dando erro")
        void shouldThrowExeptionWhenErrorOccurs(){
            //Arrange
            doThrow(new RuntimeException()).when(userRepository).save(any());

            var input = new CreateUserDTO(
                    "userName",
                    "email@gmail.com",
                    "807324"
            );

            //Act & Assert
            assertThrows(RuntimeException.class, () ->  userService.createUser(input));
        }
    }

    @Nested
    class getUserById {

        @Test
        @DisplayName("Se pegar pelo Id deu certo")
        void shouldGetUserByIdWithSuccess() {
            //Arrange
            var user = new User(
                    UUID.randomUUID(),
                    "username",
                    "email@gmail.com",
                    "807324",
                    Instant.now(),
                    null
            );
            doReturn(Optional.of(user)).when(userRepository).findById(UUIDArgumentCaptor.capture());

            //Act
            var output = userService.getUserById(user.getUserId().toString());

            //Assert
            assertTrue(output.isPresent());
            assertEquals(user.getUserId(), UUIDArgumentCaptor.getValue());
        }
    }

}