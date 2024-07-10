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
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame {
    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    public LoginFrame() {
        setTitle("Login");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        JLabel lblUsuario = new JLabel("Usuario:");
        panel.add(lblUsuario);

        txtUsuario = new JTextField();
        panel.add(txtUsuario);

        JLabel lblPassword = new JLabel("Contraseña:");
        panel.add(lblPassword);

        txtPassword = new JPasswordField();
        panel.add(txtPassword);

        btnLogin = new JButton("Login");
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usuario = txtUsuario.getText();
                String password = String.valueOf(txtPassword.getPassword());

                if (usuario.equals("admin") && password.equals("1234")) {
                    JOptionPane.showMessageDialog(LoginFrame.this, "Login exitoso");
                    abrirRegistroEstudiantes();
                } else {
                    JOptionPane.showMessageDialog(LoginFrame.this, "Usuario o contraseña incorrectos", "Error de Login", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panel.add(btnLogin);

        getContentPane().add(panel);
    }

    private void abrirRegistroEstudiantes() {
        SwingUtilities.invokeLater(() -> {
            StudentApp app = new StudentApp();
            app.setVisible(true);
            dispose(); 
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
