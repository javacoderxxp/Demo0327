package com.charliesong.demo0327.design

import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.TabLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.View
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import com.charliesong.demo0327.base.BaseActivity
import com.charliesong.demo0327.R
import com.charliesong.demo0327.base.BaseRvAdapter
import com.charliesong.demo0327.base.BaseRvHolder
import kotlinx.android.synthetic.main.activity_scroll_test.*
/**
 * Created by charlie.song on 2018/4/26.
 */
class ActivityScorllTest : BaseActivity() {

    var stateHeight=0;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scroll_test)
        defaultSetTitle("game",toolbar)
        toolbar.title="title"
        stateHeight=getStatusHeight(this@ActivityScorllTest)
        initTabs()
        toolbar.viewTreeObserver.addOnGlobalLayoutListener(object :ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {
                toolbar.viewTreeObserver.removeOnGlobalLayoutListener(this)
                ( toolbar.layoutParams as CollapsingToolbarLayout.LayoutParams).apply {
                    this.topMargin=stateHeight;
                    toolbar.layoutParams=this;
                }
                val params=tab_items.layoutParams as FrameLayout.LayoutParams
                params.topMargin=toolbar.height+stateHeight
                tab_items.layoutParams=params
                println("toolbar==========${supportActionBar?.height} stateHeight===${getStatusHeight(this@ActivityScorllTest)}")
            }
        })
        appbar2.addOnOffsetChangedListener { _, _ ->
           tab_items.visibility= if(nsv.top>tab_items.bottom) View.INVISIBLE else View.VISIBLE
        }

        fab.setOnClickListener {
            appbar2.setExpanded(false, true)
            println("click")
        }
        handleBanner()
    }
    var top=0
    private fun initTabs(){
        tab_items.addTab(tab_items.newTab().setText("place1"))
        tab_items.addTab(tab_items.newTab().setText("place2"))
        tab_items.addTab(tab_items.newTab().setText("place3"))
        tab_items.addTab(tab_items.newTab().setText("place4"))
        tab_items.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                    val child=ll.getChildAt(tab.position)
                println("onTabSelected================${nsv.top}===${nsv.scrollY}====${child.top}")
                nsv.scrollY=child.top
            }
        })
    }
    private fun handleBanner(){
        val pics= arrayListOf(R.mipmap.pic11,R.mipmap.shtq,R.mipmap.sjzg,R.mipmap.day_temperature)
        rv_banner.apply {
            layoutManager=LinearLayoutManager(this@ActivityScorllTest,LinearLayoutManager.HORIZONTAL,false)
            adapter=object :BaseRvAdapter<Int>(pics){
                override fun getLayoutID(viewType: Int): Int {
                    return R.layout.item_banner_pic
                }

                override fun onBindViewHolder(holder: BaseRvHolder, position: Int) {
                    holder.setImageRes(R.id.iv_room,getItemData(position))
                }

            }
            addOnScrollListener(object :RecyclerView.OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val index=(layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    tv_index.setText("${index+1}/${pics.size}")
                }
            })
        }
        tv_index.setText("1/${pics.size}")
        PagerSnapHelper().attachToRecyclerView(rv_banner)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_nav_bottom,menu)
        return true
    }
}