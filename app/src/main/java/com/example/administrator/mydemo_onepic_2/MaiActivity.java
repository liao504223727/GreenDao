package com.example.administrator.mydemo_onepic_2;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.mydemo_onepic_2.entity.User;
import com.example.administrator.mydemo_onepic_2.gen.UserDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/24 0024.
 */
public class MaiActivity extends Activity implements View.OnClickListener {

    private EditText mNameET;
    private Button mAddBtn;
    private ListView mUserLV;

    private UserAdapter mUserAdapter;
    private List<User> mUserList = new ArrayList<User>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mai);
        initView();
        initData();

    }

    private void initView() {
        mNameET = (EditText) findViewById(R.id.et_name);
        Log.i("TAG","========================================="+mNameET);
        mAddBtn = (Button) findViewById(R.id.btn_add);
        mUserLV = (ListView) findViewById(R.id.lv_user);

        mAddBtn.setOnClickListener(this);
    }

    private void initData() {
        mUserList = GreenDaoManager.getInstance().getSession().getUserDao().queryBuilder().build().list();
        mUserAdapter = new UserAdapter(this,mUserList);
        mUserLV.setAdapter(mUserAdapter);
        Log.i("TAG","==================================================================" + mUserLV);
    }



    /**
     * 根据名字更新某条数据的名字
     * @param prevName  原名字
     * @param newName  新名字
     */
    private void updateUser(String prevName,String newName){
        User findUser = GreenDaoManager.getInstance().getSession().getUserDao().queryBuilder()
                .where(UserDao.Properties.Username.eq(prevName)).build().unique();
        if(findUser != null) {
            findUser.setUsername(newName);
            GreenDaoManager.getInstance().getSession().getUserDao().update(findUser);
            Toast.makeText(MyApplication.getContext(), "修改成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MyApplication.getContext(), "用户不存在", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 根据名字删除某用户
     * @param name
     */
    private void deleteUser(String name){
        UserDao userDao = GreenDaoManager.getInstance().getSession().getUserDao();
        User findUser = userDao.queryBuilder().where(UserDao.Properties.Username.eq(name)).build().unique();
        if(findUser != null){
            userDao.deleteByKey(findUser.getId());
        }
    }

    /**
     * 本地数据里添加一个User
     * @param id  id
     * @param name  名字
     */
    private void insertUser(Long id, String name) {
        UserDao userDao = GreenDaoManager.getInstance().getSession().getUserDao();
        User user = new User(id, name);
        userDao.insert(user);
        mNameET.setText("");

        mUserList.clear();
        mUserList.addAll(userDao.queryBuilder().build().list());
        mUserAdapter.notifyDataSetChanged();
    }



    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId){
            case R.id.btn_add:
                insertUser(null,mNameET.getText().toString());
                break;
            default:
                break;
        }

    }
}
