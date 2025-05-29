package br.com.dio.persistence.dao;

import br.com.dio.persistence.entity.BoardEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@AllArgsConstructor
public class BoardDAO {

    private final Connection connection;

    public BoardEntity insert(final BoardEntity entity) throws SQLException{
        var sql = "INSERT INTO boards (name) VALUES (?);";

        try(var statement = connection.prepareStatement(sql)) {
            statement.setString(1, entity.getName());
            statement.executeUpdate();

            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                entity.setId(rs.getLong(1)); // Retrieve the generated ID
            }

        }
        return entity;
    }

    public Optional<BoardEntity> findById(final Long id) throws SQLException{
        var sql = "SELECT id, name FROM boards WHERE id = ?";

        try(var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeQuery();

            var resultSet = statement.getResultSet();
            if(resultSet.next()) {
                var entity = new BoardEntity();
                entity.setId(resultSet.getLong("id"));
                entity.setName(resultSet.getString("name"));

                return Optional.of(entity);
            }

            return Optional.empty();
        }
    }

    public void delete(final Long id) throws SQLException{
        var sql = "DELETE FROM boards WHERE id = ?";

        try(var statement = connection.prepareStatement(sql)) {
            statement.setLong(1,id);
            statement.executeUpdate();
        }
    }

    public boolean exists(final Long id) throws SQLException {
        var sql = "SELECT 1 FROM boards WHERE id = ?";

        try(var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeQuery();

            return statement.getResultSet().next();
        }
    }
}
