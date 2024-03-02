package org.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.demo.classes.Employee;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class HelloController {

    //List semua element/component yang ada di fxml-nya sesuai dengan fx:id dan jenis element/component-nya
    @FXML
    private TextField inpMin;
//          |          |
//          |          v
//          v       nama object-nya sesuai dengan fx:id yang ada di fxml
//      jenis element sesuai dengan yang digunakan di fxml
    @FXML
    private TextField inpMax;

    @FXML
    private Button btnGenerate;

    @FXML
    private TableView<Employee> tableEmployee;

    @FXML
    private TableColumn<Employee,String> colName;

    @FXML
    private TableColumn<Employee,Integer> colSalary;


    @FXML
    public void initialize(){       //fungsi yang pertama kali dipanggil ketika fxml di-load
        // binding column tabel dengan class yang menyimpan data
        colName.setCellValueFactory(new PropertyValueFactory<Employee,String>("name"));
        colSalary.setCellValueFactory(new PropertyValueFactory<Employee,Integer>("salary"));
//        |                                                      |         |           |
//        |                                                      |         |           V
//        |                                                      |         V     nama attribut dari class Employee (harus persis sama)
//        |                                                      |    tipe data dari attribut yang akan di bind
//        |                                                      V
//        V                                               nama class yang menyimpan data
//    object column fxml yang akan di bind

        setTableEmployee();     //funsgi untuk inisialisasi awal isi tabel ketika di load
    }

    public void setTableEmployee() {
        String sql = "SELECT * FROM employees";         //query untuk mendapatkan data employees
        ObservableList<Employee> employees = FXCollections.observableArrayList();           //List untuk menyimpan semua data employees yang akan ditampilkan di table
        try {
            Connection db = DatabaseConnection.getConnection();
            Statement statement = db.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {  //loop semua hasil query per row
                //setiap row

                String name = resultSet.getString("first_name");
                Integer salary = resultSet.getInt("salary");
//                                          |       |
//                                          |       V
//                                          V     nama kolom yang mau diambil dari tabel database
//                                      tipe data kolom yang mau diambil (sesuai tipe data kolom tabel)

                Employee employee = new Employee(name, salary);         //simpan data ke dalam object employee
                employees.add(employee);        //tambahkan ke dalam List employees yang sudah dibuat di atas
            }

            tableEmployee.setItems(employees);  //masukan List employees yang sudah disimpan ke tabel FXML
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML   //method yang di atasnya ada @FXML artinya methodnya bisa di bind/dihubungkan dengan action di fxml (click, hover, keyup, etc)
    public void clickGenerate(){
//                     ternary if
        Double min = inpMin.getText().isEmpty() ? Double.NEGATIVE_INFINITY : Integer.valueOf(inpMin.getText());         //batas min dari input
        Double max = inpMax.getText().isEmpty() ? Double.POSITIVE_INFINITY : Integer.valueOf(inpMax.getText());         //batas max dari input
//                      |                                   |                                       |
//                      |                                   |                                       V
//                      |                                   V                  jika diisi maka convert input min/max ke integer
//                      V            jika tidak diisi var min/max diisi dengan nilai tak terhingga
//          cek apakah input min/max diisi atau tidak supaya kalau tidak diisi tidak error ketika convert ke integer

        String sql = "SELECT * FROM employees";
        ObservableList<Employee> employees = FXCollections.observableArrayList();
        try {
            Connection db = DatabaseConnection.getConnection();
            Statement statement = db.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String name = resultSet.getString("first_name");
                Integer salary = resultSet.getInt("salary");

                if (salary >= min && salary <= max){        //lakukan pengecekan supaya yang gajinya diluar range tidak ditambahkan ke list
                    Employee employee = new Employee(name, salary);
                    employees.add(employee);
                }

            }

            tableEmployee.setItems(employees);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}