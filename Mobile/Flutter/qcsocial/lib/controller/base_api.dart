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
    dio = Dio(_baseOptions)..interceptors.add(AuthInterceptor());
  }

  Dio of({contentType}) {
    _baseOptions.contentType = contentType ?? Headers.jsonContentType;
    dio.options = _baseOptions;
    return dio;
  }
}

class AuthInterceptor extends InterceptorsWrapper {
  @override
  void onRequest(
    RequestOptions options,
    RequestInterceptorHandler handler,
  ) async {
    if (BaseApi.token == null) {
      log('no token，request token firstly...');
      final token = await _getAccessToken();
      options.headers['Authorization'] = 'Bearer $token';
    }
    super.onRequest(options, handler);
  }

  @override
  void onResponse(Response response, ResponseInterceptorHandler handler) {
    log('RESPONSE[${response.statusCode}] => PATH: ${response.requestOptions.path}');
    log('RESPONSE[${response.statusCode}] => DATA: ${response.data}');
    var jsonData = jsonEncode(response.data);
    response.data = jsonData;
    super.onResponse(response, handler);
  }

  @override
  void onError(DioError err, ErrorInterceptorHandler handler) async {
    log('ERROR[${err.response?.statusCode}] => PATH: ${err.requestOptions.path}');
    if (err.response?.statusCode == 401 && BaseApi.token != null) {
      // Refresh token
      final newToken = await _getAccessToken();
      if (newToken != null) {
        log('Request token succeed, value: $newToken');
        // Update access token
        var options = err.requestOptions;
        options.headers['Authorization'] = 'Bearer $newToken';
        // Re-send failed request
        return handler.resolve(await _retryCall(options));
      }
    }
    return handler.next(err);
  }

  /*
   * Retry
   **/
  Future<Response> _retryCall(RequestOptions options) async {
    final _options = Options(method: options.method, headers: options.headers);
    return BaseApi().dio.request(options.path,
        data: options.data,
        queryParameters: options.queryParameters,
        options: _options);
  }

  Future<String?> _getAccessToken() async {
    final response = await _retry(3, Dio(BaseApi()._baseOptions));
    if (response == null) {
      return null;
    }
    var newToken = response.data['data'];
    BaseApi.token = newToken;
    return newToken;
  }
}

Future<Response<T>?> _retry<T>(int retries, Dio dio) async {
  try {
    log('Refresh token');
    return await dio.get('/users/refresh');
  } on DioError catch (e) {
    log('ERROR REFRESH TOKEN: ${e.message}');
    if (retries > 1) {
      if (retries == 2) {
        var newToken =
            'eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJxY3NvY2lhbCIsInN1YiI6IjYzZTYxMDA4OWFjM2Y4MmIzMTQyYzEyYiIsImlhdCI6MTY3OTQ3NTM0NSwiZ3JvdXBzIjpbInJlZnJlc2giXSwiZXhwIjoxNjgwMDgwMTQ1LCJqdGkiOiJjMjAyMTNhMC0zYWI0LTQzMmUtYmRlNy1hOTU5ZTJmMzk1YzEifQ.5XtofvDsNybAwXVy0MAuJ8VQlhchShb72KZ3v4tfoDkG0sw8GbclNGraWX6tnJx0GuCNqnWKN95H-T-1vQoyFx7Ap90wK-fUF_igx2VyPENNo-Aqc02XihC1ZsKKmyhAvwXuPAWG3dFnWBixYxTvxuVDl8xKZwtj-didyEMUtPlN_hLEijDLoFu-P2AqpyUN6B1soVS59nSIKh3SEpi-gNfl9TyR6H0g-uG865v4h315mCezjYXgCweoRrDXqoueDSDhbMvR9HyrSFfOJ3W8JpvKGMC5lr5Bw_6rHKmlMfN-4cMXEzsEFIxaSGEJNdYzuLN4Qsu_rQbNoJj7dQ1CWQ';
        dio.options.headers['Authorization'] = 'Bearer $newToken';
      }
      // Delay 2 seconds to recall refresh token
      await Future.delayed(const Duration(seconds: 2));
      return _retry(retries - 1, dio);
    }
    return null;
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
