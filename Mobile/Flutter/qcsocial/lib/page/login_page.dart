import 'package:flutter/material.dart';

class LoginPage extends StatefulWidget {
  const LoginPage({super.key});

  @override
  State<StatefulWidget> createState() => _LoginPageState();
}

class _LoginPageState extends State<LoginPage> {
  final TextEditingController _loginCtrl = TextEditingController();
  final TextEditingController _pwCtrl = TextEditingController();

  TextField _inputField({hint, controller}) {
    return TextField(
      controller: controller,
      decoration: InputDecoration(
        border: const OutlineInputBorder(),
        contentPadding: const EdgeInsets.all(10),
        labelText: hint,
      ),
      maxLines: 1,
    );
  }

  Widget _formLogin() {
    return Card(
      margin: const EdgeInsets.all(10),
      elevation: 50,
      shadowColor: Colors.black,
      child: Padding(
        padding: const EdgeInsets.all(30),
        child: Column(
          children: [
            const Text(
              'Login',
              style: TextStyle(
                  fontWeight: FontWeight.bold,
                  fontSize: 32,
                  fontStyle: FontStyle.italic,
                  color: Colors.blue),
            ),
            Padding(
              padding: const EdgeInsets.only(top: 30),
              child: _inputField(
                hint: 'User Name',
                controller: _loginCtrl,
              ),
            ),
            Padding(
              padding: const EdgeInsets.only(top: 10, bottom: 20),
              child: _inputField(
                hint: 'Password',
                controller: _pwCtrl,
              ),
            ),
            ElevatedButton(
              onPressed: () {},
              child: const Center(child: Text("Login")),
            ),
          ],
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      resizeToAvoidBottomInset: true,
      body: GestureDetector(
        onTap: () => FocusManager.instance.primaryFocus?.unfocus(),
        child: Container(
          decoration: const BoxDecoration(
            gradient: LinearGradient(
              colors: [Colors.lightBlueAccent, Colors.greenAccent],
              begin: Alignment.topLeft,
              end: Alignment.bottomRight,
            ),
          ),
          child: Padding(
            padding: const EdgeInsets.all(30),
            child: Column(
              children: [
                const Spacer(),
                _formLogin(),
                const Spacer(),
                const Text(
                  'Term and condition',
                  style: TextStyle(
                      fontSize: 12,
                      fontStyle: FontStyle.italic,
                      color: Colors.white),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
