package com.project.community.ui.index;

import android.graphics.Color;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.project.community.R;
import com.project.community.base.BaseActivity;
import com.project.community.base.DividerItemDecoration;
import com.project.community.listener.OnRecyclerItemClickListener;
import com.project.community.model.CategoryModel;
import com.project.community.model.CategorySection;
import com.project.community.ui.adapter.CategoryAdapter;
import com.project.community.view.DividerGridItemDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by qizfeng on 17/7/27.
 * 分类activity
 */

public class CategoryActivity extends BaseActivity {
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.tv_cancel)
    TextView mTvCancel;
    private List<CategorySection> mData = new ArrayList<>();
    private CategoryAdapter mAdapter;
    private ItemTouchHelper mItemTouchHelper;
    private Menu mMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        initToolBar(mToolBar, mTvTitle, true, getString(R.string.activity_category), R.mipmap.iv_back);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(this));
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,0));
        initData();
    }

    private void initData() {
        mData = new ArrayList<>();
        mData.add(new CategorySection(true, "我的分类", false));
        mData.add(new CategorySection(new CategoryModel("0", "即时聊天", 0, -1, "", R.mipmap.c3_icon_1, false, false)));
        mData.add(new CategorySection(new CategoryModel("1", "报修", 1, 0, "", R.mipmap.c3_icon_2, false, false)));
        mData.add(new CategorySection(new CategoryModel("2", "缴费", 2, 0, "", R.mipmap.c3_icon_3, false, false)));
        mData.add(new CategorySection(new CategoryModel("3", "公交", 3, 0, "", R.mipmap.c3_icon_4, false, false)));
        mData.add(new CategorySection(new CategoryModel("4", "社区论坛", 4, 0, "", R.mipmap.c3_icon_5, false, false)));
        mData.add(new CategorySection(new CategoryModel("5", "附近商家", 5, 0, "", R.mipmap.c3_icon_6, false, false)));
        mData.add(new CategorySection(new CategoryModel("6", "物业客服", 6, 0, "", R.mipmap.c3_icon_7, false, false)));
        mData.add(new CategorySection(true, "政务", false));
        mData.add(new CategorySection(new CategoryModel("7", "宣传", 6, 1, "", R.mipmap.c3_icon_8, false, true)));
        mData.add(new CategorySection(new CategoryModel("8", "问卷", 6, 1, "", R.mipmap.c3_icon_9, false, true)));
        mData.add(new CategorySection(new CategoryModel("9", "意见", 6, 1, "", R.mipmap.c3_icon_10, false, true)));
        mData.add(new CategorySection(new CategoryModel("10", "指南", 6, 1, "", R.mipmap.c3_icon_11, false, true)));
        mData.add(new CategorySection(new CategoryModel("11", "热线", 6, 1, "", R.mipmap.c3_icon_12, false, true)));
        mData.add(new CategorySection(true, "物业", false));
        mData.add(new CategorySection(new CategoryModel("2", "缴费", 6, 2, "", R.mipmap.c3_icon_3, false, true)));
        mData.add(new CategorySection(new CategoryModel("1", "报修", 6, 2, "", R.mipmap.c3_icon_2, false, true)));
        mData.add(new CategorySection(new CategoryModel("12", "家庭信息", 6, 1, "", R.mipmap.c3_icon_13, false, true)));
        mData.add(new CategorySection(new CategoryModel("6", "物业客服", 6, 2, "", R.mipmap.c3_icon_7, false, false)));
        mData.add(new CategorySection(true, "民生", false));
        mData.add(new CategorySection(new CategoryModel("3", "公交", 6, 2, "", R.mipmap.c3_icon_4, false, true)));
        mData.add(new CategorySection(new CategoryModel("4", "社区论坛", 6, 2, "", R.mipmap.c3_icon_5, false, true)));
        mData.add(new CategorySection(new CategoryModel("5", "附件商家", 6, 2, "", R.mipmap.c3_icon_6, false, true)));
        mData.add(new CategorySection(new CategoryModel("6", "医院", 6, 1, "", R.mipmap.c3_icon_14, false, true)));
        mData.add(new CategorySection(new CategoryModel("13", "招聘信息", 6, 1, "", R.mipmap.c3_icon_15, false, true)));

        mAdapter = new CategoryAdapter(R.layout.layout_item_category, R.layout.layout_item_category_section_head, mData, new CategoryAdapter.OnClickListener() {
            @Override
            public void onCancelClick(List<CategorySection> list, int position) {
                //循环赋值
                for (int i = 0; i < list.size(); i++) {
                    if (!list.get(i).isHeader) {
                        if (list.get(position).t.id.equals(list.get(i).t.id)) {
                            list.get(i).t.state = 1;//添加按钮状态
                            list.get(i).t.isShowEdit = true;//显示按钮
                            list.get(i).t.isAdd = true;//是添加按钮
                            list.set(i, list.get(i));
                        }
                    }
                }
                if (position == 1) {
                    try {
                        if (!list.get(position + 1).isHeader) {//出现我的分类没有数据的情况,即连续2个分类标题
                            list.get(position + 1).t.state = -1;//分类标题
                            list.set(position + 1, list.get(position + 1));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                mAdapter.getData().remove(position);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onAddClick(List<CategorySection> list, int position) {
                CategorySection cs = list.get(position);
                cs.t.state = 2;//已添加
                cs.t.isShowEdit = true;//显示编辑按钮
                list.set(position, cs);
                for (int i = 1; i < list.size(); i++) {
                    if (list.get(i).isHeader) {//如果是分类头部
                        if (i == 1) {//如果是第一项分类头部
                            //设置隐藏上移按钮
                            list.add(i, new CategorySection(new CategoryModel(cs.t.id, cs.t.title, cs.t.sort, -1, "", cs.t.res, true, false)));
                        } else {
                            list.add(i, new CategorySection(new CategoryModel(cs.t.id, cs.t.title, cs.t.sort, 0, "", cs.t.res, true, false)));
                        }
                        break;
                    }
                }

                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onNothingClick(int position) {
                showToast(getString(R.string.toast_add_category_nothing));
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(new OnRecyclerItemClickListener(mRecyclerView) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {
            }

            @Override
            public void onItemLongClick(RecyclerView.ViewHolder vh) {
                //判断被拖拽的是否是前两个，如果不是则执行拖拽
                try {
                    if (vh.getLayoutPosition() != 0 && vh.getLayoutPosition() != 1) {
                        if (!mData.get(vh.getLayoutPosition()).isHeader) {
                            if (mData.get(vh.getLayoutPosition()).t.state == 0) {
                                mItemTouchHelper.startDrag(vh);
                            }
                        }
                        //获取系统震动服务
                        //Vibrator vib = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);//震动70毫秒
                        //vib.vibrate(70);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {

            /**
             * 是否处理滑动事件 以及拖拽和滑动的方向 如果是列表类型的RecyclerView的只存在UP和DOWN，如果是网格类RecyclerView则还应该多有LEFT和RIGHT
             * @param recyclerView
             * @param viewHolder
             * @return
             */
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                    final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN |
                            ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                    final int swipeFlags = 0;
                    return makeMovementFlags(dragFlags, swipeFlags);
                } else {
                    final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                    final int swipeFlags = 0;
                    //final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                    return makeMovementFlags(dragFlags, swipeFlags);
                }
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                //得到当拖拽的viewHolder的Position
                int fromPosition = viewHolder.getAdapterPosition();
                //拿到当前拖拽到的item的viewHolder
                int toPosition = target.getAdapterPosition();

                if (mData.get(toPosition).isHeader) {//如果目标位置是分类标题,则不可移动
                    return false;
                }
                if (mData.get(toPosition).t.state != 0) {//如果目标位置不是我的分类,则不可移动
                    return false;
                }
                if (toPosition == 1 || toPosition == 0) {//0是分类头部,1是默认不可编辑位置
                    return false;
                }

                if (fromPosition < toPosition) {
                    for (int i = fromPosition; i < toPosition; i++) {
                        Collections.swap(mData, i, i + 1);
                    }
                } else {
                    for (int i = fromPosition; i > toPosition; i--) {
                        Collections.swap(mData, i, i - 1);
                    }
                }
                mAdapter.notifyItemMoved(fromPosition, toPosition);
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//                int position = viewHolder.getAdapterPosition();
//                myAdapter.notifyItemRemoved(position);
//                datas.remove(position);
            }

            /**
             * 重写拖拽可用
             * @return
             */
            @Override
            public boolean isLongPressDragEnabled() {
                for (int i = 0; i < mData.size(); i++) {
                    if (mData.get(i).isHeader) {//如果是分类标题,不可长按
                        return false;
                    } else {
                        if (mData.get(i).t.state != 0) {//如果不是已定制的栏目,不可长按
                            return false;
                        } else {
                            return true;
                        }
                    }
                }
                return false;
            }

            /**
             * 长按选中Item的时候开始调用
             *
             * @param viewHolder
             * @param actionState
             */
            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
//                if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
//                    viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
//                }
                super.onSelectedChanged(viewHolder, actionState);
            }

            /**
             * 手指松开的时候还原
             * @param recyclerView
             * @param viewHolder
             */
            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                viewHolder.itemView.setBackgroundColor(0);
            }
        });
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        inflater = getActivity().getMenuInflater();
//        inflater.inflate(R.menu.menu_actionbar, menu);
        getMenuInflater().inflate(R.menu.menu_category, menu);
        mMenu=menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
           /* case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;*/

            case R.id.action_favorite:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
//                finish();
                if (item.getTitle().equals("编辑")) {
                    item.setTitle("完成");
                    mAdapter.showEditBtn(mData);
                    mToolBar.setNavigationIcon(null);
                    mTvCancel.setVisibility(View
                            .VISIBLE);
                } else if (item.getTitle().equals("完成")) {
                    mAdapter.hideEditBtn(mData);
                    finish();
                }
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_favorite).setTitle("编辑");
        mMenu=menu;
        return super.onPrepareOptionsMenu(menu);
    }

    @OnClick(R.id.tv_cancel)
    public void onCancel(View v){
        mAdapter.hideEditBtn(mData);
        mToolBar.setNavigationIcon(R.mipmap.iv_back);
        mMenu.findItem(R.id.action_favorite).setTitle("编辑");
        mTvCancel.setVisibility(View.GONE);

        mData = new ArrayList<>();
        mData.add(new CategorySection(true, "我的分类", false));
        mData.add(new CategorySection(new CategoryModel("0", "即时聊天", 0, -1, "", R.mipmap.c3_icon_1, false, false)));
        mData.add(new CategorySection(new CategoryModel("1", "报修", 1, 0, "", R.mipmap.c3_icon_2, false, false)));
        mData.add(new CategorySection(new CategoryModel("2", "缴费", 2, 0, "", R.mipmap.c3_icon_3, false, false)));
        mData.add(new CategorySection(new CategoryModel("3", "公交", 3, 0, "", R.mipmap.c3_icon_4, false, false)));
        mData.add(new CategorySection(new CategoryModel("4", "社区论坛", 4, 0, "", R.mipmap.c3_icon_5, false, false)));
        mData.add(new CategorySection(new CategoryModel("5", "附近商家", 5, 0, "", R.mipmap.c3_icon_6, false, false)));
        mData.add(new CategorySection(new CategoryModel("6", "物业客服", 6, 0, "", R.mipmap.c3_icon_7, false, false)));
        mData.add(new CategorySection(true, "政务", false));
        mData.add(new CategorySection(new CategoryModel("7", "宣传", 6, 1, "", R.mipmap.c3_icon_8, false, true)));
        mData.add(new CategorySection(new CategoryModel("8", "问卷", 6, 1, "", R.mipmap.c3_icon_9, false, true)));
        mData.add(new CategorySection(new CategoryModel("9", "意见", 6, 1, "", R.mipmap.c3_icon_10, false, true)));
        mData.add(new CategorySection(new CategoryModel("10", "指南", 6, 1, "", R.mipmap.c3_icon_11, false, true)));
        mData.add(new CategorySection(new CategoryModel("11", "热线", 6, 1, "", R.mipmap.c3_icon_12, false, true)));
        mData.add(new CategorySection(true, "物业", false));
        mData.add(new CategorySection(new CategoryModel("2", "缴费", 6, 2, "", R.mipmap.c3_icon_3, false, true)));
        mData.add(new CategorySection(new CategoryModel("1", "报修", 6, 2, "", R.mipmap.c3_icon_2, false, true)));
        mData.add(new CategorySection(new CategoryModel("12", "家庭信息", 6, 1, "", R.mipmap.c3_icon_13, false, true)));
        mData.add(new CategorySection(new CategoryModel("6", "物业客服", 6, 2, "", R.mipmap.c3_icon_7, false, false)));
        mData.add(new CategorySection(true, "民生", false));
        mData.add(new CategorySection(new CategoryModel("3", "公交", 6, 2, "", R.mipmap.c3_icon_4, false, true)));
        mData.add(new CategorySection(new CategoryModel("4", "社区论坛", 6, 2, "", R.mipmap.c3_icon_5, false, true)));
        mData.add(new CategorySection(new CategoryModel("5", "附件商家", 6, 2, "", R.mipmap.c3_icon_6, false, true)));
        mData.add(new CategorySection(new CategoryModel("6", "医院", 6, 1, "", R.mipmap.c3_icon_14, false, true)));
        mData.add(new CategorySection(new CategoryModel("13", "招聘信息", 6, 1, "", R.mipmap.c3_icon_15, false, true)));
        mAdapter.setNewData(mData);
        mAdapter.notifyDataSetChanged();

    }

}
