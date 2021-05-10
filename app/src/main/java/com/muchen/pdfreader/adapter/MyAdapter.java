package com.muchen.pdfreader.adapter;

import java.io.File;
import java.util.List;

import com.muchen.pdfreader.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
//适配器
public class MyAdapter extends BaseAdapter{
    private Context context;
    private List<String> list;
    public MyAdapter(Context context,List<String> list){
    this.context = context;
    this.list = list;
    }
    public void setList(List<String> list){
        this.list = list;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }
    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.pdf_item_list, null);
            vh = new ViewHolder();
            vh.imageView = (ImageView) convertView.findViewById(R.id.pdf_item_seq);
            vh.tv_path = (TextView) convertView.findViewById(R.id.pdf_item_name);
            convertView.setTag(vh);
        }else {
            vh = (ViewHolder) convertView.getTag();
        }
        String path = list.get(position);
        File file = new File(path);
        if(file.isDirectory()){//文件夹
            vh.imageView.setImageResource(R.drawable.folder);
            vh.tv_path.setText(file.getName());
        }else {
            if(file.getName().endsWith("pdf")){
                vh.imageView.setImageResource(R.drawable.file);
                vh.tv_path.setText(file.getName());
            }

        }
        return convertView;
    }
    class ViewHolder{
        private ImageView imageView;
        private TextView tv_path;
    }
}
