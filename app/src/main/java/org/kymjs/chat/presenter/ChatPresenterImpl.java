package org.kymjs.chat.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageHandler;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;

import org.kymjs.chat.OnOperationListener;
import org.kymjs.chat.adapter.ChatAdapter;
import org.kymjs.chat.emoji.DisplayRules;
import org.kymjs.chat.module.Emojicon;
import org.kymjs.chat.module.Faceicon;
import org.kymjs.chat.module.Message;
import org.kymjs.chat.view.IChatView;
import org.kymjs.chat.widget.KJChatKeyboard;
import org.kymjs.kjframe.ui.ViewInject;
import org.kymjs.kjframe.utils.FileUtils;
import org.kymjs.kjframe.utils.KJLoger;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by lulu on 2017/1/22.
 * 聊天界面的处理逻辑
 */
public class ChatPresenterImpl implements IChatPresenter {
    private IChatView mChatView;
    private Context mContext;
    private ChatAdapter adapter;
    public static final int REQUEST_CODE_GETIMAGE_BYSDCARD = 0x1;
    private Activity aty;
    //聊天信息
    List<Message> mDatas = new ArrayList<>();
    //输入键盘
    private KJChatKeyboard box;
    private ListView mRealListView;

    public ChatPresenterImpl(Context context, IChatView chatView) {
        mContext = context;
        mChatView = chatView;
    }

    ///////////////////////////////////////////////////////////////////////////
    //初始化Presenter的数据
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public void initPresenterData() {
        box = mChatView.onGetBox();
        mRealListView = mChatView.onGetRealListView();
        aty = mChatView.onGetAty();
    }

    @Override
    public void initWidget() {
        initMessageInputToolBox();
        initListView();
        AVIMMessageManager.registerDefaultMessageHandler(new CustomMeesageHandler(mDatas, adapter, fromUser, toUser));
    }

    ///////////////////////////////////////////////////////////////////////////
    // 初始化ListView
    ///////////////////////////////////////////////////////////////////////////
    private void initListView() {
        adapter = new ChatAdapter(mContext, mDatas, getOnChatItemClickListener());
        mRealListView.setAdapter(adapter);
    }

    ///////////////////////////////////////////////////////////////////////////
    // 测试发送
    ///////////////////////////////////////////////////////////////////////////
    private String toUser = "Tom";
    private String fromUser = "Jerry";

    AVIMClient tom = AVIMClient.getInstance(fromUser);


    ///////////////////////////////////////////////////////////////////////////
    // 初始化表情键盘
    ///////////////////////////////////////////////////////////////////////////
    private void initMessageInputToolBox() {
        box.setOnOperationListener(new OnOperationListener() {
            @Override
            public void send(String content) {
                final Message message = new Message(Message.MSG_TYPE_TEXT, Message.MSG_STATE_SENDING,
                        fromUser, "avatar", toUser,
                        "avatar", content, true, false, new Date());
                //测试发送
                tom.open(new AVIMClientCallback() {
                    @Override
                    public void done(AVIMClient client, AVIMException e) {
                        if (e == null) {
                            //创建对话
                            client.createConversation(Arrays.asList(toUser), "Tom & Jerry", null, new AVIMConversationCreatedCallback() {
                                @Override
                                public void done(AVIMConversation conversation, AVIMException e) {
                                    if (e == null) {
                                        AVIMTextMessage msg = new AVIMTextMessage();
                                        msg.setText(message.getContent());
                                        //发送消息
                                        conversation.sendMessage(msg, new AVIMConversationCallback() {
                                            @Override
                                            public void done(AVIMException e) {
                                                if (e == null) {
                                                    message.setState(Message.MSG_STATE_SUCCESS);
                                                    message.setSendSucces(true);
                                                    adapter.refresh(mDatas);
                                                } else {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                    } else {
                                        e.printStackTrace();
                                    }

                                }
                            });
                        } else {
                            e.printStackTrace();
                        }
                    }
                });
                mDatas.add(message);
                adapter.refresh(mDatas);
                //createReplayMsg(message);
            }

            @Override
            public void selectedFace(Faceicon content) {
                Message message = new Message(Message.MSG_TYPE_FACE, Message.MSG_STATE_SUCCESS,
                        fromUser, "avatar", "Jerry", "avatar", content.getPath(), true, true, new
                        Date());
                mDatas.add(message);
                adapter.refresh(mDatas);
                createReplayMsg(message);
            }

            @Override
            public void selectedEmoji(Emojicon emoji) {
                box.getEditTextBox().append(emoji.getValue());
            }

            @Override
            public void selectedBackSpace(Emojicon back) {
                DisplayRules.backspace(box.getEditTextBox());
            }

            @Override
            public void selectedFunction(int index) {
                switch (index) {
                    case 0:
                        goToAlbum();
                        break;
                    case 1:
                        ViewInject.toast("跳转相机");
                        break;
                }
            }
        });

        List<String> faceCagegory = new ArrayList<>();
//        File faceList = FileUtils.getSaveFolder("chat");
        File faceList = new File("");
        if (faceList.isDirectory()) {
            File[] faceFolderArray = faceList.listFiles();
            for (File folder : faceFolderArray) {
                if (!folder.isHidden()) {
                    faceCagegory.add(folder.getAbsolutePath());
                }
            }
        }

        box.setFaceData(faceCagegory);
        mRealListView.setOnTouchListener(getOnTouchListener());
    }


    /**
     * 跳转到选择相册界面
     */
    private void goToAlbum() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            aty.startActivityForResult(Intent.createChooser(intent, "选择图片"),
                    REQUEST_CODE_GETIMAGE_BYSDCARD);
        } else {
            intent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            aty.startActivityForResult(Intent.createChooser(intent, "选择图片"),
                    REQUEST_CODE_GETIMAGE_BYSDCARD);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_GETIMAGE_BYSDCARD) {
            Uri dataUri = data.getData();
            if (dataUri != null) {
                Log.d("lulu", "ChatPresenterImpl-onActivityResult  dataUri:" + dataUri.toString());
//                File file = FileUtils.uri2File(aty, dataUri);
//                Message message = new Message(Message.MSG_TYPE_PHOTO, Message.MSG_STATE_SUCCESS,
//                        "Tom", "avatar", "Jerry",
//                        "avatar", file.getAbsolutePath(), true, true, new Date());
//                mDatas.add(message);
//                adapter.refresh(mDatas);
            }
        }
    }

    /**
     * 若软键盘或表情键盘弹起，点击上端空白处应该隐藏输入法键盘
     *
     * @return 会隐藏输入法键盘的触摸事件监听器
     */
    private View.OnTouchListener getOnTouchListener() {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                box.hideLayout();
                box.hideKeyboard(aty);
                return false;
            }
        };
    }

