package pm1.iiipac.openidconnectapplicacion

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface Services {
    @GET("API_PHP_MOVIL1/controllers/person/ctrlPerson.php")
    fun getPerson(): Call<DataApiResponse>

    @GET("API_PHP_MOVIL1/controllers/person/ctrlPerson.php/{id}")
    fun getPersonById(@Path("id") id: Int): Call<DataApiResponseSingle>

    @POST("API_PHP_MOVIL1/controllers/person/ctrlPerson.php")
    fun createPerson(@Body person: Person): Call<DataApiResponse>

    @PUT("API_PHP_MOVIL1/controllers/person/ctrlPerson.php/{id}")
    fun updatePerson(@Path("id") id: Int, @Body person: Person): Call<DataApiResponse>

    @DELETE("API_PHP_MOVIL1/controllers/person/ctrlPerson.php/{id}")
    fun deletePerson(@Path("id") id: Int): Call<DataApiResponse>
}