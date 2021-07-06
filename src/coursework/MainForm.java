package coursework;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class MainForm extends javax.swing.JFrame {

    Connection conn;
    Vector<Vector> employees_data;
    Vector<String> employees_column_names;

    Vector<Vector> positions_data;
    Vector<String> positions_column_names;

    Vector<Vector> ingredients_data;
    Vector<String> ingredients_column_names;

    Vector<Vector> menu_data;
    Vector<String> menu_column_names;

    Vector<Vector> dishes_data;
    Vector<String> dishes_column_names;

    Vector<Vector> orders_data;
    Vector<String> orders_column_names;

    public MainForm(String login, String password) throws SQLException {
        initComponents();
        setLocationRelativeTo(null);
        try {
            String url = "jdbc:mysql://127.0.0.1:3306/restaurant";
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            conn = DriverManager.getConnection(url, login, password);

        } catch (Exception ex) {
        }
        initEmployeesTable();
        deleteEmployeeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int row = employeesTable.getSelectedRow();
                    String number = (String) employees_data.get(row).get(1);
                    PreparedStatement res = conn.prepareStatement("DELETE FROM employees WHERE phone_number=?");
                    res.setString(1, number);
                    res.executeUpdate();
                    initEmployeesTable();
                } catch (SQLException ex) {
                    Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        );
        initPositionsTable();
        deletePositionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int row = positionsTable.getSelectedRow();
                    String position = (String) positions_data.get(row).get(0);
                    PreparedStatement res = conn.prepareStatement("DELETE FROM positions WHERE position=?");
                    res.setString(1, position);
                    res.executeUpdate();
                    initPositionsTable();
                    initEmployeesTable();
                } catch (SQLException ex) {
                    Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        );
        initIngredientsTable();
        deleteIngredientButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int row = ingredientsTable.getSelectedRow();
                    String ingredient = (String) ingredients_data.get(row).get(0);
                    PreparedStatement res = conn.prepareStatement("DELETE FROM ingredients WHERE ingredient=?");
                    res.setString(1, ingredient);
                    res.executeUpdate();
                    initIngredientsTable();
                    initDishesTable();
                    initMenuTable();
                } catch (SQLException ex) {
                    Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        );
        initMenuTable();
        deleteMenuButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int dish_id = 0;
                    int row = menuTable.getSelectedRow();
                    String dish = (String) menu_data.get(row).get(0);
                    PreparedStatement res = conn.prepareStatement("SELECT iddish\n"
                            + "FROM dishes\n"
                            + " WHERE dish=?\n");
                    res.setString(1, dish);
                    ResultSet rs = res.executeQuery();
                    if (rs.next()) {
                        dish_id = rs.getInt(1);
                    }
                    res.close();
                    res = conn.prepareStatement("DELETE FROM menu WHERE iddish=?");
                    res.setInt(1, dish_id);
                    res.executeUpdate();
                    initMenuTable();
                } catch (SQLException ex) {
                    Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        );
        initDishesTable();
        deleteDishButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int row = dishesTable.getSelectedRow();
                    String dish = (String) dishes_data.get(row).get(0);
                    PreparedStatement res = conn.prepareStatement("DELETE FROM dishes WHERE dish=?");
                    res.setString(1, dish);
                    res.executeUpdate();
                    initDishesTable();
                    initMenuTable();
                } catch (SQLException ex) {
                    Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        );
        initOrdersTable();

    }

    private void initEmployeesTable() throws SQLException {
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT full_name,phone_number,position\n"
                + "FROM employees\n"
                + " JOIN positions\n"
                + "ON positions.idposition=employees.idposition;");
        employees_data = new Vector<Vector>();
        employees_column_names = new Vector<String>();
        employees_column_names.addElement("ФИО");
        employees_column_names.addElement("Номер телефона");
        employees_column_names.addElement("Должность");
        while (resultSet.next()) {
            Vector<String> row = new Vector<String>();
            row.add(resultSet.getString(1));
            row.add(resultSet.getString(2));
            row.add(resultSet.getString(3));
            employees_data.add(row);
        }
        employeesTable.setModel(new DefaultTableModel(employees_data, employees_column_names));
    }

    private void initPositionsTable() throws SQLException {
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT position,salary\n"
                + "FROM positions\n");
        positions_data = new Vector<Vector>();
        positions_column_names = new Vector<String>();
        positions_column_names.addElement("Должность");
        positions_column_names.addElement("Зарплата(руб/мес)");
        while (resultSet.next()) {
            Vector<String> row = new Vector<String>();
            row.add(resultSet.getString(1));
            row.add(resultSet.getString(2));
            positions_data.add(row);
        }
        positionsTable.setModel(new DefaultTableModel(positions_data, positions_column_names));
    }

    private void initIngredientsTable() throws SQLException {
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT ingredient,weight_kg,ingredient_date,ingredient_expiringdate\n"
                + "FROM ingredients\n");
        ingredients_data = new Vector<Vector>();
        ingredients_column_names = new Vector<String>();
        ingredients_column_names.addElement("Наименование");
        ingredients_column_names.addElement("Масса(кг)");
        ingredients_column_names.addElement("Дата привоза");
        ingredients_column_names.addElement("Годен до");
        while (resultSet.next()) {
            Vector<String> row = new Vector<String>();
            row.add(resultSet.getString(1));
            row.add(String.valueOf(resultSet.getDouble(2)));
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            row.add(dateFormat.format(resultSet.getDate(3)));
            row.add(dateFormat.format(resultSet.getDate(4)));
            ingredients_data.add(row);
        }
        ingredientsTable.setModel(new DefaultTableModel(ingredients_data, ingredients_column_names));
    }

    private void initMenuTable() throws SQLException {
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT dish,dish_weight_gr,price_rub\n"
                + "FROM menu\n"
                + " JOIN dishes\n"
                + "ON dishes.iddish=menu.iddish;");
        menu_data = new Vector<Vector>();
        menu_column_names = new Vector<String>();
        menu_column_names.addElement("Наименование");
        menu_column_names.addElement("Вес порции");
        menu_column_names.addElement("Стоимость(в руб)");
        while (resultSet.next()) {
            Vector<String> row = new Vector<String>();
            row.add(resultSet.getString(1));
            row.add(String.valueOf(resultSet.getInt(2)));
            row.add(String.valueOf(resultSet.getInt(3)));
            menu_data.add(row);
        }
        menuTable.setModel(new DefaultTableModel(menu_data, menu_column_names));
    }

    private void initDishesTable() throws SQLException {
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT dish,dish_weight_gr,ingredient,ingredient_weight_gr\n"
                + "FROM dishes\n"
                + "LEFT JOIN dishes_ingredients ON dishes.iddish = dishes_ingredients.dishes_iddish\n"
                + "INNER JOIN ingredients ON ingredients.idingredient = dishes_ingredients.ingredients_idingredient\n"
                + "ORDER BY dish\n"
        );
        dishes_data = new Vector<Vector>();
        dishes_column_names = new Vector<String>();
        dishes_column_names.addElement("Блюдо");
        dishes_column_names.addElement("Масса блюда(в граммах)");
        dishes_column_names.addElement("Ингредиент");
        dishes_column_names.addElement("Масса ингредиента(в граммах)");
        while (resultSet.next()) {
            Vector<String> row = new Vector<String>();
            row.add(resultSet.getString(1));
            row.add(String.valueOf(resultSet.getInt(2)));
            row.add(resultSet.getString(3));
            row.add(String.valueOf(resultSet.getInt(4)));
            dishes_data.add(row);
        }
        dishesTable.setModel(new DefaultTableModel(dishes_data, dishes_column_names));
    }

    private void initOrdersTable() throws SQLException {
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT order_date,dish,order_owner\n"
                + "FROM orders\n"
                + "LEFT JOIN orders_dishes ON orders.idorder = orders_dishes.orders_idorder\n"
                + "INNER JOIN dishes ON dishes.iddish = orders_dishes.dishes_iddish\n"
                + "ORDER BY order_date\n"
        );
        orders_data = new Vector<Vector>();
        orders_column_names = new Vector<String>();
        orders_column_names.addElement("Дата заказа");
        orders_column_names.addElement("Блюдо");
        orders_column_names.addElement("Владелец заказа");
        while (resultSet.next()) {
            Vector<String> row = new Vector<String>();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            row.add(dateFormat.format(resultSet.getDate(1)));
            row.add(String.valueOf(resultSet.getString(2)));
            row.add(String.valueOf(resultSet.getString(3)));
            orders_data.add(row);
        }
        ordersTable.setModel(new DefaultTableModel(orders_data, orders_column_names));
    }


    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane4 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        employeesTable = new javax.swing.JTable();
        deleteEmployeeButton = new javax.swing.JButton();
        addEmployeeButton = new javax.swing.JButton();
        changeEmployeeButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        positionsTable = new javax.swing.JTable();
        deletePositionButton = new javax.swing.JButton();
        addPositionButton = new javax.swing.JButton();
        changePositionButton = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        ingredientsTable = new javax.swing.JTable();
        deleteIngredientButton = new javax.swing.JButton();
        addIngredientButton = new javax.swing.JButton();
        changeIngredientButton = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        menuTable = new javax.swing.JTable();
        deleteMenuButton = new javax.swing.JButton();
        addMenuButton = new javax.swing.JButton();
        changeMenuButton = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        dishesTable = new javax.swing.JTable();
        deleteDishButton = new javax.swing.JButton();
        addDishButton = new javax.swing.JButton();
        changeDishButton = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        ordersTable = new javax.swing.JTable();
        addOrderButton = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        employeesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(employeesTable);

        deleteEmployeeButton.setText("Удалить");
        deleteEmployeeButton.setPreferredSize(new java.awt.Dimension(85, 25));

        addEmployeeButton.setText("Добавить");
        addEmployeeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addEmployeeButtonActionPerformed(evt);
            }
        });

        changeEmployeeButton.setText("Редактировать");
        changeEmployeeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeEmployeeButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(changeEmployeeButton)
                .addGap(18, 18, 18)
                .addComponent(addEmployeeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(deleteEmployeeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(88, 88, 88))
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 846, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 365, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(deleteEmployeeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addEmployeeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(changeEmployeeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
        );

        jTabbedPane4.addTab("Сотрудники", jPanel1);

        positionsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane5.setViewportView(positionsTable);

        deletePositionButton.setText("Удалить");
        deletePositionButton.setPreferredSize(new java.awt.Dimension(85, 25));

        addPositionButton.setText("Добавить");
        addPositionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addPositionButtonActionPerformed(evt);
            }
        });

        changePositionButton.setText("Редактировать");
        changePositionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changePositionButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(changePositionButton)
                .addGap(18, 18, 18)
                .addComponent(addPositionButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(deletePositionButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(88, 88, 88))
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 846, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 365, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(deletePositionButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addPositionButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(changePositionButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
        );

        jTabbedPane4.addTab("Должности", jPanel2);

        ingredientsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane6.setViewportView(ingredientsTable);

        deleteIngredientButton.setText("Удалить");
        deleteIngredientButton.setPreferredSize(new java.awt.Dimension(85, 25));

        addIngredientButton.setText("Добавить");
        addIngredientButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addIngredientButtonActionPerformed(evt);
            }
        });

        changeIngredientButton.setText("Редактировать");
        changeIngredientButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeIngredientButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(changeIngredientButton)
                .addGap(18, 18, 18)
                .addComponent(addIngredientButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(deleteIngredientButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(88, 88, 88))
            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 846, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 365, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(deleteIngredientButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addIngredientButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(changeIngredientButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
        );

        jTabbedPane4.addTab("Ингредиенты", jPanel3);

        menuTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane4.setViewportView(menuTable);

        deleteMenuButton.setText("Удалить");
        deleteMenuButton.setPreferredSize(new java.awt.Dimension(85, 25));

        addMenuButton.setText("Добавить");
        addMenuButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addMenuButtonActionPerformed(evt);
            }
        });

        changeMenuButton.setText("Редактировать");
        changeMenuButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeMenuButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(changeMenuButton)
                .addGap(18, 18, 18)
                .addComponent(addMenuButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(deleteMenuButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(88, 88, 88))
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 846, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 365, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(deleteMenuButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addMenuButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(changeMenuButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
        );

        jTabbedPane4.addTab("Меню", jPanel4);

        dishesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane7.setViewportView(dishesTable);

        deleteDishButton.setText("Удалить");
        deleteDishButton.setPreferredSize(new java.awt.Dimension(85, 25));

        addDishButton.setText("Добавить");
        addDishButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addDishButtonActionPerformed(evt);
            }
        });

        changeDishButton.setText("Редактировать");
        changeDishButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeDishButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(changeDishButton)
                .addGap(18, 18, 18)
                .addComponent(addDishButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(deleteDishButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(88, 88, 88))
            .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 846, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 365, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(deleteDishButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addDishButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(changeDishButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
        );

        jTabbedPane4.addTab("Блюда", jPanel5);

        ordersTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane8.setViewportView(ordersTable);

        addOrderButton.setText("Добавить");
        addOrderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addOrderButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 846, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(addOrderButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(207, 207, 207))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 365, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(addOrderButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        jTabbedPane4.addTab("История заказов", jPanel6);

        jMenu1.setText("Меню");

        jMenuItem1.setText("Просроченные ингредиенты");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setText("Заказы по дате");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem3.setText("Популярные блюда");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane4, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane4, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addEmployeeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addEmployeeButtonActionPerformed
        int position_id = 0;
        NewEmployeeForm emp = new NewEmployeeForm(this, "Добавление сотрудника", true);
        emp.setVisible(true);
        try {
            PreparedStatement res = conn.prepareStatement("SELECT idposition\n"
                    + "FROM positions\n"
                    + " WHERE position=?\n");
            res.setString(1, emp.getPosition());
            ResultSet rs = res.executeQuery();
            if (rs.next()) {
                position_id = rs.getInt(1);
            }
            res.close();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO employees (idemployee, full_name, phone_number,idposition) VALUES (?,?,?,?)");
            int next_id = employees_data.size() + 1;
            ps.setInt(1, next_id);
            ps.setString(2, emp.getName());
            ps.setString(3, emp.getNumber());
            ps.setInt(4, position_id);
            ps.executeUpdate();
            initEmployeesTable();
        } catch (SQLException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_addEmployeeButtonActionPerformed

    private void changeEmployeeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeEmployeeButtonActionPerformed
        int row = employeesTable.getSelectedRow();
        String number = (String) employees_data.get(row).get(1);
        NewEmployeeForm emp = new NewEmployeeForm(this, "Редактирование сотрудника", true);
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT full_name,phone_number,position\n"
                    + "FROM employees\n"
                    + " JOIN positions\n"
                    + "ON positions.idposition=employees.idposition;");
            while (resultSet.next()) {
                if ((resultSet.getString(2)).equals(number)) {
                    emp.setName(resultSet.getString(1));
                    emp.setNumber(resultSet.getString(2));
                    emp.setPosition(resultSet.getString(3));
                }
            }
            statement.close();
            emp.setVisible(true);
            PreparedStatement res = conn.prepareStatement("SELECT idposition\n"
                    + "FROM positions\n"
                    + " WHERE position=?\n");
            res.setString(1, emp.getPosition());
            ResultSet rs = res.executeQuery();
            int new_position = 0;
            if (rs.next()) {
                new_position = rs.getInt(1);
            }
            res.close();
            res = conn.prepareStatement("UPDATE employees SET full_name=?, phone_number=?, idposition=? WHERE phone_number=?\n");
            res.setString(1, emp.getName());
            res.setString(2, emp.getNumber());
            res.setInt(3, new_position);
            res.setString(4, number);
            res.executeUpdate();
            initEmployeesTable();
        } catch (SQLException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_changeEmployeeButtonActionPerformed

    private void addPositionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addPositionButtonActionPerformed
        NewPositionForm emp = new NewPositionForm(this, "Добавление должности", true);
        emp.setVisible(true);
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO positions (idposition, position, salary) VALUES (?,?,?)");
            int next_id = positions_data.size() + 1;
            ps.setInt(1, next_id);
            ps.setString(2, emp.getPosition());
            ps.setInt(3, emp.getSalary());
            ps.executeUpdate();
            initPositionsTable();
        } catch (SQLException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_addPositionButtonActionPerformed

    private void changePositionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changePositionButtonActionPerformed
        int row = positionsTable.getSelectedRow();
        String number = (String) positions_data.get(row).get(0);
        NewPositionForm emp = new NewPositionForm(this, "Редактирование должности", true);
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT position, salary\n"
                    + "FROM positions\n");
            while (resultSet.next()) {
                if ((resultSet.getString(1)).equals(number)) {
                    emp.setPosition(resultSet.getString(1));
                    emp.setSalary(resultSet.getInt(2));
                }
            }
            statement.close();
            emp.setVisible(true);
            PreparedStatement res = conn.prepareStatement("UPDATE positions SET position=?, salary=? WHERE position=?\n");
            res.setString(1, emp.getPosition());
            res.setInt(2, emp.getSalary());
            res.setString(3, number);
            res.executeUpdate();
            initPositionsTable();
            initEmployeesTable();
        } catch (SQLException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_changePositionButtonActionPerformed

    private void addIngredientButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addIngredientButtonActionPerformed
        NewIngredientForm emp = new NewIngredientForm(this, "Добавление ингредиента", true);
        emp.setVisible(true);
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO ingredients (ingredient, weight_kg, ingredient_date,ingredient_expiringdate) VALUES (?,?,?,?)");
            int next_id = ingredients_data.size() + 1;
            ps.setInt(1, next_id);
            ps.setDouble(2, emp.getWeight());
            ps.setDate(3, Date.valueOf(emp.getIngredient_date()));
            ps.setDate(4, Date.valueOf(emp.getIngredient_expiringdate()));
            ps.executeUpdate();
            initIngredientsTable();
        } catch (SQLException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_addIngredientButtonActionPerformed

    private void changeIngredientButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeIngredientButtonActionPerformed
        int row = ingredientsTable.getSelectedRow();
        String ingredient = (String) ingredients_data.get(row).get(0);
        NewIngredientForm emp = new NewIngredientForm(this, "Редактирование ингредиента", true);
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT ingredient, weight_kg, ingredient_date,ingredient_expiringdate\n"
                    + "FROM ingredients\n");
            while (resultSet.next()) {
                if ((resultSet.getString(1)).equals(ingredient)) {
                    emp.setIngredient(resultSet.getString(1));
                    emp.setWeight(resultSet.getDouble(2));
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    emp.setIngredient_date(dateFormat.format(resultSet.getDate(3)));
                    emp.setIngredient_expiringdate(dateFormat.format(resultSet.getDate(4)));
                }
            }
            statement.close();
            emp.setVisible(true);
            PreparedStatement res = conn.prepareStatement("UPDATE ingredients SET ingredient=?, weight_kg=?,ingredient_date=?,ingredient_expiringdate=? WHERE ingredient=?\n");
            res.setString(1, emp.getIngredient());
            res.setDouble(2, emp.getWeight());
            res.setDate(3, Date.valueOf(emp.getIngredient_date()));
            res.setDate(4, Date.valueOf(emp.getIngredient_expiringdate()));
            res.setString(5, ingredient);
            res.executeUpdate();
            initIngredientsTable();
        } catch (SQLException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_changeIngredientButtonActionPerformed

    private void addMenuButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addMenuButtonActionPerformed
        int dish_id = 0;
        NewMenuForm emp = new NewMenuForm(this, "Добавление пункта меню", true);
        emp.setVisible(true);
        try {
            PreparedStatement res = conn.prepareStatement("SELECT iddish\n"
                    + "FROM dishes\n"
                    + " WHERE dish=?\n");
            res.setString(1, emp.getName());
            ResultSet rs = res.executeQuery();
            if (rs.next()) {
                dish_id = rs.getInt(1);
            }
            res.close();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO menu (idmenu, iddish, price_rub) VALUES (?,?,?)");
            int next_id = menu_data.size() + 1;
            ps.setInt(1, next_id);
            ps.setInt(2, dish_id);
            ps.setInt(3, emp.getPrice());
            ps.executeUpdate();
            initMenuTable();
        } catch (SQLException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_addMenuButtonActionPerformed

    private void changeMenuButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeMenuButtonActionPerformed
        int row = menuTable.getSelectedRow();
        String name = (String) menu_data.get(row).get(0);
        NewMenuForm emp = new NewMenuForm(this, "Редактирование пункта меню", true);
        emp.setNonEditable();
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT dish,price_rub\n"
                    + "FROM menu\n"
                    + " JOIN dishes\n"
                    + "ON dishes.iddish=menu.iddish;");
            while (resultSet.next()) {
                if ((resultSet.getString(1)).equals(name)) {
                    emp.setName(resultSet.getString(1));
                    emp.setPrice(Integer.valueOf(resultSet.getString(2)));
                }
            }
            statement.close();
            emp.setVisible(true);
            PreparedStatement res = conn.prepareStatement("SELECT iddish\n"
                    + "FROM dishes\n"
                    + " WHERE dish=?\n");
            res.setString(1, emp.getName());
            ResultSet rs = res.executeQuery();
            int dish_id = 0;
            if (rs.next()) {
                dish_id = rs.getInt(1);
            }
            res.close();
            res = conn.prepareStatement("UPDATE menu SET price_rub=? WHERE iddish=?\n");
            res.setInt(1, emp.getPrice());
            res.setInt(2, dish_id);
            res.executeUpdate();
            initMenuTable();
        } catch (SQLException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_changeMenuButtonActionPerformed

    private void addDishButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addDishButtonActionPerformed
        int dish_id = 0;
        NewDishForm emp = new NewDishForm(this, "Добавление блюда", true);
        emp.setVisible(true);
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT MAX(iddish)\n"
                    + "FROM dishes\n");
            if (resultSet.next()) {
                dish_id = resultSet.getInt(1) + 1;
            }
            resultSet.close();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO dishes (iddish, dish, dish_weight_gr) VALUES (?,?,?)");
            ps.setInt(1, dish_id);
            ps.setString(2, emp.getName());
            ps.setInt(3, emp.getWeight());
            ps.executeUpdate();
            ps.close();
            int ingr_id = 0;
            for (int i = 1; i < 6; i++) {
                String ingr = "";
                int ingr_weight = 0;
                switch (i) {
                    case 1:
                        ingr = emp.getIngr1();
                        ingr_weight = emp.getIngr1_weight();
                        break;
                    case 2:
                        ingr = emp.getIngr2();
                        ingr_weight = emp.getIngr2_weight();
                        break;
                    case 3:
                        ingr = emp.getIngr3();
                        ingr_weight = emp.getIngr3_weight();
                        break;
                    case 4:
                        ingr = emp.getIngr4();
                        ingr_weight = emp.getIngr4_weight();
                        break;
                    case 5:
                        ingr = emp.getIngr5();
                        ingr_weight = emp.getIngr5_weight();
                        break;
                }
                if (ingr.equals("-")) {
                    continue;
                }
                ps = conn.prepareStatement("SELECT idingredient\n"
                        + "FROM ingredients\n"
                        + " WHERE ingredient=?\n");
                ps.setString(1, ingr);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    ingr_id = rs.getInt(1);
                }
                ps.close();
                ps = conn.prepareStatement("INSERT INTO dishes_ingredients (dishes_iddish,ingredients_idingredient, ingredient_weight_gr) VALUES (?,?,?)");
                ps.setInt(1, dish_id);
                ps.setInt(2, ingr_id);
                ps.setInt(3, ingr_weight);
                ps.executeUpdate();
                ps.close();
            }

            initDishesTable();
        } catch (SQLException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_addDishButtonActionPerformed

    private void changeDishButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeDishButtonActionPerformed
        int row = dishesTable.getSelectedRow();
        String name = (String) dishes_data.get(row).get(0);
        NewDishForm emp = new NewDishForm(this, "Редактирование блюда", true);
        emp.setIngr1("-");
        emp.setIngr1_weight(0);
        emp.setIngr2("-");
        emp.setIngr2_weight(0);
        emp.setIngr3("-");
        emp.setIngr3_weight(0);
        emp.setIngr4("-");
        emp.setIngr4_weight(0);
        emp.setIngr5("-");
        emp.setIngr5_weight(0);
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT dish,dish_weight_gr,ingredient,ingredient_weight_gr\n"
                    + "FROM dishes\n"
                    + "LEFT JOIN dishes_ingredients ON dishes.iddish = dishes_ingredients.dishes_iddish\n"
                    + "INNER JOIN ingredients ON ingredients.idingredient = dishes_ingredients.ingredients_idingredient\n"
                    + "ORDER BY dish\n");
            int n = 1;
            while (resultSet.next()) {
                if ((resultSet.getString(1)).equals(name)) {
                    emp.setName(resultSet.getString(1));
                    emp.setWeight(resultSet.getInt(2));
                    switch (n) {
                        case 1:
                            emp.setIngr1(resultSet.getString(3));
                            emp.setIngr1_weight(resultSet.getInt(4));
                            break;
                        case 2:
                            emp.setIngr2(resultSet.getString(3));
                            emp.setIngr2_weight(resultSet.getInt(4));
                            break;
                        case 3:
                            emp.setIngr3(resultSet.getString(3));
                            emp.setIngr3_weight(resultSet.getInt(4));
                            break;
                        case 4:
                            emp.setIngr4(resultSet.getString(3));
                            emp.setIngr4_weight(resultSet.getInt(4));
                            break;
                        case 5:
                            emp.setIngr5(resultSet.getString(3));
                            emp.setIngr5_weight(resultSet.getInt(4));
                            break;
                    }
                    n++;

                }
            }
            statement.close();
            emp.setVisible(true);
            PreparedStatement res = conn.prepareStatement("UPDATE dishes SET dish=?,dish_weight_gr=? WHERE dish=?\n");
            res.setString(1, emp.getName());
            res.setInt(2, emp.getWeight());
            res.setString(3, name);
            res.executeUpdate();
            res.close();
            res = conn.prepareStatement("SELECT iddish\n"
                    + "FROM dishes\n"
                    + " WHERE dish=?\n");
            res.setString(1, name);
            ResultSet rs = res.executeQuery();
            int dish_id = 0;
            if (rs.next()) {
                dish_id = rs.getInt(1);
            }
            res.close();
            int ingr_id = 0;
            for (int i = 1; i < 6; i++) {
                String ingr = "";
                int ingr_weight = 0;
                switch (i) {
                    case 1:
                        ingr = emp.getIngr1();
                        ingr_weight = emp.getIngr1_weight();
                        break;
                    case 2:
                        ingr = emp.getIngr2();
                        ingr_weight = emp.getIngr2_weight();
                        break;
                    case 3:
                        ingr = emp.getIngr3();
                        ingr_weight = emp.getIngr3_weight();
                        break;
                    case 4:
                        ingr = emp.getIngr4();
                        ingr_weight = emp.getIngr4_weight();
                        break;
                    case 5:
                        ingr = emp.getIngr5();
                        ingr_weight = emp.getIngr5_weight();
                        break;
                }
                if (ingr.equals("-")) {
                    continue;
                }
                res = conn.prepareStatement("SELECT idingredient\n"
                        + "FROM ingredients\n"
                        + " WHERE ingredient=?\n");
                res.setString(1, ingr);
                rs = res.executeQuery();
                if (rs.next()) {
                    ingr_id = rs.getInt(1);
                }
                res.close();
                res = conn.prepareStatement("DELETE FROM dishes_ingredients WHERE dishes_iddish=?");
                res.setInt(1, dish_id);
                res.executeUpdate();
                res.close();
                res = conn.prepareStatement("INSERT INTO dishes_ingredients (dishes_iddish,ingredients_idingredient, ingredient_weight_gr) VALUES (?,?,?)");
                res.setInt(1, dish_id);
                res.setInt(2, ingr_id);
                res.setInt(3, ingr_weight);
                res.executeUpdate();
                res.close();
            }

            initDishesTable();
            initMenuTable();

        } catch (SQLException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_changeDishButtonActionPerformed

    private void addOrderButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addOrderButtonActionPerformed
        int order_id = 0;
        NewOrderForm emp = new NewOrderForm(this, "Добавление заказа", true);
        emp.setVisible(true);
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT MAX(idorder)\n"
                    + "FROM orders\n");
            if (resultSet.next()) {
                order_id = resultSet.getInt(1) + 1;
            }
            resultSet.close();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO orders (idorder,order_date,order_owner) VALUES (?,?,?)");
            ps.setInt(1, order_id);
            ps.setDate(2, Date.valueOf(emp.getOrder_date()));
            ps.setString(3, emp.getOrder_owner());
            ps.executeUpdate();
            ps.close();
            int dish_id = 0;
            for (int i = 1; i < 4; i++) {
                String dish = "";
                switch (i) {
                    case 1:
                        dish = emp.getDish1();
                        break;
                    case 2:
                        dish = emp.getDish2();
                        break;
                    case 3:
                        dish = emp.getDish3();
                        break;
                }
                if (dish.equals("-")) {
                    continue;
                }
                ps = conn.prepareStatement("SELECT iddish\n"
                        + "FROM dishes\n"
                        + " WHERE dish=?\n");
                ps.setString(1, dish);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    dish_id = rs.getInt(1);
                }
                ps.close();
                ps = conn.prepareStatement("SELECT dish,dish_weight_gr,ingredient,ingredient_weight_gr\n"
                        + "FROM dishes\n"
                        + "LEFT JOIN dishes_ingredients ON dishes.iddish = dishes_ingredients.dishes_iddish\n"
                        + "INNER JOIN ingredients ON ingredients.idingredient = dishes_ingredients.ingredients_idingredient\n"
                        + "WHERE dish=?"
                        + "ORDER BY dish\n"
                );
                ps.setString(1, dish);
                rs = ps.executeQuery();
                while (rs.next()) {
                    String ing = rs.getString(3);
                    int ing_weight = rs.getInt(4);
                    
                    PreparedStatement res = conn.prepareStatement("UPDATE ingredients SET weight_kg=weight_kg-? WHERE ingredient=?\n");
                    res.setDouble(1,0.001*ing_weight);
                    res.setString(2, ing);
                    res.executeUpdate();
                    

                }

                ps.close();

                ps = conn.prepareStatement("INSERT INTO orders_dishes (orders_idorder,dishes_iddish) VALUES (?,?)");
                ps.setInt(1, order_id);
                ps.setInt(2, dish_id);
                ps.executeUpdate();
                ps.close();
            }
            initOrdersTable();
            initIngredientsTable();

        } catch (SQLException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_addOrderButtonActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        OutdatedIngredientsForm form;
        try {
            form = new OutdatedIngredientsForm(this, "Просроченные ингредиенты", true, conn);
            form.setVisible(true);
        } catch (SQLException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        FindOrderForm form;
        try {
            form = new FindOrderForm(this, "Заказы по дате", true, conn);
            form.setVisible(true);
        } catch (SQLException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        PopularDishesForm form;
        try {
            form = new PopularDishesForm(this, "Популярные блюда", true, conn);
            form.setVisible(true);
        } catch (SQLException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem3ActionPerformed
    static String login;
    static String password;

    public static void main(String args[]) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {

                    new MainForm(login, password).setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addDishButton;
    private javax.swing.JButton addEmployeeButton;
    private javax.swing.JButton addIngredientButton;
    private javax.swing.JButton addMenuButton;
    private javax.swing.JButton addOrderButton;
    private javax.swing.JButton addPositionButton;
    private javax.swing.JButton changeDishButton;
    private javax.swing.JButton changeEmployeeButton;
    private javax.swing.JButton changeIngredientButton;
    private javax.swing.JButton changeMenuButton;
    private javax.swing.JButton changePositionButton;
    private javax.swing.JButton deleteDishButton;
    private javax.swing.JButton deleteEmployeeButton;
    private javax.swing.JButton deleteIngredientButton;
    private javax.swing.JButton deleteMenuButton;
    private javax.swing.JButton deletePositionButton;
    private javax.swing.JTable dishesTable;
    private javax.swing.JTable employeesTable;
    private javax.swing.JTable ingredientsTable;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JTabbedPane jTabbedPane4;
    private javax.swing.JTable menuTable;
    private javax.swing.JTable ordersTable;
    private javax.swing.JTable positionsTable;
    // End of variables declaration//GEN-END:variables
}
