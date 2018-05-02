package com.sultanburger.rest;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;
import com.sultanburger.AppConstants;
import com.sultanburger.BuildConfig;
import com.sultanburger.R;
import com.sultanburger.helper.preference.PreferenceHelper;
import com.sultanburger.utils.ConnectivityUtil;
import com.sultanburger.utils.Logger;
import com.sultanburger.utils.Validator;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.MalformedParameterizedTypeException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

class RestExecutor<T> extends AsyncTask<Param, Void, Result<T>> implements AppConstants {

    private static final String TAG = RestExecutor.class.getSimpleName();
    private final String charset = "UTF-8";
    private final String BOUNDARY = "*****";
    private final String TWO_HYPHENS = "--";
    private final String LINE_FEED = "\r\n";

    private final Context context;
    private final String uri;
    private final Object payload;
    private final Type responseType;
    private final MethodType methodType;
    private final ResultReceiver resultReceiver;

    private static Gson gson;

    public RestExecutor(Context context, String uri, Object payload, Type responseType, MethodType methodType, ResultReceiver resultReceiver) {
        this.context = context;
        this.uri = uri;
        this.payload = payload;
        this.responseType = responseType;
        this.methodType = methodType;
        this.resultReceiver = resultReceiver;

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.serializeNulls();
        gsonBuilder.excludeFieldsWithoutExposeAnnotation();

//        Type type = new TypeToken<List<String>>() { }.getType();
//        registerTypeAdapter(gsonBuilder, String.class);

        gson = gsonBuilder.create();
    }

