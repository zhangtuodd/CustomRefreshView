##写在前面：
- 一个支持网络错误重试、无数据页（可自定义）、无网络界面（可自定义）的上拉加载更多，下拉刷新控件。
- 话不多说（无图说个✖️）

![效果（由于模拟器有点卡顿，但所有效果都展示出来了）.gif](http://upload-images.jianshu.io/upload_images/1933385-dcef4ad943f730ee.gif?imageMogr2/auto-orient/strip)
（由于模拟器有点卡顿，但所有效果都展示出来了）

- 为了满足大家活多事急找来即用的心态，我先上干货，怎么用？

**基本用法**（分为1，2，3步）
`ps:如果项目需要更加完善的ui显示，如：进入后界面自动刷新、网络错误重试、无数据页（可自定义）、无网络界面（可自定义）的功能请接着前三部往下看往下看`

1. **项目中添加依赖**
```
     compile 'com.zt.maven.widget:refreshview:1.0.0'
```
2. **初始化控件**
可动态代码引入，也可静态xml添加，依个人喜好和实际情况
3. **调用**
```
 refreshView.setOnLoadListener(new CustomRefreshView.OnLoadListener() {
            @Override
            public void onRefresh() {
                
                //下拉刷新，添加你刷新后的逻辑
                
                //加载完成时，隐藏控件下拉刷新的状态
                refreshView.complete();
            }

            @Override
            public void onLoadMore() {
                //上拉加载更多，添加你加载数据的逻辑

                //加载完成时，隐藏控件上拉加载的状态
                refreshView.complete();
            }
        });
```
**API扩充：**
1.  **列表自动刷新**
如需进入页面后自动刷新列表数据，请在步骤3完成后添加：
```
        refreshView.setRefreshing(true);
```
****
2. **无数据界面添加**
如果首次刷新无数据，需要显示无数据的占位图，可以在你加载完成时，根据后端接口返回的数据（一定是请求第一页且返回无数据的情况下）添加相应的占位图（上图gif中的“暂无数据”界面即控件中默认的，如果符合那恭喜你直接用即可，下面会写明调用方法。如果不符合你的审美或者和你的项目整体风格不一致，没关系，你只需把你的无数据占位图写好，api调用时当作参数传递即可，下面也会写明调用方法，很简单），并且依旧可以下拉刷新。
* 直接用控件中的默认无数据占位图：
```
      refreshView.setEmptyView("暂无数据");
```
`（注：默认的占位图界面文字显示也可以自己定义）`
* 使用自己写的无数据占位图（如customView），填入如下API的参数中
```
      refreshView.setCreateView(customView);
```
****
3. **无网络或加载失败页添加**
如果项目中需要在无网络或者加载失败的情况下（根据接口数据返回）添加相应的ui给用户一个友好的交互，那么你可以直接调用，当然也可以写自己风格的ui，下面我会一一给出用法，并且依旧可以下拉刷新
`（ps：这里需要阐明一个逻辑问题，显示无网络界面或加载出错界面只能在你首次请求数据失败的情况下。why？试想如果你的用户已经加载出了一页数据，这时突然间没网络了或者刚好服务器出了点问题导致请求失败，你这时给用户显示一个网络出错页覆盖了已经有的数据，信不信，你家产品经历已经拿着40米的砍刀等着你跑39米。。还有一个提示的地方，有朋友说这个和无数据占位图没什么区别，其实区别在于一个是请求成功但是无数据，一个是请求失败）`
* 使用引入控件中的默认加载失败（无网络）占位图-效果如上图gif的重试界面
```
      refreshView.setErrorView();
```
* 使用自己写的加载失败（无网络）占位图
这里的重试按钮点击进行重新加载的过程你只需在你的点击事件中加入refreshView.setRefreshing(true);即可。用法：
```
      refreshView.setCreateView(errorView)
```
****
4.  **加载失败重试机制**
如果项目中需要支持加载失败时重试机制（这里指已经加载出数据但是在加载下一页数据失败时，点击底部变更的ui进行加载，详见上图gif加载更多时显示点击重试），当然控件也满足需求，调用时需要判断是否时大于第一页（注：有的公司接口规定从0开始，有的从1开始），这里还有一点要注意：如果当前不是首页的情况下加载失败，你需要将你的页码数减一，否则会跳过本页数据展示，用法如下：
```
     refreshView.onError();
```
为了便于理解，我贴出我项目的具体做法，并给出解释：
```
@Override
            public void onFail(TinaException exception) {
                if (m > 1) {
                    size = size - 1;
                    refreshView.onError();
                } else {
                    refreshView.setErrorView();
                }
                refreshView.complete();
            }
```
**代码解释：**  onFail方法表示加载出错，m标记着是否是第一页数据，如果大于第一页时，页面数要减一，以保证数据不会遗漏加载。onError()方法处理了加载失败的ui显示和点击ui重新加载的机制。else里面代表当第一页数据加载失败时显示的占位。，complete():表示隐藏刷新的ui。项目最后会贴出项目demo地址，你可以自己再体会。
****
5.  **加载完成状态**
当所有数据加载加载完毕时，变更地步ui状态为"-- 没有更多了 --"
```
      refreshView.onNoMore();
```

####下面是一些显示细节补充和实现
1. 控件默认支持线性布局

2. 变更下拉刷新的ui圆圈颜色
refreshView.getSwipeRefreshLayout().setColorSchemeColors(getResources().getColor());

3. 禁止下拉刷新
refreshView.setRefreshEnable(false);

4. 禁止加载更多
refreshView.setLoadMoreEnable(false);

5. 目前项目中不支持自动添加头部，如需实现添加头部，可在第一页返回数据中的第一个位置添加一条伪造数据，然后在adapter中添加多种item判断，如果position=0，则显示头部。（ps: 这样有个弊端：首页数据加载失败时，头部也显示不出来,其实也不太影响，如果一个上线项目数据一直加载失败，这后台该多大的锅），后面会把这部分缺失补充，敬请期待。

#### 关于控件：
内部实现是通过SwipeRefreshLayout + recyclerView，通过将数据转换到控件内部的包装类WrapperAdapter获取数据进行加载更多的item包装。使用DataObserver来检测数据源的变化，来显示正常数据界面、无网络、无数据界面。

####[项目源码demo地址，如有帮助请star](https://github.com/zhangtuodd/CustomRefreshView)
`ps : 由于无网络、无数据界面、还有加载重试机制在真实的情况下不好复现，为了所有情况都展示出来以便大家观看，因此demo中的数据是自己模拟的`

[闷骚代码男，码字不易，请抬起你的小手，点个赞呗～](http://www.jianshu.com/p/1a82cdab2249)
