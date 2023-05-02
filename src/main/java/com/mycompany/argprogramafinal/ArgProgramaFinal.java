/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.argprogramafinal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author nes
 */

public class ArgProgramaFinal {

   
     
    private static final Scanner sc = new Scanner(System.in).useDelimiter("\n");

    private static final Conexion conexion = new Conexion();

    public static void main(String[] args) throws SQLException, JsonProcessingException  {

//        HashMap<Integer, Alumnos> todosAlum = new HashMap<>();
//
//        HashMap<Integer , Materia> materiasTodas = new HashMap<>();
//        Conexion conexion = new Conexion();
        int e;

        do {

            System.out.println(" *********                      BIEN VENIDOS A FACULTAD                *********");

            System.out.println(" **** PULSE LA OPCION DESEADA ****  ");

            System.out.println(" 0 :  AGREGAR UNA MATERIA");

            System.out.println(" 1 : AGREGAR UN ALUMNO");

            System.out.println(" 2 : INSCRIBIRSE EN UNA MATERIA ");

            System.out.println(" 3 : SALIR DEL PROGRAMA ");

            e = sc.nextInt();

            switch (e) {
                case 0 -> {
crearMateria();
                    
               
      
                 
      
                }
                case 1 -> {

                    agregarAlumnoBd() ;
                    
                   

                }
                case 2 -> {

                    Inscripcion inscripcion = new Inscripcion();
                   inscripcion.crearInscripcion();
                

                }
            }
            } while (e != 3);
        {

        }
    }
    
    public static Alumno traerDatosAlumno(int legajo) throws SQLException, JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

        conexion.estableceConexion();
        Statement stmt = conexion.conectar.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM alumnos_final WHERE legajo=" + legajo + ";");
        rs.next();
        Alumno alumno = new Alumno(rs.getString("nombre"), rs.getString("legajo"));

        alumno.setMateriasAprobadas(mapper.readValue(rs.getString("correlativas"), ArrayList.class));

        alumno.setMateriasAprobadas(mapper.readValue(mapper.writeValueAsString(rs.getString("correlativas")), ArrayList.class));
        conexion.cerrarConnection();

        return alumno;
    }
    

    @SuppressWarnings("DeadBranch")
        public static void agregarAlumnoBd() throws SQLException{

      

        System.out.println("Que nombre tiene el alumno?");

        String nombre = sc.next();
      
             System.out.println("Que legajo tiene el alumno? ");

        String legajo = sc.nextLine();
        
        while (!legajo.matches("[0-9]{5}")){
        
            System.out.println("INGRESE UN LEGAJO DE 5 DIGITOS NUMERICOS ! ");
            
            legajo = sc.nextLine();
        
        }
        

        ArrayList<String> materias = new ArrayList<>();

        System.out.println("Cuantas materias aprobadas tiene el alumno?");

        int numero = sc.nextInt();

        

        System.out.println("Ingrese las materias que tiene aprobadas");

        for (int i = 0; i < numero; i++) {

            materias.add(sc.next());

        }

        

        String jsonMaterias = new Gson().toJson(materias);

        

        Alumno alumno = new Alumno(nombre, legajo);

        alumno.setMateriasAprobadas(materias);

        


        

        conexion.estableceConexion();

        Statement stmt = conexion.conectar.createStatement();

        stmt.executeUpdate("INSERT INTO alumnos VALUES(\""+nombre+"\","+ legajo +",'"+ jsonMaterias +"');");

        conexion.cerrarConnection();

    }
     
        
    
    
    
    
    
    public static void crearMateria() throws SQLException{
    
        Materia materia = new Materia();

        ////---CREACION DE MATERIAS---

        System.out.println("Que nombre quiere que tenga la materia?");
        String nombre = sc.next();
        materia.setNombre(nombre);

        System.out.println("Cuantas correlativas tiene?");

        int numero = sc.nextInt();

        System.out.println("Que materias desea agregar a las correlativas?");
        ArrayList<String> correlativas = new ArrayList<>();

        String input;

        for (int i = 0; i < numero; i++) {
            input = sc.next();
            correlativas.add(input);
        }
        
        String correlativasJson = new Gson().toJson(correlativas);

        conexion.estableceConexion();
        Statement stmt = conexion.conectar.createStatement();
        stmt.executeUpdate("INSERT INTO materias_final VALUES(\"" + nombre + "\",'" + correlativasJson + "');");
        conexion.cerrarConnection();
        
    }

    //Creo el hashmap de las materias para poder trabajar dentro del codigo
    public static void traerDatos() throws SQLException, JsonProcessingException {

        Materia materia = new Materia();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        
        HashMap<String, ArrayList<String>> hmMaterias = new HashMap<>();

        conexion.estableceConexion();
        Statement stmt = conexion.conectar.createStatement();

        ResultSet rs = stmt.executeQuery("SELECT * FROM materias_final");

        while (rs.next()) {

            materia = new Materia(rs.getString("nombre"));

            String jsonText = objectMapper.writeValueAsString(rs.getString("correlativas"));

            ArrayList<String> nombreCorrelativas = objectMapper.readValue(jsonText, ArrayList.class);

            materia.setCorrelativas(nombreCorrelativas);

            hmMaterias.put(materia.getNombre(), materia.getCorrelativas());

        }
        conexion.cerrarConnection();
        
        System.out.println(hmMaterias);

    }
}
