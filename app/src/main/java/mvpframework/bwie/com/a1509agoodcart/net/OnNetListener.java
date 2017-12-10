package mvpframework.bwie.com.a1509agoodcart.net;

/**
 * Created by peng on 2017/11/16.
 */

public interface OnNetListener<T> {
    public void onSuccess(T t);

    public void onFailure(Exception e);
}
