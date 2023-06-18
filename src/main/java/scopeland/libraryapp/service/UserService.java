package scopeland.libraryapp.service;

import java.util.List;

import scopeland.libraryapp.entities.User;
import scopeland.libraryapp.entities.dtos.UserDto;

public interface UserService {
    void saveUser(UserDto userDto);

    User findUserByEmail(String email);

    List<UserDto> findAllUsers();
}
