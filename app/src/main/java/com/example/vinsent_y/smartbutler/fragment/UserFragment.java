package com.example.vinsent_y.smartbutler.fragment;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vinsent_y.smartbutler.R;
import com.example.vinsent_y.smartbutler.entity.MyUser;
import com.example.vinsent_y.smartbutler.ui.CourierActivity;
import com.example.vinsent_y.smartbutler.ui.LoginActivity;
import com.example.vinsent_y.smartbutler.ui.PhoneActivity;
import com.example.vinsent_y.smartbutler.util.L;
import com.example.vinsent_y.smartbutler.util.PermissionUtils;
import com.example.vinsent_y.smartbutler.util.ShareUtils;
import com.example.vinsent_y.smartbutler.util.TextUtil;
import com.example.vinsent_y.smartbutler.view.CustomDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 包名：   com.example.vinsent_y.com.example.vinsent_y.smartbutler.fragement
 * 文件名： UserFragment.java
 * 创建者： Vinsent_Y
 * 创建时间： 2018/10/27 21:26
 * 描述：    TODO
 */


public class UserFragment extends Fragment implements View.OnClickListener {

    private TextView edit_user;
    private Button btn_exit_user;
    private Button btn_update_ok;

    private EditText et_username;
    private EditText et_sex;
    private ImageView profile_image;

    private CustomDialog dialog;
    private Button btn_camera;
    private Button btn_picture;
    private Button btn_cancel;

