package jp.techacademy.yui.kuwahara.apiapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_web_view.*
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.recycler_favorite.view.*

class WebViewActivity : AppCompatActivity() {

    // お気に入り状態を取得
    //今開いているレストランのidを取ってき、お気に入りかお気に入りじゃないかの情報をとる
    var isFavorite: Boolean? = null

    //private val favoriteAdapter by lazy { FavoriteAdapter(requireContext()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        val selectShop = intent.getSerializableExtra(KEY_SHOP)

        if(selectShop is Shop) {
            webView.loadUrl(selectShop.couponUrls.sp)
            isFavorite = FavoriteShop.findBy(selectShop.id) != null
        }

        //クリック処理
        // お気に入りボタン押した時の処理
        favorite_imageView.apply {
            setImageResource(if (isFavorite!!) R.drawable.ic_star else R.drawable.ic_star_border) // Picassoというライブラリを使ってImageVIewに画像をはめ込む
            setOnClickListener {
                //もしお気に入りやったらお気に入りを外す、お気に入りじゃなかったらお気に入りにする
                if(selectShop is Shop) {
                    if (isFavorite!!) { //invoke(呼び出す)
                        AlertDialog.Builder(context)
                            .setTitle(R.string.delete_favorite_dialog_title)
                            .setMessage(R.string.delete_favorite_dialog_message)
                            .setPositiveButton(android.R.string.ok) { _, _ ->
                                FavoriteShop.delete(selectShop.id)
                                Update(selectShop)
                            }
                            .setNegativeButton(android.R.string.cancel) { _, _ ->}
                            .create()
                            .show()
                    } else {
                        FavoriteShop.insert(FavoriteShop().apply {
                            id = selectShop.id
                            name = selectShop.name
                            address = selectShop.address
                            imageUrl = selectShop.logoImage
                            //urlがあった場合設定されてる方を代入
                            url =
                                if (selectShop.couponUrls.sp.isNotEmpty()) selectShop.couponUrls.sp else selectShop.couponUrls.pc
                        })
                        Update(selectShop)
                        //isFavorite =! isFavorite
                    }
                }
            }
        }

        //お気に入り情報のデータをどこかに渡す

    }

    companion object {
        const val KEY_SHOP = "key_shop"
    }

    fun Update(shop: Shop){
        isFavorite = FavoriteShop.findBy(shop.id) != null
        favorite_imageView.setImageResource(if (isFavorite!!) R.drawable.ic_star else R.drawable.ic_star_border)
    }
}