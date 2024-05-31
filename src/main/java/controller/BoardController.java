package controller;

import connector.ConnectionMaker;
import model.BoardDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BoardController {
    private Connection connection;

    public BoardController(ConnectionMaker connectionMaker) {
        connection = connectionMaker.makeConnection();
    }

    //1. 전체 보드 가져오기
    public ArrayList<BoardDTO> selectAll() {
        ArrayList<BoardDTO> boardList = new ArrayList<>();
        String query = "SELECT * FROM board" +
                "INNER JOIN user" +
                "ON board.writer_id = user.id";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.getResultSet();

            while (resultSet.next()) {
                BoardDTO board = new BoardDTO();

                board.setId(resultSet.getInt("board.id"));
                board.setTitle(resultSet.getString("title"));
                board.setContent(resultSet.getString("content"));
                board.setEntryDate(resultSet.getTimestamp("entry_date"));
                board.setModifyDate(resultSet.getTimestamp("modify_date"));
                board.setWriterId(resultSet.getInt("writer_id"));
                board.setNickname(resultSet.getString("nickname"));

                boardList.add(board);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return boardList;
    }

    //2. 보드 하나 보기
    public BoardDTO selectOne(int id) {
        if (id == 0) return null;

        String query = "SELECT * FROM board " +
                "INNER JOIN user" +
                "ON board.writer_id = user.id" +
                "WHERE board.id = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                BoardDTO board = new BoardDTO();

                board.setId(resultSet.getInt("board.id"));
                board.setTitle(resultSet.getString("title"));
                board.setContent(resultSet.getString("content"));
                board.setEntryDate(resultSet.getTimestamp("entry_date"));
                board.setModifyDate(resultSet.getTimestamp("modify_date"));
                board.setWriterId(resultSet.getInt("writer_id"));
                board.setNickname(resultSet.getString("nickname"));

                return board;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    //3. 데이터 삽입
    public void insert(BoardDTO board) {
        String query = "INSERT INTO board(title, content, writer_id) VALUES(?, ?, ?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, board.getTitle());
            preparedStatement.setString(2, board.getContent());
            preparedStatement.setInt(3, board.getWriterId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //4. 데이터 업데이트
    public void update(BoardDTO board) {
        String query = "UPDATE board SET title = ?, content = ?, modify_date = NOW() WHERE id = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, board.getTitle());
            preparedStatement.setString(2, board.getContent());
            preparedStatement.setInt(3, board.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //5. 데이터 삭제
    public void delete(int id) {
        String query = "DELETE FROM board WHERE id = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
