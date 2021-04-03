package com.digir.shoppinglist

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.digir.shoppinglist.databinding.ActivityListShopBinding
import java.io.*
import java.util.*
import kotlin.collections.ArrayList

class ListShopActivity : AppCompatActivity() {



    var fileFlag : Boolean = false

    val GLOBAL_KEY = "GLOBAL_KEY"

    companion object {
        //Using ViewBinding
        private lateinit var B: ActivityListShopBinding
        var deleteFlag : Boolean = false
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        B = ActivityListShopBinding.inflate(layoutInflater)
        setContentView(B.root)

        B.recyclerView.layoutManager = LinearLayoutManager(this)
        B.recyclerView.adapter = MyAdapter(applicationContext, B.recyclerView)

        getsharePreferences()
    }

    private fun getsharePreferences() {
        var appPrefs : SharedPreferences = getSharedPreferences(GLOBAL_KEY, MODE_PRIVATE)

        var size : Int = appPrefs.getInt("listsSize", -1)
        for(i in 0 until size) {
            Log.d("SHARE1", appPrefs.getString("itemKey_$i", "").toString())
            Log.d("SHARE2", appPrefs.getString("shopKey_$i", "").toString())
            Log.d("SHARE3", appPrefs.getBoolean("isBought_$i", false).toString())
            ItemsDataBase.nameItem.add(appPrefs.getString("itemKey_$i", "").toString())
            ItemsDataBase.nameShop.add(appPrefs.getString("shopKey_$i", "").toString())
            ItemsDataBase.isBought.add(appPrefs.getBoolean("isBought_$i", false))
        }
        fileFlag = appPrefs.getBoolean("fileFlag", false)
    }
    private fun sharePreferences() {
        var sharedPreferences : SharedPreferences = getSharedPreferences(GLOBAL_KEY, MODE_PRIVATE)
        var prefsEditor = sharedPreferences.edit()
        for(i in 0 until ItemsDataBase.nameItem.size) {
            prefsEditor.putString(("itemKey_$i"), ItemsDataBase.nameItem[i])
            prefsEditor.putString(("shopKey_$i"), ItemsDataBase.nameShop[i])
            prefsEditor.putBoolean(("isBought_$i"), ItemsDataBase.isBought[i])
            Log.d("TEST1", ItemsDataBase.nameItem[i])
            Log.d("TEST1", ItemsDataBase.nameShop[i])
            Log.d("TEST1", ItemsDataBase.isBought[i].toString())
        }
        prefsEditor.putInt("listsSize", ItemsDataBase.nameItem.size)
        prefsEditor.putBoolean("fileFlag", fileFlag)
        prefsEditor.commit()

        ItemsDataBase.nameItem.clear()
        ItemsDataBase.nameShop.clear()
        ItemsDataBase.isBought.clear()
    }
    override fun onDestroy() {
        super.onDestroy()
        sharePreferences()
    }

    override fun onResume() {
        super.onResume()
        B.recyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_item_fragment, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.new_item -> {
            var intent: Intent = Intent(this, AddItemActivity::class.java)
            startActivity(intent)
            true
        }
        R.id.delete_items -> {
            if (!deleteFlag) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.toastDeleteItem),
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.toastUndeleteItem),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
            deleteFlag = !deleteFlag
            true
        }
        R.id.download_items -> {
            readFile()
            true
        }
        R.id.upload_items -> {
            saveToFile()
            true
        }
        else -> {
            false
        }
    }

    private fun saveToFile() {
        fileFlag = true
        var text1 : ArrayList<String> = ItemsDataBase.nameItem
        var text2: ArrayList<String> = ItemsDataBase.nameShop
        var text3: ArrayList<Boolean> = ItemsDataBase.isBought
        try {
            var fOut : FileOutputStream = openFileOutput("shoppingList.txt", MODE_PRIVATE)
            var osw: OutputStreamWriter = OutputStreamWriter(fOut)

            var temp : Int = text1.size

            try {
                for(i in 0 until temp) {
                    osw.write(text1[i] + "|" + text2[i] + "|" + text3[i] + "\n")
                }
            } catch (e: Exception) { }
            osw.close()
            Toast.makeText(applicationContext, getString(R.string.toastListSaved), Toast.LENGTH_SHORT)
                .show()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
    private fun readFile() {
//        var f : File = File("shoppingList.txt") - not workingi idk
//        Log.e("EXIST", f.isFile.toString())//
        if (fileFlag) { //Using this becouse f.exist() returns always false
            var fIn: FileInputStream = openFileInput("shoppingList.txt")
            var isr: InputStreamReader = InputStreamReader(fIn)
            var reader: BufferedReader = BufferedReader(isr)

            var linia: String
            val sb = StringBuilder()

            val text: List<String> = reader.readLines()

            ItemsDataBase.nameItem.clear()
            ItemsDataBase.nameShop.clear()
            ItemsDataBase.isBought.clear()
            for (line in text) {
                val st = StringTokenizer(line)
                ItemsDataBase.nameItem.add(st.nextToken("|").toString())
                ItemsDataBase.nameShop.add(st.nextToken("|").toString())
                ItemsDataBase.isBought.add(st.nextToken("|").toBoolean())
            }
            reader.close()
            B.recyclerView.adapter?.notifyDataSetChanged()
            Toast.makeText(applicationContext, getString(R.string.toastListDownloaded), Toast.LENGTH_SHORT)
                    .show()
        } else {
            Toast.makeText(applicationContext, getString(R.string.toastListFileNotExist), Toast.LENGTH_SHORT)
                    .show()
        }
    }

}

