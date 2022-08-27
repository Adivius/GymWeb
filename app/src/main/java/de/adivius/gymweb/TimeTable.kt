package de.adivius.gymweb

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


class TimeTable : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_table)
        supportActionBar!!.title = "Stundenplan"

        permcheck()
        loadImage()
        drawer()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            val selectedPhoto: Uri? = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhoto)
            saveImage(bitmap)
        }
    }

    private fun saveImage(image: Bitmap): String? {
        var savedImagePath: String? = null
        val imageFileName = "Stundenplan.jpg"
        val storageDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "gymweb")
        var success = true
        if (!storageDir.exists()) {
            success = storageDir.mkdirs()
        }
        if (success) {
            val imageFile = File(storageDir, imageFileName)
            savedImagePath = imageFile.absolutePath
            try {
                val fOut: OutputStream = FileOutputStream(imageFile)
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
                fOut.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            galleryAddPic(savedImagePath)
            loadImage()
        }
        return savedImagePath
    }

    private fun galleryAddPic(imagePath: String?) {
        imagePath?.let { path ->
            val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            val f = File(path)
            val contentUri: Uri = Uri.fromFile(f)
            mediaScanIntent.data = contentUri
            sendBroadcast(mediaScanIntent)
        }
    }

    fun loadImage() {
        val imageView: ImageView = findViewById(R.id.time_zoom)
        imageView.setImageDrawable(null)
        val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/gymweb"), "Stundenplan.jpg")
        val filePath = file.path
        val bitmap = BitmapFactory.decodeFile(filePath)
        if (bitmap == null) helptoast("Füge einen Stundenplan mit der \"ändern\" Schaltfläche hinzu")
        imageView.setImageBitmap(bitmap)
    }

    private fun verifyStoragePermissions(activity: Activity) {
        val REQUEST_EXTERNAL_STORAGE = 1
        val PERMISSIONS_STORAGE = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val permission = ActivityCompat.checkSelfPermission(
            activity,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                activity,
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            )
        }
    }

    private fun permcheck() {
        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                } else {
                    verifyStoragePermissions(this)
                }
            }
        requestPermissionLauncher.launch(
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_time, menu)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)) return true
        when (item.itemId) {
            R.id.change -> {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, 0)
            }
        }
        return super.onOptionsItemSelected(item)
    }


    fun drawer(){
        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout_time)
        val navView: NavigationView = findViewById(R.id.navView_time)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar    ?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {

            var i = MainActivity::class.java

            startActivity(Intent(this, i))

            when (it.itemId){

                R.id.nav_menu_home -> startActivity(Intent(this, MainActivity::class.java))
                R.id.nav_menu_subject -> startActivity(Intent(this, SubjectManagement::class.java))
                R.id.nav_menu_time -> startActivity(Intent(this, TimeTable::class.java))
                R.id.nav_menu_work-> startActivity(Intent(this, JobActivity::class.java))
                R.id.nav_menu_settings -> startActivity(Intent(this, Settings::class.java))
                R.id.nav_menu_share -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            }
            true
        }
    }
}







