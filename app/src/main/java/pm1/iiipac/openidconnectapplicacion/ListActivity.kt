package pm1.iiipac.openidconnectapplicacion

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ListActivity : AppCompatActivity() {
    private lateinit var crud: CRUD
    private lateinit var shared : SharedPreferences
    private var id: Int = 0
    private var position: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        shared = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
        val token = shared.getString("accessToken", "")
        Log.i("token", token ?: "Token no encontrado")
        crud = CRUD(this)
        crud.read(findViewById<ListView>(R.id.listView), "$token")
        findViewById<Button>(R.id.btnLogout).setOnClickListener {
            logout()
        }
        findViewById<ListView>(R.id.listView).setOnItemClickListener { parent, view, position, id ->
            val item = parent.getItemAtPosition(position) as String
            this@ListActivity.id = item.split(" ")[0].toInt()
        }
        findViewById<Button>(R.id.btnCreate).setOnClickListener {
            val intent = Intent(this, FormActivity::class.java)
            intent.putExtra("id", 0)
            startActivity(intent)
        }
        findViewById<Button>(R.id.btnUpdate).setOnClickListener {
            if(id != 0){
                val intent = Intent(this, FormActivity::class.java)
                intent.putExtra("id", id)
                startActivity(intent)
            }else{
                AlertDialog.Builder(this)
                    .setTitle("Actualizar")
                    .setMessage("Debe seleccionar un registro")
                    .setPositiveButton("Aceptar") { dialog, _ ->
                        dialog.dismiss()
                    }.show()
            }
        }
        findViewById<Button>(R.id.btnDelete).setOnClickListener {
            if(id != 0){
                val token = shared.getString("accessToken", "")
                crud.delete(id, "$token")
                crud.read(findViewById<ListView>(R.id.listView), "$token")
                id = 0
            }else{
                AlertDialog.Builder(this)
                    .setTitle("Eliminar")
                    .setMessage("Debe seleccionar un registro")
                    .setPositiveButton("Aceptar") { dialog, _ ->
                        dialog.dismiss()
                    }.show()
            }
        }
    }

    private fun logout() {
        val token = shared.getString("accessToken", "")
        val editor = shared.edit()
        editor.remove("accessToken")
        editor.apply()
        crud.read(findViewById<ListView>(R.id.listView), "$token")
    }
}