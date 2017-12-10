package mvpframework.bwie.com.a1509agoodcart.mode;

import mvpframework.bwie.com.a1509agoodcart.bean.GoosBean;
import mvpframework.bwie.com.a1509agoodcart.net.OnNetListener;

/**
 * Created by peng on 2017/11/16.
 */

public interface IMainModel {
    public void getGoods(OnNetListener<GoosBean> onNetListener);
}
