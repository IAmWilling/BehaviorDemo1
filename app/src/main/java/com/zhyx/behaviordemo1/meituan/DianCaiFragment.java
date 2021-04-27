package com.zhyx.behaviordemo1.meituan;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.zhyx.behaviordemo1.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DianCaiFragment extends Fragment {
    private BaseQuickAdapter classifAdapter, detailContentAdapter;
    private RecyclerView classifyRecyclerView, contentRecyclerView;
    private MyNestedScrollView myNestedScrollView;
    private List<String> testList = new ArrayList<>();
    private List<DetailData> detailDataList = new ArrayList<>();
    List<String> strings = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dian_cai, container, false);
        Log.d("yumi", "    id   =  " + R.id.nestscrollview);
        for (int i = 0; i < 5; i++) {
            testList.add("内容 " + i);
        }
        initView(view);
        return view;
    }

    private void initView(View view) {
        contentRecyclerView = view.findViewById(R.id.detail_content_recyclerview);
        classifyRecyclerView = view.findViewById(R.id.classify_recyclerview);
        myNestedScrollView = view.findViewById(R.id.nestscrollview);
        myNestedScrollView.setIsHuaDongImp(new MyNestedScrollView.IsHuaDongImp() {
            @Override
            public boolean canSlide() {
                return ((MeiTuanActivity) Objects.requireNonNull(getActivity())).canSlide();
            }
        });
        classifyRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        contentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        for (int i = 0; i < 40; i++) {
            strings.add("测试 " + i);
        }
        getdata();
        classifAdapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.adapter_classify_item, strings) {
            @Override
            protected void convert(@NotNull BaseViewHolder baseViewHolder, String s) {
                baseViewHolder.setText(R.id.text, s);
            }
        };
        classifAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                contentRecyclerView.smoothScrollToPosition(position);
            }
        });
        detailContentAdapter = new BaseQuickAdapter<DetailData, BaseViewHolder>(R.layout.detail_item, detailDataList) {


            @Override
            protected void convert(@NotNull BaseViewHolder baseViewHolder, DetailData detailData) {
                baseViewHolder.setText(R.id.title, detailData.getTitle());
                MyRecyclerView myRecyclerView = baseViewHolder.findView(R.id.item_recyclerview);
                myRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                myRecyclerView.setAdapter(new ItemAdapter(detailData.getList()));
            }
        };
        classifyRecyclerView.setAdapter(classifAdapter);
        contentRecyclerView.setAdapter(detailContentAdapter);
    }

    class DetailData {
        private String title;
        private List<String> list = new ArrayList();

        public DetailData(String title) {
            this.title = title;
            this.list = testList;
        }

        public String getTitle() {
            return title;
        }

        public List<String> getList() {
            return list;
        }
    }

    private void getdata() {
        for (String i : strings) {
            detailDataList.add(new DetailData(i));
        }
    }

    class ItemAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        public ItemAdapter(@Nullable List<String> data) {
            super(R.layout.adapter_classify_item, data);
        }

        @Override
        protected void convert(@NotNull BaseViewHolder baseViewHolder, String s) {
            baseViewHolder.setText(R.id.text, s);
        }
    }
}