package app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConversionDAO {

    public void save(Conversion conversion) throws SQLException {
        String sql = "INSERT INTO conversion "
                + "(input_value, from_unit_id, to_unit_id, result_value, lang) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, conversion.getInputValue());
            ps.setInt(2, conversion.getFromUnitId());
            ps.setInt(3, conversion.getToUnitId());
            ps.setDouble(4, conversion.getResultValue());
            ps.setString(5, conversion.getLang());
            ps.executeUpdate();
        }
    }

    public List<Conversion> getLatest(int limit) throws SQLException {
        List<Conversion> conversions = new ArrayList<>();
        String sql = "SELECT id, input_value, from_unit_id, to_unit_id, result_value, lang, created_at "
                + "FROM conversion ORDER BY created_at DESC, id DESC LIMIT ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, limit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    conversions.add(new Conversion(
                            rs.getInt("id"),
                            rs.getDouble("input_value"),
                            rs.getInt("from_unit_id"),
                            rs.getInt("to_unit_id"),
                            rs.getDouble("result_value"),
                            rs.getString("lang"),
                            rs.getTimestamp("created_at").toLocalDateTime()
                    ));
                }
            }
        }
        return conversions;
    }
}
