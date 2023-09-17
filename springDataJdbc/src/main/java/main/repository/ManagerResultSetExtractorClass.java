package main.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import main.model.Client;
import main.model.ClientDetails;
import main.model.Manager;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class ManagerResultSetExtractorClass implements ResultSetExtractor<List<Manager>> {

    @Override
    public List<Manager> extractData(ResultSet rs) throws SQLException, DataAccessException {
        var managerList = new ArrayList<Manager>();
        String prevManagerId = null;
        while (rs.next()) {
            var managerId = rs.getString("manager_id");
            Manager manager = null;
            if (prevManagerId == null || !prevManagerId.equals(managerId)) {
                manager = new Manager(managerId, rs.getString("manager_label"), new HashSet<>(), new ArrayList<>(), false);
                managerList.add(manager);
                prevManagerId = managerId;
            }
            Long clientId = (Long) rs.getObject("client_id");
            if (manager !=  null && clientId != null) {
                manager.getClients().add(
                        new Client(clientId, rs.getString("client_name"),
                                managerId, rs.getInt("order_column"),
                                new ClientDetails(rs.getString("client_info"))));
            }
        }
        return managerList;
    }
}
