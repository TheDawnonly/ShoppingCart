package mvpframework.bwie.com.a1509agoodcart.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import mvpframework.bwie.com.a1509agoodcart.R;
import mvpframework.bwie.com.a1509agoodcart.bean.GoosBean;
import mvpframework.bwie.com.a1509agoodcart.eventbusevent.MessageEvent;
import mvpframework.bwie.com.a1509agoodcart.eventbusevent.PriceAndCountEvent;

/**
 * Created by peng on 2017/11/16.
 */

public class MyAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<GoosBean.DataBean> groupList;
    private List<List<GoosBean.DataBean.DatasBean>> childList;
    private final LayoutInflater inflater;

    public MyAdapter(Context context, List<GoosBean.DataBean> groupList, List<List<GoosBean.DataBean.DatasBean>> childList) {
        this.context = context;
        this.groupList = groupList;
        this.childList = childList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childList.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view;
        final GroupViewHolder holder;
        if (convertView == null) {
            holder = new GroupViewHolder();
            view = inflater.inflate(R.layout.item_parent_market, null);
            holder.cbGroup = view.findViewById(R.id.cb_parent);
            holder.tv_number = view.findViewById(R.id.tv_number);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (GroupViewHolder) view.getTag();
        }
        final GoosBean.DataBean dataBean = groupList.get(groupPosition);
        holder.cbGroup.setChecked(dataBean.isChecked());
        holder.tv_number.setText(dataBean.getTitle());
        //一级checkbox
        holder.cbGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataBean.setChecked(holder.cbGroup.isChecked());
                changeChildCbState(groupPosition, holder.cbGroup.isChecked());
                EventBus.getDefault().post(compute());
                changeAllCbState(isAllGroupCbSelected());
                notifyDataSetChanged();
            }
        });

        return view;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view;
        final ChildViewHolder holder;
        if (convertView == null) {
            holder = new ChildViewHolder();
            view = inflater.inflate(R.layout.item_child_market, null);
            holder.cbChild = view.findViewById(R.id.cb_child);
            holder.tv_tel = view.findViewById(R.id.tv_tel);
            holder.tv_content = view.findViewById(R.id.tv_content);
            holder.tv_time = view.findViewById(R.id.tv_time);
            holder.tv_price = view.findViewById(R.id.tv_pri);
            holder.tv_del = view.findViewById(R.id.tv_del);
            holder.iv_add = view.findViewById(R.id.iv_add);
            holder.iv_del = view.findViewById(R.id.iv_del);
            holder.tv_num = view.findViewById(R.id.tv_num);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ChildViewHolder) view.getTag();
        }
        final GoosBean.DataBean.DatasBean datasBean = childList.get(groupPosition).get(childPosition);
        holder.cbChild.setChecked(datasBean.isChecked());
        holder.tv_tel.setText(datasBean.getType_name());
        holder.tv_content.setText(datasBean.getMsg());
        holder.tv_time.setText(datasBean.getAdd_time());
        holder.tv_price.setText(datasBean.getPrice() + "");
        holder.tv_num.setText(datasBean.getNum() + "");

        //二级checkbox
        holder.cbChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置该条目对象里的checked属性值
                datasBean.setChecked(holder.cbChild.isChecked());
                PriceAndCountEvent priceAndCountEvent = compute();
                EventBus.getDefault().post(priceAndCountEvent);

                if (holder.cbChild.isChecked()) {
                    //当前checkbox是选中状态

                    if (isAllChildCbSelected(groupPosition)) {
                        changGroupCbState(groupPosition, true);
                        changeAllCbState(isAllGroupCbSelected());
                    }
                } else {
                    changGroupCbState(groupPosition, false);
                    changeAllCbState(isAllGroupCbSelected());
                }
                notifyDataSetChanged();
            }
        });
        //加号
        holder.iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = datasBean.getNum();
                holder.tv_num.setText(++num + "");
                datasBean.setNum(num);
                if (holder.cbChild.isChecked()) {
                    PriceAndCountEvent priceAndCountEvent = compute();
                    EventBus.getDefault().post(priceAndCountEvent);
                }
            }
        });
        //减号
        holder.iv_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = datasBean.getNum();
                if (num == 1) {
                    return;
                }
                holder.tv_num.setText(--num + "");
                datasBean.setNum(num);
                if (holder.cbChild.isChecked()) {
                    PriceAndCountEvent priceAndCountEvent = compute();
                    EventBus.getDefault().post(priceAndCountEvent);
                }
            }
        });
        //删除
        holder.tv_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<GoosBean.DataBean.DatasBean> datasBeen = childList.get(groupPosition);
                GoosBean.DataBean.DatasBean remove = datasBeen.remove(childPosition);
                if (datasBeen.size() == 0) {
                    childList.remove(groupPosition);
                    groupList.remove(groupPosition);
                }
                EventBus.getDefault().post(compute());
                notifyDataSetChanged();
            }
        });
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class GroupViewHolder {
        CheckBox cbGroup;
        TextView tv_number;
    }

    class ChildViewHolder {
        CheckBox cbChild;
        TextView tv_tel;
        TextView tv_content;
        TextView tv_time;
        TextView tv_price;
        TextView tv_del;
        ImageView iv_del;
        ImageView iv_add;
        TextView tv_num;
    }

    /**
     * 改变全选的状态
     *
     * @param flag
     */
    private void changeAllCbState(boolean flag) {
        MessageEvent messageEvent = new MessageEvent();
        messageEvent.setChecked(flag);
        EventBus.getDefault().post(messageEvent);
    }

    /**
     * 改变一级列表checkbox状态
     *
     * @param groupPosition
     */
    private void changGroupCbState(int groupPosition, boolean flag) {
        GoosBean.DataBean dataBean = groupList.get(groupPosition);
        dataBean.setChecked(flag);
    }

    /**
     * 改变二级列表checkbox状态
     *
     * @param groupPosition
     * @param flag
     */
    private void changeChildCbState(int groupPosition, boolean flag) {
        List<GoosBean.DataBean.DatasBean> datasBeen = childList.get(groupPosition);
        for (int i = 0; i < datasBeen.size(); i++) {
            GoosBean.DataBean.DatasBean datasBean = datasBeen.get(i);
            datasBean.setChecked(flag);
        }
    }

    /**
     * 判断一级列表是否全部选中
     *
     * @return
     */
    private boolean isAllGroupCbSelected() {
        for (int i = 0; i < groupList.size(); i++) {
            GoosBean.DataBean dataBean = groupList.get(i);
            if (!dataBean.isChecked()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断二级列表是否全部选中
     *
     * @param groupPosition
     * @return
     */
    private boolean isAllChildCbSelected(int groupPosition) {
        List<GoosBean.DataBean.DatasBean> datasBeen = childList.get(groupPosition);
        for (int i = 0; i < datasBeen.size(); i++) {
            GoosBean.DataBean.DatasBean datasBean = datasBeen.get(i);
            if (!datasBean.isChecked()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 计算列表中，选中的钱和数量
     */
    private PriceAndCountEvent compute() {
        int count = 0;
        int price = 0;
        for (int i = 0; i < childList.size(); i++) {
            List<GoosBean.DataBean.DatasBean> datasBeen = childList.get(i);
            for (int j = 0; j < datasBeen.size(); j++) {
                GoosBean.DataBean.DatasBean datasBean = datasBeen.get(j);
                if (datasBean.isChecked()) {
                    price += datasBean.getNum() * datasBean.getPrice();
                    count += datasBean.getNum();
                }
            }
        }
        PriceAndCountEvent priceAndCountEvent = new PriceAndCountEvent();
        priceAndCountEvent.setCount(count);
        priceAndCountEvent.setPrice(price);
        return priceAndCountEvent;
    }

    /**
     * 设置全选、反选
     *
     * @param flag
     */
    public void changeAllListCbState(boolean flag) {
        for (int i = 0; i < groupList.size(); i++) {
            changGroupCbState(i, flag);
            changeChildCbState(i, flag);
        }
        EventBus.getDefault().post(compute());
        notifyDataSetChanged();
    }

}
