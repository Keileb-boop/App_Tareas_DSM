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

class ActualizarTareaActivity : AppCompatActivity() {

    private lateinit var tituloEditText: EditText
    private lateinit var descripcionEditText: EditText
    private lateinit var estadoEditText: EditText
    private lateinit var actualizarButton: Button
    private lateinit var api: TareaApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_tarea)

        tituloEditText = findViewById(R.id.editTextTitulo)
        descripcionEditText = findViewById(R.id.editTextDescripcion)
        estadoEditText = findViewById(R.id.editTextEstado)
        actualizarButton = findViewById(R.id.btnActualizar)

        val tareaId = intent.getIntExtra("tarea_id", -1)
        tituloEditText.setText(intent.getStringExtra("titulo"))
        descripcionEditText.setText(intent.getStringExtra("descripcion"))
        estadoEditText.setText(intent.getStringExtra("estado"))

        val retrofit = Retrofit.Builder()
            .baseUrl("https://690b78486ad3beba00f51b9b.mockapi.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(TareaApi::class.java)

        actualizarButton.setOnClickListener {
            val tareaActualizada = Tarea(
                tareaId,
                tituloEditText.text.toString(),
                descripcionEditText.text.toString(),
                estadoEditText.text.toString()
            )

            api.actualizarTarea(tareaId, tareaActualizada).enqueue(object : Callback<Tarea> {
                override fun onResponse(call: Call<Tarea>, response: Response<Tarea>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@ActualizarTareaActivity, "Tarea actualizada correctamente", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(baseContext, MainActivity::class.java))
                    } else {
                        Log.e("API", "Error al actualizar: ${response.errorBody()?.string()}")
                        Toast.makeText(this@ActualizarTareaActivity, "Error al actualizar tarea", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Tarea>, t: Throwable) {
                    Toast.makeText(this@ActualizarTareaActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
