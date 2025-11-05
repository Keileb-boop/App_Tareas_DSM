package sv.edu.udb.retrofitcrudapp

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TareaAdapter
    private lateinit var api: TareaApi
    private var tareaSeleccionada: Tarea? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fabAgregar: FloatingActionButton = findViewById(R.id.fab_agregar)
        val fabEditar: FloatingActionButton = findViewById(R.id.fab_editar)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://690b78486ad3beba00f51b9b.mockapi.io/tareasv1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(TareaApi::class.java)
        cargarDatos(api)

        fabAgregar.setOnClickListener {
            startActivity(Intent(this, CrearTareaActivity::class.java))
        }

        fabEditar.setOnClickListener {
            if (tareaSeleccionada != null) {
                val tarea = tareaSeleccionada!!
                val i = Intent(this, ActualizarTareaActivity::class.java)
                i.putExtra("tarea_id", tarea.id)
                i.putExtra("titulo", tarea.titulo)
                i.putExtra("descripcion", tarea.descripcion)
                i.putExtra("estado", tarea.estado)
                startActivity(i)
            } else {
                Toast.makeText(this, "Selecciona una tarea primero", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        cargarDatos(api)
    }

    private fun cargarDatos(api: TareaApi) {
        api.obtenerTareas().enqueue(object : Callback<List<Tarea>> {
            override fun onResponse(call: Call<List<Tarea>>, response: Response<List<Tarea>>) {
                if (response.isSuccessful) {
                    val tareas = response.body() ?: emptyList()
                    val tareasOrdenadas = tareas.sortedByDescending { it.id }
                    adapter = TareaAdapter(tareasOrdenadas)
                    recyclerView.adapter = adapter

                    adapter.setOnItemClickListener(object : TareaAdapter.OnItemClickListener {
                        override fun onItemClick(tarea: Tarea) {
                            tareaSeleccionada = tarea
                            adapter.setTareaSeleccionada(tarea)
                        }

                        override fun onItemLongClick(tarea: Tarea) {
                            val opciones = arrayOf("Modificar Tarea", "Eliminar Tarea")
                            AlertDialog.Builder(this@MainActivity)
                                .setTitle(tarea.titulo)
                                .setItems(opciones) { _, index ->
                                    when (index) {
                                        0 -> Modificar(tarea)
                                        1 -> eliminarTarea(tarea)
                                    }
                                }
                                .setNegativeButton("Cancelar", null)
                                .show()
                        }
                    })
                } else {
                    Log.e("API", "Error al obtener tareas: ${response.errorBody()?.string()}")
                    Toast.makeText(this@MainActivity, "Error al obtener las tareas", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Tarea>>, t: Throwable) {
                Log.e("API", "Error: ${t.message}")
                Toast.makeText(this@MainActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun Modificar(tarea: Tarea) {
        val i = Intent(this, ActualizarTareaActivity::class.java)
        i.putExtra("tarea_id", tarea.id)
        i.putExtra("titulo", tarea.titulo)
        i.putExtra("descripcion", tarea.descripcion)
        i.putExtra("estado", tarea.estado)
        startActivity(i)
    }

    private fun eliminarTarea(tarea: Tarea) {
        api.eliminarTarea(tarea.id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@MainActivity, "Tarea eliminada", Toast.LENGTH_SHORT).show()
                    cargarDatos(api)
                } else {
                    Toast.makeText(this@MainActivity, "Error al eliminar tarea", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
