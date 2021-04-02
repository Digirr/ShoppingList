package com.digir.shoppinglist

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.digir.shoppinglist.databinding.ActivityAddItemBinding

class AddItemActivity() : AppCompatActivity() {

    lateinit var B: ActivityAddItemBinding
    var choice : Int = 0

    var flagIsEdit = false
    var itemId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        B = ActivityAddItemBinding.inflate(layoutInflater)
        setContentView(B.root)

        B.addItem.setOnClickListener {
            addItem()
        }
    }

    override fun onResume() {
        super.onResume()
        if(intent.hasExtra("itemName")) {
            B.itemName.setText(intent.getCharSequenceExtra("itemName"))
        }
        if(intent.hasExtra("shopName")) {
            B.chosenShop.text = (getString(R.string.chosenShop)+ " " + intent.getCharSequenceExtra("shopName"))
        }
        if(intent.hasExtra("inBasket")) {
            B.itemBasket.isChecked = intent.getBooleanExtra("inBasket", false)
        }
        if(intent.hasExtra("isEdit")) {
            flagIsEdit = true
            B.addItem.text = getString(R.string.edit)
        }
        if(intent.hasExtra("itemID")) {
            itemId = intent.getIntExtra("itemID", -1)
        }

    }

    fun alertDialog(v : View) {
        var list = resources.getStringArray(R.array.shops_array)
        var builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setTitle(getString(R.string.choiceShop))
        builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->

            B.chosenShop.text = getString(R.string.chosenShop) + " " + list[choice];
        })
        builder.setSingleChoiceItems(list, -1, DialogInterface.OnClickListener { dialog, which ->
            run {
                choice = which
            }

        })
        var dialog: AlertDialog = builder.create()
        dialog.show()

    }
    fun addItem(){
        var list = resources.getStringArray(R.array.shops_array)
        var itemName = B.itemName.text.toString()
        var shopChoice = list[choice]
        var inBasket = B.itemBasket.isChecked

        var temp: String = ""

        if(itemName.isEmpty()) {
            Toast.makeText(applicationContext, getString(R.string.toastGiveProductName), Toast.LENGTH_SHORT)
                    .show()
        } else if (itemName.length > 18){
            var x = 0
            while(x < 18) {

                if(x >=15) {
                    temp += "."
                } else
                    temp += itemName[x].toString()
                x++
            }
            itemName = temp
        }



        if(itemName.isNotEmpty()) {
            if(!flagIsEdit){
                ItemsDataBase.nameItem.add(itemName)
                ItemsDataBase.nameShop.add(shopChoice)
                ItemsDataBase.isBought.add(inBasket)
                Toast.makeText(this, getString(R.string.toastItemAdded), Toast.LENGTH_SHORT)
                    .show()
            }
            else {
                if(itemId != -1){
                    ItemsDataBase.nameItem[itemId] = itemName
                    ItemsDataBase.nameShop[itemId] = shopChoice
                    ItemsDataBase.isBought[itemId] = inBasket

                    Toast.makeText(this, getString(R.string.toastItemEdited), Toast.LENGTH_SHORT)
                        .show()
                }

            }

        }
    }

}