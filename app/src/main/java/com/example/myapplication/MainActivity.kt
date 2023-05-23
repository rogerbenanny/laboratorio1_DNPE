package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.ui.theme.MyApplicationTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    //Greeting("Android")
                    MyApp()
                }
            }
        }
    }
}

data class Persona(val nombre: String, val fecha: String, val tipoSangre: String, val telefono: String, val correo: String, val monto: Double)

@Composable
fun FormScreen(onSubmit: (Persona) -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var tipoSangre by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var monto by remember { mutableStateOf(0.0) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Nombre:")
        TextField(value = nombre, onValueChange = { nombre = it })

        Text("Fecha:")
        TextField(value = fecha, onValueChange = { fecha = it })

        Text("Tipo de Sangre:")
        TextField(value = tipoSangre, onValueChange = { tipoSangre = it })

        Text("Teléfono:")
        TextField(value = telefono, onValueChange = { telefono = it })

        Text("Correo:")
        TextField(value = correo, onValueChange = { correo = it })

        Text("Monto:")
        TextField(value = monto.toString(), onValueChange = { monto = it.toDoubleOrNull() ?: 0.0 })

        Button(
            onClick = {
                val persona = Persona(nombre, fecha, tipoSangre, telefono, correo, monto)
                onSubmit(persona)
            }
        ) {
            Text("Guardar")
        }
    }
}
@Composable
fun ListScreen(personas: List<Persona>, onBackClick: () -> Unit, onPersonaDelete: (Persona) -> Unit, onPersonaEdit: (Persona) -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        personas.forEach { persona ->
            Text("Nombre: ${persona.nombre}")
            Text("Fecha: ${persona.fecha}")
            Text("Tipo de Sangre: ${persona.tipoSangre}")
            Text("Teléfono: ${persona.telefono}")
            Text("Correo: ${persona.correo}")
            Text("Monto: ${persona.monto}")

            Button(
                onClick = {
                    onPersonaDelete(persona)
                }
            ) {
                Text("Eliminar")
            }

            Button(
                onClick = {
                    onPersonaEdit(persona)
                }
            ) {
                Text("Modificar")
            }
        }

        val callback = remember { mutableStateOf(onBackClick) }
        Button(
            onClick = {
                callback.value()
            }
        ) {
            Text("Regresar")
        }
    }
}

@Composable
fun MyApp() {
    var personas by remember { mutableStateOf(emptyList<Persona>()) }
    var showListScreen by remember { mutableStateOf(false) }
    var editPersona by remember { mutableStateOf<Persona?>(null) }

    if (!showListScreen) {
        FormScreen { persona ->
            personas = personas + persona
            showListScreen = true
        }
    } else {
        if (editPersona == null) {
            ListScreen(
                personas = personas,
                onBackClick = { showListScreen = false },
                onPersonaDelete = { persona ->
                    personas = personas - persona
                },
                onPersonaEdit = { persona ->
                    editPersona = persona
                }
            )
        } else {
            val editedPersona = editPersona!!
            FormScreen { persona ->
                personas = personas.map { if (it == editedPersona) persona else it }
                editPersona = null
            }
        }
    }
}

