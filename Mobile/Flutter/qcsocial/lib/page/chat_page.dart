import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../provider/chat_provider.dart';
import '../localization/localize.dart';
import '../widget/chat_item.dart';
import '../widget/custom_widget.dart';

class ChatPage extends StatefulWidget {
  const ChatPage({Key? key}) : super(key: key);

  @override
  State<StatefulWidget> createState() => _ChatPageState();
}

class _ChatPageState extends State<ChatPage> {
  final TextEditingController _controller = TextEditingController();
  late ChatProvider _chatProvider;
  late Map _modalArgument;

  @override
  void initState() {
    super.initState();
    Future.microtask(() => _chatProvider
      ..init(_modalArgument['id'])
      ..onError = CustomWidget.showRetry(
        context,
        () => _chatProvider.connect(),
      )
      ..connect());
    _controller.addListener(() => setState(() {}));
  }

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    Localize().init(context);
    _modalArgument = ModalRoute.of(context)?.settings.arguments as Map;
    _chatProvider = Provider.of<ChatProvider>(context);
  }

  @override
  void dispose() {
    super.dispose();
    _chatProvider.disconnect();
    _controller.dispose();
  }

  @override
  Widget build(BuildContext context) {
    _actionMessage(text) {
      _chatProvider.sendMessage(text);
      _controller.clear();
    }

    Widget _viewMessage() {
      return Expanded(
        child: ScrollConfiguration(
          behavior: ScrollConfiguration.of(context).copyWith(
            scrollbars: false,
          ),
          child: ListView.builder(
            shrinkWrap: true,
            reverse: true,
            physics: const AlwaysScrollableScrollPhysics(),
            itemCount: _chatProvider.chats.length,
            itemBuilder: (context, index) {
              var listLength = _chatProvider.chats.length;
              var reversedIndex =
                  listLength == 0 ? listLength : listLength - 1 - index;
              var chat = _chatProvider.chats[reversedIndex];
              return ChatItem(chat: chat);
            },
          ),
        ),
      );
    }

    Widget _viewInputMessage() {
      return TextField(
        controller: _controller,
        decoration: const InputDecoration(
          border: InputBorder.none,
          contentPadding: EdgeInsets.only(
            top: 10,
            bottom: 10,
            left: 20,
            right: 10,
          ),
          labelText: 'Send a message',
        ),
        maxLines: 3,
        minLines: 1,
        onSubmitted:
            _controller.text.isEmpty ? null : (text) => _actionMessage(text),
      );
    }

    Widget _viewSendMessage() {
      return Row(
        children: [
          Expanded(
            child: Container(
              margin: const EdgeInsets.symmetric(horizontal: 10),
              decoration: BoxDecoration(
                color: Colors.grey.shade200,
                borderRadius: BorderRadius.circular(20),
              ),
              child: _viewInputMessage(),
            ),
          ),
          IconButton(
            icon: const Icon(Icons.send),
            onPressed: _controller.text.isEmpty
                ? null
                : () => _actionMessage(_controller.text),
          ),
        ],
      );
    }

    return Scaffold(
      resizeToAvoidBottomInset: true,
      appBar: AppBar(
        title: Text(_modalArgument['name']),
      ),
      body: GestureDetector(
        onTap: () => FocusManager.instance.primaryFocus?.unfocus(),
        child: Padding(
          padding: const EdgeInsets.all(10),
          child: Column(
            children: [_viewMessage(), _viewSendMessage()],
          ),
        ),
      ),
    );
  }
}