    private TextView tv_courier;
    private TextView tv_phone;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, null);

        findView(view);

        return view;
    }

    private void findView(View view) {
        btn_exit_user = view.findViewById(R.id.btn_exit_user);
        btn_exit_user.setOnClickListener(this);
        edit_user = view.findViewById(R.id.edit_user);
        edit_user.setOnClickListener(this);
        btn_update_ok = view.findViewById(R.id.btn_update_ok);
        btn_update_ok.setOnClickListener(this);
        profile_image = view.findViewById(R.id.profile_image);
        profile_image.setOnClickListener(this);
        tv_courier = view.findViewById(R.id.tv_courier);
        tv_courier.setOnClickListener(this);
        tv_phone = view.findViewById(R.id.tv_phone);
        tv_phone.setOnClickListener(this);

        Bitmap bitmap = ShareUtils.getBitMap(getActivity(),"image_title", null);
        if (bitmap != null) {
            profile_image.setImageBitmap(bitmap);
        }

        dialog = new CustomDialog(getActivity(), 100, 100, R.layout.dialog_photo, R.style.Theme_Dialog, Gravity.BOTTOM, R.style.pop_anim_style);
        //TODO 如何点击Dialog外就直接dismiss?
        dialog.setCancelable(true);
        btn_camera = dialog.findViewById(R.id.btn_camera);
        btn_picture = dialog.findViewById(R.id.btn_picture);
        btn_cancel = dialog.findViewById(R.id.btn_cancel);
        btn_camera.setOnClickListener(this);
        btn_picture.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);

        et_sex = view.findViewById(R.id.et_sex);
        et_username = view.findViewById(R.id.et_username);
        setEnable(false);

        MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);
        updateText(userInfo);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_exit_user:
                MyUser.logOut();   //清除缓存用户对象
                BmobUser currentUser = MyUser.getCurrentUser(); // 现在的currentUser是null了
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
                break;
            case R.id.edit_user:
                setEnable(true);
                break;
            case R.id.btn_update_ok:
                String name = et_username.getText().toString().trim();
                //此处文本输入并不是最好的方法
                String sex = et_sex.getText().toString().trim();

                if (!TextUtil.isEmpty(name) && (sex.equals("男") || sex.equals("女"))) {
                    final MyUser myUser = new MyUser();
                    myUser.setUsername(name);
                    if (sex.equals("男")) {
                        myUser.setSex(true);
                    } else {
                        myUser.setSex(false);
                    }
                    BmobUser bmobUser = BmobUser.getCurrentUser();
                    myUser.update(bmobUser.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                setEnable(false);
                                updateText(myUser);
                                Toast.makeText(getActivity(), "更新用户信息成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "更新用户信息失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "数据错误,更新失败!", Toast.LENGTH_SHORT).show();
                }
                break;
                //TODO 图片剪切功能实现
            case R.id.profile_image:
                applyPermissionAndShowDialog();
                break;
            case R.id.btn_camera:
                toCamera();
                dialog.dismiss();
                break;
            case R.id.btn_picture:
                toPicture();
                dialog.dismiss();
                break;
            case R.id.btn_cancel:
                dialog.dismiss();
                break;
            case R.id.tv_courier:
                startActivity(new Intent(getActivity(), CourierActivity.class));
                break;
            case R.id.tv_phone:
                startActivity(new Intent(getActivity(), PhoneActivity.class));
                break;
        }
    }
    public static final String PHOTO_IMAGE_FILE_NAME = "fileImg.jpg";
    public static final int CAMERA_REQUEST_CODE = 101;
    public static final int PICTURE_REQUEST_CODE = 102;
    public static final int CROP_REQUEST_CODE = 103;
    private Uri imageUri;


    private void toCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //从Android6.0系统开始,读写SD卡需要运行时权限处理才行
        //File outputImage = new File(Environment.getExternalStorageDirectory(), PHOTO_IMAGE_FILE_NAME);
        //使用应用关联目录则可以跳过这一步
        File outputImage = new File(getActivity().getExternalCacheDir(), PHOTO_IMAGE_FILE_NAME);
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(getActivity(), "com.example.vinsent_y.smartbutler.fileprovider", outputImage);
        } else {
            imageUri = Uri.fromFile(outputImage);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    private void toPicture() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, PICTURE_REQUEST_CODE);
    }

    private void updateText(MyUser userInfo) {
        et_sex.setText(userInfo.getSex() ? "男" : "女");
        et_username.setText(userInfo.getUsername());
    }

    private void setEnable(boolean visible) {
        et_sex.setEnabled(visible);
        et_username.setEnabled(visible);
        if (visible) {
            btn_update_ok.setVisibility(View.VISIBLE);
        } else {
            btn_update_ok.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != getActivity().RESULT_CANCELED) {
            switch (requestCode) {
                //相册数据
                case PICTURE_REQUEST_CODE:
                    if (data != null) {
                        if (Build.VERSION.SDK_INT >= 19) {
                            // 4.4及以上系统使用这个方法处理图片
                            handleImageOnKitKat(data);
                        } else {
                            handleImageBeforeKitKat(data);
                        }
                        ShareUtils.putBitMap(getActivity(),"image_title",profile_image);
                    }
                    break;
                case CAMERA_REQUEST_CODE:
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
                        profile_image.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
                case CROP_REQUEST_CODE:

                    break;
            }

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(getActivity(),uri)) {
            // 如果是document类型的Uri,则通过document id 处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; //解析出数字格式的ID
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            } else if("com.android.providers.downloads,documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content类型的Uri,则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是file类型的Uri,直接获取图片路径即可
            imagePath = uri.getPath();
        }
        //根据图片路径显示图片
        displayImage(imagePath);
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);

    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            profile_image.setImageBitmap(bitmap);
        } else {
            Toast.makeText(getActivity(), "获取图片失败!", Toast.LENGTH_SHORT).show();
        }
    }

    private String getImagePath(Uri uri, String  selection) {
        String path = null;
        // 通过Uri和selection来获取真是的图片路径
        Cursor cursor = getActivity().getContentResolver().query(uri,null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    //裁剪
    private void startPhotoZoom(Uri uri) {
        if (uri == null) {
            L.e("Uri is null!");
            return;
        } else {
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(uri, "image/*");
            //设置裁剪
            intent.putExtra("crop", "true");
            //裁剪宽高比例
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            //裁剪图片的质量
            intent.putExtra("outputX", 320);
            intent.putExtra("outputY", 320);
            intent.putExtra("return-date", true);
            startActivityForResult(intent, CROP_REQUEST_CODE);
        }
    }

    //方法封装
    private PermissionUtils.PermissionGrant showDialog = new PermissionUtils.PermissionGrant() {
        @Override
        public void execute() {
            dialog.show();
        }
    };

    private void applyPermissionAndShowDialog() {
        PermissionUtils.requestPermissionInFragment(this, new String[]{ PermissionUtils.PERMISSION_WRITE_EXTERNAL_STORAGE, PermissionUtils.PERMISSION_CAMERA}, showDialog);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)  {
        PermissionUtils.requestPermissionsResult(getActivity(), permissions, requestCode, grantResults, showDialog);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
