package jp.techacademy.yui.kuwahara.apiapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import io.realm.Realm
import io.realm.RealmConfiguration
import jp.techacademy.yui.kuwahara.apiapp.WebViewActivity.Companion.KEY_SHOP
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), FragmentCallback {

    //var mRealm = FavoriteShop().realm

    /*   by lazyは初期化を遅らせ、      */
    private val viewPagerAdapter by lazy { ViewPagerAdapter(this) }

    private var mShop: Shop? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Realmのセットアップ
        /*val realmConfig = RealmConfiguration.Builder()
        .deleteRealmIfMigrationNeeded()
        .build()
        mRealm = Realm.getInstance(realmConfig)
        Realm.deleteRealm(realmConfig)*/

        // ViewPager2の初期化
        viewPager2.apply {
            //adapter：ViewPager2でページングするものを決定
            adapter = viewPagerAdapter
            //orientation：スワイプの方向を決めるもの。何も指定しないと横向き。
            orientation = ViewPager2.ORIENTATION_HORIZONTAL // スワイプの向き横（ORIENTATION_VERTICAL を指定すれば縦スワイプで実装可能です）
            offscreenPageLimit = viewPagerAdapter.itemCount // ViewPager2で保持する画面数
        }

        // TabLayoutの初期化
        // TabLayoutとViewPager2を紐づける
        // TabLayoutのTextを指定する
        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.setText(viewPagerAdapter.titleIds[position])
        }.attach()

    }

    ////////////////////////////////////////////////////////////////////////////////////
    //WebViewActivityに画面遷移させる
    override fun onClickItem(shop: Shop) {
        //WebViewActivity.start(this, url)
        // intentオブジェクト生成、遷移画面定義
        val intent = Intent(this, WebViewActivity::class.java)
        // intentオブジェクトにショップの内容をプットする
        val state = Shop(shop.couponUrls,shop.id,shop.logoImage,shop.name,shop.address)
        intent.putExtra(KEY_SHOP, state)//shopを渡す
        startActivity(intent)
    }
    ///////////////////////////////////////////////////////////////////////////

    override fun onAddFavorite(shop: Shop) { // Favoriteに追加するときのメソッド(Fragment -> Activity へ通知する)
        FavoriteShop.insert(FavoriteShop().apply {
            id = shop.id
            name = shop.name
            address = shop.address
            imageUrl = shop.logoImage
            url = if (shop.couponUrls.sp.isNotEmpty()) shop.couponUrls.sp else shop.couponUrls.pc
        })
        (viewPagerAdapter.fragments[VIEW_PAGER_POSITION_FAVORITE] as FavoriteFragment).updateData()
    }

    override fun onDeleteFavorite(id: String) { // Favoriteから削除するときのメソッド(Fragment -> Activity へ通知する)
        showConfirmDeleteFavoriteDialog(id)
    }

    fun showConfirmDeleteFavoriteDialog(id: String) {
        AlertDialog.Builder(this)
            .setTitle(R.string.delete_favorite_dialog_title)
            .setMessage(R.string.delete_favorite_dialog_message)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                deleteFavorite(id)
            }
            .setNegativeButton(android.R.string.cancel) { _, _ ->}
            .create()
            .show()
    }

    private fun deleteFavorite(id: String) {
        FavoriteShop.delete(id)
        (viewPagerAdapter.fragments[VIEW_PAGER_POSITION_API] as ApiFragment).updateView()
        (viewPagerAdapter.fragments[VIEW_PAGER_POSITION_FAVORITE] as FavoriteFragment).updateData()
    }

    companion object {
        public const val VIEW_PAGER_POSITION_API = 0 //publicが冗長な書き方で消せるからグレーになってる
        public const val VIEW_PAGER_POSITION_FAVORITE = 1
    }

}

