package main.demo.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import main.demo.model.GuardDog;
import org.springframework.jdbc.core.RowMapper;

public class GuardDogMapper implements RowMapper<GuardDog> {
    @Override
    public GuardDog mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new GuardDog(rs.getLong("dog_id"), rs.getString("name"), rs.getString("owner_name"));
    }
}
