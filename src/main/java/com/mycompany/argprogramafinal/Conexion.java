/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.argprogramafinal;

import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author nes
 */
public class Conexion {

    Connection conectar = null;

    String usuario = "root";
    String contraseña = "ROOT";
    String bd = "argprograma";
    String ip = "localhost";
    String puerto = "3306";

    String ruta = "jdbc:mysql://" + ip + ":" + puerto + "/" + bd;

    public Connection estableceConexion() {

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            conectar = DriverManager.getConnection(ruta, usuario, contraseña);

            System.out.println("Se conecto");

            JOptionPane.showMessageDialog(null, "Se conecto correctamente");
            
        } catch (HeadlessException | ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, "NO se conecto correctamente" + e);
        }

        return conectar;

    }

    public void cerrarConnection() throws SQLException {
        try {
            conectar.close();
        } catch (SQLException e) {
        }
    }
}
