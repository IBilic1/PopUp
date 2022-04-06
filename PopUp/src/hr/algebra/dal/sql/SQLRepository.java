/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dal.sql;

import hr.algebra.dal.IRepository;
import hr.algebra.model.Message;
import hr.algebra.model.MessageType;
import hr.algebra.model.Person;
import hr.algebra.model.User;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.sql.DataSource;

/**
 *
 * @author HT-ICT
 */
public class SQLRepository implements IRepository {

    private static final String ID_MESSAGE = "IDMessage";
    private static final String TEXT = "Message";
    private static final String DATE_TIME = "DateTime";

    private static final String ID_PICTURE = "IDPicture";

    private static final String ID_USER = "IDUser";
    private static final String FIRST_NAME = "FirstName";
    private static final String LAST_NAME = "LastName";
    private static final String BIRTH_DATE = "BirthDate";
    private static final String EMAIL = "Email";
    private static final String PASSWORD = "Password";
    private static final String PICTURE = "Picture";
    private static final String ACTIVE = "Active";
    private static final String SENDER_ID = "SenderID";
    private static final String RECEIVER_ID = "ReceiverID";
    private static final String MESSAGE_TYPE = "MessageType";
    private static final String SIZE = "Size";

    private static final String CREATE_MESSAGE = "{ CALL CreateMessage (?,?,?,?,?) }";
    private static final String GET_MESSAGES = "{ CALL GetMessages (?,?) }";

    private static final String CREATE_PICTURE = "{ CALL CreatePicture (?,?,?,?) }";

    private static final String GET_PICTURES = "{ CALL GetPictures (?,?) }";

    private static final String DELETE_USER = "{ CALL DeleteUser (?) }";
    private static final String GET_FRIENDS = "{ CALL GetFriends (?) }";
    private static final String GET_FRIENDS_RECEIVERS = "{ CALL GetFriendsRecevicers (?) }";
    private static final String GET_USER = "{ CALL GetUser (?) }";
    private static final String GET_USER_EMAIL_PASSWORD = "{ CALL GetUserEmailAndPassword (?,?) }";
    private static final String GET_USERS = "{ CALL GetUsers }";
    private static final String LOGIN_USER = "{ CALL LogInUser (?,?,?) }";
    private static final String REGISTER_USER = "{ CALL RegisterUser (?,?,?,?,?,?,?) }";
    private static final String UPDATE_USER = "{ CALL UpdateUser (?,?,?,?,?,?,?) }";
    private static final String GET_FRIENDS_SIZE = "{ CALL GetFriendsSize (?) }";

