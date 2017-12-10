package mvpframework.bwie.com.a1509agoodcart.view;

import java.util.List;

import mvpframework.bwie.com.a1509agoodcart.bean.GoosBean;

/**
 * Created by peng on 2017/11/16.
 */

public interface IMainActivity {
    public void showList(List<GoosBean.DataBean> groupList, List<List<GoosBean.DataBean.DatasBean>> childList);
}
