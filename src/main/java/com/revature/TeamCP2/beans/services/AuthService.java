/**
 * Author(s): @George Henderson
 * Contributor(s):
 * Purpose: Service to authenticate and register a user.
 */
package com.revature.TeamCP2.beans.services;

import com.revature.TeamCP2.beans.repositories.UserRepository;
import com.revature.TeamCP2.dtos.CookieDto;
import com.revature.TeamCP2.dtos.LoginDto;
import com.revature.TeamCP2.entities.User;
import com.revature.TeamCP2.exceptions.CreationFailedException;
import com.revature.TeamCP2.exceptions.ItemHasNonNullIdException;
import com.revature.TeamCP2.exceptions.NotAuthorizedException;
import com.revature.TeamCP2.exceptions.UsernameAlreadyExistsException;
import com.revature.TeamCP2.interfaces.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    BCryptHash bCryptHash;
    JsonWebToken jsonWebToken;
    UserRepository userRepository;

    @Autowired
    public AuthService(BCryptHash bCryptHash, JsonWebToken jsonWebToken, UserRepository userRepository) {
        this.bCryptHash = bCryptHash;
        this.jsonWebToken = jsonWebToken;
        this.userRepository = userRepository;
    }

    /**
     * Returns the json web token if user is successful at logging in.
     * @param loginInfo Contains username & password
     */
    public User loginUser(LoginDto loginInfo) throws NotAuthorizedException {

        // Check to see if username exists
        Optional<User> opUser = userRepository.getByUsername(loginInfo.getUsername());
        if (!opUser.isPresent())
            throw new NotAuthorizedException();

        // See if password matches
        User user = opUser.get();
        if(!bCryptHash.verify(loginInfo.getPassword(), user.getPassword()))
            throw new NotAuthorizedException();

        // Return JsonToken
        return user;
    }

    /**
     * Registers a user and returns json web token;
     * @param user The user to register
     */
    public User registerUser(User user) throws CreationFailedException, ItemHasNonNullIdException, UsernameAlreadyExistsException {
        if(user.getId() != null)
            throw new ItemHasNonNullIdException();

        Optional<User> opUser = userRepository.getByUsername(user.getUsername());
        if(opUser.isPresent())
            throw new UsernameAlreadyExistsException();

        user.setRole(Role.USER);
        user.setPassword(bCryptHash.hash(user.getPassword()));
        return userRepository.create(user);
    }

    public boolean isAdmin(CookieDto cookieDto) {
        return cookieDto.getUserRole() == Role.ADMIN;
    }

    public CookieDto getCookieDto(String cookieString) throws NotAuthorizedException {
        return jsonWebToken.verify(cookieString);
    }
}
