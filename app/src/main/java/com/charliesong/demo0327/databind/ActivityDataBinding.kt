package com.charliesong.demo0327.databind

import android.annotation.SuppressLint
import android.databinding.BaseObservable
import android.databinding.Bindable
import android.databinding.DataBindingUtil
import android.databinding.ObservableField
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import com.charliesong.demo0327.BR
import com.charliesong.demo0327.R
import com.charliesong.demo0327.app.BaseFunction
import com.charliesong.demo0327.base.BaseActivity
import com.charliesong.demo0327.base.BaseRvAdapter
import com.charliesong.demo0327.base.BaseRvHolder
import com.charliesong.demo0327.bean.TreeBean
import com.charliesong.demo0327.databinding.ActivityDataBindingBinding
import com.charliesong.demo0327.databinding.ItemTreeBinding
import com.charliesong.demo0327.app.MyAPIManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_data_binding.*
import kotlinx.android.synthetic.main.item_contact.*

class ActivityDataBinding : BaseActivity() {

    data class LoginParams( var phone:String?=null, var psw:String?=null):BaseObservable()

    var loginParams=LoginParams().apply {
        phone="111"
        psw="password"
    }
    var testMethod2=TestMethod().apply {
        name="test"
    }

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       DataBindingUtil.setContentView<ActivityDataBindingBinding>(this,R.layout.activity_data_binding).apply {
           cbAgree.setOnCheckedChangeListener { buttonView, isChecked ->
               state=isChecked
               loginParams.phone="change"
               this.notifyPropertyChanged(BR.params)
               println("test==${testMethod?.name}============${loginParams}")
               this.notifyPropertyChanged(BR.testMethod)
           }
           cbAgree.isChecked=true;
           this.buttonText="submit"
           params=loginParams
            testMethod=testMethod2
       }

        btn_go.setOnClickListener {
            showToast("${loginParams}")
        }
        tv_name.setText("jerry")
        tv_phone.setText("10086")
//        setContentView(R.layout.activity_data_binding)
//        DataBindingUtil.bind<ActivityDataBindingBinding>(findViewById(R.id.cstlayout))?.apply {
//            cbAgree.isChecked=true;
//        }
//
//        ActivityDataBindingBinding.bind(findViewById(R.id.cstlayout))

        defaultSetTitle("databinding")
        MyAPIManager.getAPI().getAllTreeChildren()
                .compose(BaseFunction.handle())
                .subscribe({
                    initRvTree(it)
                    println("success===============${it.size}")
                }, {
                    println("failed=============${it}")
                    it.printStackTrace()
                })

//        MyAPIManager.getAPI2().getHotNovels().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
//                .subscribe({
//
//                    println("result=================${it.code}===${it.data?.size}")
//                }
//                ,{
//                    println("error=============$it")
//                }
//                ,{
//                    println("complete=============")
//                })
//        MyAPIManager.getAPI2().getWeather().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
//                .subscribe({
//
//                    println("result=================${it.status}==${it.data.city}")
//                }
//                ,{
//                    println("error=============$it")
//                }
//                ,{
//                    println("complete=============")
//                })
    }

    fun initRvTree(list: List<TreeBean>) {
        rv_tree.apply {
            layoutManager = GridLayoutManager(this@ActivityDataBinding, 4)
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(outRect: Rect, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                    outRect.apply {
                        right = 10
                        bottom = 10
                    }
                }
            })
            println("lists count================${list.size}")
            adapter = object : BaseRvAdapter<TreeBean>(ArrayList<TreeBean>().apply {
                addAll(list)
            }) {
                override fun getLayoutID(viewType: Int): Int {
                    return R.layout.item_tree
                }

//                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRvHolder {
//                   var bind= DataBindingUtil.inflate<ItemTreeBinding>(LayoutInflater.from(parent.context), R.layout.item_tree, parent, false);
//                    return BaseRvHolder(bind.root)
//                }
                override fun onBindViewHolder(holder: BaseRvHolder, position: Int) {
//                    DataBindingUtil.getBinding<ItemTreeBinding>(holder.itemView)?.apply {
//                        this.tree=getItemData(position)
//                        this.index="$position";
//                    }
//                println("$position===================${holder.itemView.tag}")
                    ItemTreeBinding.bind(holder.itemView).apply {

                        when(position%2){
                            0->{
                                this.tree=getItemData(position)
                                this.index="$position"
                            }
                            1->{
                                this.setVariable(BR.tree,getItemData(position))
                                this.setVariable(BR.index,"$position")
                            }
                            2->{
                                this.tvTree.setText(getItemData(position).name)
                                this.tvIndex.setText("$position")
                                tvIndex.setBackgroundColor(Color.RED)
                            }
                        }

                    }
                }
            }
        }
    }


}
