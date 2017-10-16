package com.example.refreshview;


public interface FooterViewListener {


    /**
     * 正常的loading的View
     */
    void onLoadingMore();

    /**
     * footerView ui-没有更多数据
     */
    void onNoMore();

    /**
     * footerView ui-加载失败的View
     */
    void onError();
}
