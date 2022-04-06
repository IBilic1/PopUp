/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dal;

import hr.algebra.model.User;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author HT-ICT
 */
public interface UserRepository {

    List<User> get() throws Exception;

    Optional<User> get(int idUser) throws Exception;

    Optional<User> get(String email, String password) throws Exception;

    void delete(int idUser) throws Exception;

    void update(User user) throws Exception;

    boolean register(User user) throws Exception;

    boolean login(String email, String password) throws Exception;

    List<User> getFriends(int senderID) throws Exception;

    int getFriendsSize(int senderID) throws Exception;

}
