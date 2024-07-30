package pm1.iiipac.openidconnectapplicacion

import android.app.AlertDialog
import android.content.Context
import android.widget.ArrayAdapter
import android.widget.ListView
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class CRUD(context: Context) {
    val context = context

    fun create(e:Person, token: String) {
        val client = Client(token)
        val service = client.getRetrofit()
        service.createPerson(e).enqueue(object : retrofit2.Callback<DataApiResponse> {
            override fun onFailure(call: Call<DataApiResponse>, t: Throwable) {
            }
            override fun onResponse(call: Call<DataApiResponse>, response: Response<DataApiResponse>) {
                if(response.isSuccessful){
                    if(response.body()?.status == 200){
                        AlertDialog.Builder(context)
                            .setTitle("Intento de Creación")
                            .setMessage("Creación exitosa")
                            .setPositiveButton("Aceptar") { dialog, _ ->
                                dialog.dismiss()
                            }.show()
                    }else{
                        AlertDialog.Builder(context)
                            .setTitle("Intento de Creación")
                            .setMessage("Creación fallida")
                            .setPositiveButton("Aceptar") { dialog, _ ->
                                dialog.dismiss()
                            }.show()
                    }
                }
            }
        })
    }

    fun read(list: ListView, token: String) {
        val client = Client(token)
        val service = client.getRetrofit()
        service.getPerson().enqueue(object : retrofit2.Callback<DataApiResponse> {
            override fun onFailure(call: Call<DataApiResponse>, t: Throwable) {
            }
            override fun onResponse(call: Call<DataApiResponse>, response: Response<DataApiResponse>) {
                if(response.isSuccessful){
                    if(response.body()?.status == 200){
                        list.adapter = null
                        val gson = Gson()
                        val json = gson.toJson(response.body()?.data)
                        val lista = gson.fromJson(json, Array<Person>::class.java)
                        val listFilter: List<String> = lista.map {"${it.id} - ${it.nombres} - ${it.apellido} \n${it.telefono}"}
                        val adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, listFilter)
                        list.adapter = adapter
                    }else{
                        AlertDialog.Builder(context)
                            .setTitle("Intento de Lectura")
                            .setMessage("Lectura fallida")
                            .setPositiveButton("Aceptar") { dialog, _ ->
                                dialog.dismiss()
                            }.show()
                        list.adapter = null
                    }
                }
            }
        })

    }

    fun update(id: Int, e: Person, token: String) {
        val client = Client(token)
        val service = client.getRetrofit()
        service.updatePerson(id, e).enqueue(object : retrofit2.Callback<DataApiResponse> {
            override fun onFailure(call: Call<DataApiResponse>, t: Throwable) {
            }
            override fun onResponse(call: Call<DataApiResponse>, response: Response<DataApiResponse>) {
                if(response.isSuccessful){
                    if(response.body()?.status == 200){
                        AlertDialog.Builder(context)
                            .setTitle("Intento de Actualización")
                            .setMessage("Actualización exitosa")
                            .setPositiveButton("Aceptar") { dialog, _ ->
                                dialog.dismiss()
                            }.show()
                    }else{
                        AlertDialog.Builder(context)
                            .setTitle("Intento de Actualización")
                            .setMessage("Actualización fallida")
                            .setPositiveButton("Aceptar") { dialog, _ ->
                                dialog.dismiss()
                            }.show()
                    }
                }
            }
        })
    }

    fun getOne(id: Int, token: String){
        val view = context as FormView
        val client = Client(token)
        val service = client.getRetrofit()
        service.getPersonById(id).enqueue(object : retrofit2.Callback<DataApiResponseSingle> {
            override fun onFailure(call: Call<DataApiResponseSingle>, t: Throwable) {
            }
            override fun onResponse(call: Call<DataApiResponseSingle>, response: Response<DataApiResponseSingle>) {
                if(response.isSuccessful){
                    if(response.body()?.status == 200){
                        val gson = Gson()
                        val json = gson.toJson(response.body()?.data)
                        val person = gson.fromJson(json, Person::class.java)
                        view.setData(person)
                    }else{
                        AlertDialog.Builder(context)
                            .setTitle("Intento de Lectura")
                            .setMessage("Lectura fallida")
                            .setPositiveButton("Aceptar") { dialog, _ ->
                                dialog.dismiss()
                            }.show()
                    }
                }
            }
        })
    }

    fun delete(id: Int, token: String) {
        val client = Client(token)
        val service = client.getRetrofit()
        service.deletePerson(id).enqueue(object : retrofit2.Callback<DataApiResponse> {
            override fun onFailure(call: Call<DataApiResponse>, t: Throwable) {
            }
            override fun onResponse(call: Call<DataApiResponse>, response: Response<DataApiResponse>) {
                if(response.isSuccessful){
                    if(response.body()?.status == 200){
                        AlertDialog.Builder(context)
                            .setTitle("Intento de Eliminación")
                            .setMessage("Eliminación exitosa")
                            .setPositiveButton("Aceptar") { dialog, _ ->
                                dialog.dismiss()
                            }.show()
                    }else{
                        AlertDialog.Builder(context)
                            .setTitle("Intento de Eliminación")
                            .setMessage("Eliminación fallida")
                            .setPositiveButton("Aceptar") { dialog, _ ->
                                dialog.dismiss()
                            }.show()
                    }
                }
            }
        })
    }
}

class Client(tok: String) {
    private var retrofit: Retrofit
    init {
        val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .addHeader("Authorization", if(!tok.isEmpty()) tok else "")
                    .build()
                chain.proceed(request)
            }
            .connectTimeout(30, TimeUnit.SECONDS)
            .callTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        val Retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit = Retrofit
    }

    fun getRetrofit(): Services {
        return retrofit.create(Services::class.java)
    }
}