    ///////////////////////////////////////////////////////////////////////////
    // 创建回复信息
    ///////////////////////////////////////////////////////////////////////////
    private void createReplayMsg(Message message) {
        final Message reMessage = new Message(message.getType(), Message.MSG_STATE_SUCCESS, "Tom",
                "avatar", "Jerry", "avatar", message.getType() == Message.MSG_TYPE_TEXT ? "返回:"
                + message.getContent() : message.getContent(), false,
                true, new Date());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000 * (new Random().nextInt(3) + 1));
                    aty.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mDatas.add(reMessage);
                            adapter.refresh(mDatas);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    ///////////////////////////////////////////////////////////////////////////
    // 测试接收
    ///////////////////////////////////////////////////////////////////////////
    private static class CustomMeesageHandler extends AVIMMessageHandler {
        private final List<Message> mDatas;
        private final ChatAdapter mAdapter;
        private String fromUser;
        private String toUser;

        public CustomMeesageHandler(List<Message> datas, ChatAdapter adapter, String fromUser, String toUser) {
            mDatas = datas;
            mAdapter = adapter;
            this.fromUser = fromUser;
            this.toUser = toUser;
        }

        @Override
        public void onMessage(AVIMMessage message, AVIMConversation conversation, AVIMClient client) {
            super.onMessage(message, conversation, client);
            if (message instanceof AVIMTextMessage) {
                final Message reMessage = new Message(Message.MSG_TYPE_TEXT, Message.MSG_STATE_SUCCESS,
                        fromUser, "avatar", toUser, "avatar", ((AVIMTextMessage) message).getText(), false, true, new Date());
                mDatas.add(reMessage);
                mAdapter.refresh(mDatas);
            }
        }
    }


    /**
     * @return 聊天列表内存点击事件监听器
     */
    private OnChatItemClickListener getOnChatItemClickListener() {
        return new OnChatItemClickListener() {
            @Override
            public void onPhotoClick(int position) {
                KJLoger.debug(mDatas.get(position).getContent() + "点击图片的");
                ViewInject.toast(aty, mDatas.get(position).getContent() + "点击图片的");
            }

            @Override
            public void onTextClick(int position) {
                Toast.makeText(mContext, "测试" + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFaceClick(int position) {
                Toast.makeText(mContext, "测试" + position, Toast.LENGTH_SHORT).show();
            }
        };
    }

    /**
     * 聊天列表中对内容的点击事件监听
     */
    public interface OnChatItemClickListener {
        void onPhotoClick(int position);

        void onTextClick(int position);

        void onFaceClick(int position);
    }
}
