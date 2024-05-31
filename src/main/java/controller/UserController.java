package controller;

import connector.ConnectionMaker;
import model.UserDTO;

import java.nio.channels.ScatteringByteChannel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserController {
    private Connection connection;

    public UserController(ConnectionMaker connectionMaker) {
        connection = connectionMaker.makeConnection();
    }

    //1. 로그인 메소드
    public UserDTO auth(String username, String password) {
        String query = "SELECT * FROM user WHERE username = ? AND password = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                UserDTO user = new UserDTO();
                user.setId(resultSet.getInt("id"));
                user.setNickname(resultSet.getString("nickname"));
                user.setPassword(resultSet.getString("password"));
                user.setNickname(resultSet.getString("nickname"));

                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    //2. 회원가입 메소드
    public boolean register(UserDTO user) {
        String query = "INSERT INTO user(username, password, nickname) VALUES(?, ?, ?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            //username과 nickname이 unique이기 때문에 중복하면 오류 발생
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getNickname());

            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    //3. 회원정보 변경
    public boolean update(UserDTO user) {
        String query = "UPDATE user SET password = ?, nickname = ? WHERE id = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            //nickname이 unique이기 때문에 중복하면 오류 발생
            preparedStatement.setString(1, user.getPassword());
            preparedStatement.setString(2, user.getNickname());
            preparedStatement.setInt(3, user.getId());

            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    //4. 회원 제거

    public void delete(int id) {
        String query = "DELETE FROM user WHERE id = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
