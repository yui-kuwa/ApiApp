package jp.techacademy.yui.kuwahara.apiapp

import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import kotlinx.android.synthetic.main.activity_web_view.*

class ViewPagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {

    //タブの名前を格納したList
    val titleIds = listOf(R.string.tab_title_api, R.string.tab_title_favorite)

    //fragmentsはページの中身　0ページ目がApiFragment(一覧画面)、1ページ目がFavoriteFragment(お気に入り画面)
    val fragments = listOf(ApiFragment(), FavoriteFragment())//クラスをリストにしてる

    override fun getItemCount(): Int {//Viewページが何ページあるかの数字を返す関数
        return fragments.size//2(ページ)
    }

    override fun createFragment(position: Int): Fragment {//引数で受け取ったpositionのページのFragmentを返す関数
        return fragments[position]
    }

}