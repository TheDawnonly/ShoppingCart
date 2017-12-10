package mvpframework.bwie.com.a1509agoodcart.presenter;

import java.util.ArrayList;
import java.util.List;

import mvpframework.bwie.com.a1509agoodcart.bean.GoosBean;
import mvpframework.bwie.com.a1509agoodcart.mode.IMainModel;
import mvpframework.bwie.com.a1509agoodcart.mode.MainModel;
import mvpframework.bwie.com.a1509agoodcart.net.OnNetListener;
import mvpframework.bwie.com.a1509agoodcart.view.IMainActivity;

/**
 * Created by peng on 2017/11/16.
 */

public class MainPresenter {

    private final IMainModel imainModel;
    private final IMainActivity iMainActivity;

    public MainPresenter(IMainActivity iMainActivity) {
        this.iMainActivity = iMainActivity;
        imainModel = new MainModel();
    }

    public void getGoods() {
        imainModel.getGoods(new OnNetListener<GoosBean>() {
            @Override
            public void onSuccess(GoosBean goosBean) {
                //List<GoosBean.DataBean> groupList, List<List<GoosBean.DataBean.DatasBean>> childList
                List<GoosBean.DataBean> dataBean = goosBean.getData();
                List<List<GoosBean.DataBean.DatasBean>> childList = new ArrayList<List<GoosBean.DataBean.DatasBean>>();
                for (int i = 0; i < dataBean.size(); i++) {
                    List<GoosBean.DataBean.DatasBean> datas = dataBean.get(i).getDatas();
                    childList.add(datas);
                }
                iMainActivity.showList(dataBean, childList);
            }

            @Override
            public void onFailure(Exception e) {

            }
        });

    }
}
