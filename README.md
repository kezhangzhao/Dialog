# Dialog
# 依赖方式
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
  	dependencies {
	       implementation 'com.github.kezhangzhao:Dialog:1.0.3'
	}
# 重点:
    要添加标题栏的高度dp值，不然会报错
  	<dimen name="title_bar_height">44dp</dimen>
# 介绍：
* PopupWindowBase（Popup的弹窗基础类）
* FullScreenDialog（充满屏幕的dialog）
* DialogFragmentBottom（底部弹窗dialog）
* DialogCreate（普通弹窗dialog）
# 1、Dialog使用：
  	      DialogCreate.Builder builder = new DialogCreate.Builder(mActivity);
          mDialogCreate = builder
                .setAddViewId(R.layout.dialog_contact_phone)
                .setIsHasCloseView(false)
                //.setTransparency(150)//默认：全屏dialog，如果设置这个透明度值后，上面的标题栏则不会被dialog挡住。
                .setDialogSetDateInterface(inflaterView -> {
                    TextView tvMsg = inflaterView.findViewById(R.id.tv_dialog_msg);
                    TextView tvCancel = inflaterView.findViewById(R.id.tv_cancel);
                    TextView tvConfirm = inflaterView.findViewById(R.id.tv_confirm);
                    tvMsg.setText("当前无网络，将自动保存！\n请待有网络后提交。");
                    tvConfirm.setOnClickListener(v -> {
                        mDialogCreate.dismiss();
                        Toast.makeText(this, "确认", Toast.LENGTH_SHORT).show();
                    });
                    tvCancel.setOnClickListener(v -> {
                        mDialogCreate.dismiss();
                        Toast.makeText(this, "取消", Toast.LENGTH_SHORT).show();
                    });
                })
                .build();
          mDialogCreate.showSingle();
	  
# 2、PopupWindow使用方法：	  
        PopupWindowBase popupWindowBase = new PopupWindowBase(mActivity, btPopupWindow, R.layout.pop_seletct);
        DialogSetDateInterface dialogSetDateInterface = inflaterView -> {
            TextView tv001 = inflaterView.findViewById(R.id.tv_001);
            TextView tv002 = inflaterView.findViewById(R.id.tv_002);
            TextView tv003 = inflaterView.findViewById(R.id.tv_003);
            tv001.setOnClickListener(view -> Toast.makeText(mActivity, "tv001", Toast.LENGTH_SHORT).show());
            tv002.setOnClickListener(view -> Toast.makeText(mActivity, "tv002", Toast.LENGTH_SHORT).show());
            tv003.setOnClickListener(view -> Toast.makeText(mActivity, "tv003", Toast.LENGTH_SHORT).show());

        };
        popupWindowBase.setShowingInterface(isShowing -> {
            if (isShowing) {
                Toast.makeText(mActivity, "显示PopupWindow", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mActivity, "关闭PopupWindow", Toast.LENGTH_SHORT).show();
            }
        });
        popupWindowBase.setCallback(dialogSetDateInterface);
        popupWindowBase.create();
	
# 3、DialogFragmentBottom使用方法
        DialogFragmentBottom.Builder builder = new DialogFragmentBottom.Builder(mActivity);
        dialogBottom = builder.setAddViewId(R.layout.dialog_select)
                .setIsVisitCancel(false)
                .setDialogSetDateInterface(inflaterView -> {
                    List<String> listStr = new ArrayList<>();
                    listStr.add("拍照");
                    listStr.add("从相册上传");
                    ListView listView = inflaterView.findViewById(R.id.lv_dialog_select_listview);
                    listView.setAdapter(new CommonLvAdapter<String>(mActivity, R.layout.dialog_select_listview_item, listStr) {
                        @Override
                        protected void convert(ViewHolderLv viewHolder, String item, int position) {
                            viewHolder.setText(R.id.tv_dialog_select_item, listStr.get(position));
                        }
                    });
                    TextView tvCancel = inflaterView.findViewById(R.id.tv_dialog_select_cancel);
                    tvCancel.setOnClickListener(v -> dialogBottom.dismiss());
                    listView.setOnItemClickListener((parent, view, position, id) -> {
                        if (position == 0) {//拍照
                            PicSelectUtils.startCamera(mActivity, true, selectList);
                        } else if (position == 1) {//从相册上传
                            PicSelectUtils.startPhoto(mActivity, 1, PictureConfig.MULTIPLE, true, false, selectList);
                        }
                        dialogBottom.dismiss();
                    });

                }).build();

        dialogBottom.show(getSupportFragmentManager(), "FeedBackActivity");
