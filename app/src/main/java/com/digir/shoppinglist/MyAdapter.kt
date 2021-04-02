package com.digir.shoppinglist

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView

class MyAdapter (context : Context, recyclerView: RecyclerView) : RecyclerView.Adapter<MyAdapter.MyViewHolder>(){

    val context: Context = context
    val recyclerView : RecyclerView = recyclerView


    class MyViewHolder (view: View) : RecyclerView.ViewHolder(view){
        val itemName: TextView = view.findViewById(R.id.item_name)
        val shopName: TextView = view.findViewById(R.id.shop_name)
        val inBasket: CheckBox = view.findViewById(R.id.checkBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var layoutInflater = LayoutInflater.from(parent.context)

        val itemRow = layoutInflater.inflate(R.layout.item_row, parent, false)
        return MyViewHolder(itemRow)
    }

    override fun getItemCount(): Int {
        return ItemsDataBase.nameItem.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       holder.itemName.text = ItemsDataBase.nameItem[position]
       holder.shopName.text = ItemsDataBase.nameShop[position]
       holder.inBasket.isChecked = ItemsDataBase.isBought[position]
       //isBought[position].toString()
        holder.itemView.setOnLongClickListener(object : View.OnLongClickListener{
            override fun onLongClick(v: View?): Boolean {
               // Toast.makeText(context, "Edycja produktu", Toast.LENGTH_SHORT).show()
                var intent : Intent = Intent(context, AddItemActivity::class.java)
                intent.putExtra("itemName", ItemsDataBase.nameItem[position])
                intent.putExtra("shopName", ItemsDataBase.nameShop[position])
                intent.putExtra("inBasket", ItemsDataBase.isBought[position])
                intent.putExtra("isEdit", true)
                intent.putExtra("itemID", position)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(context, intent, null)
                return false

            }

        })
        holder.itemView.setOnClickListener {
            if(ListShopActivity.deleteFlag) {
                ItemsDataBase.nameItem.removeAt(position)
                ItemsDataBase.nameShop.removeAt(position)
                ItemsDataBase.isBought.removeAt(position)
                refreshRecyclerView(position, context, recyclerView)
            }
        }
    }


}
fun refreshRecyclerView(position : Int, context: Context, recyclerView: RecyclerView) {
    val mAdapter = MyAdapter(context, recyclerView)
    recyclerView.removeViewAt(position)
    mAdapter.notifyItemRemoved(position)
    mAdapter.notifyItemRangeChanged(position, ItemsDataBase.nameItem.size)
}

