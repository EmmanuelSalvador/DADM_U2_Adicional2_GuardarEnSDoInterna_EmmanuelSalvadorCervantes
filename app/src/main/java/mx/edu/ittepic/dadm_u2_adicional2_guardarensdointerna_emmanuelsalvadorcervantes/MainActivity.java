package mx.edu.ittepic.dadm_u2_adicional2_guardarensdointerna_emmanuelsalvadorcervantes;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    LinearLayout layin;
    Switch sdOinterna;
    EditText edNombre, edCelular, edArchivo;
    Spinner spCiudad;
    RadioButton rbFemenino, rbMasculino;
    RadioGroup genero;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layin=findViewById(R.id.layo);
        TextView txtNombre = new TextView(this);
        txtNombre.setText("Nombre");
        edNombre = new EditText(this);
        edNombre.setEms(15);
        LinearLayout nombre= new LinearLayout(this);
        nombre.setGravity(1);
        nombre.addView(txtNombre);
        nombre.addView(edNombre);
        layin.addView(nombre);

        TextView txtCelular = new TextView(this);
        txtCelular.setText("Celular");
        edCelular = new EditText(this);
        edCelular.setEms(15);
        edCelular.setInputType(InputType.TYPE_CLASS_PHONE);
        LinearLayout celular= new LinearLayout(this);
        celular.setGravity(1);
        celular.addView(txtCelular);
        celular.addView(edCelular);
        layin.addView(celular);

        TextView txtCiudad= new TextView(this);
        txtCiudad.setText("Ciudad");
        spCiudad = new Spinner(this);
        String[] listaSpinner = new String[]{"Guadalajara", "Tepic", "Monterrey", "CDMX","Querétaro"};
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, listaSpinner);
        spCiudad.setAdapter(adaptador);
        LinearLayout ciudad= new LinearLayout(this);
        ciudad.setGravity(1);
        ciudad.addView(txtCiudad);
        ciudad.addView(spCiudad);
        layin.addView(ciudad);

        TextView txtGenero = new TextView(this);
        txtGenero.setText("Genero");
        txtGenero.setWidth(180);
        rbMasculino= new RadioButton(this);
        rbMasculino.setText("Masculino");
        rbFemenino = new RadioButton(this);
        rbFemenino.setText("Femenino");
        genero = new RadioGroup(this);
        genero.addView(rbMasculino);
        genero.addView(rbFemenino);
        LinearLayout liGenero = new LinearLayout(this);
        liGenero.addView(genero);
        LinearLayout grupo = new LinearLayout(this);
        grupo.addView(txtGenero);
        grupo.addView(liGenero);
        grupo.setGravity(1);
        layin.addView(grupo);

        Button btGuardar = new Button(this);
        btGuardar.setText("Guardar");
        btGuardar.setEms(6);
        Button btLeer = new Button(this);
        btLeer.setText("Leer");
        btGuardar.setEms(6);
        LinearLayout botones = new LinearLayout(this);
        botones.addView(btGuardar);
        botones.addView(btLeer);
        botones.setGravity(1);
        layin.addView(botones);

        sdOinterna = new Switch(this);
        sdOinterna.setTextOff("On");
        sdOinterna.setTextOff("Off");
        sdOinterna.setText("Guardar en SD");
        layin.addView(sdOinterna);

        TextView txtArchivo = new TextView(this);
        txtArchivo.setText("Archivo");
        edArchivo = new EditText(this);
        edArchivo.setEms(15);
        LinearLayout archivo= new LinearLayout(this);
        archivo.addView(txtArchivo);
        archivo.addView(edArchivo);
        archivo.setGravity(1);
        layin.addView(archivo);

        btGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decidirGuardar();
            }
        });
        btLeer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                decidirLeer();
            }
        });
    }
    private void decidirGuardar(){
        String nombre = edNombre.getText().toString();
        String celular = edCelular.getText().toString();
        String archivo = edArchivo.getText().toString();
        if (nombre.compareTo("") == 0 || celular.compareTo("") == 0 || archivo.compareTo("") == 0) {
            Toast.makeText(this,"Tienes que llenar todos los campos", Toast.LENGTH_LONG).show();
        }
        else {
            if (rbMasculino.isChecked() || rbFemenino.isChecked()){
                if (sdOinterna.isChecked()) {
                    guardarEnSD();
                } else {
                    guardarEnMemInterna();
                }
            }
            else {
                Toast.makeText(this,"Tienes que llenar todos los campos", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void guardarEnMemInterna() {
        try{
            String nombreArchivo = edArchivo.getText().toString();
            OutputStreamWriter archivo = new OutputStreamWriter(openFileOutput(nombreArchivo+".txt",MODE_PRIVATE));
            String data;
            if (rbMasculino.isChecked()) {
                data = edNombre.getText().toString() + "," + edCelular.getText().toString() + ","+spCiudad.getSelectedItem()+"," +"Masculino";
            }
            else {
                data = edNombre.getText().toString() + "," + edCelular.getText().toString() + ","+spCiudad.getSelectedItem()+", Femenino";
            }
            archivo.write(data);
            archivo.close();
            Toast.makeText(this,"Éxito, se guardó el dato en la memoria interna",Toast.LENGTH_LONG).show();
            limpiar();
        }catch (Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    private void guardarEnSD() {
        try{
            if (!validarSD()){
                Toast.makeText(this,"No tienes memoria SD insertada, o está en modo solo lectura",Toast.LENGTH_LONG).show();
                return;
            }
            File rutaSD = Environment.getExternalStorageDirectory();
            String nombreArchivo = edArchivo.getText().toString();
            File datosArchivo = new File(rutaSD.getAbsolutePath(),nombreArchivo+".txt");
            OutputStreamWriter archivo = new OutputStreamWriter(new FileOutputStream(datosArchivo));
            String data;
            if (rbMasculino.isChecked()) {
                data = edNombre.getText().toString() + "," + edCelular.getText().toString() + ","+spCiudad.getSelectedItem()+"," +"Masculino";
            }
            else {
                data = edNombre.getText().toString() + "," + edCelular.getText().toString() + ","+spCiudad.getSelectedItem()+", Femenino";
            }
            archivo.write(data);
            archivo.close();
            Toast.makeText(this,"Éxito, se guardó el dato en la memoria SD",Toast.LENGTH_LONG).show();
            limpiar();
        }catch (Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    private void limpiar() {
        edNombre.setText("");
        edCelular.setText("");
        spCiudad.setSelection(0);
        genero.clearCheck();
        edArchivo.setText("");
    }

    private void decidirLeer() {
        if (sdOinterna.isChecked()){
            leerDesdeSD();
        }
        else {
            leerDesdeMemInterna();
        }
    }

    private void leerDesdeSD() {
        try{
            if (!validarSD()){
                Toast.makeText(this,"NO hay memoria para leer",Toast.LENGTH_LONG).show();
                return;
            }
            File rutaSD = Environment.getExternalStorageDirectory();
            String nombreArchivo = edArchivo.getText().toString();
            File datosArchivo = new File(rutaSD.getAbsolutePath(),nombreArchivo+".txt");
            BufferedReader archivo = new BufferedReader(new InputStreamReader(new FileInputStream(datosArchivo)));
            String data = archivo. readLine();
            archivo.close();
            Toast.makeText(this,"El archivo "+nombreArchivo+" se cargó correctamente ",Toast.LENGTH_LONG).show();
            String[] vector = data.split(",");
            edNombre.setText(vector[0]);
            edCelular.setText(vector[1]);
            switch (vector[2]){
                case "Guadalajara":
                    spCiudad.setSelection(0);
                    break;
                case "Tepic":
                    spCiudad.setSelection(1);
                    break;
                case "Monterrey":
                    spCiudad.setSelection(2);
                    break;
                case "CDMX":
                    spCiudad.setSelection(3);
                    break;
                case "Querétaro":
                    spCiudad.setSelection(4);
                    break;
            }
            if (vector[3].compareTo("Masculino")==0){
                rbMasculino.setChecked(true);
                rbFemenino.setChecked(false);
            }
            else {
                rbMasculino.setChecked(false);
                rbFemenino.setChecked(true);
            }
        }catch (Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    private void leerDesdeMemInterna() {
        try{
            String nombreArchivo = edArchivo.getText().toString();
            BufferedReader archivo = new BufferedReader(new InputStreamReader(openFileInput(nombreArchivo+".txt")));
            String data = archivo. readLine();
            archivo.close();
            Toast.makeText(this,"El archivo "+nombreArchivo+" ae cargó correctamente ",Toast.LENGTH_LONG).show();
            String[] vector = data.split(",");
            edNombre.setText(vector[0]);
            edCelular.setText(vector[1]);
            switch (vector[2]){
                case "Guadalajara":
                    spCiudad.setSelection(0);
                    break;
                case "Tepic":
                    spCiudad.setSelection(1);
                    break;
                case "Monterrey":
                    spCiudad.setSelection(2);
                    break;
                case "CDMX":
                    spCiudad.setSelection(3);
                    break;
                case "Querétaro":
                    spCiudad.setSelection(4);
                    break;
            }
            if (vector[3].compareTo("Masculino")==0){
                rbMasculino.setChecked(true);
                rbFemenino.setChecked(false);
            }
            else {
                rbMasculino.setChecked(false);
                rbFemenino.setChecked(true);
            }
        }catch (Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
    private boolean validarSD(){
        String resp = Environment.getExternalStorageState();
        if (resp.compareTo(Environment.MEDIA_MOUNTED)==0){
            return true;
        }
        return false;
    }
}
