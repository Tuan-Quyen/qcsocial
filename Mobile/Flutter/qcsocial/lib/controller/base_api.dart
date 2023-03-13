import 'dart:convert';
import 'dart:developer';
import 'package:dio/dio.dart';
import '../utils/constant_utils.dart';

class BaseApi {
  static String? token;
  late BaseOptions _baseOptions;
  late int count;
  late Dio dio;

  BaseApi() {
    _baseOptions = BaseOptions(
        baseUrl: ConstantUtils.BASE_URL_API,
        connectTimeout: const Duration(seconds: 5),
        sendTimeout: const Duration(seconds: 5));
    dio = Dio(_baseOptions)..interceptors.add(_interceptors());
  }

  Dio of({contentType}) {
    _baseOptions.contentType = contentType ?? Headers.jsonContentType;
    dio.options = _baseOptions;
    return dio;
  }

  InterceptorsWrapper _interceptors() {
    return InterceptorsWrapper(
      onRequest: (options, handler) async {
        //Todo: Handle accessToken
        /*if (token == null) {
          log('no token，request token firstly...');
          await _refreshToken(
            options,
            onSuccess: () {
              options.headers['Authorization'] = token;
              handler.next(options);
            },
            onFailure: (error) => handler.reject(error, true),
          );
        } else {
          options.headers['Authorization'] = token;
          return handler.next(options);
        }*/
        return handler.next(options);
      },
      onResponse: (response, handler) async {
        var jsonData = jsonEncode(response.data);
        log('RESPONSE[${response.statusCode}] => PATH: ${response.requestOptions.path}');
        log('RESPONSE[${response.statusCode}] => DATA: $jsonData');
        response.data = jsonData;
        return handler.next(response);
      },
      onError: (err, handler) async {
        log('ERROR[${err.response?.statusCode}] => PATH: ${err.requestOptions.path}');
        if (err.response?.statusCode == 401) {
          await _refreshToken(
            err.requestOptions,
            onSuccess: () =>
                err.requestOptions.headers['Authorization'] = token,
            onFailure: (error) => handler.reject(error),
          );
          return handler.resolve(await _retry(err.requestOptions));
        }
        return handler.next(err);
      },
    );
  }

  /*
   * Retry
   **/
  Future<Response> _retry(RequestOptions options) async {
    final _options = Options(method: options.method, headers: options.headers);
    return dio.request(options.path,
        data: options.data,
        queryParameters: options.queryParameters,
        options: _options);
  }

  /*
   * Handle refresh token
   **/
  Future _refreshToken(
    RequestOptions options, {
    Function()? onSuccess,
    Function(DioError)? onFailure,
  }) async {
    return dio.post('/auth/refresh').then((d) {
      log('Request token succeed, value: ' + d.data['data']['token']);
      log('Continue to perform request：${options.baseUrl}/${options.path}');
      token = 'Bearer ${d.data['data']['token']}';
      if (onSuccess != null) {
        onSuccess();
      }
    }).catchError(
      (error, stackTrace) {
        token = null;
        if (onFailure != null) {
          onFailure(error);
        }
      },
    );
  }
}

class ApiProvider {
  late BaseApi baseApi;
  static final ApiProvider _instance = ApiProvider._internal();

  ApiProvider._internal();

  factory ApiProvider() {
    _instance.baseApi = BaseApi();
    return _instance;
  }

  handleError(DioError e) {
    dynamic error;
    var data = e.response?.data;
    if (data != null) {
      error = data['errorMsg'];
    } else {
      error = e.message!;
    }
    log('ERROR[${e.response?.statusCode}] => $error');
    throw error;
  }
}
