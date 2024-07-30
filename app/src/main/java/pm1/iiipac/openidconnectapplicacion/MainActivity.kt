package pm1.iiipac.openidconnectapplicacion

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        findViewById<Button>(R.id.btnLogin).setOnClickListener {
            val user = findViewById<EditText>(R.id.username).text.toString()
            val pass = findViewById<EditText>(R.id.passwd).text.toString()
            prepareData(user, pass)

        }
    }

    private fun prepareData(user: String, pass: String){
        if(user.isEmpty() || pass.isEmpty()){
            AlertDialog.Builder(this)
                .setTitle("Autenticación")
                .setMessage("Debe ingresar usuario y contraseña")
                .setPositiveButton("Aceptar") { dialog, _ ->
                    dialog.dismiss()
                }.show()
            return
        }
        val json : JSONObject = JSONObject()
        json.put("usuario", user)
        json.put("passwd", pass)
        sendRequest(json)
    }

    private fun sendRequest(e: JSONObject){
        val url = "http://10.0.2.2/API_PHP_MOVIL1/controllers/user/ctrlUser.php"
        val queue: RequestQueue = Volley.newRequestQueue(this)
        val request = JsonObjectRequest(Request.Method.POST, url, e,
            { response ->
                try {
                    val respuesta = response.getInt("statusCode")
                    if (respuesta == 200){
                        val status = response.getBoolean("status")
                        val token = response.getString("token")
                        AlertDialog.Builder(this)
                            .setTitle("Autenticación")
                            .setMessage(if(status)"Autenticación exitosa" else "No pudo autenticarse")
                            .setPositiveButton("Aceptar") { dialog, _ ->
                                if (status) {
                                    dialog.dismiss()
                                    val sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                                    val editor = sharedPreferences.edit()
                                    editor.putString("accessToken", token)
                                    editor.apply()
                                    val intent = Intent(this, ListActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    dialog.dismiss()
                                }
                            }
                            .show()
                    }else{
                        makeNegativeDialog()
                    }
                } catch (e: JSONException) {
                    makeNegativeDialog()
                }
            },
            { error ->
                makeNegativeDialog()
            }
        )
        queue.add(request)
    }

    private fun makeNegativeDialog(){
        AlertDialog.Builder(this)
            .setTitle("Autenticación")
            .setMessage("Error en la autenticación")
            .setPositiveButton("Aceptar") { dialog, _ ->
                dialog.dismiss()
            }
            .show()

    }
}