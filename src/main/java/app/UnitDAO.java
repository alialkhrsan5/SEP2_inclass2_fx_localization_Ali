package app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class UnitDAO {

    public static final String FALLBACK_LANG = "en";

    private static final String SQL =
            "SELECT u.id, u.code, "
          + "       COALESCE(t.name, e.name) AS name, "
          + "       COALESCE(t.description, e.description) AS description "
          + "FROM unit u "
          + "LEFT JOIN unit_translation t ON t.unit_id = u.id AND t.lang = ? "
          + "LEFT JOIN unit_translation e ON e.unit_id = u.id AND e.lang = ? "
          + "ORDER BY u.id";

    public List<Unit> getUnits(String lang) throws SQLException {
        List<Unit> units = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL)) {

            ps.setString(1, lang);
            ps.setString(2, FALLBACK_LANG);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    units.add(new Unit(
                            rs.getInt("id"),
                            rs.getString("code"),
                            rs.getString("name"),
                            rs.getString("description")
                    ));
                }
            }
        }
        return units;
    }
}
