package sv.edu.udb.retrofitcrudapp

import retrofit2.Call
import retrofit2.http.*

interface TareaApi {

    //aqui se hacen las peticiones de la api

    @GET("tareas")
    fun obtenerTareas(): Call<List<Tarea>>

    @GET("tareas/{id}")
    fun obtenerTareaPorId(@Path("id") id: String): Call<Tarea>

    @POST("tareas")
    fun crearTarea(@Body tarea: Tarea): Call<Tarea>

    @PUT("tareas/{id}")
    fun actualizarTarea(@Path("id") id: Int, @Body tarea: Tarea): Call<Tarea>

    @DELETE("tareas/{id}")
    fun eliminarTarea(@Path("id") id: Int): Call<Void>
}
