package br.com.dio.persistence.dao;

import br.com.dio.persistence.entity.BoardColumnEntity;
import br.com.dio.persistence.entity.BoardColumnKindEnum;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class BoardColumnDAO {

    private final Connection connection;

    public BoardColumnEntity insert(final BoardColumnEntity entity) throws SQLException {
        String sql = "INSERT INTO boards_columns (name, order, kind, board_id) VALUES (?, ?, ?, ?)";

        try (var statement = connection.prepareStatement(sql)) {
            statement.setString(2, entity.getName());
            statement.setInt(3, entity.getOrder());
            statement.setString(4, entity.getKind().name());
            statement.setLong(1, entity.getBoard().getId());
            statement.executeUpdate();
            System.out.println(statement.getResultSet().toString());
            entity.setId(statement.getResultSet().getLong("id"));
            System.out.println(entity.getId());
            return entity;
        }


    }


    public Optional<BoardColumnEntity> findById(final Long boardId) throws SQLException{
        var sql = "SELECT id, name FROM boards_columns WHERE id = ?";

        try(var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, boardId);
            statement.executeQuery();

            var resultSet = statement.getResultSet();
            if(resultSet.next()) {
                var entity = new BoardColumnEntity();
                entity.setId(resultSet.getLong("id"));
                entity.setName(resultSet.getString("name"));

                return Optional.of(entity);
            }

            return Optional.empty();
        }
    }

    public void delete(final Long id) throws SQLException{
        var sql = "DELETE FROM boards_columns WHERE id = ?";

        try(var statement = connection.prepareStatement(sql)) {
            statement.setLong(1,id);
            statement.executeUpdate();
        }
    }

    public boolean exists(final Long id) throws SQLException {
        var sql = "SELECT 1 FROM boards_columns WHERE id = ?";

        try(var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeQuery();

            return statement.getResultSet().next();
        }
    }

    public List<BoardColumnEntity> findByBoardId(final Long boardId) throws SQLException{
        List<BoardColumnEntity> entities = new ArrayList<>();
        var sql = "SELECT id, name, \"order\", kind FROM boards_columns WHERE board_id = ? ORDER BY \"order\" ";

        try(var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, boardId);
            statement.executeQuery();

            var resultSet = statement.getResultSet();
            while(resultSet.next()) {
                var entity = new BoardColumnEntity();
                entity.setId(resultSet.getLong("id"));
                entity.setName(resultSet.getString("name"));
                entity.setOrder(resultSet.getInt("order"));
                entity.setKind(BoardColumnKindEnum.findByName(resultSet.getString("kind")));
                entities.add(entity);
            }

            return entities;
        }
    }
}
