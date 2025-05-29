package br.com.dio.persistence.dao;

import br.com.dio.persistence.entity.BoardColumnEntity;
import br.com.dio.persistence.entity.BoardEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class BoardColumnDAO {

    private final Connection connection;

    public BoardColumnEntity insert (final BoardColumnEntity entity) throws SQLException {
        var sql = "INSERT INTO boards_columns (name, order, kind, board_id) VALUES (?, ?, ?, ?);";

        try(var statement = connection.prepareStatement(sql)) {
            statement.setString(1, entity.getName());
            statement.setInt(2, entity.getOrder());
            statement.setString(3, entity.getKind().name());
            statement.setLong(4, entity.getBoard().getId());
            statement.executeUpdate();

            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                entity.setId(rs.getLong(1)); // Retrieve the generated ID
            }

        }
        return entity;
    }

    public Optional<BoardColumnEntity> findById(final Long id) throws SQLException{
        var sql = "SELECT id, name FROM boards_columns WHERE id = ?";

        try(var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
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

    public List<BoardColumnEntity> findByBoardId(Long id) throws SQLException{
        return null;
    }
}
