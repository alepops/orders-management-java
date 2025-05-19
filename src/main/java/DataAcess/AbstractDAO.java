package DataAcess;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * Clasa abstracta care defineste operatii generice pentru accesul la baza de date.
 * Utilizeaza Reflection si generice pentru a efectua operatii CRUD pe orice tip de obiect model.
 *
 * @param <T> Tipul entitatii gestionate de DAO
 */
public class AbstractDAO<T> {
    /**
     * Logger pentru raportarea erorilor sau informatiilor.
     */
    protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());

    private final Class<T> type;

    @SuppressWarnings("unchecked")
    public AbstractDAO() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    }

    private String createSelectQuery(String field) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" * ");
        sb.append(" FROM ");
        sb.append(type.getSimpleName());
        sb.append(" WHERE " + field + " =?");
        return sb.toString();
       }

    public List<T> findAll() {
        // TODO:
        String query = "SELECT * FROM " + type.getSimpleName();
        List<T> results = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            results = createObjects(resultSet);

        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findAll " + e.getMessage());
        }
        return results;
    }

    public T findById(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectQuery("id");
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            return createObjects(resultSet).get(0);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    private List<T> createObjects(ResultSet resultSet) {
        List<T> list = new ArrayList<T>();
        Constructor[] ctors = type.getDeclaredConstructors();
        Constructor ctor = null;
        for (int i = 0; i < ctors.length; i++) {
            ctor = ctors[i];
            if (ctor.getGenericParameterTypes().length == 0)
                break;
        }
        try {
            while (resultSet.next()) {
                ctor.setAccessible(true);
                T instance = (T)ctor.newInstance();
                for (Field field : type.getDeclaredFields()) {
                    String fieldName = field.getName();
                    Object value = resultSet.getObject(fieldName);
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, type);
                    Method method = propertyDescriptor.getWriteMethod();
                    method.invoke(instance, value);
                }
                list.add(instance);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        return list;
    }

    public T insert(T t) {
        StringBuilder fields = new StringBuilder();
        StringBuilder values = new StringBuilder();
        List<Object> parameters = new ArrayList<>();

        for (Field field : type.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                if (!field.getName().equalsIgnoreCase("id")) {
                    fields.append(field.getName()).append(",");
                    values.append("?").append(",");
                    parameters.add(field.get(t));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        fields.setLength(fields.length() - 1);
        values.setLength(values.length() - 1);

        String query = "INSERT INTO " + type.getSimpleName() +
                " (" + fields + ") VALUES (" + values + ")";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            for (int i = 0; i < parameters.size(); i++) {
                statement.setObject(i + 1, parameters.get(i));
            }

            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                Field idField = type.getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(t, rs.getInt(1));
            }

        } catch (SQLException | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return t;
    }


    public T update(T t) {
        StringBuilder query = new StringBuilder("UPDATE " + type.getSimpleName() + " SET ");
        List<Object> parameters = new ArrayList<>();
        Object idValue = null;

        for (Field field : type.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object value = field.get(t);
                if (field.getName().equalsIgnoreCase("id")) {
                    idValue = value;
                } else {
                    query.append(field.getName()).append(" = ?, ");
                    parameters.add(value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        query.setLength(query.length() - 2); // eliminăm ultima virgulă
        query.append(" WHERE id = ?");

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(query.toString())) {

            for (int i = 0; i < parameters.size(); i++) {
                statement.setObject(i + 1, parameters.get(i));
            }

            statement.setObject(parameters.size() + 1, idValue);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return t;
    }
    public void delete(T t) {
        try (Connection connection = ConnectionFactory.getConnection()) {
            Field idField = type.getDeclaredField("id");
            idField.setAccessible(true);
            Object idValue = idField.get(t);

            String query = "DELETE FROM " + type.getSimpleName() + " WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setObject(1, idValue);
                statement.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