    @Override
    protected Result<T> doInBackground(Param... params) {
        Result<T> retVal;

        if (ConnectivityUtil.isInternetEnabled()) {
            try {
                Uri.Builder builder = Uri.parse(BuildConfig.URL_BASE).buildUpon();
//                builder.appendPath(uri);

                if (uri.contains("/")) {
                    String[] split = uri.split("/");

                    for (String s : split) {
                        builder.appendPath(s);
                    }
                } else {
                    builder.appendPath(uri);
                }

                if (Validator.isValid(params) && params.length > 0) {
                    for (Param param : params) {
                        if (param instanceof QueryParam)
                            builder.appendQueryParameter(param.getName(), param.getValue());
                    }
                }

                URL url = new URL(builder.build().toString());
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                httpURLConnection.setRequestProperty("Accept", "application/json");
                httpURLConnection.setRequestProperty("Accept-Language", "english");
                httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

                PreferenceHelper preferenceHelper = PreferenceHelper.init(context, context.getResources().getString(R.string.app_name));
                String authToken = preferenceHelper.getString(PREF_AUTH_TOKEN);
                if (Validator.isValid(authToken))
                    httpURLConnection.setRequestProperty("Authorization", authToken);

                httpURLConnection.setRequestMethod(methodType.name().toUpperCase());
                httpURLConnection.setConnectTimeout(10 * 1000);

                if (Validator.isValid(params) && params.length > 0) {
                    for (Param param : params) {
                        if (param instanceof HeaderParam)
                            httpURLConnection.addRequestProperty(param.getName(), param.getValue());
                    }
                }

                if (Validator.isValid(payload)) {
//                    String body = gson.toJson(payload);
//
//                    DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
//                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(dataOutputStream, "UTF-8"));
//                    writer.write(body);
//                    writer.flush();
//                    writer.close();
//
//                    dataOutputStream.close();

                    DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                    PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(dataOutputStream, charset), true);

                    Class<?> componentClass = payload.getClass();
                    for (Field field : componentClass.getDeclaredFields()) {
                        field.setAccessible(true);

                        SerializedName serializedName = field.getAnnotation(SerializedName.class);
                        if (Validator.isValid(serializedName)) {
                            String serializedNameValue = serializedName.value();

                            String filedName = field.getName();
                            Object filedValue = field.get(payload);

//                            Method declaredMethod = componentClass.getDeclaredMethod("get" + StringUtil.smartUpperCase(filedName));
//                            declaredMethod.setAccessible(true);
//                            Object invoke = declaredMethod.invoke(payload);

                            if (filedValue instanceof Param) {
                                addHeaderField(printWriter, serializedNameValue, (String) filedValue);
                            } else if (filedValue instanceof File) {
                                addFilePart(printWriter, dataOutputStream, serializedNameValue, (File) filedValue);
                            } else {
                                if (filedValue instanceof String)
                                    addFormField(printWriter, serializedNameValue, (String) filedValue);
                                else
                                    addFormField(printWriter, serializedNameValue, gson.toJson(filedValue));
                            }
                        }
                    }

                    printWriter.append(LINE_FEED).flush();
                    printWriter.append(TWO_HYPHENS + BOUNDARY + TWO_HYPHENS).append(LINE_FEED);
                    printWriter.close();
                }

                int responseCode = httpURLConnection.getResponseCode();
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        response.append(line);
                        response.append('\r');
                    }

                    bufferedReader.close();
                    httpURLConnection.disconnect();

                    String responseData = response.toString();
                    Logger.writeLog(TAG, responseData);

                    Result tempResult = gson.fromJson(responseData, Result.class);
                    if (tempResult.isStatus()) {
                        String jsonData = gson.toJson(tempResult.getData());

                        if (responseType == Void.class) {
                            retVal = new Result<>((T) null);
                        } else {
                            T result = gson.fromJson(jsonData, responseType);
                            retVal = new Result<>(result);
                        }
                    } else {
                        retVal = new Result<>(new Exception(tempResult.getMessage()));
                    }
                } else {
                    retVal = new Result<>(new Exception(httpURLConnection.getResponseMessage() + " (Code " + responseCode + ")"));
                }
            } catch (Exception e) {
                retVal = new Result<>(new Exception(e));
            }
        } else {
            retVal = new Result<>(new Exception("Oops... Looks like we have a network error!\\nPlease check your internet connection"));
        }

        return retVal;
    }

    @Override
    protected void onPostExecute(Result<T> result) {
        if (Validator.isValid(result)) {
            if (Validator.isValid(result.getException()))
                resultReceiver.onFailed(result.getException());
            else
                resultReceiver.onCompleted(result);
        } else {
            resultReceiver.onFailed(new Exception("Unknown error"));
        }
    }

    private void addFormField(PrintWriter printWriter, String name, String value) {
        printWriter.append(TWO_HYPHENS + BOUNDARY).append(LINE_FEED);
        printWriter.append("Content-Disposition: form-data; name=\"" + name + "\"").append(LINE_FEED);
        printWriter.append("Content-Type: text/plain; charset=" + charset).append(LINE_FEED);
        printWriter.append(LINE_FEED);
        printWriter.append(value).append(LINE_FEED);
        printWriter.flush();
    }

    private void addFilePart(PrintWriter printWriter, OutputStream outputStream, String fieldName, File uploadFile) throws IOException {
        String fileName = uploadFile.getName();
        printWriter.append(TWO_HYPHENS + BOUNDARY).append(LINE_FEED);
        printWriter.append("Content-Disposition: form-data; name=\"" + fieldName + "\"; filename=\"" + fileName + "\"").append(LINE_FEED);
        printWriter.append("Content-Type: " + URLConnection.guessContentTypeFromName(fileName)).append(LINE_FEED);
        printWriter.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
        printWriter.append(LINE_FEED);
        printWriter.flush();

        FileInputStream inputStream = new FileInputStream(uploadFile);
        byte[] buffer = new byte[4096];
        int bytesRead;

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        outputStream.flush();
        inputStream.close();

        printWriter.append(LINE_FEED);
        printWriter.flush();
    }

    private void addHeaderField(PrintWriter printWriter, String name, String value) {
        printWriter.append(name + ": " + value).append(LINE_FEED);
        printWriter.flush();
    }

    private static void registerTypeAdapter(GsonBuilder gsonBuilder, Class<?> clazz) {
        gsonBuilder.registerTypeAdapter(clazz, new SimpleDeserializer());

        Type type = ParameterizedTypeImpl.make(List.class, new Type[]{clazz}, null);
        gsonBuilder.registerTypeAdapter(type, new ListDeserializer());
    }

    private static class SimpleDeserializer implements JsonDeserializer {

        private static Gson gson = new Gson();

        @Override
        public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return gson.fromJson(json, typeOfT);
        }
    }

    private static class ListDeserializer implements JsonDeserializer {

        private static Gson gson = new Gson();

        @Override
        public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Type tType = (typeOfT instanceof ParameterizedType) ?
                    ((ParameterizedType) typeOfT).getActualTypeArguments()[0] :
                    Object.class;

            Type type = ParameterizedTypeImpl.make(List.class, new Type[]{tType}, null);
            return gson.fromJson(json, type);
        }
    }

    private static class ParameterizedTypeImpl implements ParameterizedType {

        private Type[] actualTypeArguments;
        private Class<?> rawType;
        private Type ownerType;

        private ParameterizedTypeImpl(Class<?> rawType, Type[] actualTypeArguments, Type ownerType) {
            this.actualTypeArguments = actualTypeArguments;
            this.rawType = rawType;

            if (ownerType != null)
                this.ownerType = ownerType;
            else
                this.ownerType = rawType.getDeclaringClass();

            validateConstructorArguments();
        }

        private void validateConstructorArguments() {
            TypeVariable[] formals = rawType.getTypeParameters();

            if (formals.length != actualTypeArguments.length)
                throw new MalformedParameterizedTypeException();
        }

        public static ParameterizedTypeImpl make(Class<?> rawType, Type[] actualTypeArguments, Type ownerType) {
            return new ParameterizedTypeImpl(rawType, actualTypeArguments, ownerType);
        }

        public Type[] getActualTypeArguments() {
            return actualTypeArguments.clone();
        }

        public Class<?> getRawType() {
            return rawType;
        }

        public Type getOwnerType() {
            return ownerType;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof ParameterizedType) {
                ParameterizedType type = (ParameterizedType) obj;

                if (this == type)
                    return true;

                Type thatOwner = type.getOwnerType();
                Type thatRawType = type.getRawType();

                return (ownerType == null ? thatOwner == null : ownerType.equals(thatOwner)) && (rawType == null ? thatRawType == null : rawType.equals(thatRawType)) && Arrays.equals(actualTypeArguments, type.getActualTypeArguments());
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(actualTypeArguments) ^ (ownerType == null ? 0 : ownerType.hashCode()) ^ (rawType == null ? 0 : rawType.hashCode());
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();

            if (ownerType != null) {
                if (ownerType instanceof Class)
                    sb.append(((Class) ownerType).getName());
                else
                    sb.append(ownerType.toString());

                sb.append(".");

                if (ownerType instanceof ParameterizedTypeImpl) {
                    sb.append(rawType.getName().replace(((ParameterizedTypeImpl) ownerType).rawType.getName() + "$", ""));
                } else
                    sb.append(rawType.getName());
            } else {
                sb.append(rawType.getName());
            }

            if (actualTypeArguments != null && actualTypeArguments.length > 0) {
                sb.append("<");

                boolean first = true;

                for (Type t : actualTypeArguments) {
                    if (!first)
                        sb.append(", ");

                    if (t instanceof Class)
                        sb.append(((Class) t).getName());
                    else
                        sb.append(t.toString());

                    first = false;
                }

                sb.append(">");
            }

            return sb.toString();
        }
    }
}