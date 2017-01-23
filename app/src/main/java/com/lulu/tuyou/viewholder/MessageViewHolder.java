package com.lulu.tuyou.viewholder;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.lulu.tuyou.R;
import com.lulu.tuyou.common.CommonRecyclerViewHolder;
import com.lulu.tuyou.databinding.MsgItemBinding;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lulu on 2017/1/22.
 *
 */

public class MessageViewHolder extends CommonRecyclerViewHolder<AVIMConversation> {

    ImageView avatarView;
    TextView unreadView;
    TextView messageView;
    TextView timeView;
    TextView nameView;
    RelativeLayout avatarLayout;
    LinearLayout contentLayout;


    public MessageViewHolder(ViewGroup root) {
        super(root.getContext(), root, R.layout.msg_item);
        MsgItemBinding binding = DataBindingUtil.bind(itemView);
        initView(binding);
    }

    private void initView(MsgItemBinding binding) {
        avatarView = binding.conversationItemIvAvatar;
        nameView = binding.conversationItemTvName;
        timeView = binding.conversationItemTvTime;
        unreadView = binding.conversationItemTvUnread;
        messageView = binding.conversationItemTvMessage;
        avatarLayout = binding.conversationItemLayoutAvatar;
        contentLayout = binding.conversationItemLayoutContent;
    }

    @Override
    public void bindData(final AVIMConversation conversation) {
        reset();
        if (conversation != null) {
            //获取Conversation的创建时间
            if (conversation.getCreatedAt() == null) {
                //从服务器同步对话的属性
                conversation.fetchInfoInBackground(new AVIMConversationCallback() {
                    @Override
                    public void done(AVIMException e) {
                        if (e == null) {
                            updateName(conversation);
                            //updateIcon(conversation);
                        } else {
                            e.printStackTrace();
                        }
                    }
                });
            } else {
                updateName(conversation);
                //updateIcon(conversation);
            }
            updateUnreadCount(conversation);
            updateLastMessage(conversation.getLastMessage());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setItems(new String[]{"删除该聊天"}, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return false;
                }
            });

        }
    }

    /**
     * 更新 name，单聊的话展示对方姓名，群聊展示所有用户的用户名
     *
     * @param conversation
     */
    private void updateName(AVIMConversation conversation) {

    }

    /**
     * 一开始的时候全部置为空，避免因为异步请求造成的刷新不及时而导致的展示原有的缓存数据
     */
    private void reset() {
        avatarView.setImageResource(0);
        nameView.setText("");
        timeView.setText("");
        messageView.setText("");
        unreadView.setVisibility(View.GONE);
    }

    /**
     * 更新未读消息数量
     *
     * @param conversation
     */
    private void updateUnreadCount(AVIMConversation conversation) {
        int num = 0;// TODO: 2017/1/22 未读的数量
        unreadView.setText(num + "");
        unreadView.setVisibility(num > 0 ? View.VISIBLE : View.GONE);
    }


    /**
     * 更新 item 的展示内容，及最后一条消息的内容
     *
     * @param message
     */
    private void updateLastMessage(AVIMMessage message) {
        if (null != message) {
            Date date = new Date(message.getTimestamp());
            SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
            timeView.setText(format.format(date));
//            messageView.setText(getMessageeShorthand(getContext(), message));
        }
    }

    /**
     * 获取 “对方” 的用户 id，只对单聊有效，群聊返回空字符串
     *
     * @param conversation
     * @return
     */
    private static String getConversationPeerId(AVIMConversation conversation) {
        if (null != conversation && 2 == conversation.getMembers().size()) {
            // TODO: 2017/1/22 这个地方为获取对方Id
            String currentUserId = "";//当前用户ID
            String firstMemeberId = conversation.getMembers().get(0);
            return conversation.getMembers().get(firstMemeberId.equals(currentUserId) ? 1 : 0);
        }
        return "";
    }


//    private static CharSequence getMessageeShorthand(Context context, AVIMMessage message) {
//        if (message instanceof AVIMTypedMessage) {
//            AVIMReservedMessageType type = AVIMReservedMessageType.getAVIMReservedMessageType(
//                    ((AVIMTypedMessage) message).getMessageType());
//            switch (type) {
//                case TextMessageType:
//                    return ((AVIMTextMessage) message).getText();
//                case ImageMessageType:
//                    return context.getString(R.string.lcim_message_shorthand_image);
//                case LocationMessageType:
//                    return context.getString(R.string.lcim_message_shorthand_location);
//                case AudioMessageType:
//                    return context.getString(R.string.lcim_message_shorthand_audio);
//                default:
//                    CharSequence shortHand = "";
//                    if (message instanceof LCChatMessageInterface) {
//                        LCChatMessageInterface messageInterface = (LCChatMessageInterface) message;
//                        shortHand = messageInterface.getShorthand();
//                    }
//                    if (TextUtils.isEmpty(shortHand)) {
//                        shortHand = context.getString(R.string.lcim_message_shorthand_unknown);
//                    }
//                    return shortHand;
//            }
//        } else {
//            return message.getContent();
//        }
//    }
}
