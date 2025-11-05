package sv.edu.udb.retrofitcrudapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CrearTareaActivity : AppCompatActivity() {

    private lateinit var tituloEditText: EditText
    private lateinit var descripcionEditText: EditText
    private lateinit var estadoEditText: EditText
    private lateinit var crearButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_tarea)

        tituloEditText = findViewById(R.id.editTextTitulo)
        descripcionEditText = findViewById(R.id.editTextDescripcion)
        estadoEditText = findViewById(R.id.editTextEstado)
        crearButton = findViewById(R.id.btnGuardar)

        crearButton.setOnClickListener {
            val titulo = tituloEditText.text.toString()
            val descripcion = descripcionEditText.text.toString()
            val estado = estadoEditText.text.toString()

            val tarea = Tarea(0, titulo, descripcion, estado)

            val retrofit = Retrofit.Builder()
                .baseUrl("https://690b78486ad3beba00f51b9b.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val api = retrofit.create(TareaApi::class.java)

            api.crearTarea(tarea).enqueue(object : Callback<Tarea> {
                override fun onResponse(call: Call<Tarea>, response: Response<Tarea>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@CrearTareaActivity, "Tarea creada exitosamente", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(baseContext, MainActivity::class.java))
                    } else {
                        Log.e("API", "Error: ${response.errorBody()?.string()}")
                        Toast.makeText(this@CrearTareaActivity, "Error al crear tarea", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Tarea>, t: Throwable) {
                    Toast.makeText(this@CrearTareaActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
