package controller;

import connector.ConnectionMaker;
import model.ReplyDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ReplyController {
    private Connection connection;

    public ReplyController(ConnectionMaker connectionMaker) {
        connection = connectionMaker.makeConnection();
    }

    public ArrayList<ReplyDTO> selectAllByBoardId(int boardId) {
        ArrayList<ReplyDTO> replyList = new ArrayList<>();
        String query = "SELECT * FROM reply " +
                "INNER JOIN user" +
                "ON reply.writer_id = user.id" +
                "WHERE board_id = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, boardId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ReplyDTO reply = new ReplyDTO();

                reply.setId(resultSet.getInt("reply.id"));
                reply.setContent(resultSet.getString("content"));
                reply.setEntryDate(resultSet.getTimestamp("entry_date"));
                reply.setModifyDate(resultSet.getTimestamp("modify_date"));
                reply.setWriterId(resultSet.getInt("writer_id"));
                reply.setBoardId(resultSet.getInt("board_id"));
                reply.setNickname(resultSet.getString("nickname"));

                replyList.add(reply);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ArrayList<ReplyDTO> selectAllByWriterId(int writerId) {
        String query = "SELECT * FROM reply WHERE writer_id = ?";
        ArrayList<ReplyDTO> replyList = new ArrayList<>();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, writerId);

            ResultSet resultSet = preparedStatement.getResultSet();

            while (resultSet.next()) {
                ReplyDTO reply = new ReplyDTO();

                reply.setId(resultSet.getInt("id"));
                reply.setWriterId(resultSet.getInt("writer_id"));
                reply.setBoardId(resultSet.getInt("board_id"));
                reply.setContent(resultSet.getString("content"));

                replyList.add(reply);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return replyList;
    }

    public void insert(ReplyDTO reply) {
        String query = "INSERT INTO reply(content, writer_id, board_id) VALUES(?, ?, ?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, reply.getContent());
            preparedStatement.setInt(2, reply.getWriterId());
            preparedStatement.setInt(3, reply.getBoardId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(ReplyDTO reply) {
        String query = "UPDATE reply SET content = ?, modify_date = NOW() WHERE id = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, reply.getContent());
            preparedStatement.setInt(2, reply.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String query = "DELETE FROM reply WHERE id = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
