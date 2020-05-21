package iris.playharmony.controller.db;

import iris.playharmony.util.Pair;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static iris.playharmony.util.TypeUtils.getAnnotatedFields;

public class DBObjectSerializer {

    public void serialize(Object object, PreparedStatement statement) {

        if(object == null) {
            throw new NullPointerException();
        }

        getAnnotatedFields(object, DBAttribute.class).forEach(attribute -> serializeAttribute(attribute, statement));
    }

    private void serializeAttribute(Pair<?, DBAttribute> attribute, PreparedStatement statement) {

        final int index = attribute.getValue().index();
        final String value = String.valueOf(attribute.getKey());

        try {
            statement.setString(index, value);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
