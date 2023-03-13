import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:qcsocial/model/SocketEvent.dart';

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
    return Scaffold(
      resizeToAvoidBottomInset: true,
      appBar: AppBar(
        title: Text(_modalArgument['name']),
      ),
      body: RefreshIndicator(
        onRefresh: () => _chatProvider.fetchChats(),
        child: Column(
          children: [
            Expanded(
              child: ListView.builder(
                shrinkWrap: true,
                physics: const AlwaysScrollableScrollPhysics(),
                reverse: true,
                itemCount: _chatProvider.chats.length,
                itemBuilder: (context, index) {
                  var listLength = _chatProvider.chats.length;
                  var reversedIndex =
                      listLength == 0 ? listLength : listLength - 1 - index;
                  var chat = _chatProvider.chats[reversedIndex];
                  return Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: ChatItem(chat: chat),
                  );
                },
              ),
            ),
            Row(
              children: [
                Expanded(
                  child: Form(
                    child: TextFormField(
                      controller: _controller,
                      decoration: const InputDecoration(
                        labelText: 'Send a message',
                      ),
                    ),
                  ),
                ),
                IconButton(
                  icon: const Icon(Icons.send),
                  onPressed: () {
                    _chatProvider.sendMessage(_controller.text);
                    _controller.clear();
                  },
                ),
              ],
            )
          ],
        ),
      ),
    );
  }
}
