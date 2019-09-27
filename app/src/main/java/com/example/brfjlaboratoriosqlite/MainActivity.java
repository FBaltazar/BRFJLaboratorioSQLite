package com.example.brfjlaboratoriosqlite;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{
    EditText noControl,nombre,numCelular,correo,carrera;
    Button btnInsertar,btnConsultar,btnActualizar,btnLista,btnLimpiar,btnEliminar;

    BaseDatos base;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    String [] clave = new  String[10];
    String [] nombresito = new  String[10];
    String [] numCel = new String[10];
    String [] correos = new String[10];
    String [] carreras = new String[10];

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noControl = findViewById(R.id.idEdTextNumControl);
        nombre = findViewById(R.id.idEdTextNombre);
        numCelular = findViewById(R.id.idEdTextCelular);
        correo = findViewById(R.id.idEdTextCorreo);
        carrera = findViewById(R.id.idEdTextCarrera);

        btnInsertar = findViewById(R.id.idBtnInsertar);
        btnConsultar = findViewById(R.id.idBtnConsultar);
        btnActualizar = findViewById(R.id.idBtnActualizar);
        btnLista = findViewById(R.id.idBtnLista);
        btnLimpiar = findViewById(R.id.idBtnLimpiar);
        btnEliminar = findViewById(R.id.idBtnEliminar);

        base = new BaseDatos(this, "primera",null,1);

        btnLimpiar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                limpiarCampos();
            }
        });

        btnInsertar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                codigoInsertar();
            }
        });

        btnConsultar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pedirID(1);
            }
        });

        btnActualizar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (btnActualizar.getText().toString().startsWith("CONFIRMAR ACTUALIZACION"))
                {
                    invocaConfirmacionActualizacion();
                }
                else{
                    pedirID(2);
                }
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pedirID(3);
            }
        });

        btnLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarLista();
            }
        });



    }
