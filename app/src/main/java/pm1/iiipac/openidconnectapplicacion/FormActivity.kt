package pm1.iiipac.openidconnectapplicacion

import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.ByteArrayOutputStream

class FormActivity : AppCompatActivity(), FormView {
    private val REQUEST_ACCESS_CAMERA = 100
    private val TAKE_PHOTO = 101
    private lateinit var imgCapture: ImageView
    private lateinit var txtName: EditText
    private lateinit var txtLastName: EditText
    private lateinit var txtDireccion: EditText
    private lateinit var txtTelefono: EditText
    private lateinit var btnSave: Button
    private var id = 0
    private lateinit var shared : SharedPreferences
    private lateinit var imgConvert: String

    private lateinit var crud: CRUD
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_form)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        id = intent.getIntExtra("id", 0)
        shared = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
        val token = shared.getString("accessToken", "")
        crud = CRUD(this)
        txtName = findViewById(R.id.txtName)
        txtLastName = findViewById(R.id.txtLastName)
        txtDireccion = findViewById(R.id.txtDireccion)
        txtTelefono = findViewById(R.id.txtTelefono)
        imgCapture = findViewById(R.id.imgCapture)
        btnSave = findViewById(R.id.btnSave)
        if(id != 0){
            crud.getOne(id, "$token")
        }
        btnSave.setOnClickListener {
            if(id == 0){
                crud.create(prepareData(), "$token")
            }else{
                crud.update(id,prepareData(), "$token")
                id = 0
            }
        }
        imgCapture.setOnClickListener {
            checkPermission()
        }
    }

    private fun prepareData(): Person{
        imgConvert = getBase64((if ((imgCapture.getDrawable() != null)) (imgCapture.getDrawable() as BitmapDrawable).bitmap else null)!!)
        return Person(
            0,
            txtName.text.toString(),
            txtLastName.text.toString(),
            txtTelefono.text.toString(),
            imgConvert,
            txtDireccion.text.toString()
        )
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.CAMERA),
                REQUEST_ACCESS_CAMERA
            )
            Toast.makeText(this, "Permiso concedido", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Permiso concedido", Toast.LENGTH_SHORT).show()
            takePhoto()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            REQUEST_ACCESS_CAMERA -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    takePhoto()
                }
            }
            else -> {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
                Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun takePhoto() {
        val photoActivity = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(photoActivity, TAKE_PHOTO)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TAKE_PHOTO && resultCode == RESULT_OK) {
            if (data != null) {
                val imagen = data.extras?.get("data") as Bitmap?
                imgConvert = imagen?.let { getBase64(it) }.toString()
                imgCapture.setImageBitmap(imagen)
            }
        }
    }


    private fun getBase64(bm: Bitmap): String {
        val stream = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray = stream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun base64ToBitmap(base64: String): Bitmap {
        val decodedString = Base64.decode(base64, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    }


    override fun setData(e: Person) {
        txtName.setText(e.nombres)
        txtLastName.setText(e.apellido)
        txtDireccion.setText(e.direccion)
        txtTelefono.setText(e.telefono)
        if(e.foto != null && e.foto.isNotEmpty()){
            imgCapture.setImageBitmap(base64ToBitmap(e.foto))
        }
    }
}