    @Override
    public List<User> get() throws Exception {
        List<User> users = new ArrayList();
        DataSource dataSource = DataSourceSingleton.INSTANCE.getInstance();

        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(GET_USERS);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                users.add(
                        new User(
                                rs.getInt(ID_USER),
                                rs.getString(FIRST_NAME),
                                rs.getString(LAST_NAME),
                                LocalDate.parse(rs.getString(BIRTH_DATE), Person.DATE_FORMATTER),
                                rs.getString(EMAIL),
                                rs.getString(PASSWORD),
                                rs.getBytes(PICTURE)
                        )
                );
            }
        }

        return users;
    }

    @Override
    public Optional<User> get(int idUser) throws Exception {
        DataSource dataSource = DataSourceSingleton.INSTANCE.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(GET_USER)) {
            stmt.setInt(1, idUser);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new User(
                            rs.getInt(ID_USER),
                            rs.getString(FIRST_NAME),
                            rs.getString(LAST_NAME),
                            LocalDate.parse(rs.getString(BIRTH_DATE), Person.DATE_FORMATTER),
                            rs.getString(EMAIL),
                            rs.getString(PASSWORD),
                            rs.getBytes(PICTURE)
                    ));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public void delete(int idUser) throws Exception {
        DataSource dataSource = DataSourceSingleton.INSTANCE.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(DELETE_USER)) {
            stmt.setInt(1, idUser);
            stmt.executeUpdate();
        }

    }

    @Override
    public void update(User user) throws Exception {
        DataSource dataSource = DataSourceSingleton.INSTANCE.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(UPDATE_USER)) {

            stmt.setInt(1, user.getIdUser());
            stmt.setString(2, user.getFirstName());
            stmt.setString(3, user.getLastName());
            stmt.setString(4, user.getBirthDate().format(Person.DATE_FORMATTER));
            stmt.setString(5, user.getEmail());
            stmt.setString(6, user.getPassword());
            stmt.setBytes(7, user.getPicture());
            stmt.executeUpdate();

        }
    }

    @Override
    public boolean register(User user) throws Exception {
        DataSource dataSource = DataSourceSingleton.INSTANCE.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(REGISTER_USER)) {

            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getBirthDate().toString());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getPassword());
            stmt.setBytes(6, user.getPicture());
            stmt.registerOutParameter(7, java.sql.Types.BIT);
            stmt.execute();
            return stmt.getBoolean(7);

        }
    }

    @Override
    public boolean login(String email, String password) throws Exception {
        DataSource dataSource = DataSourceSingleton.INSTANCE.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(LOGIN_USER);) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            stmt.registerOutParameter(3, Types.BIT);
            stmt.execute();
            return stmt.getBoolean(3);
        }
    }

    @Override
    public List<User> getFriends(int senderID) throws Exception {

        Set<User> users = new HashSet<>();
        DataSource dataSource = DataSourceSingleton.INSTANCE.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(GET_FRIENDS);) {
            stmt.setInt(1, senderID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    users.add(
                            new User(
                                    rs.getInt(ID_USER),
                                    rs.getString(FIRST_NAME),
                                    rs.getString(LAST_NAME),
                                    LocalDate.parse(rs.getString(BIRTH_DATE), Person.DATE_FORMATTER),
                                    rs.getString(EMAIL),
                                    rs.getString(PASSWORD),
                                    rs.getBytes(PICTURE)
                            )
                    );
                }
            }
        }
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(GET_FRIENDS_RECEIVERS);) {
            stmt.setInt(1, senderID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    users.add(
                            new User(
                                    rs.getInt(ID_USER),
                                    rs.getString(FIRST_NAME),
                                    rs.getString(LAST_NAME),
                                    LocalDate.parse(rs.getString(BIRTH_DATE), Person.DATE_FORMATTER),
                                    rs.getString(EMAIL),
                                    rs.getString(PASSWORD),
                                    rs.getBytes(PICTURE)
                            )
                    );
                }
            }
        }

        return new ArrayList<>(users);
    }

    @Override
    public void createMessage(Message message) throws Exception {
        DataSource dataSource = DataSourceSingleton.INSTANCE.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CREATE_MESSAGE)) {

            stmt.setBytes(1, message.getMessage());
            stmt.setString(2, message.getTime().format(Message.DATE_FORMATTER));
            stmt.setInt(3, message.getSender().getIdUser());
            stmt.setInt(4, message.getReceiver().getIdUser());
            stmt.setString(5, message.getType().getName());
            stmt.execute();

        }
    }

    @Override
    public Optional<User> get(String email, String password) throws Exception {
        DataSource dataSource = DataSourceSingleton.INSTANCE.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(GET_USER_EMAIL_PASSWORD)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new User(
                            rs.getInt(ID_USER),
                            rs.getString(FIRST_NAME),
                            rs.getString(LAST_NAME),
                            LocalDate.parse(rs.getString(BIRTH_DATE), Person.DATE_FORMATTER),
                            rs.getString(EMAIL),
                            rs.getString(PASSWORD),
                            rs.getBytes(PICTURE)
                    ));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Message> get(int senderID, int receiverID) throws Exception {
        List<Message> messages = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.INSTANCE.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(GET_MESSAGES);) {
            stmt.setInt(1, senderID);
            stmt.setInt(2, receiverID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    messages.add(new Message(rs.getInt(ID_MESSAGE), LocalDateTime.parse(rs.getString(DATE_TIME), Message.DATE_FORMATTER), get(rs.getInt(SENDER_ID)).get(), get(rs.getInt(RECEIVER_ID)).get(), rs.getBytes(TEXT), MessageType.from(rs.getString(MESSAGE_TYPE)).get()));
                }
            }
        }
        return messages;
    }

    @Override
    public int getFriendsSize(int senderID) throws Exception {
        DataSource dataSource = DataSourceSingleton.INSTANCE.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(GET_FRIENDS_SIZE)) {
            stmt.setInt(1, senderID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(SIZE);
                }
                //return rs.getInt(1);
            }
        }
        return 0;
    }

}
