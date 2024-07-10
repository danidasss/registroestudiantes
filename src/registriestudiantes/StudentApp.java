/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package registriestudiantes;

/**
 *
 * @author Daniel
 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class StudentApp extends JFrame {
    private JTextField txtNombre, txtApellido, txtCI, txtEdad, txtEstado, txtCiudad, txtBuscar, txtEscuela;
    private JComboBox<String> cbEstadoCivil, cbSexo;
    private JButton btnGuardar, btnModificar, btnEliminar, btnSalir, btnBuscar;
    private JTable table;
    private DefaultTableModel tableModel;

    public StudentApp() {
        setTitle("Registro de Estudiantes");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(14, 2));
        panel.setBackground(new Color(173, 216, 230)); 

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setForeground(new Color(25, 25, 112)); 
        panel.add(lblNombre);
        txtNombre = new JTextField();
        panel.add(txtNombre);

        JLabel lblApellido = new JLabel("Apellido:");
        lblApellido.setForeground(new Color(25, 25, 112)); 
        panel.add(lblApellido);
        txtApellido = new JTextField();
        panel.add(txtApellido);

        JLabel lblCI = new JLabel("C.I.:");
        lblCI.setForeground(new Color(25, 25, 112)); 
        panel.add(lblCI);
        txtCI = new JTextField();
        txtCI.setDocument(new JTextFieldLimit(8));
        panel.add(txtCI);

        JLabel lblEdad = new JLabel("Edad:");
        lblEdad.setForeground(new Color(25, 25, 112)); 
        panel.add(lblEdad);
        txtEdad = new JTextField();
        panel.add(txtEdad);

        JLabel lblEstadoCivil = new JLabel("Estado Civil:");
        lblEstadoCivil.setForeground(new Color(25, 25, 112)); 
        panel.add(lblEstadoCivil);
        cbEstadoCivil = new JComboBox<>(new String[]{"Soltero", "Casado", "Divorciado", "Viudo"});
        panel.add(cbEstadoCivil);

        JLabel lblSexo = new JLabel("Sexo:");
        lblSexo.setForeground(new Color(25, 25, 112)); 
        panel.add(lblSexo);
        cbSexo = new JComboBox<>(new String[]{"Masculino", "Femenino"});
        panel.add(cbSexo);

        JLabel lblEstado = new JLabel("Estado:");
        lblEstado.setForeground(new Color(25, 25, 112)); 
        panel.add(lblEstado);
        txtEstado = new JTextField();
        panel.add(txtEstado);

        JLabel lblCiudad = new JLabel("Ciudad:");
        lblCiudad.setForeground(new Color(25, 25, 112)); 
        panel.add(lblCiudad);
        txtCiudad = new JTextField();
        panel.add(txtCiudad);

        JLabel lblEscuela = new JLabel("N° Escuela:");
        lblEscuela.setForeground(new Color(25, 25, 112)); 
        panel.add(lblEscuela);
        txtEscuela = new JTextField();
        txtEscuela.setDocument(new JTextFieldLimit(2));
        panel.add(txtEscuela);

        btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(new Color(60, 179, 113)); 
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.addActionListener(e -> guardarEstudiante());
        panel.add(btnGuardar);

        btnModificar = new JButton("Modificar");
        btnModificar.setBackground(new Color(255, 165, 0)); 
        btnModificar.setForeground(Color.WHITE);
        btnModificar.addActionListener(e -> modificarEstudiante());
        panel.add(btnModificar);

        btnEliminar = new JButton("Eliminar");
        btnEliminar.setBackground(new Color(255, 69, 0)); 
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.addActionListener(e -> eliminarEstudiante());
        panel.add(btnEliminar);

        btnSalir = new JButton("Salir");
        btnSalir.setBackground(new Color(0, 191, 255)); 
        btnSalir.setForeground(Color.WHITE);
        btnSalir.addActionListener(e -> System.exit(0));
        panel.add(btnSalir);

        txtBuscar = new JTextField();
        panel.add(txtBuscar);

        btnBuscar = new JButton("Buscar");
        btnBuscar.setBackground(new Color(30, 144, 255)); 
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.addActionListener(e -> buscarEstudiante());
        panel.add(btnBuscar);

        JLabel lblBuscar = new JLabel("Buscar:");
        lblBuscar.setForeground(new Color(25, 25, 112)); 
        panel.add(lblBuscar);

        Container container = getContentPane();
        container.setLayout(new BorderLayout());
        container.add(panel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"Nombre", "Apellido", "C.I.", "Edad", "Estado Civil", "Sexo", "Estado", "Ciudad", "N° Escuela"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        container.add(scrollPane, BorderLayout.CENTER);

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                    cargarDatosSeleccionados(table.getSelectedRow());
                }
            }
        });

        cargarEstudiantes();
    }

    private void guardarEstudiante() {
        if (!validarCampos()) {
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sqlDireccion = "INSERT INTO Direcciones (estado, ciudad) VALUES (?, ?)";
            PreparedStatement psDireccion = conn.prepareStatement(sqlDireccion, Statement.RETURN_GENERATED_KEYS);
            psDireccion.setString(1, txtEstado.getText());
            psDireccion.setString(2, txtCiudad.getText());
            psDireccion.executeUpdate();
            ResultSet rsDireccion = psDireccion.getGeneratedKeys();
            rsDireccion.next();
            int idDireccion = rsDireccion.getInt(1);

            String sqlEstudiante = "INSERT INTO Estudiantes (nombre, apellido, ci, edad, id_direccion, estado_civil, sexo, numero_escuela) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement psEstudiante = conn.prepareStatement(sqlEstudiante);
            psEstudiante.setString(1, txtNombre.getText());
            psEstudiante.setString(2, txtApellido.getText());
            psEstudiante.setString(3, txtCI.getText());
            psEstudiante.setInt(4, Integer.parseInt(txtEdad.getText()));
            psEstudiante.setInt(5, idDireccion);
            psEstudiante.setString(6, cbEstadoCivil.getSelectedItem().toString());
            psEstudiante.setString(7, cbSexo.getSelectedItem().toString());
            psEstudiante.setInt(8, Integer.parseInt(txtEscuela.getText()));
            psEstudiante.executeUpdate();

            JOptionPane.showMessageDialog(this, "Estudiante guardado exitosamente.");
            cargarEstudiantes(); 
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al guardar el estudiante.");
        }
    }

    private void modificarEstudiante() {
        if (!validarCampos()) {
            return;
        }

        int response = JOptionPane.showConfirmDialog(this, "¿Está seguro de modificar este estudiante?", "Confirmar Modificación", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.NO_OPTION) {
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sqlEstudiante = "UPDATE Estudiantes SET nombre = ?, apellido = ?, edad = ?, estado_civil = ?, sexo = ?, numero_escuela = ? WHERE ci = ?";
            PreparedStatement psEstudiante = conn.prepareStatement(sqlEstudiante);
            psEstudiante.setString(1, txtNombre.getText());
            psEstudiante.setString(2, txtApellido.getText());
            psEstudiante.setInt(3, Integer.parseInt(txtEdad.getText()));
            psEstudiante.setString(4, cbEstadoCivil.getSelectedItem().toString());
            psEstudiante.setString(5, cbSexo.getSelectedItem().toString());
            psEstudiante.setInt(6, Integer.parseInt(txtEscuela.getText()));
            psEstudiante.setString(7, txtCI.getText());
            psEstudiante.executeUpdate();

            String sqlDireccion = "UPDATE Direcciones SET estado = ?, ciudad = ? WHERE id = (SELECT id_direccion FROM Estudiantes WHERE ci = ?)";
            PreparedStatement psDireccion = conn.prepareStatement(sqlDireccion);
            psDireccion.setString(1, txtEstado.getText());
            psDireccion.setString(2, txtCiudad.getText());
            psDireccion.setString(3, txtCI.getText());
            psDireccion.executeUpdate();

            JOptionPane.showMessageDialog(this, "Estudiante modificado exitosamente.");
            cargarEstudiantes(); 
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al modificar el estudiante.");
        }
    }

    private void eliminarEstudiante() {
        int response = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar este estudiante?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.NO_OPTION) {
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sqlEstudiante = "DELETE FROM Estudiantes WHERE ci = ?";
            PreparedStatement psEstudiante = conn.prepareStatement(sqlEstudiante);
            psEstudiante.setString(1, txtCI.getText());
            psEstudiante.executeUpdate();

            JOptionPane.showMessageDialog(this, "Estudiante eliminado exitosamente.");
            cargarEstudiantes(); 
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al eliminar el estudiante.");
        }
    }

    private void cargarEstudiantes() {
        tableModel.setRowCount(0); 

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT e.nombre, e.apellido, e.ci, e.edad, e.estado_civil, e.sexo, d.estado, d.ciudad, e.numero_escuela " +
                         "FROM Estudiantes e " +
                         "JOIN Direcciones d ON e.id_direccion = d.id";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getString("nombre"),
                    rs.getString("apellido"),
                    rs.getString("ci"),
                    rs.getInt("edad"),
                    rs.getString("estado_civil"),
                    rs.getString("sexo"),
                    rs.getString("estado"),
                    rs.getString("ciudad"),
                    rs.getInt("numero_escuela")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar los datos de los estudiantes.");
        }
    }

    private void cargarDatosSeleccionados(int row) {
        txtNombre.setText(table.getValueAt(row, 0).toString());
        txtApellido.setText(table.getValueAt(row, 1).toString());
        txtCI.setText(table.getValueAt(row, 2).toString());
        txtEdad.setText(table.getValueAt(row, 3).toString());
        cbEstadoCivil.setSelectedItem(table.getValueAt(row, 4).toString());
        cbSexo.setSelectedItem(table.getValueAt(row, 5).toString());
        txtEstado.setText(table.getValueAt(row, 6).toString());
        txtCiudad.setText(table.getValueAt(row, 7).toString());
        txtEscuela.setText(table.getValueAt(row, 8).toString());
    }

    private boolean validarCampos() {
        if (txtNombre.getText().isEmpty() || txtApellido.getText().isEmpty() || txtCI.getText().isEmpty() ||
                txtEdad.getText().isEmpty() || txtEstado.getText().isEmpty() || txtCiudad.getText().isEmpty() ||
                txtEscuela.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.");
            return false;
        }

        try {
            Integer.parseInt(txtEdad.getText());
            Integer.parseInt(txtEscuela.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Edad y N° Escuela deben ser números enteros.");
            return false;
        }

        return true;
    }

    private void buscarEstudiante() {
        String textoBuscar = txtBuscar.getText().trim();

        if (textoBuscar.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un criterio de búsqueda.");
            return;
        }

        tableModel.setRowCount(0); 

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT e.nombre, e.apellido, e.ci, e.edad, e.estado_civil, e.sexo, d.estado, d.ciudad, e.numero_escuela " +
                         "FROM Estudiantes e " +
                         "JOIN Direcciones d ON e.id_direccion = d.id " +
                         "WHERE e.ci = ? OR e.nombre LIKE ? OR e.apellido LIKE ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, textoBuscar);
            ps.setString(2, "%" + textoBuscar + "%");
            ps.setString(3, "%" + textoBuscar + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getString("nombre"),
                    rs.getString("apellido"),
                    rs.getString("ci"),
                    rs.getInt("edad"),
                    rs.getString("estado_civil"),
                    rs.getString("sexo"),
                    rs.getString("estado"),
                    rs.getString("ciudad"),
                    rs.getInt("numero_escuela")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al buscar los datos del estudiante.");
        }
    }

    public static void main(String[] args) {
        {
            StudentApp app = new StudentApp();
            app.setVisible(true);
        });
    }
}

class DatabaseConnection {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/registroestudiantes";
        String user = "root";
        String password = "";
        return DriverManager.getConnection(url, user, password);
    }
}

class JTextFieldLimit extends PlainDocument {
    private int limit;

    JTextFieldLimit(int limit) {
        super();
        this.limit = limit;
    }

    @Override
    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
        if (str == null) {
            return;
        }

        if ((getLength() + str.length()) <= limit) {
            super.insertString(offset, str, attr);
        }
    }
}