///////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////

    private void mostrarLista()
    {
        base=new BaseDatos(this, "primera",null,1);
        try{
            SQLiteDatabase tabla= base.getReadableDatabase();
            String SQL ="SELECT * FROM REGISTROALUMNOS";

            Cursor resultado =tabla.rawQuery(SQL, null);
            if(resultado.moveToFirst()) {
                int i=0;
                while(!resultado.isAfterLast()){
                    clave[i]=resultado.getString(0);
                    nombresito[i]=resultado.getString(1);
                    numCel[i]=resultado.getString(2);
                    correos[i]=resultado.getString(3);
                    carreras[i]=resultado.getString(4);
                    i++;
                    resultado.moveToNext();
                }
            }
            tabla.close();
        }catch (SQLiteException e){
            Toast.makeText(this, "NO SE PUDO REALIZAR"+e.toString(), Toast.LENGTH_LONG).show();
        }

        recyclerView = findViewById(R.id.recyclerId);

        adapter = new RecyclerAdapter(clave,nombresito,numCel,correos,carreras,this);

        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(adapter);
    }


    private void limpiarCampos()
    {
        noControl.setText("");
        nombre.setText("");
        numCelular.setText("");
        correo.setText("");
        carrera.setText("");

        btnInsertar.setEnabled(true);
        btnConsultar.setEnabled(true);
        btnEliminar.setEnabled(true);
        btnActualizar.setText("ACTUALIZAR");

        noControl.setEnabled(true);

    }

    private void codigoInsertar()
    {
        try
        {
            SQLiteDatabase tabla = base.getReadableDatabase();

            String SQL = "INSERT INTO REGISTROALUMNOS VALUES(1,'%2','%3','%4','%5')";
            SQL = SQL.replace("1", noControl.getText().toString());
            SQL = SQL.replace("%2", nombre.getText().toString());
            SQL = SQL.replace("%3", numCelular.getText().toString());
            SQL = SQL.replace("%4", correo.getText().toString());
            SQL = SQL.replace("%5",carrera.getText().toString());
            tabla.execSQL(SQL);

            Toast.makeText(this,"Transacción Exitosa",Toast.LENGTH_LONG).show();
            tabla.close();
            limpiarCampos();
        }
        catch (SQLiteException e)
        {
            Toast.makeText(this,"Fallo la Transacción",Toast.LENGTH_LONG).show();
        }

    }

    private void pedirID(final int origen)
    {
        final EditText pidoID = new EditText(this);
        pidoID.setInputType(InputType.TYPE_CLASS_NUMBER);
        pidoID.setHint("Valor entero > 0");
        String mensaje = "Escriba el ID a Buscar";

        AlertDialog.Builder alerta = new AlertDialog.Builder(this);

        if (origen == 2) mensaje = "Escriba el ID a Modificar";
        if (origen == 3) mensaje = "Escriba ID a Eliminar";

        alerta.setTitle("Atencion").setMessage(mensaje)
                .setView(pidoID)
                .setPositiveButton("Buscar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if (pidoID.getText().toString().isEmpty())
                        {
                            Toast.makeText(MainActivity.this,"Introduce solo numeros",Toast.LENGTH_LONG).show();
                            return;
                        }
                        buscarDato(pidoID.getText().toString(), origen);
                        dialog.dismiss();
                    }
                }).setNegativeButton("Cancelar",null).show();
    }
    private void buscarDato(String idaBuscar, int origen)
    {
        try
        {
            SQLiteDatabase tabla = base.getReadableDatabase();

            String SQL = "SELECT *FROM REGISTROALUMNOS WHERE ID="+idaBuscar;

            Cursor resultado = tabla.rawQuery(SQL,null);
            if(resultado.moveToFirst())
            {
                if(origen==3)
                {
                    String dato = idaBuscar+"&"+ resultado.getString(1)+"&"+resultado.getString(2)+
                            "&"+resultado.getString(3)+"&"+resultado.getString(4);
                    invocaConfirmacionEliminacion(dato);
                    return;
                }

                noControl.setText(resultado.getString(0));
                nombre.setText(resultado.getString(1));
                numCelular.setText(resultado.getString(2));
                correo.setText(resultado.getString(3));
                carrera.setText(resultado.getString(4));
                if(origen==2)
                {
                    btnInsertar.setEnabled(false);
                    btnConsultar.setEnabled(false);
                    btnEliminar.setEnabled(false);
                    btnActualizar.setText("CONFIRMAR ACTUALIZACION");
                    noControl.setEnabled(false);
                }
            }else {
                Toast.makeText(this,"No se ENCONTRO EL RESULTADO",Toast.LENGTH_LONG).show();
            }
            tabla.close();
        }
        catch (SQLiteException e)
        {
            Toast.makeText(this,"No se Logro la Busqueda",Toast.LENGTH_LONG).show();
        }
    }

    private void invocaConfirmacionActualizacion()
    {
        AlertDialog.Builder confir = new AlertDialog.Builder(this);
        confir.setTitle("IMPORTNATE").setMessage("¿Seguro de Aplicar Cambios?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        aplicarActualizar();
                        dialog.dismiss();
                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                limpiarCampos();
                dialog.cancel();
            }
        }).show();
    }
    private void aplicarActualizar(){
        try
        {
            SQLiteDatabase tabla = base.getWritableDatabase();

            String SQL= "UPDATE REGISTROALUMNOS SET NOMBRE='"+
                    nombre.getText().toString()+"', " +
                    "CELULAR='"+numCelular.getText().toString()+"',"+
                    "CORREO='"+correo.getText().toString()+"',"+
                    "CARRERA='"+carrera.getText().toString()+
                    "' WHERE ID="+noControl.getText().toString();
            tabla.execSQL(SQL);
            tabla.close();
            Toast.makeText(this,"Actualizado",Toast.LENGTH_LONG).show();

        }catch (SQLiteException e){
            Toast.makeText(this,"No se Actualizo",Toast.LENGTH_LONG).show();
        }
        limpiarCampos();
    }

    private void invocaConfirmacionEliminacion(String dato)
    {
        String datos[] = dato.split("&");
        final String id = datos[0];
        String nombre = datos[1];

        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        alerta.setTitle("Atención").setMessage("Deseas Eliminar al Usuario: "+nombre)
                .setPositiveButton("Si", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        eliminarIDTodo(id);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancelar",null).show();
    }
    private void eliminarIDTodo(String idEliminar)
    {
        try
        {
            SQLiteDatabase tabla = base.getReadableDatabase();

            String SQL = "DELETE FROM REGISTROALUMNOS WHERE ID=" + idEliminar;
            tabla.execSQL(SQL);
            tabla.close();

            Toast.makeText(this, "Dato Eliminado", Toast.LENGTH_LONG).show();
        }
        catch (SQLiteException e)
        {
            Toast.makeText(this, "No se Elimino", Toast.LENGTH_LONG).show();
        }
    }
}
