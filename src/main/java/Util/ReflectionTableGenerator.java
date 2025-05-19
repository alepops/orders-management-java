package Util;

import javax.swing.table.DefaultTableModel;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.List;

public class ReflectionTableGenerator {

    public static <T> DefaultTableModel generateTable(List<T> list) {
        if (list == null || list.isEmpty()) return new DefaultTableModel();

        try {
            Class<?> clazz = list.get(0).getClass();

            // Extragem numele coloanelor (numele câmpurilor)
            PropertyDescriptor[] descriptors = java.beans.Introspector.getBeanInfo(clazz, Object.class).getPropertyDescriptors();
            String[] columnNames = new String[descriptors.length];
            for (int i = 0; i < descriptors.length; i++) {
                columnNames[i] = descriptors[i].getName();
            }

            // Completăm rândurile
            Object[][] data = new Object[list.size()][descriptors.length];
            for (int i = 0; i < list.size(); i++) {
                T obj = list.get(i);
                for (int j = 0; j < descriptors.length; j++) {
                    Method getter = descriptors[j].getReadMethod();
                    data[i][j] = getter.invoke(obj);
                }
            }

            return new DefaultTableModel(data, columnNames);
        } catch (Exception e) {
            e.printStackTrace();
            return new DefaultTableModel(); // fallback
        }
    }
}
